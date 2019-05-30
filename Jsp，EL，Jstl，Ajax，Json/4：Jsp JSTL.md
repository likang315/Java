### JSTL （JSP标准标签库） : 

是一个JSP标签集合，它封装了JSP应用的通用核心功能，比如迭代，条件判断，XML文档操作，国际化标签，SQL标签等

### 1：JSTL 库配置

- 下载地址：http://archive.apache.org/dist/jakarta/taglibs/standard/binaries/
- 下载 jakarta-taglibs-standard-1.1.2.zip  包并解压，将 jakarta-taglibs-standard-1.1.2/lib/下的两个jar文件 **standard.jar 和 jstl.jar** 包拷贝到/WEB-INF/lib/下
- 将 tld 下的需要引入的 tld 文件复制到 WEB-INF 目录下

### 2：核心标签

​	关联语法：<%@ taglib prefix ="c" uri="http://java.sun.com/jsp/jstl/core" %>

##### 1：c:out 输出一个表达式的结果

| value     | 要输出的内容        | 是   | 无           |
| --------- | ------------------- | ---- | ------------ |
| default   | 输出的默认值        | 否   | 主体中的内容 |
| escapeXml | 是否忽略XML特殊字符 | 否   | true         |

```jsp
<c:out value="&lt要显示的数据对象（未使用转义字符）&gt" escapeXml="true" default="默认值"></c:out><br/>
<c:out value="&lt要显示的数据对象（使用转义字符）&gt" escapeXml="false" default="默认值"></c:out><br/>
<c:out value="${null}" escapeXml="false">使用的表达式结果为null，则输出该默认值</c:out><br/>
```

##### 2：c:set 设置变量值和对象属性,用于保存数据

| **属性** | **描述**         | **是否必要** | **默认值** |
| :------- | :--------------- | :----------- | :--------- |
| value    | 要存储的值       | 否           | 主体的内容 |
| var      | 存储信息的变量   | 否           | 无         |
| scope    | var 属性的作用域 | 否           | Page       |

##### 3：c:if ：标签判断表达式的值，如果表达式的值为 true 则执行其主体内容

```jsp
<c:set var="salary" scope="session" value="${2000*2}"/>
<c:if  test="${salary > 2000}">
   <p>我的工资为: <c:out value="${salary}"/><p>
</c:if>
```

##### 4：c:choose 本身只当做 c:when 和 c:otherwise 的父标签



 <c:when> <c:choose>的子标签，用来判断条件是否成立 ? <c:otherwise> <c:choose>的子标签，接在<c:when>标签后，当<c:when>标签判断为false时被执行

<c:choose>标签与Java switch语句的功能一样，用于在众多选项中做出选择 switch语句中有case，而<c:choose>标签中对应有<c:when>，switch语句中有default，而<c:choose>标签中有<c:otherwise>

###### 5：<c:import>	导入一个网页

?	url 待导入资源的URL，可以是相对路径和绝对路径，并且可以导入其他主机资源 ?	var 用于存储所引入的文本的变量 ? scope var属性的作用域

###### 6：<c:forEach> 基础迭代标签，接受多种集合类型

?	items 要被循环的信息 ?	begin 开始的元素，默认0 ?	end 最后一个元素,默认最后一个元素 ?	step 每一次迭代的步长 ?	var 代表当前信息变量名称 ?	varStatus 代表循环状态的变量名称，有index,end,begin,count ?

### 2：格式化标签：用来格式化并输出文本、日期、时间、数字

关联语法 <%@ taglib prefix="fmt" uri="<http://java.sun.com/jsp/jstl/fmt>" %>

###### 1：fmt:formatDate 用于使用不同的方式格式化日期

?	value	要显示的日期

<fmt:formatDate type="both" dateStyle="medium" timeStyle="medium" value="${now}" />

###### 3：JSTL函数 包含一系列标准函数，大部分是通用的字符串处理函数

关联语法 <%@ taglib prefix="fn" uri="<http://java.sun.com/jsp/jstl/functions>" %>

```
<c:if test="${fn:contains(<原始字符串>, <要查找的子字符串>)}">
...
</c:if>
```