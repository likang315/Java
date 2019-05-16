package com.xzy.edu;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.xzy.pojo.Stu;
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
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		try {
		
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			List<Stu> list=sm.findAllStu();
			for(Stu s:list)
			{
				System.out.println(s.getId()+"\t"+s.getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
			}

		} finally {
			sqlSession.close();
		}
    }

	
	
}
