package com.hw;

import java.io.IOException;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * HttpMessageConverter应用的两种方式
 * @Resquest和@ResponseBody
 * 
 * @author likang
 */

@Controller
@RequestMapping("/admin")
public class HelloWorldController 
{
	@RequestMapping("/hi")
	public String handle4(@RequestBody String body) {
		System.out.println(body);
		return "hello";
	}

	@ResponseBody
	@RequestMapping(value = "/hi1/{imageId}")
	public byte[] handle2(@PathVariable("imageId") String imageId) throws IOException {
		System.out.println("load image of " + imageId);
		
		Resource res = new ClassPathResource("/image.jpg");
		byte[] fileData = FileCopyUtils.copyToByteArray(res.getInputStream());
		return fileData;
	}
	
	@ResponseBody
	@RequestMapping("/hi2")
	public String handle41() {
		return "<font color=red>hello</font>";
	}
	
	@RequestMapping("/hi3")
	public String handle43(HttpEntity<String> requestEntity) 
	{
		long contentLen = requestEntity.getHeaders().getContentLength();
	
		System.out.println("user:" + requestEntity.getBody()+contentLen);
		return "hello";
	}
	
	@RequestMapping("/hi4")
	public ResponseEntity<String> handle43() 
	{
		String str="<font color='red'>Hello....</font>";
		ResponseEntity<String> re=new ResponseEntity<String>(str,HttpStatus.OK);
		return re;
	}

}