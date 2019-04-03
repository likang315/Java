package com.xupt.dao.imp;

import com.xupt.dao.Email;
import org.springframework.stereotype.Repository;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 14:25
 */
@Repository
public class FlashEmail implements Email {

    @Override
    public void send()
    {
        System.out.println("FlashEmail...........");
    }
}
