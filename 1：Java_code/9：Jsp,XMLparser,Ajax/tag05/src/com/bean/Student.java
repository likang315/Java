package com.bean;

public class Student {
  private int id;
  private int age;
  private String name;
  private String sex;
  
  public Student() {}
  public Student(int id,int age,String name,String sex)
  {
	  this.id=id;
	  this.age=age;
	  this.name=name;
	  this.sex=sex;
  }
  
public int getId() {
	return id;
}
public void setId(int id) {
	this.id = id;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
  
}
