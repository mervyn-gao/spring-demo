<%--
  Created by IntelliJ IDEA.
  User: mengran.gao
  Date: 2017/8/8
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String path = request.getContextPath();
  String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path + "/";
%>
<html>
  <head>
    <title>Title</title>
    <base href="<%=basePath%>">
  </head>
  <body>
  Hello World！8083<br>
  <form action="users/login" method="post">
    用户名：<input type="text" name="username">&nbsp;&nbsp;&nbsp;&nbsp;
    密码：<input type="password" name="password">&nbsp;&nbsp;
    <input type="submit">
  </form>
<%--  <a href="/springmvc-demo/users/exportUsers">下载</a>
  <a href="/springmvc-demo/users/downWorkersTemplate">下载2</a>
  <a href="/springmvc-demo/users/downWorkersTemplate2">下载3</a>
  <form action="/springmvc-demo/users/importUsers" method="post" enctype="multipart/form-data">
    <input type="file" name="file"/>
    <input type="submit" value="提交">
  </form>--%>
  <%--
  <button id="btn01">ajax提交</button>
  <span id="sp01"></span>
  </body>

  <script type="text/javascript" src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>

  <script type="text/javascript">
    $(function() {
      $("#btn01").click(function (e) {
          $.ajax({
              type: 'PUT',
              url: '/users',
              data: JSON.stringify({
                  "name":"yiyi",
                  "age":100,
                  "email":"mervyn_gao@163.com",
                  "tel":"13661736454",
                  "idCard":"420621199004024274",
                  "birthDay":"1990-04-02"
              }),
              contentType: 'application/json',
              dataType: 'json',
              success: function(data, status, jqXHR) {
                  console.log(data);
                  console.log(data.responseJSON);
              },
              error: function (data, status, jqXHR) {
                  console.log(data);
                  console.log(data.responseJSON);
              }
          });
      });
    });
  </script>--%>
</html>
