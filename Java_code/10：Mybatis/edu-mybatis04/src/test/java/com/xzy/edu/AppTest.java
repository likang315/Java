package com.xzy.edu;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.ArrayList;
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
    public void testSelect()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Students> stus=sm.findStudentsById(2);
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
			assertEquals(3, stus.size());
			
	//		Asser.assertEquals(3, stus.size());
		} finally {
		 session.close();
		}
		
    }
	
	@Test
    public void testSelectfindid()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			Map<String,Object> map=sm.findById(4);
			
			Iterator<Map.Entry<String, Object>> it=map.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, Object> en=it.next();
				System.out.println(en.getKey()+"--->"+en.getValue());
			}
	//		Asser.assertEquals(3, stus.size());
		} finally {
		 session.close();
		}
		
    }
	
	@Test
    public void testSelectfindid2()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Map<String,Object>> maps=sm.findById2(2);
			
			for(Map<String,Object> map:maps)
			{
			Iterator<Map.Entry<String, Object>> it=map.entrySet().iterator();
			while(it.hasNext())
			{
				Map.Entry<String, Object> en=it.next();
				System.out.println(en.getKey()+"--->"+en.getValue());
			}
			}
	//		Asser.assertEquals(3, stus.size());
		} finally {
		 session.close();
		}
		
    }
	
	
	@Test
    public void testSelectfindid3()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);

		// SqlSession相当于我们的Connection
		SqlSession session = sqlSessionFactory.openSession();
		try {
		    
			Map<String,Object> param=new HashMap<String, Object>();
			param.put("id", 0);
			param.put("name","Stude%");
			
			//没有用接口
			List<Students> stus=session.selectList("com.zxy.mapper.StudentMapper.findBy03", param);
			
			
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}
	//		Asser.assertEquals(3, stus.size());
		} finally {
		 session.close();
		}
		
    }
	
	
}
