###  Ajax  (Asynchronous JavaScript And XML)：异步 JavaScript 和 XML

​	是一种在无需重新加载整个网页的情况下，能够更新部分网页的技术

- 传统的WEB页面: 每次请求期间不能再次操作 
- AJAX异步交互方式: 采用异步的方式交与服务器交换数据并更新部分网页的技术，不重新加载整个页面

### 1：AJAX 工作流程

1. 根据用户触发的操作，页面发出相应的DHTML事件
2. 调用注册到该DHTML事件的客户端 JavaScript 事件处理函数，其中初始化了一个用以向服务器发送异步请求的 XMLHttpRequest对象，同时指定了一个回调函数，当服务器端的响应返回时状态改变，将自动调用该回调函数
3. 服务器收到 XMLHttpRequest 对象的请求之后，开始根据请求进行一系列的处理
4. 处理完毕，服务器返回客户端所需要的数据
5. 数据到达客户端之后，执行JavaScript回调函数，并根据返回的数据对用户界面进行更新

### 2：Ajax 工作原理

![]()

### 3 ：XMLHttpRequest 对象

###### 属性

- onreadystatechange： 指定当readyState属性改变时就会触发处理事件
- readyState：返回当前请求的状态
  - 0 ：(未初始化) 对象已建立，但是尚未初始化（尚未调用open方法）
  - 1 ：(初始化) 对象已建立，尚未调用send方法 
  - 2 ：(发送数据) send方法已调用，但是当前的状态及http头未知
  - 3 ：(数据传送中) 已接收部分数据，因为响应及http头不全，通过responseText获取部分数据会出现错误 
  - 4 ：(完成) 数据接收完毕,此时可以通过responseBody和responseText获取完整的回应数据
- status ：
  - 200: "OK" 
  - 404: 未找到页面
- responseText ：返回字符串形式的响应
- responseXML ：如果来自服务器的响应是 XML，而且需要作为 XML 对象进行解析

###### 方法

- open(method，url，async) ：创建一个新的http请求，并指定此请求的方法、URL

  - method：请求的类型，GET 或 POST
  - url：文件在服务器上的位置
  - async：true（异步）或 false（同步）
- send( ) ：将请求发送到服务器
- setRequestHeader(header，value)
-  向请求添加 HTTP 报头

```Jsp
xmlhttp.open("POST","/try/ajax/demo_post.php",true);
xmlhttp.setRequestHeader("App-Product","HomeSystem");
xmlhttp.send();
```

### 3：如何判断前端是 Ajax 请求？

###### String type = request.getHeader("X-Requested-With");

requestType 能拿到值，并且值为 XMLHttpRequest ,表示客户端的请求为异步请求，那自然是ajax请求了，反之如果为null,则是普通的请求

```javascript
<script>
  function loadXMLDoc(url) {
    var xmlhttp;
    if (window.XMLHttpRequest){
      // Ajax 请求
      xmlhttp = new XMLHttpRequest();
    } else {
      // 普通请求
      xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function() {
      if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
        document.getElementById('p1').innerHTML = xmlhttp.getAllResponseHeaders();
      }
    }
    xmlhttp.open("GET",url,true);
    xmlhttp.send();
	}
</script>
```

