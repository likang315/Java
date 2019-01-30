package com.xupt.singleton_hungry_03;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 23:48
 */

/**
 * 枚举实现，不仅能避免多线程同步问题，而且还自动支持序列化机制
 */
public enum Singleton {
    INSTANCE;
    public void whateverMethod() {
    }
}

