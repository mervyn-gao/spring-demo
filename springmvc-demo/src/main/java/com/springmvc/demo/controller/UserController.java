package com.springmvc.demo.controller;

import com.alibaba.fastjson.JSON;
import com.springmvc.demo.constant.ResultCodeConstant;
import com.springmvc.demo.enums.ResultStateEnum;
import com.springmvc.demo.model.User;
import com.springmvc.demo.service.UserService;
import com.springmvc.demo.view.UserExcelView;
import com.springmvc.demo.vo.JsonResult;
import com.springmvc.demo.vo.UserAddValidGroup;
import com.springmvc.demo.vo.UserVo;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * Created by mengran.gao on 2017/8/7.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(value = "/hello", method = RequestMethod.POST)
//    @ResponseBody
    public UserVo hello(Integer id) {
        UserVo userVo = new UserVo();
        userVo.setId(id);
        return userVo;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
//    @ResponseBody
    public JsonResult<UserVo> add(@Validated({UserAddValidGroup.class}) @RequestBody UserVo userVo, BindingResult result) {
        LOGGER.debug("debug日志，userVo={}", JSON.toJSONString(userVo));
        LOGGER.info("info日志，userVo={}", JSON.toJSONString(userVo));
        if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return new JsonResult<>(ResultCodeConstant.VALID_ERROR, message);
        }
        return JsonResult.success(ResultStateEnum.SUCCESS, userVo);
    }

    @RequestMapping(value = "/importUsers", method = {RequestMethod.POST})
//    @ResponseBody
    public JsonResult<String> importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        if (null == file) {
            JsonResult.fail(ResultStateEnum.INNER_ERROR);
        }
        String originalFilename = file.getOriginalFilename();
        String fileNameExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!"xls".equals(fileNameExt) && !"xlsx".equals(fileNameExt)) {
            JsonResult.fail(ResultStateEnum.INNER_ERROR);
        }
        userService.importWorkers(file.getInputStream());
        return JsonResult.success(ResultStateEnum.SUCCESS, null);
    }

    @RequestMapping(value = "/exportUsers", method = RequestMethod.GET)
    public ModelAndView exportUsers() throws Exception {
        List<User> userList = new ArrayList<>();
        User u1 = new User("yiyi", 10, "yiyi@qq.com", new Date());
        User u2 = new User("erer", 20, "erer@qq.com", new Date());
        User u3 = new User("sasa", 30, "sasa@qq.com", new Date());
        userList.add(u1);
        userList.add(u2);
        userList.add(u3);
        Map<String, Object> model = new HashMap<>();
        model.put("userList", userList);
        return new ModelAndView(new UserExcelView(), model);
    }

    @RequestMapping(value = "/downWorkersTemplate", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downWorkersTemplate() throws Exception {
        URL url = new URL("http://img1.uat1.rs.com/g1/M00/02/0F/wKh8yloC1qaAeAzvAAAkFtI7mU452.xlsx");
        InputStream is = url.openStream();
        byte[] bytes = new byte[is.available()];
        is.read(bytes);
        is.close();
        String filename = URLEncoder.encode("三工人员信息导入模板.xlsx", "UTF-8");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename + "; filename*=utf-8''" + filename);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/downWorkersTemplate2", method = RequestMethod.GET)
    public void downWorkersTemplate2(HttpServletResponse response) throws Exception {
        String filename = URLEncoder.encode("三工人员信息导入模板.xlsx", "UTF-8");
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setHeader("Content-Disposition", "attachement; filename=" + filename + "; filename*=utf-8''" + filename);
        URL url = new URL("http://img1.uat1.rs.com/g1/M00/02/0F/wKh8yloC1qaAeAzvAAAkFtI7mU452.xlsx");
        InputStream is = url.openStream();

        OutputStream os = response.getOutputStream();
        //循环写入输出流
        byte[] b = new byte[is.available()];
        int length = 0;
        while ((length = is.read(b)) > 0) {
            os.write(b, 0, length);
        }
        // 这里主要关闭。
        os.close();
        is.close();
    }
}
