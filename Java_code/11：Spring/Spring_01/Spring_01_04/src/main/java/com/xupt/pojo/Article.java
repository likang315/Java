package com.xupt.pojo;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 9:51
 */
public class Article {
    private  String  name;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

  
    @Override
    public String toString()
    {
        return "Article{" + "name='" + name + '\'' + '}';
    }

}
