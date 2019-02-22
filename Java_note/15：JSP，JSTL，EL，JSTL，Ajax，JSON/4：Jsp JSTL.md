###  JSTL(JSP标准标签库):是一个JSP标签集合，它封装了JSP应用的通用核心功能

 JSTL 库配置
	下载jakarta-taglibs-standard-1.1.2.zip 包并解压，将jakarta-taglibs-standard-1.1.2/lib/下的两个jar文件
	standard.jar和jstl.jar文件拷贝到/WEB-INF/lib/下

1：核心标签
     关联语法：<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

 1：<c:out>  输出一个表达式的结果
	<c:out value="<string>" default="<string>" escapeXml="<true|false>"/>

	属性 
	value 		要输出的内容 	
	default 	输出的默认值 	
	escapeXml  	是否忽略XML特殊字符,默认值true，忽略
 2：<c:set>   设置变量值和对象属性,可以计算表达式的值
	value 	要存储的值，输出的值 
	var 	存储信息的变量
	scope 	var属性的作用域

 3：<c:if>  标签判断表达式的值，如果表达式的值为 true 则执行其主体内容
	test 	判断条件 
	var 	用于存储条件结果的变量 	
	scope 	var属性的作用域

 4：<c:choose> 		本身只当做<c:when>和<c:otherwise>的父标签
    <c:when> 		<c:choose>的子标签，用来判断条件是否成立
    <c:otherwise> 	<c:choose>的子标签，接在<c:when>标签后，当<c:when>标签判断为false时被执行

	<c:choose>标签与Java switch语句的功能一样，用于在众多选项中做出选择
	switch语句中有case，而<c:choose>标签中对应有<c:when>，switch语句中有default，而<c:choose>标签中有<c:otherwise>

 5：<c:import>	导入一个网页
	url 	待导入资源的URL，可以是相对路径和绝对路径，并且可以导入其他主机资源 	
	var 	用于存储所引入的文本的变量 
	scope 	var属性的作用域 

 6：<c:forEach>  基础迭代标签，接受多种集合类型
	items 		要被循环的信息 	
	begin 		开始的元素，默认0	
	end 		最后一个元素,默认最后一个元素
	step 		每一次迭代的步长 	
	var 		代表当前信息变量名称 	
	varStatus 	代表循环状态的变量名称，有index,end,begin,count
	

2：格式化标签：用来格式化并输出文本、日期、时间、数字
   关联语法
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

 1：<fmt:formatDate>   用于使用不同的方式格式化日期
	value	要显示的日期

    <fmt:formatDate type="both" dateStyle="medium" timeStyle="medium"  value="${now}" />


3：JSTL函数  包含一系列标准函数，大部分是通用的字符串处理函数
   关联语法
	<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	
	<c:if test="${fn:contains(<原始字符串>, <要查找的子字符串>)}">
	...
	</c:if>







