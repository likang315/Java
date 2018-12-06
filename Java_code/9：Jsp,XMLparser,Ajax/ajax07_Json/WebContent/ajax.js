var xmlhttp;//XMLHttpRequest
function newInstance()
{
	if (window.XMLHttpRequest)
	  {// code for IE7+, Firefox, Chrome, Opera, Safari
		xmlhttp=new XMLHttpRequest();
	  }
	else
	  {// code for IE6, IE5
		xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
	  }
}
/**
 * 封装ajax的请求
 * @param method   get post
 * @param url      url
 * @param data     数据
 * @param proc     回调函数
 * @returns
 */
function ajax(method,url,data,proc)
{
	 newInstance();
	 
	 xmlhttp.onreadystatechange=function(){
		 if(xmlhttp.status==200&&xmlhttp.readyState==4)
		   {
			   var resp=xmlhttp.responseText;
			   proc.call(this,resp);
		   }
	 };
     if(method=="POST"||method=="post")
     {
    	 xmlhttp.open(method,url);
    	 xmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
 		 xmlhttp.send(data);
     }else
     {
    	 xmlhttp.open(method,url);
    	 xmlhttp.send();
     }
		
	
}