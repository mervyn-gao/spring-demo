package com.springmvc.demo.view;

import com.springmvc.demo.util.excel.poi.ExcelUtils;
import com.springmvc.demo.util.excel.poi.User;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mengran.gao on 2017/11/7.
 */
public class UserExcelView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model, Workbook workbook, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<User> userList = (List<User>) model.get("userList");
        LinkedHashMap<String, String> fieldMap = new LinkedHashMap<>();
        fieldMap.put("username", "用户名");
        fieldMap.put("age", "年龄");
        fieldMap.put("email", "邮箱");
        fieldMap.put("birthday", "出生日期");
        ExcelUtils.listToExcel(workbook, userList, fieldMap,"三工人员信息", "三工人员信息.xlsx", response);
    }
}
