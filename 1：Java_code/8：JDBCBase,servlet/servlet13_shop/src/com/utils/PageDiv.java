package com.utils;

import java.util.List;

/**
 * 分页操作
 * @author Administrator
 * @param <T>
 */
public class PageDiv<T> 
{
     private int pageNo;//当前页
     private int pageSize;//每页多少条
     private int total;//总共多少条
     private int totalPage;//总共多少页
     
     private int start;//从第几页开始
     private int end;//第几页结束    
     private int counter;//显示几个页码
     
     private List<T> datas=null;//存放所有goods
     
     public PageDiv(int pageNo,int pageSize,int total,List<T> datas)
     {
    	 this.pageNo=pageNo;
    	 this.pageSize=pageSize;
    	 this.total=total;
    	 this.datas=datas;
    	 this.counter=5;
    	 
    	 this.totalPage=(total+pageSize-1)/pageSize;//取整，的下一页
    	 
    	 //设置也页码显示格式------
    	 this.start=pageNo-(counter/2)>0?pageNo-(counter/2):1;
    	 this.end=pageNo+(counter/2)<=totalPage?pageNo+(counter/2):totalPage;
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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
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

	public int getCounter() {
		return counter;
	}

	public void setCounter(int counter) {
		this.counter = counter;
	}

	public List<T> getDatas() {
		return datas;
	}

	public void setDatas(List<T> datas) {
		this.datas = datas;
	}
     
     
}
