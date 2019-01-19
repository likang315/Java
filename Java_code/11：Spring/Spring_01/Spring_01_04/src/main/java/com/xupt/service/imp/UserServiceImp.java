package com.xupt.service.imp;

import com.xupt.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @Author: likang
 * @Email: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/16 14:32
 */
@Service
public class UserServiceImp extends ServiceBase implements UserService {
    @Override
    public void check()
    {
        adminDao.checklogin();
    }
}
