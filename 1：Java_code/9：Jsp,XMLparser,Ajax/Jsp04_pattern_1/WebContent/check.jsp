<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@page import="com.utils.MD5,com.utils.ConnectionUtils,java.sql.*,com.bean.*"%>

<%--实例化对象，参数 setXXX() --%>
<jsp:useBean id="admin" class="com.poji.Admin"></jsp:useBean>
<jsp:setProperty property="*" name="admin"/>

<%
	//把业务写到纯Java执行
   	com.bean.CheckLoginUtils.checkLogin(admin,pageContext);
%>
