<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
   String uname=request.getParameter("uname");
   String upwd=request.getParameter("upwd");
   if("admin".equals(uname)&&"123".equals(upwd))
   {
	   session.setAttribute("loged", uname);
	   
	   request.setAttribute("uname", uname);
	   RequestDispatcher rd=request.getRequestDispatcher("success.jsp");
	   rd.forward(request, response);
   }else
   {
	   response.sendRedirect("index.jsp");
   }
%>
</body>
</html>