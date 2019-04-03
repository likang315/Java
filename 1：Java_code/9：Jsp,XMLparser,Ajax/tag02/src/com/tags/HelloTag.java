package com.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
/**
 * 标签处处理器，必须实现JspTag 或其子接口，顺便测生命周期
 * @author likang
 *
 */
public class HelloTag implements Tag 
{
	 private  PageContext pc; 	//环境 
	 private  Tag parent;  		//父标签
	 private int count;			//Tag属性
	     
     public HelloTag()
     {
    	 System.out.println("实例化了HelloTag..");
     }
     

	@Override
	public int doEndTag() throws JspException {
		System.out.println("doEndTag..............");
		
		try {
			pc.getOut().println("<h1>Hello Tag "+count+"</h1>");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		System.out.println("doStartTag..............");
		return Tag.SKIP_BODY;
	}

	@Override
	public Tag getParent() {
		return parent;
	}

	@Override
	public void release() {
		System.out.println("release.............");
	}

	@Override
	public void setPageContext(PageContext arg0) {
		 System.out.println("setPageContext.............");
		 this.pc=arg0;
	}

	@Override
	public void setParent(Tag arg0) {
	   System.out.println("setpaternt.............");
       this.parent=arg0;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		System.out.println("setXXX.....");
		this.count = count;
	}

}
