package com.springmvc.demo.test;

import com.chinaredstar.demeter.excel.annotation.RedStarExcelField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by xuli on 2018/2/8.
 */
public class RedStarImportExcel <T> {

    private static Logger LOGGER = LoggerFactory.getLogger(RedStarImportExcel.class);

    /**
     * 2003- 版本的excel
     */
    private static final String EXCEL_2003L = ".xls";
    /**
     * 2007+ 版本的excel
     */
    private static final String EXCEL_2007U = ".xlsx";

    private static final DecimalFormat FORMAT_NUMBER = new DecimalFormat("0");
    private static final DecimalFormat FORMAT_DECIMAL = new DecimalFormat("0.00");
    private static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat("yyy-MM-dd");

    private static final String DATA_FORMAT_STRING_GENERAL = "General";
    private static final String DATA_FORMAT_STRING_DATE = "m/d/yy";

    private static final String DATE_TYPE_ERROR = "ERROR";
    private static final String DATE_TYPE_UNDEFINED = "UNDEFINED";

    private static final String METHOD_SET = "set";

    private static final int DEFAULT_TITLE_ROW_NUM = 0;
    private static final int DEFAULT_START_ROW_NUM = 1;

    private List<T> dataList;

    public RedStarImportExcel(File file, Class<T> cls) {
        if (file == null) {
            LOGGER.error("文件格式有误!");
            return;
        }
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            LOGGER.error("解析文件失败!");
            return;
        }
        init(inputStream, file.getName(), cls);
    }

    public RedStarImportExcel(MultipartFile file, Class<T> cls) {
        if (file == null) {
            LOGGER.error("文件格式有误!");
            return;
        }
        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            LOGGER.error("解析文件失败!");
            return;
        }
        init(inputStream, file.getOriginalFilename(), cls);
    }


    private void init(InputStream inputStream, String fileName, Class<T> cls) {
        // 创建Excel工作薄
        Workbook work;
        try {
            work = getWorkbook(inputStream, fileName);
        } catch (Exception e) {
            LOGGER.error("error::", e);
            return;
        }
        dataList = new ArrayList<>();

        // 只取Excel中第一个非隐藏sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            if (work.isSheetHidden(i)) {
                continue;
            }
            Sheet sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            if (sheet.getLastRowNum() < DEFAULT_START_ROW_NUM) {
                break;
            }

            Row row = sheet.getRow(DEFAULT_TITLE_ROW_NUM);
            if (row == null) {
                break;
            }

            Map<String, Integer> titleMap = new HashMap<>();

            // 遍历所有的表头
            for (int k = 0; k < row.getLastCellNum(); k++) {
                Cell cell = row.getCell(k);
                if (cell == null) {
                    continue;
                }
                titleMap.put(String.valueOf(this.getCellValue(cell)), k);
            }

            Map<Integer, Method> setMethodMap = new HashMap<>();

            // 记录Excel列与对象字段的对应关系
            Field[] fs = cls.getDeclaredFields();
            for (Field f : fs) {
                RedStarExcelField ef = f.getAnnotation(RedStarExcelField.class);
                if (ef == null) {
                    continue;
                }
                if (!titleMap.containsKey(ef.title())) {
                    continue;
                }
                String name = f.getName();
                String setMethodName = METHOD_SET + name.substring(0, 1).toUpperCase() + name.substring(1);
                Class[] parameterTypes = new Class[1];
                parameterTypes[0] = f.getType();
                try {
                    Method m = cls.getMethod(setMethodName, parameterTypes);
                    setMethodMap.put(titleMap.get(ef.title()), m);
                } catch (NoSuchMethodException e) {
                    LOGGER.error("获取SET方法失败：{}()", setMethodName);
                }
            }

            // 遍历当前sheet中的所有行
            for (int j = DEFAULT_START_ROW_NUM; j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || isFullEmpty(row)) {
                    continue;
                }
                T t;
                try {
                    t = cls.newInstance();
                } catch (Exception e) {
                    LOGGER.error("初始化对象失败：{}", cls);
                    continue;
                }

                // 遍历所有的列
                for (int k = 0; k < row.getLastCellNum(); k++) {
                    Cell cell = row.getCell(k);
                    if (cell == null) {
                        continue;
                    }
                    if (setMethodMap.containsKey(k)) {
                        Method m = setMethodMap.get(k);
                        Object[] objects = new Object[1];
                        objects[0] = this.getCellValue(cell);
                        try {
                            m.invoke(t, objects);
                        } catch (Exception e) {
                            LOGGER.error("执行SET方法失败：{}, {}", m, objects[0]);
                        }
                    }
                }
                dataList.add(t);
            }
            break;
        }
    }

    private boolean isFullEmpty(Row row){
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if(!StringUtils.isEmpty(getCellValue(cell))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 根据文件后缀，自适应上传文件的版本
     *
     * @param inputStream 数据源
     * @param fileName    文件名
     * @return Excel workbook
     * @throws Exception 异常
     */
    private Workbook getWorkbook(InputStream inputStream, String fileName) throws Exception {
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        // 2003-
        if (EXCEL_2003L.equals(fileType)) {
            return new HSSFWorkbook(inputStream);
        }
        // 2007+
        if (EXCEL_2007U.equals(fileType)) {
            return new XSSFWorkbook(inputStream);
        }
        throw new Exception("解析的文件格式有误！");
    }

    /**
     * 对表格中数值进行格式化
     *
     * @param cell 单元格
     * @return 数据
     */
    private Object getCellValue(Cell cell) {
        Object value;
        switch (cell.getCellType()) {
            // 数值类型
            case Cell.CELL_TYPE_NUMERIC:
                if (DATA_FORMAT_STRING_GENERAL.equals(cell.getCellStyle().getDataFormatString())) {
                    value = FORMAT_NUMBER.format(cell.getNumericCellValue());
                    break;
                }
                if (DATA_FORMAT_STRING_DATE.equals(cell.getCellStyle().getDataFormatString())) {
                    synchronized (FORMAT_DATE) {
                        value = FORMAT_DATE.format(cell.getDateCellValue());
                    }
                    break;
                }
                value = FORMAT_DECIMAL.format(cell.getNumericCellValue());
                break;
            // 字符串类型
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            // 表达式类型
            case HSSFCell.CELL_TYPE_FORMULA:
                value = cell.getCellFormula();
                break;
            // 空
            case Cell.CELL_TYPE_BLANK:
                value = null;
                break;
            // 布尔类型
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_ERROR:
                value = DATE_TYPE_ERROR;
                break;
            default:
                value = DATE_TYPE_UNDEFINED;
                break;
        }
        return value;
    }

    public List<T> getDataList() {
        return dataList;
    }

}
