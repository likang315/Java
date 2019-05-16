package com.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class Md5 
{
	public static String tomd5(String msg)
	{
		StringBuilder sb=new StringBuilder();
		
	    try {
			MessageDigest md=MessageDigest.getInstance("md5");
			byte [] datas=msg.getBytes("utf-8");
			System.out.println(Arrays.toString(datas));
			
		  byte[] datas2=md.digest(datas);
			
			System.out.println(Arrays.toString(datas2));
			
			for(int i=0;i<datas2.length;i++)
			{
				byte tem=(byte)(datas2[i]^0x23);
				
				String str=Integer.toHexString(tem);
				if(str.length()>2)
				  str=str.substring(str.length()-2, str.length());
				sb.append(str);
				System.out.println(str);
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return sb.toString();
	}

	public static void main(String[] args) {

      System.out.println(tomd5("admin"));  

	}

}
