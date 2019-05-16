package com.bean;

import com.annotation.Colunm;
import com.annotation.Exclude;
import com.annotation.Table;

@Table("t_admin")
public class Admin {
	
 private long id;
 
 //属性名和字段名不一样
 @Colunm("t_name")
 private String uname;
 
 //属性名和字段名一样
 private String upwd;
 private String upur;
 
 @Exclude
 public int dcc;
 

public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public int getDcc() {
	return dcc;
}
public void setDcc(int dcc) {
	this.dcc = dcc;
}
public String getUname() {
	return uname;
}
public void setUname(String uname) {
	this.uname = uname;
}
public String getUpwd() {
	return upwd;
}
public void setUpwd(String upwd) {
	this.upwd = upwd;
}
public String getUpur() {
	return upur;
}
public void setUpur(String upur) {
	this.upur = upur;
}
 
}
