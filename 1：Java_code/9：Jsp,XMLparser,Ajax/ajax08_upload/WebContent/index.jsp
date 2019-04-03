<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript">

$(function(){
	$("#ups").click(function(){
		//在Dom中构建一个表单，然后通过ajax把这个表单的数据给了服务器
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
	  		  	 var img="<img src=\"ups/"+json.newname+"\" id=\"showpic\" width=\"30\" height=\"30\"/> ";
	   	    	 $("#show").append(img);
	    	 	 alert("上传成功!");
	  		   }
    	   else
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
<center>
   <input type="file" id="pic"/>
   <input type="button" value="上传" id="ups"/>
   <div id="show">
   
   </div>
</center>
</body>
</html>