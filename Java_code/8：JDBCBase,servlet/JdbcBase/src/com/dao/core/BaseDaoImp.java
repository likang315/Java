package com.dao.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import com.annotation.Colunm;
import com.annotation.Exclude;
import com.annotation.Table;

/**
 * 封装了SQL语句，直接传与表对应的bean对象
 * @author likang
 *
 * @param <T>
 */
public class BaseDaoImp<T> implements BaseDao<T> {

	/**
	 * 添加表数据，传与表对应的Bean对象
	 */
	@Override
	public int insert(T t) throws SQLException 
	{
		  //解析表名
		  String tname=getTableName(t);
	      // insert into tname(fiel1,fiel,...) values(?,?,?)
		  
		  //生成sql
		  TreeMap<String,Object>  map=parseAllField(t);
		  StringBuilder flist=new StringBuilder();
		  StringBuilder qlist=new StringBuilder();
		  List<Object> values=new ArrayList<Object>();
		  //把所有属性按SQL语句需要标准取出
		  if(null!=map && null!=map.keySet() && map.keySet().size()>0)
		  {
			  Iterator<String> it=map.keySet().iterator();
			  while(it.hasNext())
			  {
				  String key=it.next();
				  flist.append(key+",");
				  qlist.append("?,");
				  values.add(map.get(key));
			  }
		  }
		  //把最后一个多余的 ',' 删除
		  if(flist.length()>0)
			  flist.delete(flist.length()-1, flist.length());
		  if(qlist.length()>0)
			  qlist.delete(qlist.length()-1, qlist.length());
		  String sql="insert into "+tname+"("+flist.toString()+") values("+qlist.toString()+")";

		  
		//执行预编译的sql,传t的参数
		ConnectionManager cm=ConnectionManager.newInstance();
		Connection con=cm.getConnection();
		PreparedStatement ps=con.prepareStatement(sql);
		for(int i=0;i<values.size();i++)
		{
			ps.setObject(i+1, values.get(i));
		}
		int re=ps.executeUpdate();
		
		//得到当前自增字段值，例id
		Statement stat=con.createStatement();
		ResultSet rs=stat.executeQuery("select LAST_INSERT_ID() from dual");
		int lastid=0;
	    if(rs.next())
	    {
		   lastid=rs.getInt(1);
	    }
		return lastid;
	}
	
	/**
	 * 通过反射，解析类上所有成员，将其成员名和值加入map
	 * @param t
	 * @return
	 */
	public TreeMap<String,Object> parseAllField(T t)
	{
		 TreeMap<String,Object> map=new TreeMap<String,Object>();
		 try {
			Class clazz=t.getClass();
			 Field[] all=clazz.getDeclaredFields();
			 if(null!=all)
			 {
				 for(Field f:all)
				 {
					 String fname=f.getName();
					 //要排除的字段
					 if("id".equals(fname))
						 continue;
					 
					 Annotation ano=f.getAnnotation(Exclude.class);
					 if( null!=ano && ano instanceof Exclude)
						 continue;
					 
					 Annotation clu=f.getAnnotation(Colunm.class);
					 f.setAccessible(true);
					 if(null!=clu&&clu instanceof Colunm)
					 {
						 map.put(((Colunm)clu).value(),f.get(t));
					 }else
					 {
						 map.put(fname, f.get(t));
					 }
					 
				 }
			 }
		} catch (Exception e) {
			e.printStackTrace();
		} 
		 return map;
	}
	
	/**
	 * 通过反射解析表名
	 * @param t
	 * @return
	 */
	public String getTableName(T t)
	{
		String re=null;
		Class clazz=t.getClass();
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
	 * 修改表中的数据
	 */
	@Override
	public void update(T t) throws SQLException 
	{
	  try {
			  String tname=getTableName(t);
			  TreeMap<String,Object>  map=parseAllField(t);
			  //update tname set key1=?,key2=? where id=?;
			  
			  StringBuilder flist=new StringBuilder();
			  List<Object> values=new ArrayList<Object>();
			  
			  if(null!=map && null!=map.keySet() && map.keySet().size()>0)
			  {
				  Iterator<String> it=map.keySet().iterator();
				  while(it.hasNext())
				  {
					  String key=it.next();
					  flist.append(key+"=?,");
					  values.add(map.get(key));
				  }
			  }
			  if(flist.length()>0)
				  flist.delete(flist.length()-1, flist.length());

			  String sql="update "+tname+" set "+flist.toString()+" where id=?";

				//执行sql 传t的参数
				ConnectionManager cm=ConnectionManager.newInstance();
				Connection con=cm.getConnection();
				PreparedStatement ps=con.prepareStatement(sql);
				for(int i=0;i<values.size();i++)
				{
					ps.setObject(i+1, values.get(i));
				}
				Field fid=t.getClass().getDeclaredField("id");
				fid.setAccessible(true);
				ps.setObject(values.size()+1,fid.get(t) );
				
				int re=ps.executeUpdate();
				System.out.println(re);
				
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
	}
	
	/**
	 * 删除表的数据
	 */
	@Override
	public void delete(int id, Class<T> clazz) throws SQLException {
		try {
			String tablename=getTableName(((T)clazz.newInstance()));
		
		    String sql="delete from "+tablename+" where id=?";
		    
		    ConnectionManager cm=ConnectionManager.newInstance();
			Connection con=cm.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, id);
			int re=ps.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	
	/**
	 * 根据表中所查的一条记录，和Bean对象绑定，返回此对象
	 */
	@Override
	public T get(int id, Class<T> clazz) throws SQLException {
	
		Object obj=null;
		try {
			obj=clazz.newInstance();
			String tablename=getTableName(((T)clazz.newInstance()));
			String sql="select * from "+tablename+" where id=?";
		
			ConnectionManager cm=ConnectionManager.newInstance();
			Connection con=cm.getConnection();
			PreparedStatement ps=con.prepareStatement(sql);
			ps.setInt(1, id);
			ResultSet rs=ps.executeQuery();
			if(rs.next())
			{
				Field[] all=clazz.getDeclaredFields();
				for(Field field:all)
				{
					String colname=getFieldByProperties(field);
					if(null!=colname)
					{
						 Object colvalue=rs.getObject(colname);
						 field.setAccessible(true);
						 field.set(obj, colvalue);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T)obj;
	}

	
	/**
	 * 根据类属性的名字找属性对应的表字段
	 * @param f
	 * @return
	 */
	public String getFieldByProperties(Field f)
	{
		String tablecol=null;
		 try {
			 	//类的属性名
				String fname=f.getName();
				 //要排除的字段
				 Annotation exc=f.getAnnotation(Exclude.class);
				 if(null==exc && exc instanceof Exclude )
				 {
					 Annotation clu=f.getAnnotation(Colunm.class);
					 if(null!=clu && clu instanceof Colunm)
					 {
						 Colunm col=(Colunm) clu;
						 tablecol=col.value();
					 }else
					 {
						 tablecol=fname;
					 }
				 }
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		 return tablecol;
	}
	
	/**
	 * 得到多记录，绑定的对象
	 */
	@Override
	public List<T> getAll(Class<T> clazz) throws SQLException {
		List<T> list=new ArrayList<T>();
		try {
				String tablename=getTableName(((T)clazz.newInstance()));
				String sql="select * from "+tablename+" order by id desc";
			
				ConnectionManager cm=ConnectionManager.newInstance();
				Connection con=cm.getConnection();
				PreparedStatement ps=con.prepareStatement(sql);
				ResultSet rs=ps.executeQuery();
				while(rs.next())
				{
					Object	obj=clazz.newInstance();
					Field[] all=clazz.getDeclaredFields();
					for(Field field:all)
					{
						String colname=getFieldByProperties(field);
						if(null!=colname)
						{
						 Object colvalue=rs.getObject(colname);
						 field.setAccessible(true);
						 field.set(obj, colvalue);
						}
					}
					list.add((T)obj);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		return list;
	}

	/**
	 * PageDiv封装了查询当前页的所有数据
	 * @Override
	 */
	public PageDiv<T> getByPage(Class<T> clazz, int pageNo, int pageSize) throws SQLException
	{
		PageDiv<T> pd=null;
		
		//存储当前页面的数据
        List<T> list = new ArrayList<T>();
		try {
				String tablename=getTableName(((T)clazz.newInstance()));
				String sql="select * from "+tablename+" order by id desc limit ?,?";
				ConnectionManager cm=ConnectionManager.newInstance();
				Connection con=cm.getConnection();
				PreparedStatement ps=con.prepareStatement(sql);
				ps.setInt(1, (pageNo-1)*pageSize);
				ps.setInt(2, pageSize);
				ResultSet rs=ps.executeQuery();
				
				while(rs.next())
				{
					Object	obj=clazz.newInstance();
					Field all[]=clazz.getDeclaredFields();
					for(Field field:all)
					{
						String colname=getFieldByProperties(field);
						if(null!=colname)
						{
							 Object colvalue=rs.getObject(colname);
							 field.setAccessible(true);
							 field.set(obj, colvalue);
						}
					}
					list.add((T)obj);
				}
			
				String sqltotal="select count(id) from "+tablename;
				PreparedStatement totalps=con.prepareStatement(sqltotal);
				ResultSet totalrs=totalps.executeQuery();
				if(totalrs.next())
				{
					int total=totalrs.getInt(1);
					pd=new PageDiv<T>(pageSize, pageNo, total, list);
				} 
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pd;
	}

}
