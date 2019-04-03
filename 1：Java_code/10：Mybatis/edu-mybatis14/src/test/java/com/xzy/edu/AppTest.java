package com.xzy.edu;


import java.io.InputStream;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.xzy.pojo.Students;
import com.zxy.mapper.StudentMapper;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	//MyBatis默认支持一级缓存
	@Test
    public void testSelectfindid()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			Students s=sm.findStudentsById(4);
			System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			
			System.out.println("---------------------------------------------------------------");
			Students s1=sm.findStudentsById(4);
			System.out.println(s1.getStud_id()+"\t"+s1.getName()+"\t"+s1.getEmail()+"\t"+s1.getDob());
			
			
		} finally {
			sqlSession.close();
		}
		
    }
	
	
	
	
	
}
