package com.part01;

public class Demo02 {

	static class Node
	{
		Node pre;
		Object data;
		Node nex;
		
		public Node() {}
		public Node(Node pre,Object data,Node nex)
		{
			this.pre=pre;
			this.data=data;
			this.nex=nex;
		}
		
	}
	
	
	Node n1=null;
	public void add(Object obj)
	{
		 Node node=new Node(n1,obj,null);
	}
	
	public static void main(String[] args)
	{  //类似于C语言中的结构体
       Node n1=new Node(null,"hello",null);
       Node n2=new Node(n1,"welcome",null);
       Node n3=new Node(n2,"hi",n1);
       n1.nex=n3;
       n2.nex=n2;
       n1.pre=n3;
	}

}
