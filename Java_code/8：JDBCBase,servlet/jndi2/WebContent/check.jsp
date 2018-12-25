<%@page import="com.utils.MD5"%>
<%@page import="com.utils.ConnectionUtils,java.sql.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 

<%
   String uname=null!=request.getParameter("uname")?request.getParameter("uname"):"";
   String upwd=null!=request.getParameter("upwd")?request.getParameter("upwd"):"";
   com.utils.CheckLoginUtils.checkLogin(uname, upwd, pageContext);
%>
