package com.xupt.class_adapter;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/2/2 16:45
 */

/**
 * 类适配器，实现了两个接口之间的不匹配，不兼容问题
 */
public class Adapter extends Type_imp implements Andriod {

    @Override
    public void transfer_2()
    {
        System.out.println("Andriod:传输数据.......");
    }

}
