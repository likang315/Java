package com.xupt.singleton_hungry_01;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/30 23:05
 */

/**
 * 饿汉式，
 */
public class Singleton {

        private static Singleton instance=new Singleton();
        private Singleton() {

        }

        public static Singleton getInstance() {
            return instance;
        }


        public static void main(String[] args) {
            Singleton p = Singleton.getInstance();
        }


}
