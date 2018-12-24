package com.zxy.mapper;

import java.util.List;

import com.xzy.pojo.Score;
import com.xzy.pojo.Stu;

public interface StudentMapper {
	//查询所有学生
	public List<Stu> findAllStu();
	//对应select标签
	public Score findScorebyid(int id);
}
