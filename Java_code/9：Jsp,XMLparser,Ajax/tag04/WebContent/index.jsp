<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="my" uri="/mytags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" href="css/bootstrap.min.css" />
<title>Insert title here</title>
</head>
<body>
<table class="table table-striped table-bordered table-hover">
  <tr><th>ID</th><th>Name</th><th>Sex</th></tr>
   <my:repeat count="6">
    	<tr><td>${index}</td><td>Name</td><td>Sex</td></tr>
   </my:repeat>
</table>
</body>
</html>