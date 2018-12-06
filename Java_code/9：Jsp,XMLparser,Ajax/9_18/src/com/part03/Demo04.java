package com.part03;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

//protertise¿‡
public class Demo04 {
	
public static void main(String[] args)throws Exception {
	
	
//	Properties p=new Properties();
//	p.put("1", "hello");
//	p.setProperty("2", "welcome");
//	p.setProperty("3", "haha~~~");
//	
//	p.list(System.out);
//	
//	p.store(new FileOutputStream("c:\\aaa.properties"), "≤‚ ‘");
//	System.out.println("Ok");
//	
//	
//	
//	Properties p=new Properties();
//	p.load(new FileInputStream("c:\\aaa.properties"));
//	
//	p.list(System.out);
//	
//	p.storeToXML(new FileOutputStream("c:\\aaa.xml"), "≤‚∂»≤˝");
//	
	Properties p=System.getProperties();
	p.setProperty("user.name", "lisi");
	p.list(System.out);	
	
	
	}
}
