package com.springmvc.demo.controller;

import com.alibaba.fastjson.JSON;
import com.springmvc.demo.exception.BusinessException;
import com.springmvc.demo.model.User;
import com.springmvc.demo.service.UserService;
import com.springmvc.demo.view.UserExcelView;
import com.springmvc.demo.vo.base.Result;
import com.springmvc.demo.vo.base.ResultCode;
import com.springmvc.demo.vo.UserAddValidGroup;
import com.springmvc.demo.vo.UserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
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

    //这里也可以不加BindingResult result,然后交给统一异常去处理
    //如果校验逻辑不通过时，就会抛出MethodArgumentNotValidException这个异常，异常携带有一个BindingResult对象这里面封装了出错的详细信息，
    // 企业开发中我们通常会把这个异常统一的处理下，也就是通过@ControllerAdvice注解建立统一异常处理类
    @PutMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Result<UserVO> add(@Validated({UserAddValidGroup.class}) @RequestBody UserVO userVO) {
        LOGGER.debug("debug日志，userVO={}", JSON.toJSONString(userVO));
        LOGGER.info("info日志，userVO={}", JSON.toJSONString(userVO));
        /*if (result.hasErrors()) {
            String message = result.getFieldError().getDefaultMessage();
            return Result.failure(ResultCode.PARAM_IS_INVALID.getCode(), message);
        }*/
        if (userVO.getAge().equals(99)) {
            throw new BusinessException(ResultCode.PARAM_TYPE_BIND_ERROR);
        } else if (userVO.getAge().equals(100)) {
//            System.out.println(1 / 0);
            throw new IllegalArgumentException("test");
        }
        return Result.success();
    }

    @PostMapping(value = "/importUsers")
//    @ResponseBody
    public Result<String> importUsers(@RequestParam("file") MultipartFile file) throws Exception {
        if (null == file) {
            return Result.failure(ResultCode.PARAM_IS_INVALID.getCode(), "导入文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String fileNameExt = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        if (!"xls".equals(fileNameExt) && !"xlsx".equals(fileNameExt)) {
            return Result.failure(ResultCode.PARAM_IS_INVALID.getCode(), "文件格式不正确");
        }
        userService.importWorkers(file.getInputStream());
        return Result.success("导入成功");
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

    @GetMapping(value = "/downWorkersTemplate")
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

    @GetMapping(value = "/downWorkersTemplate2")
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


    public static void main(String[] args) {
        List<User> dateList = new ArrayList<>();
        dateList.add(new User("yiyi", 11, "11@11.aa", new Date(1530842381891L)));
        dateList.add(new User("erer", 22, "22@22.aa", new Date(1530842381890L)));
        dateList.add(new User("sasa", 33, "33@33.aa", new Date(1530842381892L)));
        dateList.sort(Comparator.comparing(User::getBirthday).reversed());
        dateList.sort((t1, t2) -> t2.getBirthday().compareTo(t1.getBirthday()));
        dateList.sort(new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getBirthday().compareTo(o2.getBirthday());
            }
        });
        dateList.forEach(u -> System.out.println(u.getUsername()));

        /*String a = "";
        System.out.println(a.substring(0, a.length() - 2));*/

        Integer in = 2;
        System.out.println(Objects.equals(in, 1));
    }
}
