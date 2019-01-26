package com.xupt.service.base;

import com.xupt.dao.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 17:45
 */

/**
 * 统一管理dao层
 * @param <T>
 */
public abstract class ServiceBaseImp<T> implements ServiceBase<T> {

    @Autowired
    protected UserMapper usermapper;
}
