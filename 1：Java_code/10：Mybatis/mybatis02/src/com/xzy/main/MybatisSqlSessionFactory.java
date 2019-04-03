package com.xzy.main;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.xzy.mapper.StudentMapper;
import com.xzy.pojo.Students;

public class MybatisSqlSessionFactory {

	private static SqlSessionFactory sqlSessionFactory;
	//得到SQLSessionFactory
	public static SqlSessionFactory getSqlSessionFactory()
	{
		if(sqlSessionFactory == null)
		{
			InputStream in;
			try
			{
				in = Resources.getResourceAsStream("mybatis-config.xml");
				sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
			}
			catch (IOException e)
			{	
				throw new RuntimeException(e.getCause());
			}
		}
		return sqlSessionFactory;
	}
	
	//得到SqlSession
	public static SqlSession openSession()
	{
		return getSqlSessionFactory().openSession();
	}
	
	
	public static void main(String[] args) 
	{
		SqlSession sqlSession=openSession();
		StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
		
		try {
		
		//增加
		Students stu=new Students();
		stu.setName("likang");
		stu.setEmail("13571881752@163.com");
		stu.setDob(new SimpleDateFormat("yyyy-MM-dd").parse("1997-11-12"));
		
			sm.insertStudent(stu);
			sqlSession.commit();
			System.out.println("OK");
			
		
		//查找一个
		Students stu1=sm.findStudentById(5);
		sqlSession.commit();
		System.out.println(stu1.toString());
		
		//修改
		sm.updateStudent(1);
		sqlSession.commit();
		System.out.println("OK");
		
		//删除
		sm.deleteStudent(5);
		sqlSession.commit();
		System.out.println("OK");
		
		} catch (ParseException e) {
			sqlSession.rollback();
			e.printStackTrace();
		} finally{
			sqlSession.close();
		}
		
		
	}

}
