package com.xupt.pojo;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/23 20:03
 */
public class User {

    private String  name;
    private int age;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getAge()
    {
        return age;
    }

    public void setAge(int age)
    {
        this.age = age;
    }

    public User(String name, int age)
    {
        this.name = name;
        this.age = age;
    }

    public User()
    {
    }
}
