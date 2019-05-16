package com.xupt.dao;

import com.xupt.pojo.Stu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @Author: likang
 * @mail: 13571887352@163.com
 * @GitHub：https://github.com/likang315
 * @CreateTime： 2019/1/24 17:18
 */

/**
 * 实例化此类放入到loc容器中，持久层
 */
@Repository
public interface StuDao extends JpaRepository<Stu,Integer> {

}
