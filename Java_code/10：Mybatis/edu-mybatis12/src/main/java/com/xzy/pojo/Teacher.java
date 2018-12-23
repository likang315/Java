package com.xzy.pojo;

import java.util.ArrayList;
import java.util.List;

public class Teacher {
	private int id;
	private String name;
	 private int age;
	 private String sex;
	 
	 List<Stu> stus=new ArrayList<Stu>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public List<Stu> getStus() {
		return stus;
	}

	public void setStus(List<Stu> stus) {
		this.stus = stus;
	}
	 
}
