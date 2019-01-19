package com.xupt.proxy;

import java.lang.reflect.Proxy;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/17 12:59
 */

/**
 * 代理工厂，传入实际对象返回代理对象
 */
public class ProxyFactory {
    public static Object getProxy(Object obj)
    {
        MyInvocation my=new MyInvocation(obj);
        return Proxy.newProxyInstance(obj.getClass().getClassLoader(),obj.getClass().getInterfaces(),my);
    }
}
