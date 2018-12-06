package com.part1;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
//正则表达式的应用
public class TT {
  public static void main(String[] args) {
	  //将正则表达式设置为模式
	  Pattern pattern=Pattern.compile("((.*?)\\s{1}/(.*?)\\s{1}HTTP/1[.][0,1])");
	  
	  String str="GET /abd/bbc/index.html HTTP/1.1";
	  //创建匹配器
	  Matcher mat=pattern.matcher(str);
	  //判断是否匹配
	  if(mat.find())
	  {
		  System.out.println(mat.group(3));
	  }
  }
}
