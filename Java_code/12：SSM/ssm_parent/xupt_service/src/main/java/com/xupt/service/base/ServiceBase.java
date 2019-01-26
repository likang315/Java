package com.xupt.service.base;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 17:41
 */
public interface ServiceBase<T> {

    public void insert(T t);

    public void deleteById(Integer id);

    public void deleteByUUID(String uuid);

    public void update(T t);

    public T findById(int id);

    public T findByUUID(String uuid);


}



