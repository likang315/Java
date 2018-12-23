package com.zxy.mapper;

import java.util.List;
import java.util.Map;

import com.xzy.pojo.Students;

public interface StudentMapper {
	
	//根据ID查询
	public List<Students> findStudentsById(int id);
	
	//返回Map，一个对象
	public Map<String,Object> findById(int id);
	
	//返回List<Map> ,多个对象
	public List<Map<String,Object>> findById2(int id);
	
	
}
