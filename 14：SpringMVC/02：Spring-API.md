### Spring MVC API

------

[TOC]

##### 01：@RequestMapping

- 用在**控制器的类及方法的定义处**，定义在方法的是在类的URL下的下一层URL，不旦支持标准的 URL，还支持 Ant 风格（？、*、**、字符）；


###### 原理

- DispacherServlet 截获请求后，通过控制器上@RequestMapping 提供的**映射信息**确定请求所对应的处理方法，将请求映射到控制器处理方法的工作包含一系列映射规则，具体包括**请求 URL、请求参数、请求方法、请求头**；
- Path 和 value 设置路径：@RequestMapping(value = {"/page","page*", "view/*,**/msg"})
- Method 请求方法：@RequestMapping(method = RequestMethod.GET)
- Headers 报头过滤：@RequestMapping(headers = {"content-type=text/plain"}) 
- params 参数过滤 ：@RequestMapping(params = {"personId=10"}) ，可以是数组

##### 02：获取请求参数

- @PathVariable：用来获得请求url中的动态参数的
- **@RequestParam：**Spring能够根据名字自动赋值对应的函数参数值；
- **@RequestBody：**接收请求报体中的数据，POST请求；
- @RequestHeader ：按请求报头绑定
- **@CookieValue ：**将参数名帮定了对像属性
- @MatrixVariable：用矩阵变量绑定参数

```java
@RequestMapping("/req")
public void findPet(@RequestParam String ownerId, @RequestParam String petId)

@RequestMapping(value = "/handle")
public ModelAndView handle2(@CookieValue("JSESSIONID") String sessionId,
                            @RequestHeader("User-Agent") String agent)

// 请求参数按名称匹配的方式绑定到 user 的属性中、方法返回对应的字符串代表逻辑视图名
@RequestMapping(value = "/handle3")
public String handle3(User user) {
  return "hello"
}

// GET传参
http://localhost:8080/handle3?userName=zhangsan&password=123
```

##### 03：MVC 支持的请求参数【servlet】

-  使用 Servlet API 传入参数时，Spring MVC 将 web 层的 Servlet 对象传递给处理方法，参数顺序没有特殊要求

-  HttpServletRequest request，HttpservletResponse response
- HttpSession session
- InputStream/Reader： 对应 request.getInputStream()
- OutputStream/Writer：对应 response.getOutputStram()

```JAVA
@RequestMapping(value = "/login")
public void login(HttpServletRequest request, HttpServletResponse response) {
    String userName = request.getParameter("userName");
    response.addCookie(new Cookie("userName", userName))
}
```

##### 04：HttpMessageConverter

- 它负责将请求信息转换为一个对象（类型为T），或者将对象（类型为 T）输出为响应信息；
- Spring 根据**请求 Content-type** 来遍历所有的Convert 来选择，响应则是根据**Accept-type**选择匹配的Convert
- DispatcherServlet 默认配置了 RequestMappingHandlerAdpter 作为 HandlerAdapter 的组件实现类，而RequestMappingHandlerAdpter 配置了HttpMessageConverter，通过使用 RequestMappingHandlerAdapter，将请求信息转换为对象，或将对象转换为响应信息；

##### 05：请求、响应体

###### @RequestBody

- 用于读取Request请求的body部分数据（Json），选择系统默认配置的HttpMessageConverter的实现类进行解析，然后把**相应的数据绑定到要返回的对象**上，处理POST请求，再把HttpMessageConverter返回的对象数据绑定到 controller 中方法的参数上。

###### @ResponseBody

- 用于将 Controller 方法的返回对象，选取适当的HttpMessageConverter转换为指定格式后，写入到**Response对象的body数据区中**，返回的数据不是HTML页面，而是其他某种格式的数据（如json、xml等）

```java
@RequestMapping("/login.do")
@ResponseBody
public ModelAndView login(@RequestBody User user, HttpSession session) {
    user = userService.checkLogin(loginUser);
    session.setAttribute("user", user);
    ModelAndView model = new ModelAndView();
	model.addObject("Object", new JsonObject.toString(user));
    
    return model;
}
```

##### 06：Model、@ModelAttribute、@SessionAttributes

###### @ModelAttribute

1. 用于参数上，会将客户端传递过来的参数按名称注入到指定对象中，并且会将这个对象自动加入ModelMap中，便于View层使用；
2. 用于方法上，会在**每一个@RequestMapping标注的方法前执行**，调用此方法，如果有返回值，则自动将该返回值加入到ModelMap中；

###### 原理：

1. Spring MVC 在调用（controller）处理的方法前，在请求线程中**自动创建**一个隐含的模型对象(Model)；
2. **调用所有使用@ModelAttribute的方法**，将在所有controller的方法调用前，调用此方法，将方法返回值添加到隐含模型中；
3. 查看Session中，是否**存在@SessionAttributes("xx")所指定的xx属性**，如果有，则将其添加到隐含模型中，如果隐含模型中已经有xx属性，则该步操作会覆盖模型中已有的属性值；
4. 对标注@ModelAttribute("XX") 处理方法的入参的流程
   1. 如果隐含模型拥有名为xx的属性，则将隐含模型中的其属性赋给该入参，再用请求消息填充该入参对象（执行方法）直接返回，否则转 2；
   2. 如果 xx是会话属性，即在处理类定义处标注了@SessionAttributes(“xxx”)，则尝试从会话中获取该属性，并将其赋给该入参，然后再用请求消息填充该入参对象，如果在会话中找不到对应的属性，则抛出 HttpSessionRequiredException 异常，否则转到3；
   3. 如果隐含模型中不存在 xxx 属性，且 xxx 与不是会话属性，则创建入参的对象实例，然后再用请求消息填充该入参；

##### 07：返回值类型（五种）【重要】

###### void

```java
// 使用此方式进行跳转
response.sendRedirect("/springmvc-web2/itemEdit.action");
```

###### String

- 返回对应的逻辑视图名称真实url为：**prefix + 视图名称 + suffix** 
- 如果方法声明了注解@ResponseBody ，则会直接将返回值输出到页面；
- "redirect:path" ：重定向：响应给客户端，客户端重新请求别的URL；
- "forward:path" ：转发：服务器内部跳转；
- "/admin/views/hello" ：视图解析器会拼接路径字符串；

###### ModelAndView

- 通过 setViewName() 设置跳转的页面名，通过 addObject() 设置需要返回的值，将值设置到一个名为ModelMap的类属性，**使用此返回结果定义**；


```java
ModelAndView model = new ModelAndView();
model.addObject("itemList", list);
model.setViewName("/WEB-INF/jsp/itemList.jsp");
return model;
```

###### ModelMap

- public class ModelMap extends LinkedHashMap<String, Object>
- ModelMap 主要用于传递控制方法处理数据到结果页面，就是说把结果页面上需要的数据放入到ModelMap对象中即可

```java
public ModelMap addAttribute (String attributeName, Object attributeValue) {...}
```

###### Model

- public class ExtendedModelMap extends ModelMap implements Model
- 返回值 String 直接写跳转页面名，用addAttribute（）添加们key-value

