package com.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class ServletUtils 
{
	 public static String toServlet(String path)
	 {
		 StringBuilder sb=new StringBuilder();
		 try {
			//File f=new File(path);
			
			 BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
			 String str=null;
			 while(null!=(str=br.readLine()))
			 {
				 sb.append("out.println(\""+str.replaceAll("\"", "'")+"\");\r\n");
			 }
			 
			 br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 return sb.toString();
	 }
	 
	 public static void main(String[] args) {
		String re=toServlet("D:\\develop\\hbuilder_workspace2\\shopcar\\shopcar.html");
		System.out.println(re);
	}
}
