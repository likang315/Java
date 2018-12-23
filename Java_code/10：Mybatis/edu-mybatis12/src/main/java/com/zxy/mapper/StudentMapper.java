package com.zxy.mapper;

import java.util.List;

import com.xzy.pojo.Clazz;
import com.xzy.pojo.Score;
import com.xzy.pojo.Stu;
import com.xzy.pojo.Teacher;

public interface StudentMapper {
	//查询所有学生
	public List<Stu> findAllStu01();

	public Score findScorebyid(int id);
	
	public List<Clazz> findAllClazz();
	
	public List<Teacher> findAllTeacher();
}
