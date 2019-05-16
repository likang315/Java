### 自定义标签：采用标签的结构，不是预定义的标签，也可定义函数

<%@ taglib prefix="my" uri=" <http://www.xxx.com/tags> " %> 使用此方式和 .tld （标签描述文件) 关联

标签处理器：是 java类，实现了规范

javax.servlet.jsp.tagext

### 1：JspTag(Interface):所有自定义标签必须实现的接口

?

### 2：Tag(Interface):JspTag 的子接口

Field static int	EVAL_BODY_INCLUDE 还有实体需要处理. static int	SKIP_BODY 没有实体处理 static int	EVAL_PAGE 继续处理页面 static int	SKIP_PAGE 不在处理页面

Method void	setPageContext(PageContext pc) 设置当前的页面环境 int	doStartTag() 开始处理标签

int	doEndTag() 结束处理标签

Tag	getParent() 得到父标签 void	setParent(Tag t) 设置父标签

void	release() 释放所有资源

### 3：SimpleTag：JSPTag的子接口，方便输出

### 4：IterationTag(Interface)：Tag的子接口，用来迭代实体内容

static int	EVAL_BODY_AGAIN 继续迭代实体内容

int	doAfterBody() 迭代处理实体内容，返回Tag的属性，有不同的作用

### 5：BodyTag(Interface): 实体标签接口， extends IterationTag

? Filed ?	static int	EVAL_BODY_BUFFERED ?	缓存,创建BodyContent ? Method ?	void	setBodyContent(BodyContent b) ? 设置BodyContent，实体内容 ?	void	doInitBody() ?	初始化实体标签

### 6：BodyContent(class) :封装了实体内容

abstract java.lang.String	getString() 得到实体内容

JspWriter	getEnclosingWriter() 得到JspWriter

### 7：TagSupport(class): 实现了IterationTag 接口

属性： protected PageContext	pageContext 方法： int	doStartTag()

?	int	doAfterBody()

?	int	doEndTag() ? return EVAL_PAGE

?	Tag	getParent()

```
void	setParent(Tag t) 
```

### 8：BodyTagSupport(Class):实现了BodyTag接口和其方法，写标签处理器时,直接继承它,重写需要的方法

Filed protected BodyContent	bodyContent 封装自定义标签实体内容 Method

int	doStartTag() Returns:EVAL_BODY_BUFFERED. int	doAfterBody() Returns:SKIP_BODY int	doEndTag() Returns: EVAL_PAGE.

void	doInitBody() evaluation: no action

BodyContent	getBodyContent()

JspWriter	getPreviousOut()

void	setBodyContent(BodyContent b)

void	release()

### 9：SimpleTagSupport (class)：实现 SimpleTag ，输出实体内容时，使用最为方便

void	doTag() 处理标签 protected JspContext	getJspContext() 得到其JspContext