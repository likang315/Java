package com.part01;

public class Demo02 {

	public static void main(String[] args)
	{
	    String str="Hello likang,Welcome to xi'an!";
	    
	    char [] ch=str.toCharArray();
	    
	    for(int i=0;i<ch.length;i++)
	    {	//Character 的 toUpptercase ,tolowercase
	    	ch[i]=toUpper(ch[i]);
	    }
	    System.out.println(new String(ch));
	}
	
	
	public static char toUpper(char c)
	{
		if((c>=97&&c<=(97+26))||(c>=65&&c<=(65+26)))
		{
			int mask=0b1011111;//声明二进制
		    return (char)(c&mask);
		}else
		{
			return c;
		}
	}

}
