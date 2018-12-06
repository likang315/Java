package com.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;

/**
 * 处理标签体的内容
 * @author likang
 *
 */
public class BodyTestTag implements BodyTag 
{
	private PageContext pc;
	private Tag parent;
	private BodyContent bc;
	
    public BodyTestTag()
    {
    	System.out.println("构造方法。。。。。。。。。。。。");
    }
	@Override
	public int doAfterBody() throws JspException {
		  System.out.println("doAfterBody.....................");
		return Tag.SKIP_BODY;
	}

	@Override
	public int doEndTag() throws JspException {
	   System.out.println("doEndTag...............");
	   //得到标签提的内容
	   String txt="<div style='width:100px;height:100px;border:1px #CCC solid; text-align:center;line-height:100px; color:red;'>"+bc.getString()+"</div>";
	   
	   try {
		//输出 标签体的内容
		bc.getEnclosingWriter().println(txt);
	} catch (IOException e) {
		e.printStackTrace();
	}
	   return Tag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		// TODO Auto-generated method stub
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	@Override
	public Tag getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void release() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPageContext(PageContext arg0) {
	   System.out.println("setpageContext...");
	   this.pc=arg0;
	}

	@Override
	public void setParent(Tag arg0) {
		System.out.println("setParent..");
		   this.parent=arg0;

	}

	@Override
	public void doInitBody() throws JspException {
		System.out.println("doinitBody...........");
	}

	@Override
	public void setBodyContent(BodyContent arg0) {
		System.out.println("setBodyContent...........");
       this.bc=arg0;
	}

}
