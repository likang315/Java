package com.xupt.singleton_lazy_02;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 23:28
 */

/**
 * 线程安全，同步方法，效率太低
 */
public class Singleton {
    private static Singleton singleton;

    private Singleton() {}

    public static synchronized Singleton getInstance() {
        if (singleton == null)
        {
            singleton = new Singleton();
        }
        return singleton;
    }
}
