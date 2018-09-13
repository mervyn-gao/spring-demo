package com.springmvc.demo.converter;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mengran.gao on 2018/5/22.
 */
public class JsonDateConverter extends SimpleDateFormat {
    private static final List<String> formarts = new ArrayList<String>(4);

    static {
        formarts.add("yyyy-MM");
        formarts.add("yyyy-MM-dd");
        formarts.add("yyyy-MM-dd HH");
        formarts.add("yyyy-MM-dd HH:mm");
        formarts.add("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public Date parse(String source) throws ParseException {
        if (source == null || "".equals(source.trim())) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, formarts.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}$")) {
            return parseDate(source, formarts.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(3));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, formarts.get(4));
        } else {
            throw new IllegalArgumentException("Invalid boolean value '" + source + "'");
        }
    }

    public static void main(String[] args) {
        String s = "1990-04-02 11:11:11";
        String REGEX = "^\\d{4}-\\d{1,2}-\\d{1,2} {1}\\d{1,2}:\\d{1,2}:\\d{1,2}$";
        boolean matches = s.matches(REGEX);
        System.out.println(matches);
    }

    @Override
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        return new StringBuffer(smartFormat(date));
    }

    public Date parseDate(String dateStr, String format) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(format);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String smartFormat(Date date) {
        if (date == null) return "";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);
        if (dateStr.endsWith(" 00:00:00")) {   //时分秒
            dateStr = dateStr.substring(0, 10);
        } else if (dateStr.endsWith(":00:00")) {  //时分
            dateStr = dateStr.substring(0, 13);
        } else if (dateStr.endsWith(":00")) {  //秒
            dateStr = dateStr.substring(0, 16);
        }
        return dateStr;
    }
}
