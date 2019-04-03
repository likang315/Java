package com.dbutils;

import java.util.List;

/**
 * 封装一页数据，完整的分页算法
 * @author Administrator
 * @param <T>
 */
public class PageDiv<T> 
{
    private int pageNo;//当前多少页
    private int pageSize;//每页多少条
    private int totalPage;//总共多少页,用来下一页，上一页操作
    private int totalCount;//总共多少条
    private int indexCount=7;//要显示几个页码
    private int start;//从第几页开始
    private int end;//到第几页结束
    
    private  List<T> list;//当前页中的数据
    
    public PageDiv(){}
    public PageDiv(int pageNo,int pageSize,int totalCount,List<T> list)
    {
    	this.pageNo=pageNo;
    	this.pageSize=pageSize;
    	this.totalCount=totalCount;
    	this.totalPage=(totalCount+pageSize-1)/pageSize;
    	this.list=list;
    	
    	if(pageNo-indexCount/2<1)
    		start=1;
    	else
    		start=pageNo-indexCount/2;
    	
    	if(pageNo+indexCount/2>totalPage)
            end=totalPage;
    	else
    		end=pageNo+indexCount/2;
    }
    
    //上一页
    public int getPrevious()
    {
       int re=0;
       if(this.pageNo-1>0)
    	   re=this.pageNo-1;
       else
    	   re=1;
    	   
       return re;
    }
    //下一页
    public int getNext()
    {
       int re=0;
       if(this.pageNo+1<this.totalPage)
    	   re=this.pageNo+1;
       else
    	   re=this.totalPage;
       
       return re;
    }
    
	public int getPageNo() {
		return pageNo;
	}
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getList() {
		return list;
	}
	public void setList(List<T> list) {
		this.list = list;
	}
	public int getIndexCount() {
		return indexCount;
	}
	public void setIndexCount(int indexCount) {
		this.indexCount = indexCount;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getEnd() {
		return end;
	}
	public void setEnd(int end) {
		this.end = end;
	}

    
    
}
