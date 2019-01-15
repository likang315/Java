package com.xzy.edu;


import java.io.InputStream;
import java.util.List;

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
	@Test
    public void testSelect()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		try {
		
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			Students param=new Students();
			param.setStud_id(0);
			param.setEmail("Hello@163.com");
			
			List<Students> stus=sm.findAllStudents(param);
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
	
		} finally {
			sqlSession.close();
		}
		
    }
	
	
	
	
}
