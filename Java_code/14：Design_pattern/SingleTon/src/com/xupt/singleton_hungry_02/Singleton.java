package com.xupt.singleton_hungry_02;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 23:19
 */

/**
 * 通过静态块，装载类的时候就加载了
 */
public class Singleton {
    private static Singleton instance;

    static {
        instance = new Singleton();
    }

    private Singleton() {}

    public static Singleton getInstance() {
        return instance;
    }
}
