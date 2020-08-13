### Spring-MVC-config

------

##### 1：@RequestMapping：

​	用在**控制器的类及方法的定义处**，定义在方法的是在类的URL下的下一层URL，不旦支持标准的 URL，还支持 Ant 风格（？、*和**字符）

###### 原理：

​	DispacherServlet 截获请求后，通过控制器上@RequestMapping 提供的映射信息确定请求所对应的处理方法
，将请求映射到控制器处理方法的工作包含一系列映射规则，具体包括请求 URL、请求参数、请求方法、请求头

- Path 和 value 设置路径：@RequestMapping(value = {"/page","page*", "view/*,**/msg"})
- Method 请求方法：@RequestMapping(method = RequestMethod.GET)
- Headers 报头过滤：@RequestMapping(headers = {"content-type=text/plain"}) 
- params 参数过滤 ：@RequestMapping(params = {"personId=10"}) ，可以是数组

##### 2：获取请求携带的参数

- @PathVariable：用来获得请求url中的动态参数的
- @RequestParam和@PathVariable：**Spring能够根据名字自动赋值**对应的函数参数值
- @RequestHeader ：按请求报头绑定
- @CookieValue   ：将参数名帮定了对像属性
- @MatrixVariable：用矩阵变量绑定参数

```java
@RequestMapping("/pets/{petId}")
public void findPet(@PathVariable String ownerId, @PathVariable String petId)

@RequestMapping(value = "/handle2")
public ModelAndView handle2(@CookieValue("JSESSIONID") String sessionId,
                            @RequestHeader("User-Agent") String agent )

// 请求参数按名称匹配的方式绑定到 user 的属性中、方法返回对应的字符串代表逻辑视图名
@RequestMapping(value = "/handle3")
public String handle3(User user) {
  return "hello"
}

// GET传参
http://localhost:8080/handle3?userName=zhangsan&password=123
```

##### 3：乱码问题（过滤器）

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
```

##### 4：Spring mvc 支持的方法参数

使用 Servlet API 传入参数时，Spring MVC 将 web 层的 Servlet 对象传递给处理方法，参数顺序没有特殊要求

-  HttpServletRequest request，HttpservletResponse response
- HttpSession session
- InputStream/Reader： 对应 request.getInputStream()
- OutputStream/Writer：对应 response.getOutputStram()

```JAVA
@RequestMapping(value = "/handle1")
public void handle1(HttpServletRequest request,HttpServletResponse response) {
    String userName = request.getParameter("userName");
    response.addCookie(new Cookie("userName", userName))
}
```

##### 5：HttpMessageConverter <T>：org.springframework.http.converter

​	它负责将请求信息转换为一个对象（类型为T），或者将对象（类型为 T）输出为响应信息

###### 原理：

​	DispatcherServlet 默认配置了 RequestMappingHandlerAdpter 作为 HandlerAdapter 的组件实现类，而RequestMappingHandlerAdpter 配置了HttpMessageConverter，通过使用 RequestMappingHandlerAdapter，将请求信息转换为对象，或将对象转换为响应信息

###### 默认加载HttpMessageConverter的六个实现类

**Spring 根据请求 Content-type 来遍历所有的Convert 来选择，响应则是根据Accept-type选择匹配的Convert**

- ByteArrayHttpMessageConvert：读写二进制数据，T 为 byte[]类型，通过supportedMediaTypes属性指定媒体类型 ，响应信息媒体类型为application/octer-stream
- StringHttpMessageConverter：请求信息转为字符串，可读取所有媒体类型(*/*),通过 supportedMediaTypes 属性指定媒体类型
- ResourceHttpMessageConverter
- SourceHttpMessageConvert
- AllEncompassingFormHttpMessageonvert
- Jaxb2RootElementHttpMessageConverter
- MappingJacksonHttpMessageConverter：倒入FastJson后，加入的
  - 利用jackson 的 ObjectMapper读写 Json 数据，T为 Object，可读取application/json响应媒体类型为 application/json

###### Method

- Boolean canRead(Class<?> clazz, MediaType mediaType)
  - 指定转换器可以读取的对象类型，同时指定支持 MIME 类型
- Boolean canWriter(Class<?> clazz, MediaTypemediaType)
  - 指定转换器可以将 clazz 类型的对象写到响应流中，响应流支持类型在 mediaType 中
- List<MediaType> getSupportMediaTypes()
  - 返回该转换器支持的媒体类型
- T read(class<? extends T> clazz , HttpInputMessage inputMessage)
  - 将请求信息流转换成 T 类型的对象
- void write(T t , MediaType contentType,HttpOutputMessageoutputMessage)
  - 将 T 类型的对象写到响应流中，同时指定响应媒体类型为 contentType

##### 6：@RequestBody ，@ResponseBody

###### @RequestBody

- 用于读取Request请求的body部分数据，选择系统默认配置的HttpMessageConverter的实现类进行解析，然后把**相应的数据绑定到要返回的对象**上，处理POST请求
- 再把HttpMessageConverter返回的对象数据绑定到 controller 中方法的参数上

###### @ResponseBody

- 用于将 Controller 方法的返回对象，选取适当的HttpMessageConverter转换为指定格式后，写入到**Response对象的body数据区中**，返回的数据不是HTML页面，而是其他某种格式的数据（如json、xml等）

```java
@RequestMapping("/login.do")
@ResponseBody
public ModelAndView login(@RequestBody User loginUuser, HttpSession session) {
    user = userService.checkLogin(loginUser);
    session.setAttribute("user", user);
    ModelAndView model = new ModelAndView();
		model.addObject("Object", new JsonObject.toString(user));
    return model;
}
```

##### 7：Model、@ModelAttribute、@SessionAttributes

###### @ModelAttribute

1. 用于参数上，会将客户端传递过来的参数按名称注入到指定对象中，并且会将这个对象自动加入ModelMap中，便于View层使用
2. 用于方法上，会在**每一个@RequestMapping标注的方法前执行**，调用此方法，如果有返回值，则自动将该返回值加入到ModelMap中

###### 原理：

1. Spring MVC 在调用（controller）处理的方法前，在请求线程中**自动创建**一个隐含的模型对象(Model)
2. 调用所有使用@ModelAttribute的方法，将在所有controller的方法调用前，调用此方法，将方法返回值添加到隐含模型中
3. 查看Session中，是否存在@SessionAttributes("xx")所指定的xx属性，如果有，则将其添加到隐含模型中，如果隐含模型中已经有xx属性，则该步操作会覆盖模型中已有的属性值
4. 对标注@ModelAttribute("XX") 处理方法的入参的流程
   1. 如果隐含模型拥有名为xx的属性，则将隐含模型中的其属性赋给该入参，再用请求消息填充该入参对象（执行方法）直接返回，否则转 2
   2. 如果 xx是会话属性，即在处理类定义处标注了@SessionAttributes(“xxx”)，则尝试从会话中获取该属性，并将其赋给该入参，然后再用请求消息填充该入参对象，如果在会话中找不到对应的属性，则抛出 HttpSessionRequiredException 异常，否则转到3
   3. 如果隐含模型中不存在 xxx 属性，且 xxx 与不是会话属性，则创建入参的对象实例，然后再用请求消息填充该入参

##### 8：Controller 方法的返回值类型（五种）【重要】

###### 1：void：直接作为报体返回Json数据

```java
// 使用此方式进行跳转
response.sendRedirect("/springmvc-web2/itemEdit.action");
```

###### 2：String

​	返回对应的逻辑视图名称真实url为：**prefix + 视图名称 + suffix** 

###### 注意：

- 如果方法声明了注解@ResponseBody ，则会直接将返回值输出到页面
- "redirect:path" ：重定向
- "forward:path" ：跳转
- "/admin/views/hello" ：视图解析器会拼接路径字符串

###### 3：ModelAndView

​	通过 setViewName() 设置跳转的页面名，通过 addObject() 设置需要返回的值，将值设置到一个名为ModelMap的类属性，**使用此返回结果定义**

###### ModelAndView：实例是用户手动创建的，这也是和ModelMap的一个区别	

```java
ModelAndView model = new ModelAndView();
model.addObject("itemList", list);
model.setViewName("/WEB-INF/jsp/itemList.jsp");
return model;
```

###### 4：ModelMap

- public class ModelMap extends LinkedHashMap<String, Object>
- ModelMap 主要用于传递控制方法处理数据到结果页面，就是说把结果页面上需要的数据放入到ModelMap对象中即可

```java
public ModelMap addAttribute (String attributeName, Object attributeValue) {...}
```

###### 5：Model

- public class ExtendedModelMap extends ModelMap implements Model
- 返回值 String 直接写跳转页面名，用addAttribute（）添加们key-value

