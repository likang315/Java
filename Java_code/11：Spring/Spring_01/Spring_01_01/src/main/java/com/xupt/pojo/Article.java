package com.xupt.pojo;

import java.util.Objects;

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
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Article)) return false;
        Article article = (Article) o;
        return Objects.equals(getName(), article.getName());
    }

   @Override
    public int hashCode()
    {
        return Objects.hash(getName());
    }

    @Override
    public String toString()
    {
        return "Article{" + "name='" + name + '\'' + '}';
    }

}
