package com.xzy.mapper;

import java.util.List;

import com.xzy.pojo.Students;

public interface StudentMapper {
	
	//查询所有学生
	public List<Students> findAllStudents();
	
	//据据id找学生
	public Students findStudentById(int id);
	//增加一个学生
	public int insertStudent(Students student);
	//修改学生信息
	public void updateStudent(int id);
	//删除学生信息
	public void deleteStudent(int id);
	
	
	
}
