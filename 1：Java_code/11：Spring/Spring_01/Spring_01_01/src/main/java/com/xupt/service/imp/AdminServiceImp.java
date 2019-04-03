package com.xupt.service.imp;

import com.xupt.service.AdminService;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 10:03
 */
public class AdminServiceImp extends ServiceBase implements AdminService{

    @Override
    public void check()
    {
        adminDao.checklogin();
        articleDao.add(null);
    }

}