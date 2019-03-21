## Spring MVC：基于Model2 实现的技术框架

![](F:\note\19：SpringMVC\1：Spring MVC.png)

### 1：MVC request 流程（六大步）

######  1：前端控制器：请求第一个访问Spring 的 DispatcherServlet(前端控制器)

Spring MVC 所有的请求都会通过一个前端控制器（front controller），是常用的 Web 应用程序模式，在这里一个
单实例的Servlet 将请求委托给应用程序的其他组件来执行实际的处理

######  2：控制映射：DispatcherServlet 的任务是将请求发送给 Spring MVC 控制器（controller）

控制器是一个用于处理请求的 Spring 组件,在典型的应用程序中可能会有多个控制器，DispatcherServlet 需要知道应该将
请求发送给哪个控制器,所以 DispatcherServlet会查询一个或多个**处理器映射（handler mapping）**来确定请求的下一站在哪处理器映射会根据请求所携带的 URL 信息来进行决策

######  3：控制器处理：选择合适的控制器，DispatcherServlet 会将请求发送给选中的控制器 

到了控制器，请求会卸下其负载（用户提交的信息）并等待控制器处理这些信息

######  4：模型（model）：控制器在完成逻辑处理后，返回的信息需要给用户并在浏览器上显示的信息

不过仅仅给用户返回原始的信息是不够的——这些信息需要以用户友好的方式进行格式化，一般会是 HTML,所以信息需要发送给一个视图（view），通常会是JSP

######  5：控制器将模型和视图给前端控制器：控制器将模型数据打包，并且标示出用于渲染输出的视图解析器名

控制器会将请求连同模型和视图名发送回 DispatcherServlet，DispatcherServlet 将会使用视图解析器来和逻辑视图名匹配为一个特定的视图实现																														 

###### 6：视图的实现（可能是JSP），在这里它交付模型数据视图将使用模型数据渲染输出，然后通过响应对象传递给客户端



##### 2：Spring MVC 配置（两种Java config 和xml）

xml：

###### 1：在 WEB-INF/lib 中导入 jar

​		spring-web-4.3.7.RELEASE.jar
​		spring-webmvc-4.3.7.RELEASE.jar

###### 2：配置 DispatcherServlet （WEB-INF/web.xml）

```jsp
	<servlet>
		<servlet-name>名字与xxx-servlet.xml 对应</servlet-name>
		<servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
		<init-param>
			<param-name>contextConfigLocation</param-name>	
			<param-value>classpath:dispacter-servlet.xml</param-value>
		</init-param>
		<load-on-startup>1</load-on-startup>
	</servlet>

	<servlet-mapping>
		<servlet-name>名字与xxx-servlet.xml 对应</servlet-name>
		<url-pattern>/</url-pattern>
	</servlet-mapping>
	
	<!--监听器-->
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>

<listener>：会在整个Web应用程序启动的时候运行一次，并初始化传统意义上的Spring的容器
<context-param>：加载全局化配置文件
```

### 3：配置视图解析器（xxx-servlet.xml）

```XML
xmlns:mvc="http://www.springframework.org/schema/mvc"
<bean id="jspViewResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">

 	<property name="prefix" value="/WEB-INF/jsp/"/>
	<property name="suffix" value=".jsp"/>
</bean>	
```

​		

### 4：编写控制器

```java
@controller
@RequestMapping（"/"）
public class HelloWorldController {
		@RequestMapping("/hello.htm")
		public String helloWorld(Model model)
		{
			model.addAttribute("message", "Hello World!");
			return "Hello";
		}
}
```



### 4：@RequestMapping：使用在控制器的类定义 及方法定义处，定义在方法的是在类的URL下的下一层url

 @RequestMapping 不旦支持标准的 URL，还支持 Ant 风格（？、*和**字符）

DispacherServlet 截获请求后，就通过控制器上@RequestMapping 提供的映射信息确定请求所对应的处理方法
将请求映射到控制器处理方法的工作包含一系列映射规则，具体包括请求 URL、请求参数、请求方法、请求头

1：Path 和 value 设置路径   ：@RequestMapping(value = {"/page","page*", "view/*,**/msg"})
2：Method 请求方法 	      ：@RequestMapping(method = RequestMethod.GET) 
3：Headers 报头过滤           ：@RequestMapping(headers = {"content-type=text/plain"})  可以是数组 
4：params 参数过滤 	      ：@RequestMapping(params = {"personId=10"})  可以是数组

### 5：获取请求携带的参数

 @PathVariable：用来获得请求url中的动态参数的
 @RequestParam和@PathVariable：Spring能够根据名字自动赋值对应的函数参数值

```java
 @RequestMapping("/pets/{petId}")															  public void findPet(@PathVariable String ownerId, @PathVariable String petId, Model model)

@RequestHeader ：按请求报头绑定
@CookieValue   ：将参数名帮定了对像属性
@RequestMapping(value = "/handle2")
public ModelAndView handle2(@CookieValue("JSESSIONID") String sessionId,@RequestHeader("Accept-Language") String acctLa)

请求参数按名称匹配的方式绑定到 user 的属性中、方法返回对应的字符串代表逻辑视图名
@RequestMapping(value = "/handle3")
public String handle3(User user) 

http://localhost:8080/handle1?userName=zhangsan&password=123&realName=jack
```



### 6： @MatrixVariable：用矩阵变量绑定参数

​	能够将请求中的矩阵变量，绑定到处理器的方法参数中，在 Matrix Variable 中，多个变量可以使用”;”（分号）分隔
​		例如:/books;author=Tom;year=2016
​	如果一个变量对应多个值，那么可以使用”,”(逗号)分隔，
​		例如: author=smart1,smart2,smart3 或者使用重复变量名：使如：author=smar1;author=smart2;author=smart3

### 7：乱码问题（过滤器）

```xml
<filter>
	<filter-name>characterEncodingFilter</filter-name>
	<filter-class>org.springframework.web.filter.CharacterEncodingFilter</filter-class>
		<init-param>
			<param-name>encoding</param-name>
			<param-value>UTF-8</param-value>
		</init-param>
		<init-param>
			<param-name>forceEncoding</param-name>
			<param-value>true</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>characterEncodingFilter</filter-name>
		<url-pattern>/*</url-pattern>
	</filter-mapping>
</filter>
```



### 8：Spring mvc 支持的方法参数

###### 使用 Servlet API 传入参数时，Spring mvc 将 web 层的 Servlet 对象传递给处理方法，参数顺序没有特殊要求

 	1：HttpServletRequest request，HttpservletResponse response
	2：HttpSession session
	3：InputStream/Reader 对应 request.getInputStream()
	4：OutputStream/Writer 对应 response.getOutputStram()

```JAVA
@RequestMapping(value = "/handle1")
public void handle1(HttpServletRequest request,HttpServletResponse response)
{
    String userName = request.getParameter("userName");
    response.addCookie(new Cookie("userName", userName))
}
```

### 9：HttpMessageConverter<T>：Spring 的接口，它负责将请求信息转换为一个对象（类型为 T）,将对象（类型为 T）输出为响应信息

   org.springframework.http.converter

DispatcherServlet 默认安装了 RequestMappingHandlerAdpter 作为 HandlerAdapter 的组件实现类，
HttpMessageConverter 通过使用 RequestMappingHandlerAdapter 使用，将请求信息转换为对象，或将对象转换为响应信息

 method：

Boolean canRead(Class<?> clazz,MediaType mediaType)
	指定转换器可以读取的对象类型，同时指定支持 MIME 类型
Boolean canWriter(Class<?> clazz,MediaTypemediaType)
	指定转换器可以将 clazz 类型的对象写到响应流中，响应流支持类型在 mediaType 中定义
List<MediaType> getSupportMediaTypes()
	返回该转换器支持的媒体类型

T read(class<? extends T> clazz , HttpInputMessage inputMessage)
	将请求信息流转换成 T 类型的对象
void write(T t , MediaType contentType,HttpOutputMessageoutputMessage)
	将 T 类型的对象写到响应流中，同时指定响应媒体类型为 contentType

   实现类：
    AnnotationMethodHandlerAdapter默认安装了以下实现类

StringHttpMessageConverter：请求信息转为字符串，可读取所有媒体类型(*/*),通过 supportedMediaTypes 属性指定媒体类型
FormHttpMessageConverter：将表单数据读取到 mutiValueMap 中
ByteArrayHttpMessageConverter：读写二进制数据，T 为 byte[]类型，可读取*/*，可通过supportedMediaTypes属性指定媒体类型 ，响应信息媒体类型为application/octer-stream
MappingJacksonHttpMessageConverter：利用jackson 的 ObjectMapper读写 Json 数据，T为 Object，可读取application/json响应媒体类型为 application/json

使用 HttpMessageConverter<T>：将请求信息转换并绑定到处理方法的入参中 
SpringMVC 两种方式：
	使用 @RequestBody/@ResposeBody 对处理方法进行标注
	使用 HttpEntity<T>/ResponseEntity<T> 作为处理方法的入参或返回值



### 10：处理模型数据

​	控制器（C）是为了产生模型数据(M)，而视图(V)则是为了渲染模型数据，使MV而分离

   将模型数据暴露给视图 

##### 1:ModelAndView: 处理方法返回,其中封装了视图字符串，模型相当于request.setArrtibut(),，里面有个modelMap和view

###### 	1：返回指定页面

​	ModelAndView构造方法可以指定返回的页面名称，也可以通过**setViewName()方法**跳转到指定的页面 

###### 	2：返回所需数值

​	使用**addObject()设置**需要返回的值，addObject()有几个不同参数的方法，可以默认和指定返回对象的名字

##### 2:@ModelAttribute:处理方法参数使用该注解后，参数会放到模型中相当于request.setArrtibute()

##### 3:Map 和 Model:

​		处理方法参数为 Model,ModelMap,map 时,Map中的数据会自动加到模型中，相当于 request.setArrtibut()

##### 4:@SessionAttributes：将模型中的某个属性放入 HttpSession



### 11：SpringMVC 对Model ，@ModelAttribute 及@SessionAttributes 和处理流程

1:Spring MVC 在调用（controller）处理的方法前，在请求线程中自动创建一个隐含的模型对象(Model)
2:**调用所有标注了@ModelAttribute的方法，将在所有controller的方法调用前，调用此方法，将方法返回值添加到隐含模型中**

3:查看Session中是否存中@SessionAttributes("xx")所指定的xx属性，如果有，则将其添加到隐含模型中，如果隐含模型中已经有xx属性，则该步操作会覆盖模型中已有的属性值

4:对标注@ModelAttribute(“xx”)处理方法的入参,按如下流程处理(标注在参数)

1:如果隐含模型拥有名为xx的属性,则将隐含模型中的其属性赋给该入参，再用请求消息填充该入参对象直接返回,否则转到2
2:如果 xx是会话属性，即在处理类定义处标注了@SessionAttributes(“xxx”),则尝试从会话中获取该属性，并将其赋给该
 入参，然后再用请求消息填充该入参对象,如果在会话中找不到对应的属性，则抛出 HttpSessionRequiredException 异常，否则转到3
3:如果隐含模型中不存在 xxx 属性，且 xxx 与不是会话属性，则创建入参的对象实例，然后再用请求消息填充该入参

### 12：controller 的方法的返回值类型

###### 1：ModelMap

###### public class ModelMap extends LinkedHashMap<String, Object>

ModelMap对象主要用于传递控制方法处理数据到结果页面，也就是说我们把结果页面上需要的数据放到ModelMap对象中即可，他的作用类似于request对象的setAttribute方法的作用:用来在一个请求过程中传递处理的数据。

```java
public ModelMap addAttribute(String attributeName, Object attributeValue){...}
```
###### 2：Model

###### public class ExtendedModelMap extends ModelMap implements Model

**返回值直接写跳转页面名**，用addAttribute（）添加们key-value

###### 	3：ModelAndView

###### 通过构造方法以指定返回的页面名称，也可以通过setViewName()方法跳转到指定的页面

###### 使用addObject()设置需要返回的值,将值设置到一个名为ModelMap的类属性，返回ModelAndView

ModelAndView的实例是由用户手动创建的，这也是和ModelMap的一个区别

###### 	2：String

​		指定返回的视图页面名称，结合设置的返回地址路径加上页面名称后缀即可访问到	
​	   注意：
​		1：如果方法声明了注解@ResponseBody ，则会直接将返回值输出到页面
​		2："redirect:path,重定向
​		3："forward:path  跳转
​		4："/admin/views/hello"   视图解析器会解析路径字符串

###### 	3：void,map,Model ,ModelMap,String

​		返回对应的逻辑视图名称真实url为：prefix+视图名称 + suffix 组成






