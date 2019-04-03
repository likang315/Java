package com.part03;

public class Demo02 extends Object{
//String常量池，hashCode与equals
	public static void main(String[] args)
	{ 
		
	   Demo02 d1=new Demo02();
	   Demo02 d2=new Demo02();
	   
	   System.out.println(d1.hashCode());
	   System.out.println(d2.hashCode());
	   System.out.println(d1==d2);
	   System.out.println("-----------------------");
	   
	   String s1=new String("abc");
	   String s2=new String("abc");
	   System.out.println(s1==s2);//比较地址
	   
	   System.out.println(s1.equals(s2));//比较内容
	   System.out.println(s1.hashCode()==s2.hashCode());//equals相等，hahsCode一定相等
	   
	}

}
