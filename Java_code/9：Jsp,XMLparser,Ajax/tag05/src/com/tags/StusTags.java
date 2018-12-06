package com.tags;

import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.IterationTag;
import javax.servlet.jsp.tagext.Tag;

import com.bean.Student;

public class StusTags implements IterationTag {
	PageContext pc;
	List<Student> stus;
	Student stu=null;
	private String data; //属性存放stus数组
	int index=0;
	
	@Override
	public int doEndTag() throws JspException {
	
		return 0;
	}

	@Override
	public int doStartTag() throws JspException 
	{
       stus=(List<Student>)pc.getAttribute(data, PageContext.REQUEST_SCOPE);
       
		if(null!=stus&&stus.size()>0)
		{
			stu=stus.get(index++);
			return Tag.EVAL_BODY_INCLUDE;
		}else
		{
			return Tag.SKIP_BODY;
		}
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
		this.pc=arg0;
	}

	@Override
	public void setParent(Tag arg0) {

	}

	@Override
	public int doAfterBody() throws JspException {

      if(null!=stus&&index<stus.size())
      {
    	  stu=stus.get(index++);
    	  
    	  return IterationTag.EVAL_BODY_AGAIN;
      }else
      {
		
		return Tag.SKIP_BODY;
      }
	}

	public Student getStu() {
		return stu;
	}

	public void setStu(Student stu) {
		this.stu = stu;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

}
