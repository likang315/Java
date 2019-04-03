package com.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;


/**
 * 把HTML文件转为servlet输出
 * @param path
 * @return
 */	
public class ServletUtils 
{
	 public static String toServlet(String path)
	 {
		 StringBuilder sb=new StringBuilder();
		 try {
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			 String str=null;
			 while(null!=(str=br.readLine()))
			 {
				 sb.append("out.println(\""+str.replaceAll("\"", "'")+"\");\r\n");
			 } 
			 br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		 return sb.toString();
	 }
	 public static void main(String[] args) {
		String re=toServlet("C:\\Users\\a'su's\\Desktop\\servlet07_forward\\WebContent\\index.html");
		System.out.println(re);
	 }
}
