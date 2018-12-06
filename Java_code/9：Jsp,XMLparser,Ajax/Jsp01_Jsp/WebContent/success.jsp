<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Success</title>
</head>
<body>
<h1>
 Welcome,
 <%
 	out.println(request.getAttribute("uname"));
 %>
</h1>
<h2><%out.println(session.getAttribute("loged")); %></h2>
<H3><%out.println(request.getSession().getAttribute("loged")); %></H3>
</body>
</html>