package com.xupt.dao;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Author: likang
 * Email: 13571887352@163.com
 * GitHub：https://github.com/likang315
 * CreateTime： 2019/1/22 16:02
 */

public class DaoTeacher{

    public static void main(String[] args) throws SQLException
    {
        ApplicationContext context = new ClassPathXmlApplicationContext(new String[] {"/ApplicationContext.xml"});
        DruidDataSource ds=(DruidDataSource) context.getBean("dataSource");


        Connection con=ds.getConnection();
        System.out.println(con);

        String sql="insert into clazz values (4,'datasource')";
        PreparedStatement stat=con.prepareStatement(sql);

        stat.executeQuery();

    }
}
