### EL（Expression Language）: JSTL1.0中定义，JSP2.0引入，访问存储在JavaBean中的数据

##### 特点

1:具有可获得的名称空间（PageContext属性） 2:具有嵌套的属性，可以访问集合对象 3:可以执行关系的、逻辑的算术的运算 4:扩展函数可以和Java类的静态方法映射 5:可以访问JSP内置对象

##### 1:EL表达式的表示

```
$ {"xxx"}  如{username}, ${username.firstName}
```

##### 2:算术运算符

```
+,-,*,/,%	 ${3+3},6
```

##### 3:关系运算符

```
==,!=,<,>,<=,>=     ${3<=3},true
```

##### 4:逻辑运算符

```
&&(and),||(or),!(not)    ${A&&B},ture
```

##### 5:条件运算符和Empty操作符

```
	条件运算符： $ {A>B?B:C}
	Empty操作符：  ${emptyB}
	判断一个值是否为null
```

##### 6:隐含对象（内置对象）

与范围有关的隐含对象

```
applicationScope , application范围内的scoped变量组成的集合 
sessionScope  ,所有会话范围的对象的集合 
requestScope  ,所有请求范围的对象的集合 
pageScope     ,页面范围内所有对象的集合
```

其他隐含对象

```
cookie        ,所有cookie组成的集合
header        ,HTTP请求头部，字符串 
headerValues  ,HTTP所有请求头部，字符串集合 
initParam     ,全部应用程序参数名组成的集合 
pageContext   ,当前页面的javax.servlet.jsp.PageContext对象 
```

与输入有关的隐含对象

```
param       ,所有请求参数字符串组成的集合 
paramValues ,所有请求参数字符串值的集合
   param和paramValues对象用来访问参数值，通过使用request.getParameter方法和request.getParameterValues方法
   ${param["username"]}
   ${header["user-agent"]}
```

##### 7:自定义变量的查找

通过PageContext.getAttribute(String)方法来完成的 表达式将按照page,request,session,application范围的顺序查找username,如果没有找到将返回null 我们也可以利用pageScope、requestScope、sessionScope和applicationScope指定范围,如：${requestScope.username}

##### 8:函数

JSP EL允许您在表达式中使用函数,但是这些函数必须被定义在自定义标签库中,它的函数必须是静态的 ${ns:func(param1, param2, ...)}