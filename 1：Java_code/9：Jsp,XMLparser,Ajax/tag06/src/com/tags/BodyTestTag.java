package com.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.tagext.BodyTag;
import javax.servlet.jsp.tagext.Tag;
/**
 * 测BodyTag的生命周期
 * @author likang
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
		return Tag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
	
		return BodyTag.EVAL_BODY_BUFFERED;
	}

	@Override
	public Tag getParent() {
		
		return null;
	}

	@Override
	public void release() {

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
