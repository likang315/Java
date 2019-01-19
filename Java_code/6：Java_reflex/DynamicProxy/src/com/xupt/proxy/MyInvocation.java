package com.xupt.proxy;

import com.xupt.util.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/17 12:43
 */

/**
 * 动态代理必须实现invovationHandler
 */
public class MyInvocation implements InvocationHandler {

    //代理的实际对象
    private Object obj;
    public MyInvocation(Object obj)
    {
        this.obj=obj;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable
    {
        Object re=null;
        if("send".equals(method.getName()))
        {
            Logger.before();
            method.invoke(obj,objects);//传入代理的实际对象和参数,调用原始方法
            Logger.after();
        }else
        {
            method.invoke(obj,objects);
        }

        return re;
    }
}
