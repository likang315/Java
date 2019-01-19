package com.xupt.dao.imp;

import com.xupt.service.AdminService;
import com.xupt.service.imp.ServiceBase;
import org.springframework.stereotype.Service;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 14:37
 */
@Service
public class AdminServiceImp extends ServiceBase implements AdminService {

    @Override
    public void addArticle()
    {
        adminDao.checklogin();
        articleDao.add(null);
    }

}
