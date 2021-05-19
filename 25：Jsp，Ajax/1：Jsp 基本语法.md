###  JSP ( Java Server Pages )：

​	Jsp 就是在 html 文件中嵌入  java 代码  jsp = html+java，其实就是运行时的 servlet

- Jsp 在运行时，自动转换为一个 Servlet 类
- Jsp 在运行时，是运行对应的 Servlet 的 XXX_jspservice 方法 	

### 1：Jsp 转换为  Servlet 

JSP文件在 Jsp 网页被访问时，会被 Jsp 引擎生成对应的 Java 文件，Index.jsp--index_jsp.java

1. html 的代码会自动拼成 out.write(“html 代码”);
2. <%   %> 中的代码原样输出
3. Jsp 转为 java 类有一定模版，按照模板固定的加载方式
4. Jsp 代码会转为 java 文件中 _jspservice( ) 的一部分，每个Jsp 对应一个Servlet
5. 对于Tomcat而言，JSP页面生成的 Servlet 放在work路径对应的Web应用下

### 2：Jsp 转换对应的 Java 类

- 会继承 org.apache.jasper.runtime.HttpJspBase 这个类由 tomcat 提供，它继承于 HttpSevlet,实现了HttpJspPage 接口，所以说 **HttpJspBase 是一个符合 jsp 规范的 Servlet**
- Jsp 转换成的 java 类 默认导入了四个包：
  - import javax.servlet.* ;
  - import javax.servlet.http. *;
  - import javax.servlet.jsp. *;
  - import java.lang. *;

### 3：Jsp 的规范

jsp 转换成的 java 类时，一定要实现 javax.servlet.jsp.JspPage 接口，这个接口就是实现了 Servlet 接口（子接口)

###### JspPage(Interface)：实现了Servlet接口

- void	jspInit() 
  	    Jsp文件被转化成servlet，调用
- void	jspDestroy() 
  	    Jsp网页被从servlet容器中移除时，销毁

###### HttpJspPage (InterFace) : JspPage 的子接口其中有方法：

- void	_jspService  (HttpServletRequest request, HttpServletResponse response)
  	相当于 servlet 的 services()

### 4：Jsp 对应的Java类示例

```java
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class index_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {
  //导入的包
  private static final java.util.Set<java.lang.String> _jspx_imports_packages;
	//导入的字节码文件
  private static final java.util.Set<java.lang.String> _jspx_imports_classes;
  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }
	//初始化时调用
  public void _jspInit() {
  }
	//销毁对象时调用
  public void _jspDestroy() {
  }
	//转换的对应的Servlet
  public void _jspService(final HttpServletRequest request, final HttpServletResponse response) throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
		//八个内置对象，exception
    final javax.servlet.jsp.PageContext pageContext;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, false, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      out = pageContext.getOut();
      _jspx_out = out;
			//输出的html代码
      out.write("\r\n");
      out.write("\r\n");
			request.setAttribute("tomcatExamplesUrl", "/examples/");
      out.write("\r\n");
   
    } catch (java.lang.Throwable t) {
         throw new ServletException(t);
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
```



### 3：Jsp 的执行流程

1. 用户直接请求 *.jsp文件
2. 如果用户是第一次访问，或者这个 jsp 文件内容发生修改（查看修改时间）,则执行 3，如果用户是第 n 次访问，且这个 jsp 文件内容没有发生修改，执行 7
3. 将 jsp 文件转换成 java 类
4. 编译 java 文件为 class 文件
5. 实例化 jsp 对应 servlet 对象
6. 调用 jspinit()方法,初始化
7. 生成 HttpServletRequest 和 HttpServletResponse 调用 jspservice 方法
8. Tomcat 宕机时，才调用 jspdestroy( ),销毁对象



### 4：Jsp 的基本用法

##### 1：Jsp 注释

​	<%--  Jsp注释  --%>

##### 2：Jsp 指令

- ###### Page  指令

  - 用于在 jsp 转换 java 类时提供一些配置或参数: <%@ page 属性 1=“值 1” 属性 2="值 2" ....%>
  - import：用来导入你需要的包	

```jsp
<%-- 一定要编写，否则会出现乱码--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%-- import 用来导包--%>
<%@page import="java.sql.*,com.bean.*"%>
```

- ###### include  指令

  - 静态包含：在 jsp 转换成 java 文件时遇到**<%@ include file=”aa.html”%>,**那么就把aa.html 文件的内容，直接转换到 java 文件中,只在转换阶段包含，被包含文件中不要出现<html> <head><title><body>，否则会导致两个Html页面，出现Error

```jsp
<%@ include file = "relativeURI"%>                   是在翻译阶段执行
<jsp:include page ="relativeURI" flush="true" />  在请求处理阶段执行.
```

- ###### taglib 指令

  - 引入标签库的定义，可以是自定义标签，prefix="给标签库起前缀名" ，uri="标签描述文件的uri " 

```jsp
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set value="${stu.name}" var="sname"> </c:set>
```

##### 4：Jsp 异常处理

- errorPage------isErrorPage :属性成对使用
  - <%@ page errorPage="ShowError.jsp" %>
- 如果是错误页面跳转到ShowError.jsp
  - <%@ page isErrorPage="true" %>

##### 5：Jsp 动作元素

| 语法            | 描述                           |
| --------------- | :----------------------------- |
| jsp:include     | 在页面被请求的时候引入一个文件 |
| jsp:useBean     | 寻找或者实例化一个JavaBean     |
| jsp:setProperty | 设置JavaBean的属性             |
| jsp:getProperty | 输出某个JavaBean的属性         |
| jsp:forward     | 把请求转到一个新的页面         |

```jsp
<jsp:include page="相对 URL 地址" flush="true" />
<%-- 实例化class 对应的类--%>
<jsp:useBean id="myUser" class="com.xupt.main.UserBean（绝对路径名）" />
	<jsp:setProperty  name="myUser" property="name" value="lisi"/> 
	<jsp:getProperty  name="myUser" property="name"/>

<jsp:forward page="welcome.jsp" />
```

###### 所有的动作要素都有两个属性：id 和 scope

- id属性：

  id属性是动作元素的唯一标识，可以在JSP页面中引用。动作元素创建的id值可以通过PageContext来调用

- scope属性：

  该属性用于识别动作元素的生命周期， scope属性有四个可能的值： (a) page, (b)request, (c)session, 和 (d) application

##### 6：声明

- <%! 变量或方法的声明 %>
- <%! %> 中的内容会在 jsp 转换成 java 文件时，生成对应的成员变量或成员方法（全局），用来增加方法或属性

##### 7：脚本

​	<%
​		Java 代码
​	%>
注意：html 代码作为 java 代码的语句体时，一定要用{ } ，因为一行 html 代码可能要生成多行 java 代码

##### 8：表达式

- <%= 表达式   %>，注意其间没有分号;  被转化为String
- 等价于
  ​		<%
  ​	             out.println(表达式);
  ​		%>

##### 9：Jsp 内置对象 ( 9 个)

​	就是 jspService 方法模版中定义的形参和局部变量，总共9个

| **对象**    | **描述**                                                     |
| :---------- | :----------------------------------------------------------- |
| request     | **HttpServletRequest** 接口的实例                            |
| response    | **HttpServletResponse** 接口的实例                           |
| out         | **JspWriter**类的实例，用于把结果输出至网页上                |
| session     | **HttpSession**类的实例                                      |
| application | **ServletContext**类的实例，与应用上下文有关                 |
| config      | **ServletConfig**类的实例                                    |
| pageContext | **PageContext**类的实例，提供对JSP页面所有对象以及命名空间的访问 |
| page        | 类似于Java类中的this关键字                                   |
| Exception   | **Exception**类的对象，代表发生错误的JSP页面中对应的异常对象 |



### 4：JspContext 和 PageContext  

```java
public abstract class PageContext extends JspContext
//所以用 PageContextP
// pageContext：封装了servlet 的内置对象（request,response,session,servletContext,servletConfig,out，page），有类似一个map
```

###### 字段：

- PAGE_SCOPE，REQUEST_SCOPE，SESSION_SCOPE， APPLICATION_SCOPE

###### 方法：

- abstract  java.lang.Object	getAttribute(java.lang.String name) 
- abstract  java.lang.Object	findAttribute(java.lang.String name) 
  - 依次在 pageContext.getAttribute() 、 reqeust.getAttribute()、session.getAttribute()、application.getAttribute()中查找，找到即返回

- abstract  void	setAttribute(java.lang.String name, java.lang.Object value) 
- abstract  void	removeAttribute(java.lang.String name) 

- abstract  void	forward(java.lang.String relativeUrlPath) 
- abstract  void	include(java.lang.String relativeUrlPath) 



### 5：定时刷新页面（文字直播）

使用response对象的setIntHeader()方法

- public void setIntHeader(String header, int headerValue)

```Java
// 5秒刷新一次
response.setIntHeader("Refresh", 5);
```



### 6：Jsp示例

```jsp
<%@ page language= "java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.lang.*,java.util.*" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%--使用getParamter获取 POST 或者GET 传参 --%>
<%
   String uname = request.getParameter("uname");
   String upwd = request.getParameter("upwd");
   if("admin".equals(uname)&&"123".equals(upwd) {
	   session.setAttribute("loged", uname);
	   RequestDispatcher rd = request.getRequestDispatcher("success.jsp");
	   rd.forward(request, response);
   } else {
	   response.sendRedirect("index.jsp");
   }
%>
  
<%
   Cookie url = new Cookie("url",request.getParameter("url"));
   // 设置cookie过期时间为24小时
   url.setMaxAge(60*60*24);s 

   // 在响应头部添加cookie
   response.addCookie( url );
   Cookie[] cookies = null;
   // 获取cookies的数据,是一个数组
   cookies = request.getCookies();
%>
</body>
</html>
```


