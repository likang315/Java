package com.zxy.mapper;

import java.util.List;

import com.xzy.pojo.Students;

public interface StudentMapper {
	//查询所有学生
	public List<Students> findAllStudents();
	
}
