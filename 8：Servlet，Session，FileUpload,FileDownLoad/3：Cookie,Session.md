###  1：Cookie：服务器把客户状态信息写到客户端硬盘上的一种技术

​			Cookie信息通过 name-value (map)
 Cookie (Class)
  构造方法：
​	Cookie(java.lang.String name, java.lang.String value)
​		两个字符串只可以是单字节编码，汉字必须转换为单字节    URLEncoder. encode (uname,"utf-8")
  方法：
​	int	getMaxAge() 
​         	得到生命周期
 	java.lang.String	getName() 
​          	返回Cookie名
 	java.lang.String	getPath()    
​          	得到绝对路径
​	void	setMaxAge(int expiry) 
​        	得知Cookie的生命周期（秒 为单位）
 	void	setPath(java.lang.String uri) 
​		设置路径

######   HttpResponse

​	 void addCookie(Cookie cookie) 
​		在报头中添加Cookie(Set-Cookie 属性)

######   HttpResquest

​	 Cookie[]	getCookies()
​		得到Cookie 数组，通过Cookie 名来筛选出Cookie

```Java
//添加cookie
Cookie cookie=new Cookie("user_name",uname);
cookie.setMaxAge(60*60*24*7);
cookie.setPath("/");
resp.addCookie(cookie);
```



###  2：Session： Http通过令牌的机制完成和客户端的会话（查图），是基于Cookie 技术实现的，当然是在服务器端，但不是保存在内存中，而是保存在文件或数据库中

![](F:\note\14 Tomcat，Servlet\Session.png)

### 原理：

客户端会根据从服务器端发送的**响应报文内一个Set-Cookie的首部字段信息通知客户端保存此次Cookie,**当下次客户端再往该服务器发送请求时客户端会自动在请求报文中**加入Cookie首部字段，值SessionID 后发送出去**,服务器会检查是哪一个客户端发送的请求，然后对比服务器上的记录，得到客户的状态信息


###   HttpSession(Interface)：

1：一个用户的多次访问同一个 HttpSession对象

2：HttpSession 中也有一个Map,用 setAttribute,getAttriute.....
3：请求第一次得到Session时，没有，会在Server中创建HttpSession对象,然后Set-Cookie中把HTTPSession的ID返回
	 
方法

###### 	java.lang.Object	getAttribute(java.lang.String name) 

###### 	void	setAttribute(java.lang.String name, java.lang.Object value) 

int	getMaxInactiveInterval() 
	得到生命周期
void	setMaxInactiveInterval(int interval)
	设置Session的生命周期，秒为单位 boolean	

###### boolean	isNew() 

​	判断是不是新建的
java.lang.String   getId() 
  		得到Session的ID

long	getCreationTime() 
	得到Session的创建时间，返回值Date包装

 long getLastAccessedTime() 
	返回最后一次访问的时间，Date包装

void	invalidate() 
	销毁Session

######  HttpServletRequest

​	 HttpSession	getSession() 
​		得到HttpSession 对象，如果对象没有创建一个并返回它

```java
//重写 Session，验证用户登录
HttpSession hs = req.getSession();
hs.setAttribute("loged", uname); //设置属性来控制直接访问成功页面
```



### 统一页面的访问量

```java
   //Web应用程序只有一个ServletContext，且所有的servlet共享
   ServletContext context = getServletContext();
   Integer count = null;
   synchronized(context)
   {
   	   count = (Integer) context.getAttribute("counter");
       if (null == count)
           count = new Integer(1);
       else
           count = new Integer(count.intValue() + 1);
       
       context.setAttribute("counter", count);
   }
```

### 统计有多少用户的访问量

```java
ServletContext context = ServletConfig.getServletContext();
Integer count = null;
synchronized(context)
{
    //一个用户对应一个session
    if(session.isNew())
	{
        count = (Integer) context.getAttribute("counter");
        if (null == count)
            count = new Integer(1);
        else
            count = new Integer(count.intValue() + 1);

        context.setAttribute("counter", count);
	}
}
```

### 统计同一个用户的访问量（JsessionID）

通过HTTPSession得到用户的JsessionID 判断值是否相同，相同则+1



###  3：URL重写维持会话

如果浏览器禁掉 cookie 那么，session 失效，浏览发的所有请求中都不会带 cookie 请求报头，服务器认为每个请求都是一个新的请求

   httpServletResponse.encodeURL("checkLogin") //在 checkLogin 后会自动添加加 jsessionid=...
   httpServletResponse.sendRedirect(resp.encodeRedirectURL("welcome"));

### URL重写

它允许不支持Cookie的浏览器也可以与WEB服务器保持连续的会话。**将会话标识号以参数形式附加在超链接的URL地址**后面的技术



###  4：Servlet  线程安全

Servlet 是单实例常驻服务器的，多个线程会共享同一个 Servlet 对象，Threadocal这种方式保证线程安全




