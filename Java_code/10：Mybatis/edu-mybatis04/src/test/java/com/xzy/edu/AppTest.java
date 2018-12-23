package com.xzy.edu;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    public void testfindStudentsById()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlsession = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=sqlsession.getMapper(StudentMapper.class);
			List<Students> stus=sm.findStudentsById(1);
			
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
			
			//断言测试，两个参数一致继续执行，反之抛出异常
			assertEquals(1, stus.size());
			
			
		} finally {
			sqlsession.close();
		}
    }

	@Test
    public void testfindById()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlsession = sqlSessionFactory.openSession();
		try {
			StudentMapper sm=sqlsession.getMapper(StudentMapper.class);
			Map<String,Object> map=sm.findById(2);
		
			Iterator<Map.Entry<String, Object>> it=map.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, Object> en=it.next();
				System.out.println(en.getKey()+":"+en.getValue());
			}

		} finally {
			sqlsession.close();
		}
    }
	
	@Test
    public void testfindById2()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlsession = sqlSessionFactory.openSession();
		try {
			StudentMapper sm=sqlsession.getMapper(StudentMapper.class);
			List<Map<String,Object>> list=sm.findById2(0);
			
			for(Map<String,Object> map:list)
			{
				Iterator<Map.Entry<String, Object>> it=map.entrySet().iterator();
				while(it.hasNext())
				{
					Map.Entry<String, Object> en=it.next();
					System.out.println(en.getKey()+"--->"+en.getValue());
				}
			}

		} finally {
			sqlsession.close();
		}
    }
	
	@Test
    public void testfindBy03()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlsession = sqlSessionFactory.openSession();
		try {
		    
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("id", 0);
			param.put("name","Stu%");
	
			//不用接口，是SqlSession的方法,第一个参数相当于给Interface加智力一个方法
			List<Students> stus=sqlsession.selectList("com.zxy.mapper.StudentMapper.findBy03", param);
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
	
		} finally {
			sqlsession.close();
		}
		
    }
	
	
}
