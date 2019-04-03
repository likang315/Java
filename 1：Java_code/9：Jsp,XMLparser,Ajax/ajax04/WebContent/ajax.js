var xmlhttp;//xmlHttpRequest
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
 * 封装发送异步请求
 * @param method  方法get post
 * @param url   服务器url
 * @param data   数据  name=lisi&age=22&sex=F
 * @param proc   回调函数,响应数据结束后由它来处理
 * @returns
 */
function ajax(method,url,data,proc)
{
	 newInstance();
	 xmlhttp.onreadystatechange= function ()
	  {
		  if(xmlhttp.readyState==4&&xmlhttp.status==200)
			  {
			  	   //调用proc这个函数，并传xmlhttp.responseText
			       proc.call(this,xmlhttp.responseText);
			  }
	  };
	if(method=="post")
		{
		 xmlhttp.open("POST",url);
		 xmlhttp.setRequestHeader("Content-type","application/x-www-form-urlencoded");
		 xmlhttp.send(data);
		}else
		{
			 xmlhttp.open("GET",url+"?"+data);
			 xmlhttp.send();
		}

}