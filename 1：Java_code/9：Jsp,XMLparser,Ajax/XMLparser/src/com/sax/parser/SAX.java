package com.sax.parser;

import java.io.FileInputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * SAX 解析
 * @author likang
 *
 */
public class SAX {

	public static void main(String[] args) throws Exception
	{
		SAXParserFactory  sf=SAXParserFactory.newInstance();
		SAXParser sp=sf.newSAXParser();
		
		FileInputStream fi=new FileInputStream("C:\\Users\\a'su's\\Desktop\\Java_web\\8：XML\\XML_parser\\dom\\students.xml");
		sp.parse(fi, new MyHandler());
		
	}
	
	static class MyHandler extends DefaultHandler
	{

		@Override
		public void startDocument() throws SAXException {
			 
		}

		@Override
		public void endDocument() throws SAXException {
			 
		}

		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes)
				throws SAXException {
			  
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			 
		}

		@Override
		public void characters(char[] ch, int start, int length) throws SAXException {
			
		}

		@Override
		public void error(SAXParseException e) throws SAXException {
			
		}
		
	}

}
