package com.dbutils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import com.alibaba.druid.pool.DruidDataSource;
/**
 * druid连接池的操作，以及事务 
 * @author likang
 *
 */
public class Db //extends QueryRunner 
{
	private static DruidDataSource ds=null;
	//dbtuils 中的类，用来执行sql语句的
	private static QueryRunner run=new QueryRunner();
	//只放进行事务的Connection,如果多个进程同时访问，会给个副本，不会造成线程安全的问题
	private static ThreadLocal<Connection> conn=new ThreadLocal<Connection>();
	   
	   static{
		  //初始化连接池
		   try {
				ResourceBundle res=ResourceBundle.getBundle("jdbc");
				
				ds=new DruidDataSource();
				ds.setUrl(res.getString("url"));
				ds.setDriverClassName(res.getString("driverClassName"));
				ds.setUsername(res.getString("username"));
				ds.setPassword(res.getString("password"));
				ds.setFilters(res.getString("filters"));
				ds.setMaxActive(Integer.parseInt(res.getString("maxActive")));
				ds.setInitialSize(Integer.parseInt(res.getString("initialSize")));
				ds.setMaxWait(Long.parseLong(res.getString("maxWait")));
				ds.setMinIdle(Integer.parseInt(res.getString("minIdle")));
				
			} catch (Exception e){
				
			} 
	   }
	 
	    /**
	     *从DruidDataSource或者Threadlocal 中得到Connection  
	     * @return
	     * @throws SQLException
	     */
	    public static Connection getConnection() throws SQLException{  
	        //从ThreadLocal中得到connection  
	        Connection con = conn.get();  
	        if(null==con)
	        {
	        	con=ds.getConnection();
	        	conn.set(con);
	        }
	        return con;  
	    }  
	      
	    /**
	     * 开启事务  
	     * @throws SQLException
	     */
	    public static void beginTransaction() throws SQLException {  
	        Connection con = conn.get();  
	        //判断con是否为空，如果不为空，则说明事务已经开启,开启事务就是得到连接对象
	        if(con != null){  
	            throw new SQLException("事务已经开启了,不能重复开启事务");  
	        }  
	        //如果不为空，则开启事务  
	        con = getConnection();  
	        //设置事务提交为手动  
	        con.setAutoCommit(false);  
	        //把当前开启的事务放入ThreadLocal中  
	        conn.set(con);  
	    }  
	  
	    /**
	     * 提交事务  
	     * @throws SQLException
	     */
	    public static void commitTransaction() throws SQLException {  
	        //得到ThreadLocal中的connection  
	        Connection con = conn.get();  
	        //判断con是否为空，如果为空，则说明没有开启事务  
	        if(con == null){  
	            throw new SQLException("没有开启事务,不能提交事务");  
	        }  
	        //如果con不为空,提交事务  
	        con.commit();  
	        //事务提交后，关闭连接  
	        con.close();  
	        //将连接移出ThreadLocal  
	        conn.remove();
	    }

	    /**
	     * 回滚事务  
	     * @throws SQLException
	     */
	   public static void rollbackTransaction() {  
	        try {
				//得到ThreadLocal中的connection  
				Connection con = conn.get();  
				//判断con是否为空，如果为空，则说明没有开启事务，也就不能回滚事务  
				if(con == null){  
				    throw new SQLException("没有开启事务,不能回滚事务");  
				}  
				//事务回滚  
				con.rollback();  
				//事务回滚后，关闭连接  
				con.close();  
				//将连接移出ThreadLocal  
				conn.remove();
			} catch (SQLException e) {
				System.out.println("ERROR_002:事务回滚错误");
			}
	    }

	    /**
	     * 关闭事务  
	     * @param connection
	     * @throws SQLException
	     */
	   public static void closeConnection(Connection connection) throws SQLException {  
	        //得到ThreadLocal中的connection  
	        Connection con = conn.get();  
	        //如果参数连接与当前事务连接不相等，则说明参数连接不是事务连接，可以关闭，否则交由事务关闭  
	        if(connection != null && con != connection){ 
	            //如果连接没有被关闭，关闭 
	            if(!connection.isClosed()){  
	                connection.close();  
	            }  
	        }
	   }
	   
	   /**
	    * 关闭数据库连接池
	    */
	   public static void closeDataSource()
	   {
		   if(null!=ds)
			   ds.close();
	   }
	   
	   
	   //---------------------重写QueryRuner中的方法------------------------
	   	
	    //封装了connection,ResultSethandler--------?
	    public static  int[] batch(String sql, Object[][] params) throws SQLException 
	    {  
	        Connection conn = getConnection();  
	        int[] result = run.batch(conn, sql, params);  
	        closeConnection(conn);  
	        return result;  
	    }  
	  
	    public static <T> T query(String sql, ResultSetHandler<T> rsh,  Object... params) throws SQLException {  
	        Connection conn = getConnection();  
	        T result =  run.query(conn, sql, rsh, params);  
	        closeConnection(conn);  
	        return result;  
	    }  
	    
	    public static <T> T query(String sql, ResultSetHandler<T> rsh)  throws SQLException {  
	        Connection conn = getConnection();  
	        T result =  run.query(conn, sql, rsh);  
	        closeConnection(conn);  
	        return result;  
	    }  
	  
	 
	    public static int update(String sql) throws SQLException {  
	        Connection conn = getConnection();  
	        int result =  run.update(conn, sql);  
	        closeConnection(conn);  
	        return result;  
	    }
	    
	    public static int update(String sql, Object... params)  throws SQLException {  
	        Connection conn = getConnection();  
	        int result = run.update(conn, sql, params);  
	        closeConnection(conn);  
	        return result;  
	    }
	    
}
