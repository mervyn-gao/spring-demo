package com.springmvc.demo.util.excel.poi;

/**
 * Created by mengran.gao on 2017/11/7.
 */
public class T {
    public static void main(String[] args) {
        String filename = "C:\\Users\\mengran.gao\\Desktop\\三工.xlsx";
        String extName = filename.substring(filename.lastIndexOf(".") + 1);
        System.out.println(extName);
    }
}
