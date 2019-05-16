package com.xupt.dao.imp;

import com.xupt.dao.AdminDao;
import org.springframework.stereotype.Repository;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 9:45
 */
@Repository
public class AdminDaoImp implements AdminDao {
    @Override
    public void checklogin()
    {
        System.out.println("验证....");
    }

}
