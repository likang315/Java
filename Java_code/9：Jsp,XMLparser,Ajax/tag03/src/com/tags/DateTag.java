package com.tags;

import java.io.IOException;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

/**
 * 带有实体内容的自定义标签
 * @author likang
 *
 */
public class DateTag implements Tag {
	private PageContext pc;
	
	@Override
	public int doEndTag() throws JspException {

       try {
		pc.getOut().print(new Date().toLocaleString());
	} catch (IOException e) {
		e.printStackTrace();
	}
		
		return Tag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		return Tag.EVAL_BODY_INCLUDE;
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
	 this.pc=arg0;

	}

	@Override
	public void setParent(Tag arg0) {
		// TODO Auto-generated method stub

	}

}
