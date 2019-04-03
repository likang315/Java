<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<center>
  <h1>EL表达式</h1>
  <%session.setAttribute("aa", "bb"); %>
  <%pageContext.setAttribute("msg", "^_^");%>  

  <ul>
    <li>${msg}</li>
    <li>${stu.name}</li>
    <li>${requestScope.msg}</li>
    
    <li>${stu.name=="李四"}</li>
    <li>${stu.name=='李四'}</li>
    
    <li>${stu.age+20}</li>
    <li>${12>10&&stu.age>10}</li>
    <li>${sex=="M"?"男":"女" }</li>
    
    <li>${cookie['JSESSIONID'].value }</li>
    <li>${cookie.JSESSIONID.value }</li>
    
    <li>${header['Accept-Encoding']}</li>
    <li>${headerValues['accept-encoding'][0]}</li>
    
    <li>${param['hello']}</li>
    
    <li>${initParam['hello']}</li>
  </ul>
</center>
</body>
</html>