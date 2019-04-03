package com.xupt.bean;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/17 12:41
 */
public class FlushEmail implements Email{
    @Override
    public void receive()
    {
        System.out.println("receive Email......");
    }

    @Override
    public void send()
    {
        System.out.println("Send Email....");
    }
}
