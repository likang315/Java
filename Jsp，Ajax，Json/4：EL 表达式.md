### EL（Expression Language）: 访问存储在JavaBean中的数据

- 既可以用来创建算术表达式也可以用来创建逻辑表达式。
- EL表达式内可以使用整型数，浮点数，字符串，常量，true、false，null
- 在JSP EL中通用的操作符是 **.** 和 **{ }** 

##### 1：EL表达式的使用前提

```jsp
<%@ page isELIgnored ="true|false" %>
```

##### 2：EL表达式的表示

JSP编译器在属性中见到"${}"格式后，它会产生代码来计算这个表达式，并且产生一个替代品来代替表达式的值

```jsp
$ {"xxx"} 
${username.firstName}
${lists[i]}  //访问数组或者集合
```

##### 2：算术运算符

```jsp
+,-,*,/,%	 ${3+3},6
```

##### 3：关系运算符

```jsp
==,!=,<,>,<=,>=     ${3<=3},true
```

##### 4：逻辑运算符

```jsp
&&(and),||(or),!(not)    ${A&&B},ture
```

##### 5：条件运算符和Empty操作符

```jsp
	条件运算符： $ {A>B?B:C}
	Empty操作符：  ${empty B}
	判断一个值是否为null 或者 "" 都返回true
```

##### 6：隐含对象（内置对象）

可以在表达式中使用这些对象，就像使用变量一样

```html
<p> ${pageContext.request.queryString} </p>
```

| **隐含对象**     | **描述**                      |
| :--------------- | :---------------------------- |
| pageScope        | page 作用域                   |
| requestScope     | request 作用域                |
| sessionScope     | session 作用域                |
| applicationScope | application 作用域            |
| param            | Request 对象的参数，字符串    |
| paramValues      | Request对象的参数，字符串集合 |
| header           | HTTP 信息头，字符串           |
| headerValues     | HTTP 信息头，字符串集合       |
| initParam        | 上下文初始化参数              |
| cookie           | Cookie值                      |
| pageContext      | 当前页面的pageContext         |