package com.springmvc.demo.util.excel.poi;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by mengran.gao on 2017/11/6.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Main m = new Main();
//        m.write();
        m.read();
    }

    public void read() throws FileNotFoundException {
        InputStream in = new FileInputStream("C:\\Users\\mengran.gao\\Desktop\\三工.xls");
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("username", "用户名");
        fieldMap.put("age", "年龄");
        fieldMap.put("email", "邮箱");
        fieldMap.put("birthday", "出生日期");
        List<User> users = ExcelUtil.excelToList(in, 0, User.class, fieldMap);
        users.forEach(p -> System.out.println(p));
    }

    public void write() {
        FileOutputStream fos = null;
        try {
            String filename = "C:\\Users\\mengran.gao\\Desktop\\三工.xls";
            String extName = filename.substring(filename.lastIndexOf(".") + 1);
            File file = new File(filename);
            LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
            fieldMap.put("username", "用户名");
            fieldMap.put("age", "年龄");
            fieldMap.put("email", "邮箱");
            fieldMap.put("birthday", "出生日期");
            if (!file.exists()) {
                //file.mkdirs();
                file.createNewFile();

            }
            fos = new FileOutputStream(file);
            List<User> datas = new ArrayList<>();
            User u1 = new User("yiyi", 10, "yiyi@qq.com", new Date());
            User u2 = new User("erer", 20, "erer@qq.com", new Date());
            User u3 = new User("sasa", 30, "sasa@qq.com", new Date());
            datas.add(u1);
            datas.add(u2);
            datas.add(u3);
//            ExcelUtil.listToExcel(datas, fieldMap, extName, null, fos);
        } catch (IOException ex) {
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException ex) {
            }
        }
    }
}
