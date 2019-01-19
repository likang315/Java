package com.db;

public class Test {
public static void main(String[] args)throws Exception {
	
	Article art=new Article();
	
	Db.add(art);
	
	Db.update(art);
	
	Db.delete(100, Article.class);
	
	
}
}
