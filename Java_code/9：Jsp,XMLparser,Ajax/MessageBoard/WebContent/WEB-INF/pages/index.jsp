<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@page import="com.bean.*,java.util.*,com.dbutils.*" %>
<%@ taglib prefix="my" uri="/mytags" %>>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>留言主页</title>
		<link rel="stylesheet" href="../css/bootstrap.min.css" />
		<script type="text/javascript" src="../js/jquery.min.js" ></script>
		<script type="text/javascript" src="../js/bootstrap.min.js"></script>
		<style type="text/css">
			.navtit{ height: 50px; }
			.navbut{ line-height: 50px;}
		</style>
	</head>
	<body>
		<div class="container">
			<div class="row navtit">
				<div class="col-xs-6">
					<p class="navbut">
						<a href="#"  class="btn btn-info">首页</a>
						<a href="message"  class="btn btn-danger">发表留言</a>
						<span class="pull-right">
						<%
							Admin admin=(Admin)session.getAttribute("loged");
						%>
						<img src="../ups/<%=admin.getPic()%>" style="max-width: 30px;"/><%=admin.getName() %></span>
					</p>
				</div>
				
			</div>
			
			<div class="row">
<div class="panel panel-success" style="margin-top: 30px;">
  <div class="panel-heading">所有问题列表</div>
  <div class="panel-body">
   
     <ul class="list-group">
     <%
     PageDiv<Msg> pd=(PageDiv<Msg>)request.getAttribute("pd");
     if(null!=pd&&null!=pd.getList()&&pd.getList().size()>0)
     {
    	 for(Msg msg:pd.getList())
    	 {
    		 %>
    		 <li class="list-group-item">
	    		 <a href="msginfo?id=<%=msg.getId()%>"> <%=msg.getTitle() %> </a>
	    		 <span class="badge">
	    		 	<%=msg.getCreatetime() %>
	    		 </span>
    		 </li>
    		 <%
    	 }
     }
     
     %>  
     </ul>
   
   <my:page isinfo="true"/>

    </div>

  </div>
</div>
			</div>
		</div>
		<%@include file="msg.jsp" %>
	</body>
</html>
