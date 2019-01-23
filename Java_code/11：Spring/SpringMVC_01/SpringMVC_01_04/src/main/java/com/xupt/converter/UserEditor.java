package com.xupt.converter;

import com.xupt.bean.User;

import java.beans.PropertyEditorSupport;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 16:04
 */

/**
 * 自定义转换器string ---> User
 */
public class UserEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String s) throws IllegalArgumentException
    {
        User user =new User();
        if(s!=null)
        {
            String str[] =s.split(":");
            user.setName(str[0]);
            user.setAddress(str[1]);

        }

        setValue(user);
    }
}
