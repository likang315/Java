package com.service.imp;

import java.sql.SQLException;

import com.bean.Admin;
import com.bean.Logs;
import com.bean.T1;
import com.bean.Userlog;
import com.service.UserService;
import com.service.core.ServiceBase;

public class UserServiceImp extends ServiceBase implements UserService {

	@Override
	public void addUser(Admin admin)throws SQLException 
	{
		 int lastid= adminDao.insert(admin);
		 Userlog ul=new Userlog();
		 ul.setAdmin_id(lastid);
		 ul.setMsg("增加了一个用户");
		 userlogDao.insert(ul);
	}

	@Override
	public void addT1(T1 t1) throws SQLException 
	{
		int id=t1dao.insert(t1);
		Logs log=new Logs();
		log.setT1id(id);
		log.setMsg("大家好");
		
		logDao.insert(log);
	}

}
