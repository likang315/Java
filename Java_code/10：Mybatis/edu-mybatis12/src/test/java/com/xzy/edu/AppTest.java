package com.xzy.edu;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.xzy.pojo.Stu;
import com.xzy.pojo.Teacher;
import com.zxy.mapper.StudentMapper;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	@Test
    public void many2many()
    {

		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		try {
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			List<Teacher> clazzs=sm.findAllTeacher();
			for(Teacher c:clazzs)
			{
				System.out.println(c.getId()+"\t"+c.getName()+"\t"+c.getAge()+"\t"+c.getSex());
			
				for(Stu s:c.getStus())
				{
					System.out.println("----------------->"+s.getId()+"\t"+s.getName()+"\t"+s.getClazz().getName()+"\t  "+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
				}
		
			}

		} finally {
			sqlSession.close();
		}
    }
	
	
}
