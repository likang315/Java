package com.bean;

public class User {
   private int age;
   private String name;
   private String sex;
   public User() {}
   public User(String name,int age,String sex)
   {
	   this.name=name;
	   this.age=age;
	   this.sex=sex;
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
