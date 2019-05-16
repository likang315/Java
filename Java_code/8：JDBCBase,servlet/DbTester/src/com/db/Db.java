package com.db;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.log4j.Logger;

import com.alibaba.druid.pool.DruidDataSource;
import com.db.annotation.Colunm;
import com.db.annotation.Exclude;
import com.db.annotation.Table;

//操作数据库的工具类
public class Db //extends QueryRunner 
{
	   private static Logger log=Logger.getLogger(Db.class);
	   private static QueryRunner run=new QueryRunner();
	   private static DruidDataSource ds=null;
	   //只放进行事务的Connection
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
				ds.setValidationQuery("SELECT 'x'");
				ds.setTestWhileIdle(true);
				ds.setTestOnBorrow(false);
				ds.setTestOnReturn(false);
				ds.setTimeBetweenEvictionRunsMillis(600000);
				//ds.setMaxIdle(Integer.parseInt(res.getString("maxIdle")));

				//ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(res.getString("timeBetweenEvictionRunsMillis")));
				//ds.setMinEvictableIdleTimeMillis(Long.parseLong(res.getString("minEvictableIdleTimeMillis")));
				//ds.setValidationQuery(res.getString("validationQuery"));
				
				
			} catch (Exception e) {
				log.error("ERROR_001_com.hq.db.Db_初始化连接池失败_line63"+e.getMessage());
			} 
	   }
	 
	    /**
	     * 通过DataSource得到Connection  
	     * @return
	     * @throws SQLException
	     */
	    public static Connection getConnection() throws SQLException{  
	        //得到ThreadLocal中的connection  
	        Connection con = conn.get();  
	        //如果开启了事务，则con不为空，应该直接返回con  
	       /* if(con != null){  
	            return con;  
	        } */ 
	        if(null==con||con.isClosed())
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
	        //得到ThreadLocal中的connection  
	     /*   Connection con = conn.get();  
	        //判断con是否为空，如果不为空，则说明事务已经开启  
	        if(con != null){  
	            throw new SQLException("事务已经开启了,不能重复开启事务");  
	        } */ 
	        //如果不为空，则开启事务  
	    	 Connection con = getConnection();  
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
			    log.error("ERROR_002_com.hq.db_回滚事务失败_line145..."+e);
			}  
	    }  
	  
	    /**
	     * 关闭事务  
	     * @param connection
	     * @throws SQLException
	     */
	    public static void releaseConnection(Connection connection) throws SQLException {  
	        //得到ThreadLocal中的connection  
	        Connection con = conn.get();  
	        //如果参数连接与当前事务连接不相等，则说明参数连接不是事务连接，可以关闭，否则交由事务关闭  
	        if(connection != null && con != connection){  
	            //如果连接没有被关闭，关闭之  
	            if(!connection.isClosed()){  
	                connection.close();  
	            }  
	        }  
	    } 
		
	   public static void closeDataSource()
	   {
		   if(null!=ds)ds.close();
	   }
	   //---------------------重写QueryRuner中的方法------------------------
	     
	    public static  int[] batch(String sql, Object[][] params)  throws SQLException 
	    {  
	        Connection conn = getConnection();  
	        int[] result = run.batch(conn, sql, params);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	  
	 
	    public static <T> T query(String sql, ResultSetHandler<T> rsh,  
	            Object... params) throws SQLException {  
	        Connection conn = getConnection();  
	        T result =  run.query(conn, sql, rsh, params);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	  
	    
	    public static <T> T query(String sql, ResultSetHandler<T> rsh)  
	            throws SQLException {  
	        Connection conn = getConnection();  
	        T result =  run.query(conn, sql, rsh);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	  
	 
	    public static int update(String sql, Object... params)  
	            throws SQLException {  
	        Connection conn = getConnection();  
	        int result = run.update(conn, sql, params);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	
	    public static  int update(String sql, Object param)  
	            throws SQLException {  
	        Connection conn = getConnection();  
	        int result =  run.update(conn, sql, param);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	  
	     
	    public static int update(String sql) throws SQLException {  
	        Connection conn = getConnection();  
	        int result =  run.update(conn, sql);  
	        releaseConnection(conn);  
	        return result;  
	    }  
	    /**
	     * 增删改查方法************************************************************************************
	     */
	    
	    /**
	     * 增加一个对像
	     * @param t  直接传一下对象
	     * @return 最后加入对象的id,如果是-1就是没有成功
	     * @throws SQLException
	     */
	    public static<T> long add(T t) throws SQLException 
		{
			//解析表名，
			  String tname=getTableName(t.getClass());
		    // insert into tname(fiel1,fiel,...) values(?,?,?)
			//生成sql
			
			  TreeMap<String,Object>  map=parseAllField(t);
			  StringBuilder flist=new StringBuilder();
			  StringBuilder qlist=new StringBuilder();
			  List<Object> values=new ArrayList<Object>();
			  if(null!=map&&null!=map.keySet()&&map.keySet().size()>0)
			  {
				  Iterator<String> it=map.keySet().iterator();
				  while(it.hasNext())
				  {
					  String key=it.next();
					  if(null==map.get(key))continue;
					  
					  flist.append(key+",");
					  qlist.append("?,");
					  values.add(map.get(key));
				  }
			  }
			  if(flist.length()>0)flist.delete(flist.length()-1, flist.length());
			  if(qlist.length()>0)qlist.delete(qlist.length()-1, qlist.length());
			  
			//  parseFildAndQuery(flist,values,map);
			  String sql="insert into "+tname+"("+flist.toString()+") values("+qlist.toString()+")";

			  
			//执行sql 传t的参数
			update(sql, values.toArray());
			
			Object lastid=query("select LAST_INSERT_ID() from dual", new ArrayHandler())[0];
			long relastid=-1;
			if(null!=lastid&&lastid instanceof Long)
			{
				relastid=((Long)lastid).longValue();
			}else if(null!=lastid&&lastid instanceof BigInteger)
			{
				relastid=((BigInteger)lastid).longValue();
			}
			return relastid;
		}
	    /**
	     * 修改对象
	     * @param t
	     * @throws SQLException
	     */
		public static<T> void update(T t) throws SQLException 
		{
			
			
					try {
						String tname=getTableName(t.getClass());
						  TreeMap<String,Object>  map=parseAllField(t);
						  StringBuilder flist=new StringBuilder();
						  StringBuilder qlist=new StringBuilder();
						  List<Object> values=new ArrayList<Object>();
						  //将Map中的值分解为值列表与?列表
						  parseFildAndQuery(flist,values,map);
						
						 
						  String sql="update "+tname+" set "+flist.toString()+" where id=?";

						log.debug(sql);
						//执行sql 传t的参数
						
/*	Connection con=Db.getConnection();
						PreparedStatement ps=con.prepareStatement(sql);
						for(int i=0;i<values.size();i++)
						{
							ps.setObject(i+1, values.get(i));
						}
						
						ps.setObject(values.size()+1,fid.get(t) );
						int re=ps.executeUpdate();*/
						  
						  //追加id最后
						  Field fid=t.getClass().getDeclaredField("id");
						  fid.setAccessible(true);
						  values.add(fid.get(t));
						  update(sql, values.toArray());
					} catch (NoSuchFieldException|SecurityException|IllegalAccessException e) {
					    log.error("ERROR_003_com.hq.db.Db_line315_自动增加方法中出错 "+e);
					} 
			
				
			
			
		}
	    /**
	     * 删除对象
	     * @param id
	     * @param clazz
	     * @throws SQLException
	     */
		public static<T> void delete(long id, Class<T> clazz) throws SQLException {
			
				String tablename=getTableName(clazz);
			    String sql="delete from "+tablename+" where id=?";
			   update(sql,id);

		}
	    /**
	     * 查询一个对象
	     * @param id
	     * @param clazz
	     * @return
	     */
	    public static<T> T get(long id,Class<T> clazz)throws SQLException
	    {
	    	T t=null;
	
			String tablename=getTableName(clazz);
			String sql="select * from "+tablename+" where id=?";

			t=query(sql,new BeanHandler<T>(clazz),id);
	    	return t;
	    }
	    
	    /**
	     * 查询表中所有数据
	     */
	    public static<T> List<T> getAll(Class<T> clazz) throws SQLException 
	    {
		     	List<T> list=new ArrayList<T>();
				String tablename=getTableName(clazz);
				String sql="select * from "+tablename+" order by id desc";
				list=query(sql, new BeanListHandler<T>(clazz));
			    return list;
		}
	    public static<T> List<T> getAll(Class<T> clazz,String sql) throws SQLException 
        {
	     	List<T> list=new ArrayList<T>();
			String tablename=getTableName(clazz);
			//String sql="select * from "+tablename+" order by id desc";
			list=query(sql, new BeanListHandler<T>(clazz));
		    return list;
	   }
        public static<T> List<T> getAll(Class<T> clazz,String sql,Object ... params) throws SQLException 
        {
	     	List<T> list=new ArrayList<T>();
			String tablename=getTableName(clazz);
			//String sql="select * from "+tablename+" order by id desc";
			list=query(sql, new BeanListHandler<T>(clazz),params);
		    return list;
	   }
        /**
         * 对全表分页查询
         * @param pageNo
         * @param pageSize
         * @return
         * @throws SQLException
         */
    	public static<T> PageDiv<T> getByPage(Class<T> clazz,int pageNo, int pageSize) throws SQLException {
    		PageDiv<T> pd=null;
    		
    		//当前页面的数据
               List<T> list=new ArrayList<T>();
    		
    			String tablename=getTableName(clazz);
    			String sql="select * from "+tablename+" order by id desc limit ?,?";
    			log.debug(sql);
    			 list=query(sql, new BeanListHandler<T>(clazz),(pageNo-1)*pageSize,pageSize);
    		
    		
    			String sqltotal="select count(id) from "+tablename;
    			
    			Object re=query(sqltotal, new ArrayHandler())[0];
    			long total=0;
    			if(null!=re&&re instanceof Long)
    			{
    				total=(Long)re;
    			}
    			pd=new PageDiv<T>( pageNo,pageSize, total, list);
    			
    	

    		return pd;
    	}
	    
    	
    	/**
    	 * 根把sql语句分页
    	 * @param sql   select .... from .... 不用limit
    	 * @param pageNo
    	 * @param pageSize
    	 * @return
    	 * @throws SQLException
    	 */
    	public static<T> PageDiv<T> getByPage(Class<T> clazz,String sql, int pageNo, int pageSize,Object ... param) throws SQLException {
    		PageDiv<T> pd=null;
    		
    		//当前页面的数据
           List<T> list=new ArrayList<T>();
    
    			
    		
    			//String sql="select * from "+tablename+" order by id desc limit ?,?";
                
                Object[]params=new Object[param.length+2];
                System.arraycopy(param, 0, params, 0, param.length);
                params[param.length]=(pageNo-1)*pageSize;
                params[param.length+1]=pageSize;
                
    			list=query(sql+" limit ?,?", new BeanListHandler<T>(clazz),params);
    			//select aa,bbc,ddd from aaa where adfsdf sdfds fsdfsdfds 
    			int fromstart=sql.toLowerCase().indexOf("from");
    			String totalsql="select count(id) "+sql.substring(fromstart);
    			Object re=query(totalsql, new ArrayHandler(),param)[0];
    			long total=0;
    			if(null!=re&&re instanceof Long)
    			{
    				total=(Long)re;
    			}
    			pd=new PageDiv<T>( pageNo,pageSize, total, list);
    			
    		

    		return pd;
    	}
	    /*********************通用方法封装********************************/

	    /**
		 * 解析表名
		 * @param t
		 * @return
		 */
		public static<T> String getTableName(Class<T> clazz)
		{
			String re=null;
			Annotation ano=clazz.getDeclaredAnnotation(Table.class);
			if(null!=ano&&ano instanceof Table)
			{
				Table table=(Table)ano;
				re=table.value();
			}else
			{
				//表名和类名同名，第一个字母小写
				String allname=clazz.getName();
				int lastdot=allname.lastIndexOf(".");
				re=allname.substring(lastdot+1,lastdot+2).toLowerCase()+allname.substring(lastdot+2);
			}
			return re;
		}
	
		/**
		 * 解析类上所有成员，将其成员名和值加入map
		 * @param t
		 * @return
		 */
		public static<T> TreeMap<String,Object> parseAllField(T t)
		{
			 TreeMap<String,Object> map=new TreeMap<String,Object>();
			 try {
				
				 Field []all=t.getClass().getDeclaredFields();
				 if(null!=all&&all.length>0)
				 {
					 for(Field f:all)
					 {
						 String fname=f.getName();
						 //要排除的字段
						 if("id".equals(fname))continue;
						 Annotation ano=f.getAnnotation(Exclude.class);
						 if(null!=ano&&ano instanceof Exclude)continue;
						 //列名解析
						 Annotation clu=f.getAnnotation(Colunm.class);
						 f.setAccessible(true);
						 
						 if(null==f.get(t))continue;//如果字段中值为空的话不参与数据库操作
						 
						 if(null!=clu&&clu instanceof Colunm)
						 {
							 map.put(((Colunm)clu).value(), f.get(t));
						 }else
						 {
							 map.put(fname, f.get(t));
						 }
					 }
				 }
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			 return map;
		}
	    /**
	     * 把map中的数据解析到flist,qlist values中 
	     * @param flist
	     * @param qlist
	     * @param values
	     * @param map
	     */
		public static void parseFildAndQuery(StringBuilder flist,List<Object> values,TreeMap<String,Object>  map)
		{
			  if(null!=map&&null!=map.keySet()&&map.keySet().size()>0)
			  {
				  Iterator<String> it=map.keySet().iterator();
				  while(it.hasNext())
				  {
					  String key=it.next();
					  flist.append(key+"=?,");
					  values.add(map.get(key));
				  }
			  }
			  if(flist.length()>0)flist.delete(flist.length()-1, flist.length());
			
		}
}
