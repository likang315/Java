### 自定义标签：

​	当JSP页面包含一个自定义标签时将被转化为servlet，标签转化为对被 称为tag handler的对象的操作

### 1：引入标签库

```jsp
<%-- 使用此方式和 xx.tld(标签描述文件) 关联--%>
<%@ taglib prefix="my" uri=" http://www.xxx.com/tags " %> 

<tag>
  <name>hello</name>
  <tag-class>com.tags.HelloTag</tag-class>
  <body-content>empty</body-content>
  <attribute>
    <name>count</name>
    <required>true</required>
    <rtexprvalue>true</rtexprvalue>
  </attribute>
</tag>
```

标签处理器：是 java类，实现了规范

javax.servlet.jsp.tagext

### 1：Interface    JspTag ：所有自定义标签必须实现的接口

![]()

### 2：Interface    Tag： JspTag 的子接口

###### Field

- static int    EVAL_BODY_INCLUDE ：还有实体需要处理
- static int	SKIP_BODY ：没有实体处理 
- static int	EVAL_PAGE ：继续处理页面 
- static int	SKIP_PAGE  ：不在处理页面

###### Method 

- int	doStartTag() ：开始处理标签
- int	doEndTag() ：结束处理标签
- Tag	getParent() ：得到父标签 
- void	setParent(Tag t) ：设置父标签
- void	setPageContext(PageContext pc) ：设置当前的页面环境
- void	release() 释放所有资源

```java
@Override
public int doStartTag() throws JspException {
  	System.out.println("doStartTag..............");
  	//没有实体处理
  	return Tag.SKIP_BODY;
}
```

### 3：SimpleTag：JspTag 的子接口，方便输出

```java
@Override
//方便输出，直接处理
public void doTag() throws JspException, IOException {
  StringBuilder sb=new StringBuilder();
  PageContext pc = (PageContext)this.getJspContext();
  String path = pc.getServletContext().getContextPath();
  sb.append(path);
  this.getJspContext().getOut().print(sb.toString());
}
```

### 4：javax.servlet.jsp.tagext  Class   SimpleTagSupport

- void	doTag() ：处理标签 
- protected JspContext	getJspContext() ：得到其JspContext

### 5：IterationTag：Tag的子接口，用来迭代实体内容

- static int	EVAL_BODY_AGAIN ：继续迭代实体内容
- int	doAfterBody( ) ：迭代处理实体内容，返回Tag的属性，有不同的作用

### 6：javax.servlet.jsp.tagext  Class    BodyTagSupport



### 7：BodyTag：实体标签接口

- static int	EVAL_BODY_BUFFERED ：缓存,创建BodyContent 
- static int EVAL_BODY_TAG：执行body
- void	setBodyContent(BodyContent b) ：设置BodyContent，实体内容
- void	doInitBody() ：初始化实体标签

### 8：TagSupport ：实现了IterationTag 接口

- protected PageContext	pageContext 
- int	doStartTag()
- int	doAfterBody()
- int	doEndTag() ? return EVAL_PAGE
- Tag	getParent()
- void	setParent(Tag t) 

### 9：BodyTagSupport：

​	实现了BodyTag 接口，写标签处理器时，直接继承它，重写我们需要的方法

### 10：实现其接口时，参考

![](https://github.com/likang315/Java-and-Middleware/blob/master/Jsp%EF%BC%8CEL%EF%BC%8CJstl%EF%BC%8CAjax%EF%BC%8CJson/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9/BodyTag.png?raw=true)