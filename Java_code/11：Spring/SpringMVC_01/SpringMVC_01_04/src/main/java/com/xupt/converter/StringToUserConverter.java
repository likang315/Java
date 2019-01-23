package com.xupt.converter;

import com.xupt.bean.User;
import org.springframework.core.convert.converter.Converter;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/21 16:31
 */
public class StringToUserConverter implements Converter<String,User> {

    @Override
    public User convert(String o)
    {
        User user=new User();

        if(!o.equals(null))
        {
            String str[]=o.split(":");
            user.setName(str[0]);
            user.setAddress(str[1]);

        }
        return user;
    }
}

