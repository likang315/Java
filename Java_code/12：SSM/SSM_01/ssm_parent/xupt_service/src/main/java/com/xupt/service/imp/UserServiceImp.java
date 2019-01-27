package com.xupt.service.imp;

import com.xupt.model.pojo.User;
import com.xupt.service.IUserService;
import com.xupt.service.base.ServiceBaseImp;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 17:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImp extends ServiceBaseImp<User> implements IUserService{


    @Override
    public User findById(int id)
    {
        User user=usermapper.findById(id);
        return user;
    }

    @Override
    public User findByUUID(String uuid)
    {
        return null;
    }

    @Override
    public void deleteById(Integer id)
    {

    }

    @Override
    public void deleteByUUID(String uuid)
    {

    }

    @Override
    public void update(User user)
    {

    }

    @Override
    public void insert(User user)
    {

    }

    @Override
    public Boolean login(String uname, String upwd)
    {
        return null;
    }
}
