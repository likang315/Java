package com.xzy.edu;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.xzy.pojo.Students;
import com.zxy.mapper.StudentMapper;

/**
 * Unit test for simple App
 */
public class AppTest 
{
	@Test
    public void testADD()
    {

		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			//增加
			Students stu=new Students();
			stu.setName("小李");
			stu.setEmail("Hello@163.com");
			stu.setDob(new SimpleDateFormat("yyyy-MM-dd").parse("1997-11-12"));
			
			sm.insertStudent(stu);
			
			sqlSession.commit();
			System.out.println("主键回填："+stu.getStud_id());
			
		} catch (ParseException e) {
			sqlSession.rollback();
			e.printStackTrace();
			
		} finally {
			sqlSession.close();
		}
    }

	
	
}
