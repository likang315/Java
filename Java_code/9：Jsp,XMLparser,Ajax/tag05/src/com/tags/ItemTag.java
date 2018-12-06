package com.tags;

import java.lang.reflect.Field;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import com.bean.Student;

public class ItemTag implements Tag {

	private PageContext pc;
	private String value; 		//属性用来传递参数
	private Tag parent;
	
	@Override
	public int doEndTag() throws JspException
	{
		if(null!=parent&&parent instanceof StusTags)
		{
			StusTags st=(StusTags)parent;
			Student stu=st.getStu();
			Class clazz=stu.getClass();
			try {
				Field f=clazz.getDeclaredField(value);
				f.setAccessible(true);
				if(null!=f)
				{
					Object val=f.get(stu);
					pc.getOut().println(val);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			} 
			
		}
		return Tag.EVAL_PAGE;
	}

	@Override
	public int doStartTag() throws JspException {
		return Tag.SKIP_BODY;
	}

	@Override
	public Tag getParent() {
		return parent;
	}

	@Override
	public void release() {

	}

	@Override
	public void setPageContext(PageContext arg0) {
	 this.pc=arg0;
	}

	@Override
	public void setParent(Tag arg0) {
		this.parent=arg0;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
