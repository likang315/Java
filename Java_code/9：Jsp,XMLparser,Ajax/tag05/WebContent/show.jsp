<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="my" uri="/mytags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="css/bootstrap.min.css" />
</head>
<body>
<table class="table table-striped table-bordered table-hover">
  <tr><th>ID</th><th>Name</th><th>Sex</th></tr>
  <my:stus data="stus">
    <tr><td><my:item value="id"/></td><td><my:item value="name"/></td><td><my:item value="sex"/></td></tr>
  </my:stus>
</table>
</body>
</html>