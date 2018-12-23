package com.xzy.main;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

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
		SqlSession session = sqlSessionFactory.openSession();
		
		try {
			StudentMapper sm=session.getMapper(StudentMapper.class);
			List<Students> stus=sm.findAllStudents();
			
			for(Students s:stus)
			{
				System.out.println(s.getStud_id()+"\t"+s.getName()+"\t"+s.getEmail()+"\t"+s.getDob());
			}

		} finally {
		 session.close();
		}
		
	}

}
