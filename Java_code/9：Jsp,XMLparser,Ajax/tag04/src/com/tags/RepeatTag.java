package com.tags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;
/**
 * 迭代自定义标签的实体内容
 * @author likang
 *
 */
public class RepeatTag implements IterationTag {
	private PageContext pc;
	private int index=1;
	private int count;
	
	@Override
	public int doEndTag() throws JspException {
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		pc.getRequest().setAttribute("index", index++);
		return Tag.EVAL_BODY_INCLUDE;
	}

	@Override
	public int doAfterBody() throws JspException {
		 if(--count>0)
		 {
			pc.getRequest().setAttribute("index", index++);
			return IterationTag.EVAL_BODY_AGAIN;
		 }else
		 {
			 return Tag.SKIP_BODY; 
		 }
		
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

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}


}
