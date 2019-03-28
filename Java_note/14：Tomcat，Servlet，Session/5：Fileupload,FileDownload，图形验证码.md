## 文件的上传和下载

upload:基于RFC1867协议
     RFC1867协议主要是**在HTTP协议的基础上为input标签增加了type="file"属性，同时限定了Form的method="post"
     enctype="multipart/form-data"**

### 1：Http 文件上传客户端要求：

1、用表单提交数据 <from></form>
2、请求方法必须为 post   <form method="post">
3、以二进制流提交数据给服务器

```html
 <form action="upload" method="post" enctype="multipart/form-data">
	    描述：<input type="text" name="dis"/><br/>
	  	文件:<input type="file" name="pic"/><br/>
	   <input type="submit" value="上传"/>
  </form>
```

enctype 默认值为 application/x-www-form-urlencoded,不会提交文件内容
enctype 值设为 multipart/form-data 时提交文件内容给服务器

### 2：Servlet 3.0  文件上传

配置：@MultipartConfig 

HttpServletRequest 增加了对文件上传的支持
	Part getPart(String name)
		根据名称来获取文件上传域（part)
	Collection<Part> getParts()
		获取所有的文件上传域

### 3：part (InterFace)：每个 Part 对象封装一个文件上传域，该对象提供访问上传文件的文件类型、大小、输入流等方法，

###### 并提供了一个 write(String file)方法将上传文件写入服务器磁盘，上传域指 type="file" 的input

java.lang.String   getContentType() 
		文件的类型（mimetype:根据前几个字节，判断文件类型）

 	java.util.Collection<java.lang.String>	getHeaderNames() 
         	得到报头key 的集合
	java.lang.String   getHeader(java.lang.String name) 
          	得到指定报头的值
 	java.lang.String	getName() 
        	得到文本域的name属性
 	long	getSize() 
         	得到文件的大小（字节）

######  	void	write(java.lang.String fileName) 	

根据绝对路径，将上传文件写到server磁盘的指定路径位置

### 4： Apache Commons  fileupload 文件上传组件，导入 jar 包

其核思想是将用户的请求转换为一个 List<FileItem> 的集合，其中 FileItem 就是一个part，文件上传域

###### FileItem(Interface ):

String	getContentType()
	得到文件类型(mimetype)
String	getName()
	得到上传的文件名
String	getFieldName()
	得到表单的name属性

boolean	isFormField()
	判断form的type是否是普通属性(非上传文件字段)，否就是上传文件
long    getSize()
	得到文件大小	
void	write(File file)
	把上传文件到指定的文件

```java
		 // Create a factory for disk-based file items
		 DiskFileItemFactory factory = new DiskFileItemFactory();
		 ServletContext sc = this.getServletContext();
		 File repository = (File) sc.getAttribute("javax.servlet.context.tempdir");
		 factory.setRepository(repository);
		 // Create a new file upload handler
		 ServletFileUpload upload = new ServletFileUpload(factory);
		 
		 List<FileItem> items = upload.parseRequest(req);?	
```

### 5:文件的下载

##### 	download（两种）

##### 	   1：直接将超链接指向要下载的文件，当浏览器不能识辨的 MIME 类型会出现下载对话框（压缩包） 但是这种方式程序无法通过用户权限来控制下载

##### 	   2：通过设置响应报头Content-Disposition来下载文件

用Servlet 将bit流数据响应(写)给客户端，可以更自由地控制用户下载权限

```java
Content-Disposition属性有两种类型：inline 和 attachment，作为下载文件字段
 	 inline：将文件内容直接显示在页面
	 attachment：弹出对话框让用户下载

Content-Disposition:inline;filename=文件名;	
Content-Disposition:attachment;filename=文件名;

resp.setHeader("Content-Disposition", "attachment;filename="+showcname);
```

###### 


### 6：图片验证码

验证码原理
	由程序随机生成字母、数字或汉字的图片，并将图片上的随机内容记在 **session** 中，让用户看到图片后将随机值填写在表单项中]提交给服务器，服务端程序将用户填写的信息与 Session 中的信息进行对比，就知道是不是用户填写的，关键在于不让能程序自动识辨出图片上的信息，所以一般我们都加干扰线、点，或旋转、缩放等




