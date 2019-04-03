package com.part01;

public class Demo01 {

	public static void main(String[] args)
	{	//∂‡¡À32  2£®5£©
	    System.out.println("a:"+(int)'a'+"--A:"+(int)'A');
	   
	    for(int i=97;i<97+26;i++)
	     {
	    	System.out.println((char)i+"-----"+Integer.toBinaryString(i));
	    	System.out.println((char)(i-32)+"-----"+Integer.toBinaryString(i-32));
	     }
	}

}
