<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="ajax.js"></script>
<script type="text/javascript">

function proc()
{
   ajax("GET","jsondata",null,function(data){
	   var json=eval("("+data+")");
	   var show=document.getElementById("show");
	   show.innerHTML="name:"+json.name+"<br/>age:"+json.age+"<br/>emails:"+json.emails[1];
   });
}

</script>
</head>
<body>
<center>
     <input type="button" value="OK" onclick="proc();"/>
     <div id="show">
     
     </div>
</center>
</body>
</html>