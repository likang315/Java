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
	
			List<Students> stus=sm.findAllStudents();
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
			sqlSession.close();//把sesion 关闭
	        System.out.println("----------------------------------------");
	        
	      
	  		SqlSession sqlSession1 = sqlSessionFactory.openSession();
			StudentMapper sm1=sqlSession1.getMapper(StudentMapper.class);
			List<Students> stus1=sm1.findAllStudents();
			for(Students s1:stus1)
			{
				System.out.println(s1.getStud_id()+"\t"+s1.getName()+"\t"+s1.getEmail()+"\t"+s1.getDob());
			}
			sqlSession1.close();//把sesion 关闭
	      
		} finally {
			sqlSession.close();
		}
		
    }
	
}
