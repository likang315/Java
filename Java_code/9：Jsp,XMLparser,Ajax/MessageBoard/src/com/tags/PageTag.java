package com.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import com.bean.Msg;
import com.dbutils.PageDiv;
/**
 * 自定义分页标签
 * @author likang
 *
 */
public class PageTag extends SimpleTagSupport 
{
    private boolean isinfo=false;
    
	@Override
	public void doTag() throws JspException, IOException {
		 StringBuilder  sb=new StringBuilder();
		 PageContext pc=(PageContext)this.getJspContext();
		 
		 PageDiv<Msg> pd=(PageDiv<Msg>)pc.getAttribute("pd", PageContext.REQUEST_SCOPE);
		 
 sb.append("<div class='row'>");
 sb.append("<div class='col-xs-8'>");
 sb.append("<nav aria-label='Page navigation pull-left'>");
 sb.append(" <ul class='pagination'>");
 sb.append(" <li>");
 
 
 sb.append(" <a href='show?pageNo="+pd.getPrevious()+"' aria-label='Previous'>");
 sb.append("<span aria-hidden='true'>上一页</span>");
 sb.append("</a>");
 sb.append(" </li>");
						 
						      for(int i=pd.getStart();i<=pd.getEnd();i++)
						      {
						    	  
						    	  sb.append(" <li><a href='show?pageNo="+i+"'>"+i+"</a></li>");
						    	 
						      }
						 
   
  sb.append(" <li>");
  sb.append("  <a href='show?pageNo="+pd.getNext()+"' aria-label='Next'>");
  sb.append("  <span aria-hidden='true'>下一页</span>");
  sb.append("  </a>");
  sb.append("  </li>");
  sb.append("  </ul>");
 
  sb.append(" </nav>");
  sb.append(" </div>");
  sb.append(" <div class='col-xs-4'>");
     if(isinfo)
     {
		  sb.append(" <span style='line-height: 50px;'><p>当前"+pd.getPageNo()+"/"+pd.getTotalPage()+"页，总共"+pd.getTotalCount()+"条</p></span>");
     }
				  sb.append(" </div>");
						  sb.append(" </div>");
		
		 pc.getOut().print(sb.toString());
		 
		 
	}

	public boolean getIsinfo() {
		return isinfo;
	}

	public void setIsinfo(boolean isinfo) {
		this.isinfo = isinfo;
	}

}
