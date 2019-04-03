<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8" />
		<title>注册</title>
		<link rel="stylesheet" href="css/bootstrap.min.css" />
		<script type="text/javascript" src="js/jquery.min.js" ></script>
		<script type="text/javascript" src="js/bootstrap.min.js"></script>
		<script type="text/javascript">
          //让js给服务器发一个请求，这个服务器上传文件，并把上传到服务器的新文件名反回当前页面
          $(function(){
        	
        	  $("#pic").change(function(){
        		  var fd=new FormData();
        		  var upf=document.getElementById("pic").files[0];
        		  fd.append("uppic",upf);
        	
        		    $.ajax({
        			   type:'POST',
        			   url:'upload',
        			   data:fd,
        			   contentType:false,
        		       processData:false,
        		       success:function(data)
        		       {
        		    	   var json=eval("("+data+")");
        		    	   if(json.error==0)
        		    		   {
	        		    		   $("#hidpic").val(json.newname);
	             		    	   var img="<img src=\"ups/"+json.newname+"\" id=\"showpic\" width=\"30\" height=\"30\"/> ";
	            		    	   $("#showpic").append(img);
        		    		   }else
        		    		   {
        		    			  alert("上传失败!");
        		    		   }
        		       }
        		   }); 
        	  });
        	  
          });
       
		</script>
	</head>
	
	<body>
		<div class="container">
				<div class="row">
					<div class="col-md-offset-3 col-md-6">
							<div class="panel panel-info" style="margin-top: 50px;">
							  <div class="panel-heading">用户注册</div>
							  <div class="panel-body">
 
 <form action="regist"  method="post">
   <input type="hidden" name="action" value="registed"/>
  <div class="form-group">
    <label>Email:</label>
    <input type="email" name="email" class="form-control" required="required" placeholder="输入Email">
  </div>
  <div class="form-group">
    <label>密码：</label>
    <input type="password" name="upwd" class="form-control" required="required"  placeholder="Password">
  </div>
  <div class="form-group">
    <label>姓名:</label>
    <input type="text" name="name" class="form-control" placeholder="请输入用户名">
  </div>
  <div class="form-group">
  	<label>头像:</label>
  	 <span id="showpic"></span>
  <input type="hidden" name="pic" id="hidpic"/>
   
  <input type="file" id="pic" />
  </div>
 
  <button type="submit" class="btn btn-info">注册</button>
  <a href="login"  class="btn btn-success">登录</a>
</form>
							  </div>
							</div>
					</div>
				</div>
		</div>
		

<%@include file="msg.jsp" %>
	</body>
</html>
