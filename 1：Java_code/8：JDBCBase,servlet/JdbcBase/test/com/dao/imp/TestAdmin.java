package com.dao.imp;

import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import com.dao.AdminDao;
import com.dao.core.DaoFactory;
import com.dao.core.PageDiv;
import com.bean.Admin;
import com.utils.MD5;

public class TestAdmin {

	@Test
	public void testUpdate()
	{
		AdminDao ad=new AdminDaoImp();
		Admin admin=new Admin();
		admin.setId(4);
		admin.setUname("lisa");
		admin.setUpwd(MD5.tomd5("456"));
		admin.setUpur("110000000000000");
		
		
		try {
			ad.update(admin);
			System.out.println("Ok");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testAdd()
	{
		AdminDao ad=new AdminDaoImp();
		Admin admin=new Admin();
		admin.setId(4);
		admin.setUname("lisa");
		admin.setUpwd(MD5.tomd5("456"));
		admin.setUpur("110000000000000");
		
		
		try {
			int re=ad.insert(admin);
			System.out.println(re);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testDelete()
	{
		AdminDao ad=new AdminDaoImp();
		try {
			ad.delete(8, Admin.class);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void testGet()
	{
		AdminDao ad=new AdminDaoImp();
		try {
			Admin admin=ad.get(4, Admin.class);
			System.out.println(admin.getId()+"\t"+admin.getUname()+"\t"+admin.getUpwd()+"\t"+admin.getUpur());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testGetAll()
	{
		AdminDao ad=new AdminDaoImp();
		try {
			List<Admin> all=ad.getAll(Admin.class);
			for(Admin admin:all)
			System.out.println(admin.getId()+"\t"+admin.getUname()+"\t"+admin.getUpwd()+"\t"+admin.getUpur());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testByPage()
	{
		AdminDao ad=(AdminDao)DaoFactory.getDao(AdminDao.class);
		try {
			PageDiv<Admin> pd=ad.getByPage(Admin.class, 3, 3);
			List<Admin> all=pd.getDatas();
			for(Admin admin:all)
			System.out.println(admin.getId()+"\t"+admin.getUname()+"\t"+admin.getUpwd()+"\t"+admin.getUpur());
		
		    System.out.println("--->"+pd.getPageNo()+"/"+pd.getTotalPage()+"---"+pd.getTotal()+"条");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
