package com.xzy.main;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.xzy.pojo.PhoneNumber;
import com.xzy.pojo.Students;
import com.zxy.mapper.StudentMapper;
/**
 * mybatis 演示实例
 * @author likang
 *
 */
public class Main {

	public static void main(String[] args) 
	{
		//加载mybatis的配置文件
		InputStream inputStream = Main.class.getResourceAsStream("/mybatis-config.xml");
		//相当于根据mybatis-config.xml构建接池
		SqlSessionFactory sqlSessionFactory =new SqlSessionFactoryBuilder().build(inputStream);
		// SqlSession相当于我们的Connection
		SqlSession sqlSession = sqlSessionFactory.openSession();
		
		try {
			
			StudentMapper sm=sqlSession.getMapper(StudentMapper.class);
			Students stu=new Students();
			stu.setName("lihui");
			stu.setEmail("13152003556@163.com");
			stu.setDob(new SimpleDateFormat("yyyy-MM-dd").parse("1996-1-12"));
			stu.setPhone(new PhoneNumber("029-13-84558"));
			
			sm.insertStudent(stu);
			sqlSession.commit();
			
		} catch (ParseException e) {
			e.printStackTrace();
			sqlSession.rollback();
			
		} finally {
		 sqlSession.close();
		}
		
	}

}
