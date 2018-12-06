package com.dao.core;

import java.util.List;

//分页
public class PageDiv<T>
{
   private int pageSize;//每页几条数据
   private int pageNo;//要第几页
   private int total;//总共多少条
   private int totalPage;//总共多少页
   private List<T> datas;//当前页数据
   
   public PageDiv(){}
   public PageDiv(int pageSize,int pageNo,int total,List<T>datas)
   {
	   this.pageSize=pageSize;
	   this.pageNo=pageNo;
	   this.total=total;
	   this.datas=datas;
	   
	   this.totalPage=(total+pageSize-1)/pageSize;
   }
   
   
public int getPageSize() {
	return pageSize;
}
public void setPageSize(int pageSize) {
	this.pageSize = pageSize;
}
public int getPageNo() {
	return pageNo;
}
public void setPageNo(int pageNo) {
	this.pageNo = pageNo;
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
public List<T> getDatas() {
	return datas;
}
public void setDatas(List<T> datas) {
	this.datas = datas;
}

}
