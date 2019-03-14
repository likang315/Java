##  JSP(Java Server Pages)：

### Jsp 就是在 html 文件中嵌入 java 代码 jsp=html+java，其实就是运行时的 servlet

**Jsp 在运行时，自动转换为一个 Servlet 类**
Jsp 在运行时，是**运行对应的 Servlet 的 _jspservice 方法** 	
 <%--  Jsp注释  --%>

### 1：Jsp 转换为  Servlet：JSP文件在 Jsp 网页被访问时，会被Jsp引擎生成Java文件，Index.jsp--index_jsp.java

​	1：html的代码会自动拼成 out.write(“html 代码”);
​	2：<% %> 中的代码原样输出

###### ​	3：Jsp 转为 java 类有一定模版，按照模板固定的加载方式

###### 	4：Jsp 代码会转为 java 文件中 _jspservice() 的一部分



### 转换的Java类：

1：会继承org.apache.jasper.runtime.HttpJspBase 这个类由 tomcat 提供，它继承于 HttpSevlet,实现了HttpJspPage 接口，所以说 HttpJspBase 是一个符合 jsp 规范的 Servlet
2：Jsp 转换成的 java 类默认导入了四个包：
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;
import java.lang.*;

### 2：Jsp的规范

jsp 转换成的java 类时，一定要实现 javax.servlet.jsp.JspPage 接口，这个接口就是实现了Servlet 接口（子接口)

JspPage(Interface):实现了Servlet接口
void	jspInit() 
	    Jsp文件被转化成servlet，调用
void	jspDestroy() 
	    Jsp网页被从servlet容器中移除时，销毁

HttpJspPage(InterFace) : JspPage 的子接口其中有方法：
void	_jspService(HttpServletRequest request, HttpServletResponse response)
	相当于servlet 的services()

### 3：Jsp 的执行流程

Step1:用户直接请求 *.jsp文件
Step2:如果用户是第一次访问，或者这个 jsp 文件内容发生修改（查看修改时间）,则执行 step3
	如果用户是第 n 次访问，且这个 jsp 文件内容没有发生修改，执行 step7
Step3:将 jsp 文件转换成 java 类
Step4:编译 java 文件为 class 文件
Step5:实例化 jsp 对应 servlet 对象
Step6:调用 jspinit()方法,初始化
Step7:生成 HttpServletRequest 和 HttpServletResponse 调用 jspservice 方法
Step8:Tomcat 宕机时，才调用 jspdestroy(),销毁对象

### 4：Jsp 的基本用法

###### 1：Page  指令

用于在 jsp 转换 java 类时提供一些配置或参数:
<%@ page 属性 1=“值 1” 属性 2="值 2" ....%>
注意：
	1:import:用来导入你需要的包	

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@page import="com.utils.MD5,com.utils.ConnectionUtils,java.sql.*,com.bean.*"%>
```

###### 2：include  指令

<%@ include file="被包含的文件名" %>

这种包含叫**静态包含：**在 jsp 转换成 java 文件时遇到**<%@ include file=”aa.html”%>,**那么就把aa.html 文件的内容
直接转换到 java 文件中,只在转换阶段包含，被包含文件中不要出现<html> <head><title><body>，否则会导致两个Html页面

###### 3：taglib 指令

​	<%@ taglib ... %> 	
​		引入标签库的定义，可以是自定义标签，prefix="" 给标签库起名字，uri="" 标签描述文件的uri 

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 <c:set value="${stu.name}" var="sname"></c:set>
```

###### 4：Jsp异常处理,page 指令中

​	errorPage------isErrorPage :属性成对使用
​	<%@ page errorPage="ShowError.jsp" %>
​		如果是错误页面跳转到ShowError.jsp
​	<%@ page isErrorPage="true" %>

###### 5：声明

<%! 变量或方法的声明 %>

写在 <%! %> 中的内容会在 jsp 转换成 java 文件时，生成对应的成员变量或成员方法（全局），用来增加方法或属性

###### 6：脚本

​	<%
​		Java 代码
​	%>
注意：html 代码作为 java 代码的语句体时，一定要用{} ，因为一行 html 代码可能要生成多行 java 代码

###### 7：表达式

​	<%=表达式%>，注意其间没有分号; ，被转化为String
​	
​	等价于
​		<%
​	             out.println(表达式);
​		%>

###### 8：Jsp 动作元素

​	< jsp:forward/>  < jsp:include/>    < jsp:param/>   -----传参数的
	

```jsp
	<jsp:forward page="welcome.jsp">
		<jsp:param value="haha" name="hi"/>
		<jsp:param value="hee" name="ha"/>
	< /jsp:forward>	
	等价于
	RequestDispatcher rd=request.getRequestDispatcher(“welcome.jsp?hi=haha&ha=hee”);
	Rd.forward(request,response)
```

###### ​9：Jsp:usebean 	

​	当页面上表单项比较多时，提交给 jsp，jsp 需要很多条 request.getParamter 方法来获取这些值，很麻烦	<jsp:userbean id="" class="类名">   这个标签是实例对象的
< jsp:setProperty>   是将用户请求参数绑定到指定对象的相应属性上
< jsp:getProperty>   是取指定对象的指定属性的值	

<jsp:setProperty property="name" name="user" param="uname"/>    name="" :为指定对象id
<jsp:getProperty property="sex" name="user"/>

### 10：Jsp 内置对象 

Jsp 内置对象：就是 jspService 方法模版中定义的形参和局部变量，总共9个
		javax.servlet.http.**HttpServletRequest** request
		javax.servlet.http.**HttpServletResponse** response
 		javax.servlet.jsp.**PageContext** pageContext
 		javax.servlet.http.HttpSession **session** = null
 		javax.servlet.ServletContext **application** ;     与上下文环境有关
 		javax.servlet.ServletConfig **config** ;
 		javax.servlet.jsp.JspWriter **out**
 		java.lang.Object page = this;

###### ​	         	Exception 	Exception类的对象，代表发生错误的JSP页面中对应的异常对象

**记 住 ： 我 们 在 jsp 文 件 中 可 以 直 接 使 用 上 以 对 象**

(request,response,pageContext,session,application,config,out,page)

###### 11：JspContext(class) 和 PageContext(class)

```java
public abstract class PageContext extends JspContext，所以用 PageContext
```

  PageContext：封装了servlet的内置对象（request,response,session,servletContext,servletConfig,out，page），有类似一个map

字段：
	PAGE_SCOPE，REQUEST_SCOPE，SESSION_SCOPE， APPLICATION_SCOPE
方法：

​	abstract  java.lang.Object	getAttribute(java.lang.String name) 
​	abstract  java.lang.Object	findAttribute(java.lang.String name) 

​	abstract  void	setAttribute(java.lang.String name, java.lang.Object value) 
​	abstract  void	removeAttribute(java.lang.String name) 

​	abstract  void	forward(java.lang.String relativeUrlPath) 
​	abstract  void	include(java.lang.String relativeUrlPath) 

  注意：
abstract  java.lang.Object	findAttribute(java.lang.String name)
	    pageContext.findAttribute("aa") ,依次 在 pageContext.getAttribute() 、 reqeust.getAttribute()、
	    session.getAttribute()、application.getAttribute()中查找 aa,找到即返回

pageConext.getAttribute(param,int scope)
pageConext.getAttribute("aa" ,PageContext.SESSION_SCOP) 相 当 于   session.getAttribute("aa")

######  page 对象：页面实例的引用，它可以被看做是整个JSP页面的代表(this)




​	