package com.part7.dbutils_source;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.alibaba.druid.pool.DruidDataSource;

/**
 * 
 * @author Administrator
 *
 */
public class JdbcUtils<T> {
	private static DruidDataSource  ds=new DruidDataSource();
	static{
		  //从配置文件中导入
		   try {
				ResourceBundle res=ResourceBundle.getBundle("druid");
				ds.setUrl(res.getString("url"));
				ds.setDriverClassName(res.getString("driverClassName"));
				ds.setUsername(res.getString("username"));
				ds.setPassword(res.getString("password"));
				ds.setFilters(res.getString("filters"));
				ds.setMaxActive(Integer.parseInt(res.getString("maxActive")));
				ds.setInitialSize(Integer.parseInt(res.getString("initialSize")));
				ds.setMaxWait(Long.parseLong(res.getString("maxWait")));
				ds.setMinIdle(Integer.parseInt(res.getString("minIdle")));
				//ds.setMaxIdle(Integer.parseInt(res.getString("maxIdle")));

				//ds.setTimeBetweenEvictionRunsMillis(Long.parseLong(res.getString("timeBetweenEvictionRunsMillis")));
				//ds.setMinEvictableIdleTimeMillis(Long.parseLong(res.getString("minEvictableIdleTimeMillis")));
				//ds.setValidationQuery(res.getString("validationQuery"));
				
				
			} catch (Exception e) {
				
			} 
	   }

	/**
	 *  增删改 表或者数据，应用可变长参数
	 * @return
	 */
	public static  int update(String sql,Object ... params)
	{
		Connection con=null;
		int re=-1;
		try {
			con=ds.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			if(null!=params&&params.length>0)
			{
				for(int i=0;i<params.length;i++)
				{
					ps.setObject(i+1, params[i]);
				}
			}
			re=ps.executeUpdate();
		} catch (SQLException e) {
			System.out.println("Error_01:update  失败");
		}finally
		{
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Error_02:update 连接断开失败");
			}
		}
		return re;
	}
	
	
	/**
	 * 查询
	 * @return
	 */
	public   T  query(String sql,ResultSetHandler<T> hand,Object...params) {
		T t=null;
		Connection con=null;
		try {
			con=ds.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			if(null!=params&&params.length>0)
			{
				for(int i=0;i<params.length;i++)
				{
					ps.setObject(i+1, params[i]);
				}
			}
			
			ResultSet rs=ps.executeQuery();
			
			t=hand.handle(rs);
		} catch (SQLException e) {
			System.out.println("Error_03:query 失败");
		}finally
		{
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Error_04:query 连接断开失败");
			}
		}
		return t;
		}
	
	
	public static void main(String[] args) {
		
		/*JdbcUtils.update("create table stu(id int ,name varchar(20))", null);
		JdbcUtils.update("update stu set name=? where id=?","hello",1);
		System.out.println("Ok");
		*/
		
		
		
		/*//   1:ArrayListHandler
		List<Object[]> list=new JdbcUtils<List<Object[]>>().query("select * from emp where id>?", new ArrayListHandler(), 3);
		for(Object[] tem:list)
		{
			for(Object t:tem)
			{
				System.out.print(t+"\t");
			}
			System.out.println();
		}
		*/
		
		
		/*//   2:BeanListHandler
		JdbcUtils<List<Person>> ju=new JdbcUtils<List<Person>>();
		@SuppressWarnings("unchecked")
		List<Person> list= ju.query("select * from person where id>?",new BeanListHandler<Person>(Person.class), 3);
		for(Person p:list)
		{
			System.out.println(p.getId()+"\t"+p.getName()+"\t"+p.getSex()+"\t"+p.getAge());
		}
		*/
		
		
		/*    //2:BeanListHandler
		JdbcUtils<Object> ju=new JdbcUtils<Object>();
		List<Emp> list=(List<Emp>)ju.query("select * from emp", new BeanListHandler<Emp>(Emp.class), null);
		for(Emp e:list)
		{
			System.out.println(e.getId()+"\t"+e.getName()+"\t"+e.getSex());
		}
		*/
		
		
		
		/*	//3:PersonHandler
		JdbcUtils<Object> ju=new JdbcUtils<Object>();
		List<Person> list=ju.query("select * from person", new PersonHandler(), null);
		for(Person p:list)
		{
			System.out.println(p.getId()+"\t"+p.getName()+"\t"+p.getSex()+"\t"+p.getAge());
		}
		*/
		
		
	}
}
