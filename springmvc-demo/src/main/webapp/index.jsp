<%--
  Created by IntelliJ IDEA.
  User: mengran.gao
  Date: 2017/8/8
  Time: 14:32
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
  <a href="/springmvc-demo/users/exportUsers">下载</a>
  <a href="/springmvc-demo/users/downWorkersTemplate">下载2</a>
  <a href="/springmvc-demo/users/downWorkersTemplate2">下载3</a>
  <form action="/springmvc-demo/users/importUsers" method="post" enctype="multipart/form-data">
    <input type="file" name="file"/>
    <input type="submit" value="提交">
  </form>
  </body>
</html>
