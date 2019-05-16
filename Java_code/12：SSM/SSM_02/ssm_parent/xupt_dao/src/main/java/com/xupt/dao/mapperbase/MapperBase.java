package com.xupt.dao.mapperbase;

/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/25 16:50
 */

/**
 * mapperBase 基类封装了 CRUD
 * @param <T>
 */
public interface MapperBase<T> {

    public T findById(Integer id);
    public T findByUUID(String uuid);

    public void deleteById(Integer id);
    public void deleteByUUID(String uuid);

    public void update(T t);

    public void insert(T t);

}
