package com.xupt.interface_adapter;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/2/2 16:57
 */

/**
 * 使用抽象类，空方法体，可以让实现类有选择的实现
 */
public abstract class Usb_abs implements Usb{
    @Override
    public  void transfer_01(){}

    @Override
    public  void transfer_02(){}
}
