package com.img;

import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * 产生随机数字，汉字，字符 
 * @author likang
 *
 */
public class RandUtils 
{
	static int index=0;
	static Random rand =new Random();
   
	public static int randInt()
    {
    	return rand.nextInt(10);//10以内的数字
    }
    
    public static char randChar()
    {
    	char re=0;
    	//偶数大写，奇数小写
    	if(index++%2==0)
    		re=(char)(rand.nextInt(26)+65);
    	else
    		re=(char)(rand.nextInt(26)+97);
    	
    	return re;
    }
    
    public static  String randChinese()
    {
    	String re=null;
    	byte [] tem=new byte[2];
    	tem[0]=(byte)(16+rand.nextInt(35)+0xA0);
    	tem[1]=(byte)(30+rand.nextInt(10)+0xA0);
    	
       	try {
			re=new String(tem,"GBK");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
    	return re;
    }
    
    
	public static void main(String[] args) {
		  for(int i=0;i<10;i++)
		  {
			  System.out.println(randInt()+"--"+randChar()+"---"+randChinese());
		  }
	}

}
