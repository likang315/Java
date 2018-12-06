package com.service.core;

import com.dao.AdminDao;
import com.dao.LogDao;
import com.dao.T1Dao;
import com.dao.UserlogDao;
import com.dao.core.DaoFactory;

public class ServiceBase 
{
   protected AdminDao adminDao=(AdminDao)DaoFactory.getDao(AdminDao.class);
   protected UserlogDao userlogDao=(UserlogDao)DaoFactory.getDao(UserlogDao.class);
   
   //...
   protected T1Dao t1dao=(T1Dao)DaoFactory.getDao(T1Dao.class);
   protected LogDao logDao=(LogDao)DaoFactory.getDao(LogDao.class);
}
