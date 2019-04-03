package com.part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpServer 
{
  private static String basepath="c:\\www";
  private static String path="/index.html";
  
  public static void main(String[] args) throws Exception
  {
	   ServerSocket  server=new ServerSocket(80);
	   System.out.println("server 启动....");
	   
	   while(true)
	   {
		   Socket s=server.accept();
		   System.out.println("client 已经连上...");
		   //client发送的消息
		   BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
		   //发送到client的消息
		   PrintStream ps=new PrintStream(s.getOutputStream());
		   
		   new Thread(new Runnable() {
				@Override
				public void run() {
					   try {
						   StringBuilder sb=new StringBuilder();
						   String str=null;
						   while(null!=(str=br.readLine()))
						   {
						   	  Pattern pattern=Pattern.compile("((.*?)\\s{1}/(.*?)\\s{1}HTTP/1[.][0,1])");
							  Matcher mat=pattern.matcher(str);
							  if(mat.find())
							  {
								 path=mat.group(3);
							  }
						      sb.append(str+"\r\n");
						   }
					} catch (IOException e) {
						System.out.println("");
					}	
				}
		  }).start();
		   
	   //模拟WEB服务器的响应
	   StringBuilder   resp=new StringBuilder();
	   resp.append("HTTP/1.1 200 OK\r\n");
	   resp.append("Content-Type:text/html; charset=utf-8\r\n\r\n\r\n");
	   resp.append("<h1>hello</h1>");
	   
	   ps.println(resp.toString());
	   //ps.close();
	 
	   //  br.close();
	   
	   }
	   
}
}
