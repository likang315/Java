package com.dom.parser;

import java.io.FileInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
/**
 * Dom解析
 * @author likang
 */
public class Dom 
{
	public static void main(String[] args) throws Exception
	{
		//得到DocumentBuilderFactory 实例时，三种引擎搜索方式
		/*Properties p=System.getProperties();
		  p.put("javax.xml.parsers.DocumentBuilderFactory", "com.part01.MyDocumentBuilderFactory");
		*/

		DocumentBuilderFactory  df=DocumentBuilderFactory.newInstance();
		DocumentBuilder  db=df.newDocumentBuilder();
		Document doc=db.parse(new FileInputStream("C:\\Users\\a'su's\\Desktop\\Java_web\\8：XML\\XML_parser\\dom\\students.xml"));
		//得到根节点
		Element root=doc.getDocumentElement();
		NodeList allstu=doc.getElementsByTagName("students");
		for(int i=0;i<allstu.getLength();i++)
		{
			Element el=(Element)allstu.item(i);
			String st=el.getAttribute("sn");
		    if("01".equals(st))
		    {
		    	Element name=(Element)el.getElementsByTagName("name").item(0);
		    	System.out.println(name.getFirstChild().getNodeValue());
		    	break;
		    }
		}
		
	}
}
