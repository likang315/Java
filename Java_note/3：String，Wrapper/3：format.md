### 1：format(String format.object... args)---使用指定的格式字符串，和参数返回一个格式化的字符串

args:格式说明符

日期格式化:  %tb--月份的简称
	     %tB--月份的全--称
	     %tA--星期几的全称
	     %ta--星期几的简称
	     %tY--年份---2006
	     %td--一个月中的第几天

时间格式化： %tH---24时制的小时
	     %tM---分钟
	     %tS---秒数
	     %tL---毫秒
	     %tP---上午或者下午
	     

时间和日期的格式化组合：
		%tF---年-月-日格式
		%tT---时：分：秒
		%tc---包括全部日期和时间信息

```java
 例：Date date=new Date();
	String year=String.format("%tc", date);
	System.out.println(year);
```

