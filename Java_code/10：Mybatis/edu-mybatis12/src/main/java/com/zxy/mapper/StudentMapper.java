package com.zxy.mapper;

import java.util.List;

import com.xzy.pojo.Clazz;
import com.xzy.pojo.Stu;
import com.xzy.pojo.Teacher;

public interface StudentMapper {
	
	//查询所有学生
	public List<Stu> findAllStu01();
	
	//查询所有班级
	public List<Clazz> findAllClazz();
	
	//查询所有老师
	public List<Teacher> findAllTeacher();
}
