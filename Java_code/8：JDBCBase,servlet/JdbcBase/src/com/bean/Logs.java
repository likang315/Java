package com.bean;

import com.annotation.Colunm;
import com.annotation.Exclude;
import com.annotation.Table;
@Table("log")
public class Logs {
private int	id;
@Colunm("t_id")
private int	t1id;
private String	msg;
@Exclude
private int t;

public int getId() {
	return id;
}

public void setId(int id) {
	this.id = id;
}

public int getT1id() {
	return t1id;
}

public void setT1id(int t1id) {
	this.t1id = t1id;
}

public String getMsg() {
	return msg;
}

public void setMsg(String msg) {
	this.msg = msg;
}

public int getT() {
	return t;
}

public void setT(int t) {
	this.t = t;
}


}
