package com.part1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Client {

	public static void main(String[] args)throws Exception {
	
		Socket s=new Socket(InetAddress.getByName("localhost"), 80);
		System.out.println("连上服务器...");
		
		BufferedReader br=new BufferedReader(new InputStreamReader( s.getInputStream()));
		PrintWriter out = new PrintWriter(s.getOutputStream());
	   
	    String req="GET /index.html HTTP/1.1\r\n\r\n" + 
	   		"Host: 192.168.43.20";
	   
		 new Thread(new Runnable() {
			@Override
			public void run() {
				 try {
					
					   String str=null;
					   while(null!=(str=br.readLine()))
					   {
						   System.out.println(str);
					   }
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			 }
		  }).start();
	   
	   
	   out.println(req);
	   out.flush();
	   
	}

}
