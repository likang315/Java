package com.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
/**
 * extends SimpleTagSupport，重写 doTag方法，处理其内容
 * @author likang
 *
 */
public class HomeTag extends SimpleTagSupport {

	@Override
	public void doTag() throws JspException, IOException 
	{
		   StringBuilder sb=new StringBuilder();
		   
		   PageContext pc=(PageContext)this.getJspContext();
		   String path=pc.getServletContext().getContextPath();
		   
		   sb.append(path);
		   this.getJspContext().getOut().print(sb.toString());
		   
	}

}
