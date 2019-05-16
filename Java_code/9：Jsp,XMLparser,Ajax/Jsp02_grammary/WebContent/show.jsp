<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="com.bean.User,java.util.*" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>

<!-- 新 Bootstrap 核心 CSS 文件 -->
<link rel="stylesheet" href="css/bootstrap.min.css">
</head>
<body>
  <div class="container">
    <table class="table table-striped table-bordered table-hover table-condensed table-responsive">
       <tr><th>ID</th><th>Name</th><th>Age</th><th>Sex</th></tr>
       <%
         List<User> users=(List<User>)request.getAttribute("users");
         if(null!=users&&users.size()>0)
         {
        	 int index=1;
        	 for(User u:users)
        	 {
        		 %>
        		  <tr>
        		  <td><%=index++ %></td>
        		  <td><%out.println(u.getName()); %></td>
        		  <td><%=u.getAge()%></td>
        		  <td><%="M".equals(u.getSex())?"男":"女" %></td>
        		  </tr>
        		 <% 
        	 }
         }
       %>
       
    </table>
    
    <hr/>
    <%=request.getParameter("pp") %>
  </div>
</body>
</html>