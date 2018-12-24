package com.xzy.edu;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.Test;

import com.xzy.pojo.Stu;
import com.xzy.pojo.Teacher;
import com.zxy.mapper.StudentMapper;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
	private static	Logger log=Logger.getLogger(AppTest.class);
	
	@Test
    public void manytomany()
    {
		InputStream inputStream = AppTest.class.getResourceAsStream("/mybatis-config.xml");
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		try {
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			List<Stu> list=sm.findAllStu();
			for(Stu s:list)
			{
				System.out.println(s.getId()+"\t"+s.getName()+"\t"+s.getClazz().getName()+"\t"+s.getSex()+"\t"+s.getAge()+"\t"+s.getScore().getEnglish()+"\t"+s.getScore().getMath()+"\t"+s.getScore().getPe());
			
			    List<Teacher> teas=s.getTeas();
			    for(Teacher tea:teas)
			    {
			    	System.out.println("------------>"+tea.getId()+"\t"+tea.getCourse()+":"+tea.getName()+"\t"+tea.getAge()+"\t"+tea.getSex());
			    }
			}
			
		}catch (Exception e) {
			log.equals(e);
				
		}finally {
			sqlSession.close();
		}
    }
}
