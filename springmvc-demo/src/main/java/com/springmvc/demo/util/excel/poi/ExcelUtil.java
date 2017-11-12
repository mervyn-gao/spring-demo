package com.springmvc.demo.util.excel.poi;

import com.springmvc.demo.util.DateUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ExcelUtil {
    /**
     * 导出Excel（可以导出到本地文件系统，也可以导出到浏览器，可自定义工作表大小）
     *
     * @param list      数据源
     * @param fieldMap  类的英文属性和Excel中的中文列名的对应关系
     *                  如果需要的是引用对象的属性，则英文属性使用类似于EL表达式的格式
     *                  如：list中存放的都是student，student中又有college属性，而我们需要学院名称，则可以这样写
     *                  fieldMap.put("college.collegeName","学院名称")
     * @param sheetName 工作表的名称
     * @param out       导出流
     */
    public static <T> void listToExcel(
            Workbook wwb,
            List<T> list,
            LinkedHashMap<String, String> fieldMap,
            String sheetName,
            OutputStream out) throws ExcelException {
        if (list == null || list.size() == 0) {
            throw new ExcelException("数据源中没有任何数据");
        }
//        Workbook wwb;
        try {
            sheetName = sheetName == null ? "sheet" : sheetName;
            /*if ("xls".equals(excelExtName)) {
                int sheetSize = list.size() > 65535 ? 65535 : list.size();
                wwb = new HSSFWorkbook();
                //因为2003的Excel一个工作表最多可以有65536条记录，除去列头剩下65535条
                //所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
                //1.计算一共有多少个工作表
                double sheetNum = Math.ceil(list.size() / new Integer(sheetSize).doubleValue());
                //2.创建相应的工作表，并向其中填充数据
                for (int i = 0; i < sheetNum; i++) {
                    //如果只有一个工作表的情况
                    if (1 == sheetNum) {
                        Sheet sheet = wwb.createSheet(sheetName + "1");
                        fillSheet(sheet, list, fieldMap, 0, list.size() - 1);
                        //有多个工作表的情况
                    } else {
                        Sheet sheet = wwb.createSheet(sheetName + (i + 1));
                        //获取开始索引和结束索引
                        int firstIndex = i * sheetSize;
                        int lastIndex = (i + 1) * sheetSize - 1 > list.size() - 1 ? list.size() - 1 : (i + 1) * sheetSize - 1;
                        //填充工作表
                        fillSheet(sheet, list, fieldMap, firstIndex, lastIndex);
                    }
                }
            } else if ("xlsx".equals(excelExtName)) {
                wwb = new XSSFWorkbook();
                Sheet sheet = wwb.createSheet(sheetName);
                fillSheet(sheet, list, fieldMap, 0, list.size() - 1);
            } else {
                throw new ExcelException("当前文件不是excel文件");
            }*/
            Sheet sheet = wwb.createSheet(sheetName);
            fillSheet(sheet, list, fieldMap, 0, list.size() - 1);
            wwb.write(out);
//            wwb.close();
        } catch (Exception e) {
            throw new ExcelException("导出Excel失败");
        }
    }

    /**
     * 导出Excel（导出到浏览器，可以自定义工作表的大小）
     *
     * @param list     数据源
     * @param fieldMap 类的英文属性和Excel中的中文列名的对应关系
     * @param response 使用response可以导出到浏览器
     * @throws ExcelException
     */
    public static <T> void listToExcel(
            Workbook wwb,
            List<T> list,
            LinkedHashMap<String, String> fieldMap,
            String sheetName,
            String fileName,
            HttpServletResponse response) throws ExcelException, UnsupportedEncodingException {
        fileName = URLEncoder.encode(fileName, "UTF-8");
        //设置response头信息
        response.reset();
        response.setHeader("Content-Disposition", "attachement; filename=" + fileName + "; filename*=utf-8''" + fileName);
        //创建工作簿并发送到浏览器
        try {
            OutputStream out = response.getOutputStream();
            listToExcel(wwb, list, fieldMap, sheetName, out);
        } catch (Exception e) {
            throw new ExcelException("导出Excel失败", e);
        }
    }

    /**
     * 将Excel转化为List
     *
     * @param in          ：承载着Excel的输入流
     * @param sheetNum    ：要导入的工作表序号
     * @param entityClass ：List中对象的类型（Excel中的每一行都要转化为该类型的对象）
     * @param fieldMap    ：Excel中的中文列头和类的英文属性的对应关系Map
     * @return ：List
     */
    public static <T> List<T> excelToList(
            InputStream in,
            int sheetNum,
            Class<T> entityClass,
            LinkedHashMap<String, String> fieldMap) throws ExcelException {
        //定义要返回的list
        List<T> resultList = new ArrayList<>();
        try {
            //根据Excel数据源创建WorkBook
            Workbook wb = WorkbookFactory.create(in);
            //获取工作表
            Sheet sheet = wb.getSheetAt(sheetNum);
            //获取工作表的有效行数
            int realRows = 0;
            for (int i = 0; i < sheet.getLastRowNum(); i++) {
                int nullCols = 0;
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;//略过空行
                }
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    Cell currentCell = row.getCell(j);
                    if (currentCell == null) {
                        nullCols++;
                    }
                }
                if (nullCols == row.getLastCellNum()) {
                    break;
                } else {
                    realRows++;
                }
            }

            //如果Excel中没有数据则提示错误
            if (realRows <= 1) {
                throw new ExcelException("Excel文件中没有任何数据");
            }
            Row firstRow = sheet.getRow(0);
            String[] excelFieldNames = new String[firstRow.getLastCellNum()];
            //获取Excel中的列名
            for (int i = 0; i < firstRow.getLastCellNum(); i++) {
                excelFieldNames[i] = getStringCellValue(firstRow.getCell(i));
            }

            //判断需要的字段在Excel中是否都存在
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                if (!excelFieldList.contains(entry.getValue())) {
                    isExist = false;
                    break;
                }
            }

            //如果有列名不存在，则抛出异常，提示错误
            if (!isExist) {
                throw new ExcelException("Excel中缺少必要的字段，或字段名称有误");
            }

            //将列名和列号放入Map中,这样通过列名就可以拿到列号
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<>();
            for (int i = 0; i < excelFieldNames.length; i++) {
                colMap.put(excelFieldNames[i], firstRow.getCell(i).getColumnIndex());
            }

            //将sheet转换为list
            for (int i = 1; i <= realRows; i++) {
                //新建要转换的对象
                T entity = entityClass.newInstance();
                //给对象中的字段赋值
                for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
                    //获取英文字段名
                    String enNormalName = entry.getKey();
                    //获取中文字段名
                    String cnNormalName = entry.getValue();
                    //根据中文字段名获取列号
                    int col = colMap.get(cnNormalName);
                    //获取当前单元格中的内容
                    Object content = getStringCellValue(sheet.getRow(i).getCell(col));
                    //给对象赋值
                    setFieldValueByName(enNormalName, content, entity);
                }
                resultList.add(entity);
            }
        } catch (Exception e) {
            throw new ExcelException("导入Excel失败", e);
        }
        return resultList;
    }

    /*<-------------------------辅助的私有方法----------------------------------------------->*/

    /**
     * 根据字段名获取字段值
     *
     * @param fieldName 字段名
     * @param o         对象
     * @return 字段值
     */
    private static Object getFieldValueByName(String fieldName, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        Object value;
        if (field != null) {
            field.setAccessible(true);
            value = field.get(o);
        } else {
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }
        return value;
    }

    /**
     * 根据字段名获取字段
     *
     * @param fieldName 字段名
     * @param clazz     包含该字段的类
     * @return 字段
     */
    private static Field getFieldByName(String fieldName, Class<?> clazz) {
        //拿到本类的所有字段
        Field[] selfFields = clazz.getDeclaredFields();

        //如果本类中存在该字段，则返回
        for (Field field : selfFields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        //否则，查看父类中是否存在此字段，如果有则返回
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz != Object.class) {
            return getFieldByName(fieldName, superClazz);
        }
        //如果本类和父类都没有，则返回空
        return null;
    }


    /**
     * @param fieldNameSequence 带路径的属性名或简单属性名
     * @param o                 对象
     * @return 属性值
     * @throws Exception 根据带路径或不带路径的属性名获取属性值
     *                   即接受简单属性名，如userName等，又接受带路径的属性名，如student.department.name等
     */
    private static Object getFieldValueByNameSequence(String fieldNameSequence, Object o) throws Exception {
        Object value;
        //将fieldNameSequence进行拆分
        String[] attributes = fieldNameSequence.split("\\.");
        if (attributes.length == 1) {
            value = getFieldValueByName(fieldNameSequence, o);
        } else {
            //根据属性名获取属性对象
            Object fieldObj = getFieldValueByName(attributes[0], o);
            String subFieldNameSequence = fieldNameSequence.substring(fieldNameSequence.indexOf(".") + 1);
            value = getFieldValueByNameSequence(subFieldNameSequence, fieldObj);
        }
        return value;

    }


    /**
     * 根据字段名给对象的字段赋值
     *
     * @param fieldName  字段名
     * @param fieldValue 字段值
     * @param o          对象
     */
    private static void setFieldValueByName(String fieldName, Object fieldValue, Object o) throws Exception {
        Field field = getFieldByName(fieldName, o.getClass());
        if (field != null) {
            field.setAccessible(true);
            //获取字段类型
            Class<?> fieldType = field.getType();

            //根据字段类型给字段赋值
            if (String.class == fieldType) {
                field.set(o, String.valueOf(fieldValue));
            } else if ((Integer.TYPE == fieldType)
                    || (Integer.class == fieldType)) {
                field.set(o, Integer.parseInt(fieldValue.toString()));
            } else if ((Long.TYPE == fieldType)
                    || (Long.class == fieldType)) {
                field.set(o, Long.valueOf(fieldValue.toString()));
            } else if ((Float.TYPE == fieldType)
                    || (Float.class == fieldType)) {
                field.set(o, Float.valueOf(fieldValue.toString()));
            } else if ((Byte.TYPE == fieldType)
                    || (Byte.class == fieldType)) {
                field.set(o, Byte.valueOf(fieldValue.toString()));
            } else if ((Short.TYPE == fieldType)
                    || (Short.class == fieldType)) {
                field.set(o, Short.valueOf(fieldValue.toString()));
            } else if ((Double.TYPE == fieldType)
                    || (Double.class == fieldType)) {
                field.set(o, Double.valueOf(fieldValue.toString()));
            } else if (BigDecimal.class == fieldType) {
                field.set(o, BigDecimal.valueOf(Double.valueOf(fieldValue.toString())));
            } else if (Character.TYPE == fieldType) {
                if ((fieldValue != null) && (fieldValue.toString().length() > 0)) {
                    field.set(o, fieldValue.toString().charAt(0));
                }
            } else if (Date.class == fieldType) {
                field.set(o, DateUtils.convert(fieldValue.toString()));
            } else {
                field.set(o, fieldValue);
            }
        } else {
            throw new ExcelException(o.getClass().getSimpleName() + "类不存在字段名 " + fieldName);
        }
    }


    private static String getStringCellValue(Cell cell) {
        if (cell.getCellType() == CellType.BOOLEAN.getCode()) {
            return String.valueOf(cell.getBooleanCellValue());
        } else if (cell.getCellType() == CellType.NUMERIC.getCode()) {
            // 返回数值类型的值
            Object inputValue = null;// 单元格值
            Long longVal = Math.round(cell.getNumericCellValue());
            Double doubleVal = cell.getNumericCellValue();
            if (Double.parseDouble(longVal + ".0") == doubleVal) {   //判断是否含有小数位.0
                inputValue = longVal;
            } else {
                inputValue = doubleVal;
            }
            return String.valueOf(inputValue);      //返回String类型
        } else if (cell.getCellType() == CellType.STRING.getCode()) {
            return String.valueOf(cell.getStringCellValue());
        } else {
            return String.valueOf(cell.getStringCellValue());
        }
    }

    /**
     * 向工作表中填充数据
     *
     * @param sheet      工作表
     * @param list       数据源
     * @param fieldMap   中英文字段对应关系的Map
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     */
    private static <T> void fillSheet(
            Sheet sheet,
            List<T> list,
            LinkedHashMap<String, String> fieldMap,
            int firstIndex,
            int lastIndex) throws Exception {
        //定义存放英文字段名和中文字段名的数组
        String[] enFields = new String[fieldMap.size()];
        String[] cnFields = new String[fieldMap.size()];

        //填充数组
        int count = 0;
        for (Map.Entry<String, String> entry : fieldMap.entrySet()) {
            enFields[count] = entry.getKey();
            cnFields[count] = entry.getValue();
            count++;
        }
        //填充标题
        /*Row firstRow = sheet.createRow(0);
        CellStyle titleCellStyle = makeStyle(sheet.getWorkbook(), 1);
        for (int i = 0; i < cnFields.length; i++) {
            Cell cell = firstRow.createCell(i);
        }
        region(sheet, 0, 0, 0, fieldMap.size() - 1);
        Cell titleCell = firstRow.getCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleCellStyle);*/
        //填充表头
        Row secondRow = sheet.createRow(0);
        CellStyle titleCellStyle = makeStyle(sheet.getWorkbook(), 2);
        for (int i = 0; i < cnFields.length; i++) {
            Cell cell = secondRow.createCell(i);
            cell.setCellValue(cnFields[i]);
            cell.setCellStyle(titleCellStyle);
        }

        //填充内容
        for (int index = firstIndex; index <= lastIndex; index++) {
            //获取单个对象
            T item = list.get(index);
            Row row = sheet.createRow(index + 1);
            for (int i = 0; i < enFields.length; i++) {
                Object objValue = getFieldValueByNameSequence(enFields[i], item);
                if (objValue instanceof Date) {
                    Date dateValue = (Date) objValue;
                    LocalDateTime dateTimeValue = LocalDateTime.ofInstant(dateValue.toInstant(), ZoneId.systemDefault());
                    objValue = dateTimeValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                String fieldValue = objValue == null ? "" : objValue.toString();
                Cell cell = row.createCell(i);
                cell.setCellValue(fieldValue);
            }
        }
        //设置自动列宽
//        sheet.autoSizeColumn(3, true);// 设置列宽度自适应
    }

    /**
     * 设置cell 样式
     *
     * @param sheet
     * @param colIndex 指定列，从 0 开始
     * @return
     */
    private static void setStyle(Sheet sheet, int rowIndex, int colIndex, CellStyle style) {
        // sheet.autoSizeColumn(colIndex, true);// 设置列宽度自适应
        sheet.setColumnWidth(colIndex, 4000);
        Cell cell = sheet.getRow(rowIndex).getCell(colIndex);
        cell.setCellStyle(style);
    }

    /**
     * 设置样式
     *
     * @param type 1：标题 2：第一行
     * @return
     */
    private static CellStyle makeStyle(Workbook workbook, int type) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@"));// // 内容样式 设置单元格内容格式是文本
        style.setAlignment(HorizontalAlignment.CENTER);// 内容居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        // style.setBorderTop(CellStyle.BORDER_THIN);// 边框样式
        // style.setBorderRight(CellStyle.BORDER_THIN);
        // style.setBorderBottom(CellStyle.BORDER_THIN);
        // style.setBorderLeft(CellStyle.BORDER_THIN);
        Font font = workbook.createFont();// 文字样式
        if (type == 1) {
            // style.setFillForegroundColor(HSSFColor.LIGHT_BLUE.index);//颜色样式
            // 前景颜色
            // style.setFillBackgroundColor(HSSFColor.LIGHT_BLUE.index);//背景色
            // style.setFillPattern(CellStyle.ALIGN_FILL);// 填充方式
            font.setBold(true);
            font.setFontHeight((short) 300);
        }
        if (type == 2) {
            font.setBold(true);
            font.setFontHeight((short) 200);
        }
        style.setFont(font);
        return style;
    }

    /**
     * 合并单元格
     *
     * @param firstRow 开始行
     * @param lastRow  结束行
     * @param firstCol 开始列
     * @param lastCol  结束列
     */
    private static void region(Sheet sheet, int firstRow, int lastRow, int firstCol, int lastCol) {
        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
    }
}
