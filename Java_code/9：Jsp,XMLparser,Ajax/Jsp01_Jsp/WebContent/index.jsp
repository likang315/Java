<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HelloJSP</title>
</head>
<body>
 <h1>Login </h1>
<center>
  <form action="check.jsp" method="post">
    User:<input type="text" name="uname"/><br/>
    Upwd:<input type="password" name="upwd"/><br/>
    <input type="submit" value="登录"/>
  </form>
</center>
</body>
</html>