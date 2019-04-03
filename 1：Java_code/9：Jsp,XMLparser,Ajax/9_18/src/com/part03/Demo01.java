package com.part03;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Demo01 {

	static class Stu{
		private int age;
		private String name;
		public Stu(String name,int age)
		{
			this.name=name;
			this.age=age;
		}
		public int getAge() {
			return age;
		}
		public void setAge(int age) {
			this.age = age;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		@Override
		public int hashCode() {
			// TODO Auto-generated method stub
			return this.name.hashCode()+age;
		}
	/*	@Override
		public boolean equals(Object obj) {
		    return this==obj;
			// TODO Auto-generated method stub
			return this.hashCode()==obj.hashCode();
		}*/
		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "["+this.name+"::"+this.age+"]";
		}
		
		
	}
	public static void main(String[] args) 
	{
		Set set=new HashSet();
		set.add(new Stu("lisi",23));
		set.add(new Stu("zs",27));
		set.add(new Stu("ww",28));
		set.add(new Stu("xs",24));
		set.add(new Stu("lisi",23));
		
		//”–Œ Ã‚
		Iterator it=set.iterator();
		Iterator it1=set.iterator();
		
		while(it.hasNext())
		{
			System.out.println(it.next().toString());
			if(it.hasNext())
				System.out.println(it.hashCode());
		}
		
		
		while(it1.hasNext())
		{
			System.out.println(it.next().hashCode());
		}

		
	}

}
