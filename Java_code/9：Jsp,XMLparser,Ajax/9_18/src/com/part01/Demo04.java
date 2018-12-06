package com.part01;

public class Demo04 {

	public static void main(String[] args) {
	  String s1="hello world";
	  String s0="hello world";
	  String s6=new String("hello world");
	  String s2="hello";
	  String s3=" world";
	  
	  String s4=s2.concat(s3);
	 // s4=s4.intern();
	  //s6=s6.intern();
	  System.out.println(s1==s4);
	  System.out.println(s1==s0);
	  System.out.println(s1==s6);
	  
	  
	}

}
