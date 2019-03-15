<%--
  Created by IntelliJ IDEA.
  User: sicwen
  Date: 2019/3/14
  Time: 10:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>登陆</title>
</head>
<body>
    <form action="/login" method="post">
        <input type="text" name="username"/>
        <input type="password" name="password"/>
        <input type="submit" value="登陆"/>
    </form>
</body>
</html>
