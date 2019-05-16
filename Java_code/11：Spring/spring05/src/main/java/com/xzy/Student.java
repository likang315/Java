package com.xzy;

import java.util.Arrays;

public class Student {
  private String name;
  private int age;
  
  public Student() {}
  public Student(String name,int age)
  {
	  this.name=name;
	  this.age=age;
  }
  
  public String toPwd(String pwd)
  {
	 char[] aa=pwd.toCharArray();
	 Arrays.sort(aa);
	 return new String(aa);
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
  
  
}
