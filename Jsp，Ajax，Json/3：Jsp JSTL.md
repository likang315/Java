### JSTL （JSP标准标签库） : 

是一个JSP标签集合，它封装了JSP应用的通用核心功能，比如迭代，条件判断，XML文档操作，国际化标签，SQL标签等

### 1：JSTL 库配置

- 下载地址：http://archive.apache.org/dist/jakarta/taglibs/standard/binaries/
- 下载 jakarta-taglibs-standard-1.1.2.zip  包并解压，将 jakarta-taglibs-standard-1.1.2/lib/下的两个jar文件 **standard.jar 和 jstl.jar** 包拷贝到/WEB-INF/lib/下
- 将 tld 下的需要引入的 tld 文件复制到 WEB-INF 目录下
- 也可以直接通过Maven 直接导入

### 2：核心标签

​	关联语法：<%@ taglib prefix ="c" uri=" http://java.sun.com/jsp/jstl/core" %>

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

- c:choose：标签与Java switch语句的功能一样，用于在众多选项中做出选择
- c:when： 用来判断条件是否成立 
- c:otherwise：用在<c:when>标签后，当所有的 c:when 标签判断为 false 时被执行

```jsp
<c:set var="salary" scope="session" value="${2000*2}"/>
<c:choose>
    <c:when test="${salary <= 0}">
       太惨了。
    </c:when>
    <c:when test="${salary > 1000}">
       不错的薪水，还能生活。
    </c:when>
    <c:otherwise>
        什么都没有。
    </c:otherwise>
</c:choose>
```

##### 5：c:import	导入一个网页

- url ：待导入资源的URL，可以是相对路径和绝对路径，并且可以导入其他主机资源
- var：用于存储所引入的文本的变量

##### 6：c:forEach 基础迭代标签，接受多种集合类型

| **属性**  | **描述**                                 | **是否必要** | **默认值**   |
| :-------- | :--------------------------------------- | :----------- | :----------- |
| items     | 要被循环的信息                           | 否           | 无           |
| begin     | 开始的元素（0=第一个元素，1=第二个元素） | 否           | 0            |
| end       | 最后一个元素                             | 否           | Last element |
| step      | 每一次迭代的步长                         | 否           | 1            |
| var       | 代表当前条目的变量名称                   | 否           | 无           |
| varStatus | 用于指定循环的状态  ( 有固定的四个值)    | 否           | 无           |

```jsp
<c:forEach vatrStatus = "i" var="i" begin="1" end="5">
   Item <c:out value="${i}"/><p>
</c:forEach>
```

##### 7： c:redirect ：重定向

​	标签通过自动重写URL来将浏览器重定向至一个新的URL

```jsp
<c:redirect url="http://www.runoob.com"/>
```

