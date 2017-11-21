package com.springmvc.demo.util.excel.poi;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Excel工具类.
 *
 * @author 133682
 * @date 2014-12-31 下午4:06:51
 */
public class ExcelUtils2 {

    private static Logger LOGGER = LoggerFactory.getLogger(ExcelUtils2.class);

    /**
     * 读取excel
     * @param titles    （表头）用于校验模板字段的顺序及字段是否正确
     * @param is        输入流
     * @param sheetName 页名
     * @param startRow  数据开始行
     * @return
     */
    public static String[][] read(List<String> titles, InputStream is, String sheetName, int startRow) {
        startRow = startRow < 1 ? 1 : startRow;
        return read(titles, is, sheetName, startRow, 1);
    }

    /**
     * 读取excel
     *
     * @param is        输入流
     * @param sheetName 页名
     * @param startRow  数据开始行
     * @param startCol
     * @return
     */
    public static String[][] read(
            List<String> titles, InputStream is, String sheetName, int startRow, int startCol) {
        startCol = startCol < 1 ? 1 : startCol;
        Workbook workbook = null;
        try {
            workbook = WorkbookFactory.create(is);
        } catch (IOException | InvalidFormatException e) {
            LOGGER.error("创建workbook异常，异常信息{}", e);
        }
        Sheet sheet = null;
        if (StringUtils.isEmpty(sheetName)) {
            sheet = workbook.getSheetAt(0);
        } else {
            workbook.getSheet(sheetName);
        }

        // 读取标题行数据
        Row titleRow = sheet.getRow(startRow - 2);
        int totalCol = titleRow.getPhysicalNumberOfCells();
        if (titles.size() != totalCol) {
            throw new ExcelException("文件中缺少或多出字段，请参考导入模板");
        }
        for (int i = 0; i < totalCol; i++) {
            Cell cell = titleRow.getCell(i);
            String cellValue = cell.getStringCellValue();
            if (!cellValue.equals(titles.get(i))) {
                throw new ExcelException("文件中字段的顺序错误，请参考导入模板");
            }
        }

        int totalRow = sheet.getPhysicalNumberOfRows(), dataRow = totalRow - startRow + 1;
        String[][] result = new String[dataRow][];
        for (int i = 0; i < dataRow; i++) {
            Row row = sheet.getRow(i + startRow - 1);
            int dataCol = totalCol - startCol + 1;
            result[i] = new String[dataCol];
            for (int j = 0; j < dataCol; j++) {
                Cell cell = row.getCell(j + startCol - 1);
                result[i][j] = getStringCellValue(cell);
            }
        }
        return result;
    }

    /**
     * 获取单元格字符串值
     *
     * @param cell
     * @return
     */
    private static String getStringCellValue(Cell cell) {
        String result = "";
        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    Date date = cell.getDateCellValue();
                    result = new SimpleDateFormat("yyyy-MM-dd").format(date);
                } else {// 简单数字
                    result = NumberToTextConverter.toText(cell.getNumericCellValue());
                }
                break;
            case STRING:    // 字符串
                result = cell.getStringCellValue();
                break;
            case FORMULA:   //公式
                Workbook wb = cell.getSheet().getWorkbook();
                CreationHelper crateHelper = wb.getCreationHelper();
                FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
                result = getStringCellValue(evaluator.evaluateInCell(cell));
                break;
            case BLANK: // 空
                break;
            case BOOLEAN:
                result = String.valueOf(cell.getBooleanCellValue());
                break;
            case ERROR: // 错误
                result = null;
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 导出excel
     *
     * @param records   数据
     * @param clazz     导出类
     * @param sheetName 页面名
     * @param os        输出流
     * @param <T>
     */
    public static <T> void write(List<T> records, Class<?> clazz, String sheetName, OutputStream os) {
        Workbook workbook = new XSSFWorkbook();
        sheetName = StringUtils.isEmpty(sheetName) ? "sheet1" : sheetName;
        Sheet sheet = workbook.createSheet(sheetName);

        Field[] fields = ReflectUtils2.getClassAndSuperClassFields(clazz);
        // 创建第一行表头
        Row firstRow = sheet.createRow(0);
        CellStyle cellStyle = firstRowStyle(workbook);//设置第一行样式

        Map<Integer, String> titleMap = new HashMap<>();//excel的表头
        for (Field field : fields) {
            Excel excel = field.getAnnotation(Excel.class);
            if (null == excel) {
                continue;
            }
            titleMap.put(excel.sort(), field.getName());
            Cell cell = firstRow.createCell(excel.sort() - 1);// 创建表头单元格
            sheet.setColumnWidth(excel.sort() - 1, excel.width() * 256);//设置单元格宽度
            cell.setCellStyle(cellStyle);//设置单元格样式
            cell.setCellValue(excel.name());//设置单元格值
        }

        for (int i = 0; i < records.size(); i++) {
            T o = records.get(i);
            Row row = sheet.createRow(i + 1);
            for (Map.Entry<Integer, String> entry : titleMap.entrySet()) {
                // 创建数据单元格
                Cell cell = row.createCell(entry.getKey() - 1);
                cell.setCellValue(getStringValueByFieldName(entry.getValue(), o));
            }
        }
        try {
            workbook.write(os);
        } catch (IOException e) {
            LOGGER.error("写入excel文件异常");
            throw new ExcelException("写入excel文件异常");
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                LOGGER.error("关闭workbook异常");
            }
        }
    }

    /**
     * 获取field的字符串值
     *
     * @param fieldName
     * @param o
     * @return
     */
    private static String getStringValueByFieldName(String fieldName, Object o) {
        Field field = ReflectUtils2.getFieldByName(fieldName, o.getClass());
        Object result = ReflectUtils2.getValueByFieldName(field, o);
        Excel excel = field.getAnnotation(Excel.class);
        if (result instanceof Date) {
            String format = excel.dateFormat();
            Date date = (Date) result;
            return new SimpleDateFormat(format).format(date);
        } else if (result instanceof Float || result instanceof Double || result instanceof BigDecimal) {
            int precision = excel.precision();
            DecimalFormat df = new DecimalFormat(fillZero(precision));
            return df.format(result);
        } else {
            return result.toString();
        }
    }

    /**
     * 格式化补0
     *
     * @param len
     * @return
     */
    private static String fillZero(int len) {
        StringBuilder builder = new StringBuilder("0.0");
        for (int i = 1; i < len; i++) {
            builder.append("0");
        }
        return builder.toString();
    }

    /**
     * 设置表头样式
     *
     * @param workbook
     * @return
     */
    private static CellStyle firstRowStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("@"));// // 内容样式 设置单元格内容格式是文本
        style.setAlignment(HorizontalAlignment.CENTER);// 内容居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        Font font = workbook.createFont();// 文字样式
        font.setBold(true);//设置粗体
        font.setFontHeight((short) 220);
        style.setFont(font);
        return style;
    }

    public static void main(String[] args) throws Exception {
        // 写入文件
//        FileOutputStream fos = new FileOutputStream("E:\\test.xlsx");
//        List<UserExporter> records = new ArrayList<>();
//        records.add(new UserExporter("yiyi", 10, "yiyi@qq.com", new Date(), new BigDecimal(10000)));
//        records.add(new UserExporter("erer", 11, "erer@qq.com", new Date(), new BigDecimal(10001)));
//        records.add(new UserExporter("sasa", 12, "sasa@qq.com", new Date(), new BigDecimal(10002)));
//        write(records, UserExporter.class, "人员信息", fos);

        // 读取文件
        FileInputStream fis = new FileInputStream("E:\\tt.xlsx");
        List<String> titles = new ArrayList<>();
        titles.add("用户名");
        titles.add("年龄");
        titles.add("邮箱");
        titles.add("出生日期");
        titles.add("薪水");
        String[][] result = read(titles, fis, "", 2);
        System.out.println(result.length);
        for (int i = 0; i < result.length; i++) {
            String[] ss = result[i];
            System.out.println(CollectionUtils.arrayToList(ss));
        }
    }
}
