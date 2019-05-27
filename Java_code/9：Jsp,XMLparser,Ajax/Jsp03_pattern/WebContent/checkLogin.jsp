<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="com.login.LoginUtils"%>

<%
	//测试findAttribute()
	pageContext.setAttribute("aa", "page_aaa");
	request.setAttribute("aa", "request_aaa");
	session.setAttribute("aa", "session_aaa");
	application.setAttribute("aa", "application_aaa");

    LoginUtils.checkLogin(pageContext);//调用Java bean 完成业务
%>
