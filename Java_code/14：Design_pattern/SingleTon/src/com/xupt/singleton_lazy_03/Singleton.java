package com.xupt.singleton_lazy_03;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 23:40
 */

/**
 * 静态内部类，通过类加载机制，懒加载，调用方法才会装载，效率高
 */
public class Singleton {

    private Singleton() {}

    private static class SingletonInstance {
        private static final Singleton INSTANCE = new Singleton();
    }

    public static Singleton getInstance() {
        return SingletonInstance.INSTANCE;
    }
}
