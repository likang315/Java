<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript" src="ajax.js"></script>
<script type="text/javascript">

//XML Docunment 返回
function proc()
{
   ajax("GET","xmlservlet",null,function(doc){
	   var res="";
	   var stus=doc.getElementsByTagName("stu");
	  
	   for(var i=0;i<stus.length;i++)
	   {
	      var name=stus[i].getElementsByTagName("name")[0].firstChild.nodeValue;
	      var age=stus[i].getElementsByTagName("age")[0].firstChild.nodeValue;
	      res+="name:"+name+"&nbsp;&nbsp;age:"+age+"<br/>"
	   }
	   
	   var show=document.getElementById("show");
	   show.innerHTML=res;
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