<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>留言板登录界面</title>
		
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
	</head>
	<body>
		<div class="container">
				<div class="row">
					<div class="col-md-offset-3 col-md-6">
							<div class="panel panel-info" style="margin-top: 50px;">
							  <div class="panel-heading">登录留言板</div>
							  <div class="panel-body">
<form action="login"  method="post">
	 <!-- 传参 -->
	 <input type="hidden" name="action" value="checkLogin"/>
	  <div class="form-group">
	    <label>Email:</label>
	    <input type="email" name="email"  class="form-control" placeholder="请输入Email">
	  </div>
	  <div class="form-group">
	    <label>密码：</label>
	    <input type="password" name="upwd" class="form-control"  placeholder="Password">
	  </div>
	 
	  <button type="submit" class="btn btn-info">登录</button>
	  <a href="regist"  class="btn btn-success">注册</a>
</form>
							  </div>
							</div>
					</div>
				</div>
		</div>
		
		<%@include file="msg.jsp" %>
	</body>
</html>
