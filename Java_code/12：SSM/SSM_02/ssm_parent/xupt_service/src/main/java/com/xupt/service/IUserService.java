package com.xupt.service;

import com.xupt.model.pojo.User;
import com.xupt.service.base.ServiceBase;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 19:45
 */

/**
 * service层对User表特有的方法
 * @author likang
 */
public interface IUserService extends ServiceBase<User> {

    public Boolean login(String uname,String upwd);

}
