## Ajax(Asynchronous JavaScript And XML)：异步的js和xml请求

 使用DOM实现动态显示和交互,使用HTML和CSS标准呈现,使用XMLHttpRequest（内置对象）进行异步数据读取
,最后用JavaScript 绑定事件和处理所有数据

传统的WEB页面:
	每次请求期间不能再次操作
AJAX异步交互方式:
	采用异步的方式，交互数据，与服务器交换数据并更新部分网页的技术，不重新加载整个页面

![5+：Ajax.png](https://github.com/likang315/Java/blob/master/Java_note/15%EF%BC%9AJsp%EF%BC%8CEL%EF%BC%8CJstl%EF%BC%8CAjax%EF%BC%8CJson/5+%EF%BC%9AAjax.png?raw=true)

### 1:AJAX工作流程

1:用户在页面**上执行了某个操作**，例如鼠标移动、点击某个区域等

2:根据用户的操作，页面发出相应的DHTML事件

3:调用注册到**该DHTML事件的客户端JavaScript事件处理函数**，其中**初始化了一个用以向服务器发送异步请求的 XMLHttpRequest对 象，同时指定了一个回调函数，当服务器端的响应返回时，将自动调用该回调函数**

4:服务器收到XMLHttpRequest对象的请求之后，开始根据请求进行一系列的处理

5:处理完毕，服务器返回客户端所需要的数据

6:**数据到达客户端之后，执行JavaScript回调函数，**并根据返回的数据对用户界面进行更新

其中使用DOM对象树进行数据的修改

### 2:XMLHttpRequest 对象

#####    属性

###### onreadystatechange 指定当readyState属性改变时的处理事件 

###### readyState  		 返回当前请求的状态

?		0 ：(未初始化)   对象已建立，但是尚未初始化（尚未调用open方法） 
?		1 ：(初始化)      对象已建立，尚未调用send方法 
?		2 ：(发送数据)   send方法已调用，但是当前的状态及http头未知 
?		3 ：(数据传送中) 已接收部分数据，因为响应及http头不全，这时通过responseText获取部分数据会出现错误 
?		4 ：(完成)          数据接收完毕,此时可以通过responseBody和responseText获取完整的回应数据 

###### responseText 		将响应信息作为字符串返回

###### responseXML 		将响应信息格式化为Xml Document对象并返回

###### status 				返回当前请求的http状态码

###### statusText  			返回当前请求的响应行状态

#####    方法

###### open(bstrMethod, bstrUrl） 创建一个新的http请求，并指定此请求的方法、URL

###### send( ) 		   发送请求到http服务器并接收回应 

setRequestHeader 单独指定请求的某个http头 
abort 取消当前请求 
getAllResponseHeaders 获取响应的所有http头 
getResponseHeader 从响应信息中获取指定的http头 

### 3：如何判断前端是Ajax请求？

###### String type = request.getHeader("X-Requested-With");  

 requestType能拿到值，并且值为XMLHttpRequest,表示客户端的请求为异步请求，那自然是ajax请求了，反之如果为null,则是普通的请求





