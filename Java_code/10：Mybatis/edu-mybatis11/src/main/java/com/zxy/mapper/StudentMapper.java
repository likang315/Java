package com.zxy.mapper;

import java.util.List;

import com.xzy.pojo.Clazz;
import com.xzy.pojo.Score;
import com.xzy.pojo.Stu;

public interface StudentMapper {
	
	//查询所有学生,one to one 
	public List<Stu> findAllStu();

	public Score findScorebyid(int id);
	
	public Clazz findClazzById(int id);
	
	
	//one to many
	public List<Clazz> findAllClazz();
	
	public List<Stu> findStuByClassId();
}
