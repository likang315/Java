<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>

<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<center>

<h1>JSTL</h1>
 	 <c:set value="${stu.name}" var="sname"></c:set>
 	 <c:set var="now" value="<%=new java.util.Date()%>" ></c:set>
   <ul>
     <li>${sname}</li>
     
     <li><c:out value="${msg}"></c:out></li>
     <li><c:out value="${aaa}" default="null"></c:out></li>
     <li><c:out value="${msg}" escapeXml="false"></c:out></li>
    
     <li><c:if test="${sex=='M'}">男</c:if></li>
     
     <li>
       <c:choose>
          <c:when test="${sex=='M'}">男</c:when>
          <c:when test="${sex=='F'}">女</c:when>
          <c:otherwise>
           otherwise
          </c:otherwise>
       </c:choose>
     </li>
   </ul>
   <hr/>
   
   <ul>
    <c:forEach items="${stus}" var="st" varStatus="sts">
       <li>${sts.index}-${st.name}-${st.age}</li>
    </c:forEach>
    
   </ul>
   
 	<ul>
	<li><fmt:formatDate type="both" dateStyle="short" timeStyle="short" value="${now}" /></li>li>
	
	</ul>

  
</center>
</body>
</html>