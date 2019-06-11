### FileUpload：三种

upload：基于RFC1867协议 ，其协议主要是在HTTP协议的基础上为

input标签增加了type="file"属性，同时限定了 Form 表单的 method="post" enctype="multipart/form-data"

###### javax.servlet.http  

### Interface Part

​	每个 Part 对象封装一个文件上传域，该对象提供访问上传文件的文件类型、大小、输入流等方法，并提供了一个 write(String file)方法将上传文件写入服务器磁盘，上传域指 type="file" 的input

- java.lang.String getContentType() ：文件的类型（mimetype:根据前面几个字节，判断文件类型）

- java.util.Collection<java.lang.String>	getHeaderNames() ：得到报头key 的集合

- java.lang.String   getHeader(java.lang.String name) ：得到指定报头的值

- java.lang.String	getName() ：得到文本域的 name 属性

- long	getSize() ：得到文件的大小（字节）

- ###### void	write(java.lang.String fileName)：根据绝对路径，将上传文件写到服务器上指定路径位置

### 

##### 1：Http 文件上传客户端要求：

1. 用表单提交数据
2. 请求方法必须为 post
3. enctype 默认值为 application/x-www-form-urlencoded，不会提交文件内容，enctype 值设为 multipart/form-data 时提交文件内容给服务器

```html
<form action="upload" method="post" enctype="multipart/form-data">
    描述：<input type="text" name="dis"/><br/>
    文件: <input type="file" name="pic"/><br/>
         <input type="submit" value="上传"/>
</form>
```

```java
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {
  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) throws 			ServletException, IOException {
		req.setCharacterEncoding("utf-8");
    resp.setContentType("text/html;charset=utf-8");
    PrintWriter out = resp.getWriter();
    
    InputStream input = req.getInputStream();
    byte [] buffer = new byte[1024];
    FileOutputStream fout = new FileOutputStream("e:\\up.txt");
    while((len = input.read(buffer)) > 0) { 
      fout.write(buffer, 0, len); 
    }
    input.close();
    fout.close();
    out.println("<h1>OK</h1>");
    out.close();
  }
}
```



##### 2：Servlet 3.0 文件上传

配置：Servlet类上配置：@MultipartConfig

HttpServletRequest 增加了对文件上传的支持

-  Part getPart(String name) ：根据名称来获取文件上传域

```java
PrintWriter out = resp.getWriter();
//得到文件上传域
Part part=req.getPart("pic");
//得到绝对路径
String realpath = this.getServletContext().getRealPath("ups");
String fname = getFileName(part);
String newname = newName() + getExtName(fname);
part.write(realpath+"/"+newname)
```



##### 3： Apache Commons fileupload 文件上传组件，导入 jar 包

其核心思想是将用户的请求转换为一个 List<FileItem>，其中 FileItem 就是一个part，文件上传域

##### FileItem(Interface):

- String	getContentType() 得到文件类型(mimetype) 
- String	getName() 得到上传的文件名 String	
- getFieldName() 得到表单的name属性
- boolean	isFormField() 判断 form 的 type 是普通属性还是上传文件(非上传文件字段)，否就是上传文件 
- long getSize() 得到文件大小 void	write(File file) 把上传文件到指定的文件

```java
@Override
protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    req.setCharacterEncoding("utf-8");
    resp.setContentType("text/html;charset=utf-8");
    PrintWriter out=resp.getWriter();

    out.println("<ul>");
    try {
        DiskFileItemFactory factory = new DiskFileItemFactory();
        ServletContext sc = this.getServletContext();
        File repository = (File) sc.getAttribute("javax.servlet.context.tempdir");
        factory.setRepository(repository);
        ServletFileUpload upload = new ServletFileUpload(factory);
				//开始编写业务代码
        List<FileItem> items = upload.parseRequest(req);
        if(null != items && items.size() > 0) {
            for(FileItem it:items) {
              	//true 为普通属性
                if(it.isFormField()) {
                    if("dis".equals(it.getFieldName()))
                        out.println("<li>描述："+it.getString("utf-8")+"</li>"); 		
                } else {
                    String extname = this.getExtName(it.getName());
                    String newname = this.getRandName()+extname;	   
                    it.write(new File(sc.getRealPath("ups")+"/"+newname));
                }
            }
        }
    } catch (Exception e) {
				
    }
}
```



### FiledownLoad：两种

1. 直接将超链接指向要下载的文件，当浏览器不能识辨的 MIME 类型会出现下载对话框（压缩包） 但是这种方式程序无法通过用户权限来控制下载
2. 通过设置响应报头Content-Disposition来下载文件
   - Content-Disposition 属性有两种类型：inline 和 attachmen
     - inline：将文件内容直接显示在页面
     -  attachment：弹出对话框让用户下载
     - Content-Disposition:attachment;filename=文件名

```xml
<body>
	<center>
	  <h1>文件下载</h1>
	  <a href="res/ad01.jpg">图片</a>
	  <!--利用程序无法获取 mime 类型，而弹出下载框  -->
	  <a href="res/kaptcha-2.3.2.zip">文件</a>
	  <a href="download?fname=ad01.jpg">下载</a>
	</center>
</body>
```

用Servlet 将bit流数据响应(写)给客户端，可以更自由地控制用户下载权限

```java
//设置下载字段值
resp.setHeader("Content-Disposition", "attachment;filename="+showcname);
ServletContext sc = this.getServletContext();
String path = sc.getRealPath("res");
//根据服务器上文件URL获取输入流
FileInputStream input = new FileInputStream(new File(path,fname););
ServletOutputStream  out = resp.getOutputStream();
byte [] buffer = new byte[1024];
int len = -1;
while(( len = fi.read(buffer))>0) {
    out.write(buffer, 0, len);
}
input.close();
out.close();
```



### 图片验证码原理

由程序随机生成字母、数字或汉字的图片，并将图片上的随机内容记**在 session 中，作为报头**，让用户看到图片后将随机值填写在表单项中]提交给服务器，服务端程序将用户填写的信息与 Session 中的信息进行对比，就知道是不是用户填写的，关键在于不让能程序自动识辨出图片上的信息，所以一般我们都加干扰线、点，或旋转、缩放等