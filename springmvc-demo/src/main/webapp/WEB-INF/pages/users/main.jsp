<%@ page import="java.util.Date" %><%--
  Created by IntelliJ IDEA.
  User: mengran.gao
  Date: 2018/6/30
  Time: 21:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>欢迎</title>
</head>
<body>
    Hello, ${username}, 今天是<fmt:formatDate value="<%=new Date()%>" pattern="yyyy-MM-dd"/>
</body>
</html>
