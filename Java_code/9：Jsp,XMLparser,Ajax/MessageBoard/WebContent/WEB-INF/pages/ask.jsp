<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.bean.*"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>留言页面</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<style type="text/css">
			.navtit{ height: 50px; }
			.navbut{ line-height: 50px;}
		</style>
		<link rel="stylesheet" href="../css/bootstrap.min.css" />
	<link rel="stylesheet" href="../kindeditor/themes/default/default.css" />
		<script charset="utf-8" src="../kindeditor/kindeditor-all-min.js"></script>
		<script charset="utf-8" src="../kindeditor/lang/zh-CN.js"></script>
		<script>
			var editor;
			KindEditor.ready(function(K) {
				editor = K.create('textarea[name="txt"]', {
					resizeType : 0,
					width:'180px',
					height:'100px',
					allowPreviewEmoticons : false,
					allowImageUpload : false,
					items : [
						'fontname', 'fontsize', '|', 'forecolor', 'hilitecolor', 'bold', 'italic', 'underline',
						'removeformat', '|', 'justifyleft', 'justifycenter', 'justifyright', 'insertorderedlist',
						'insertunorderedlist', '|',  'image']
				});
			});
		</script>
	</head>
	<body>
		<div class="container">
			<div class="row navtit">
				<div class="col-xs-6">
					<p class="navbut">
						<a href="#"  class="btn btn-info">首页</a>
						<a href="#"  class="btn btn-danger">发表留言</a>
						<span class="pull-right">
						<%
							Admin admin=(Admin)session.getAttribute("loged");
						%>
						<img src="../ups/<%=admin.getPic()%>" style="max-width: 30px;"/><%=admin.getName() %></span>
					</p>
				</div>
				
			</div>
			
			<div class="row">
				
<div class="panel panel-success" style="margin-top: 15px;">
  <div class="panel-heading">发表留言</div>
  <div class="panel-body">
 
    <form action="message" method="post">
    <input type="hidden" name="action" value="message"/>
  <div class="form-group">
  
    <input type="hidden" name="admin_id" value="<%=admin.getId()%>"/>
    <input type="text" name="title" class="form-control" id="exampleInputEmail1" placeholder="请输入要提问的问题">
  </div>
  <div class="form-group">
     <textarea name="txt" class="form-control"></textarea>
    
  </div>
  
  <button type="submit" class="btn btn-success">留言</button>
</form>
    	
    </div>

  </div>
</div>
			</div>
		</div>
	</body>
</html>
