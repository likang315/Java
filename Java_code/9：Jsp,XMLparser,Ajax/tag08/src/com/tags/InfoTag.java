package com.tags;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 *直接继承其实现类，BodyTagSupport，重写需要的方法 ，替换标签实体内容
 * @author likang
 *
 */
public class InfoTag extends BodyTagSupport
{

	@Override
	public int doEndTag() throws JspException {
		try {
			this.getPreviousOut().print(MessageFormat.format(bodyContent.getString(), "张三",new Date()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Tag.EVAL_PAGE;
	}

}
