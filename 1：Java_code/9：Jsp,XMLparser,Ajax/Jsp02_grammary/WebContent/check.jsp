<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.bean.User"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<!-- 实例对象，将参数绑定到指定的对象，* ：全绑定 -->
<jsp:useBean id="user" class="com.bean.User" ></jsp:useBean>
<jsp:setProperty property="*" name="user"/>

<jsp:setProperty property="name" name="user" param="uname"/>

<hr/>
<h1><jsp:getProperty property="name" name="user"/></h1>
<h1><%=user.getAge() %></h1>
</body>
</html>