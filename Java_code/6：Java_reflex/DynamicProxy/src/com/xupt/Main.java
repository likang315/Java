package com.xupt;

import com.xupt.bean.Email;
import com.xupt.bean.FlushEmail;
import com.xupt.proxy.ProxyFactory;

public class Main {

    public static void main(String[] args)
    {
        FlushEmail flu=new FlushEmail();
        flu.send();
        flu.receive();
        System.out.println("=================");

        Email proxy=(Email)ProxyFactory.getProxy(flu);
        proxy.send();
        proxy.receive();

    }
}
