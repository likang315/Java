package com.xzy.edu;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.xzy.pojo.Clazz;
import com.xzy.pojo.Stu;
import com.xzy.pojo.Teacher;
import com.zxy.mapper.StudentMapper;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
    public void testOne2ont()
    {

		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Stu> list=sm.findAllStu01();
			for(Stu s:list)
			{
				//System.out.println(s.getId()+"\t"+s.getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore());
				System.out.println(s.getId()+"\t"+s.getName()+"\t"+s.getClazz().getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
			}
		
			
			//查询
			
			

		} finally {
		 session.close();
		}
    }

	@Test
    public void one2many()
    {

		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Clazz> clazzs=sm.findAllClazz();
			for(Clazz c:clazzs)
			{
				System.out.println(c.getId()+"\t"+c.getName());
			
			
				for(Stu s:c.getStus())
				{
					//System.out.println(s.getId()+"\t"+s.getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore());
					System.out.println("----------------->"+s.getId()+"\t"+s.getName()+"\t"+s.getClazz().getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
				}
		
			}
			//查询
			
			

		} finally {
		 session.close();
		}
    }
	
	

	@Test
    public void many2many()
    {

		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Teacher> clazzs=sm.findAllTeacher();
			for(Teacher c:clazzs)
			{
				System.out.println(c.getId()+"\t"+c.getName()+"\t"+c.getAge()+"\t"+c.getSex());
			
			
				for(Stu s:c.getStus())
				{
					System.out.println("----------------->"+s.getId()+"\t"+s.getName()+"\t"+s.getClazz().getName()+"\t  "+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
				}
		
			}
			//查询
			
			

		} finally {
		 session.close();
		}
    }
	
	
}
