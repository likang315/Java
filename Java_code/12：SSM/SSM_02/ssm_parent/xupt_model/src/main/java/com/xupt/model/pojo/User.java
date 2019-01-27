package com.xupt.model.pojo;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 17:04
 */
public class User {

    private int id;
    private String uname;
    private String upwd;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getUname()
    {
        return uname;
    }

    public void setUname(String uname)
    {
        this.uname = uname;
    }

    public String getUpwd()
    {
        return upwd;
    }

    public void setUpwd(String upwd)
    {
        this.upwd = upwd;
    }

    public User(){}

    public User(int id, String uname, String upwd)
    {
        this.id = id;
        this.uname = uname;
        this.upwd = upwd;
    }

    @Override
    public String toString()
    {
        return "User{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", upwd='" + upwd + '\'' +
                '}';
    }
}
