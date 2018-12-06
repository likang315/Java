<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" errorPage="e404.jsp" import="com.bean.User" %>
<%@ page import="java.util.*,java.text.*" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jsp的基本语法</title>
</head>
<body>

<%!
   //声明
   public String toStr(String str)
   {
	return "&lt;&lt;"+str+"&gt;&gt;";
   }
%>
<%! int i=123;%>
  
  <%@ include file="header.jsp" %>
  <h1>这是页面内容</h1>
  
  
  <%
  
    List<User> users=new ArrayList<User>();
    users.add(new User("张三",22,"F"));
    users.add(new User("小明",20,"F"));
    users.add(new User("丽莎",21,"M"));
    users.add(new User("张华",26,"F"));
    users.add(new User("小强",23,"M"));
    
    request.setAttribute("users", users);//集合作为属性
  %>
  

  <center>
    <jsp:forward page="show.jsp">
       <jsp:param  name="pp" value="aa"/>
    </jsp:forward>
  </center>
</body>
</html>