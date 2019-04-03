package com.dao.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * 得到java.sql.Connection的一个管理类,不能实例化,得到的是子类：DruidConnectionManager
 * @author Administrator
 *
 */
public abstract class ConnectionManager 
{
     public static ConnectionManager cm=null;
     private static ThreadLocal<Connection> cl=new ThreadLocal<Connection>();
     
    
     /**
      * 得到自己的实例
      * @return
      */
     public static ConnectionManager newInstance()
     {
    	 if(null==cm)
    	 {
		    	 ResourceBundle res=ResourceBundle.getBundle("jdbc");
		    	 String str=res.getString("cm");
	    	 try{
				cm=(ConnectionManager)(Class.forName(str).newInstance());
			 } catch (Exception e) {
				e.printStackTrace();
			 } 
    	 }
    	return cm;
     }
     
     public abstract Connection getRealConnection();
     
     /**
      * 得到一个链接对像
      * @return
      */
     public Connection getConnection()
     {
    	 Connection con=cl.get();
    	 try {
			if(null==con||con.isClosed())
			 {
				 con=this.getRealConnection();
				 cl.set(con);
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	 return con;
     }
     
     /**
      * 关闭链接的功能
      */
     public void closeConnection()
     {
    	 Connection con=cl.get();
    	 try {
			if(null!=con&&!con.isClosed())
			 {
				 con.close();
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
     
     /**
      * 开启事务
      */
     public void beginTransaction()
     {
    	 Connection con=this.getConnection();
    	 try {
				 if(null!=con&&!con.isClosed())
				 {
					 con.setAutoCommit(false);
				 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
     
     /**
      * 提交
      */
     public void commint()
     {
    	 Connection con=this.getConnection();
    	 try {
			if(null!=con&&!con.isClosed())
			 {
				 con.commit();
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
     
     /**
      * 回滚
      */
     public void rollback()
     {
    	 Connection con=this.getConnection();
    	 try {
				 if(null!=con&&!con.isClosed())
				 {
					 con.rollback();
				 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
     }
}
