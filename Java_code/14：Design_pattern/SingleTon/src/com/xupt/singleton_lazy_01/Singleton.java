package com.xupt.singleton_lazy_01;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 22:58
 */

/**
 * 懒汉式，对象的延时加载,线程安全，同步代码块，双重检查Double-Check,效率高
 */

public class Singleton {
    private static Singleton instance=null;
    private Singleton() {

    }

    public static Singleton getInstance() {

        if(instance==null)
        {
            //只允许一个类的实例进入，同步块
            synchronized(Singleton.class)
                    {
                        if(instance==null)
                        {
                            instance=new Singleton();
                        }
                    }
        }
        return instance;
    }

    public static void main(String[] args) {
        Singleton p = Singleton.getInstance();
    }

}
