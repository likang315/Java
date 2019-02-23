
远程登录数据库服务器

	1：命令行工具：Oracle自带的SOL*PLUS
	2：图形化界面工具：SOL Developer	

基本数据类型
	
	NUMBER：表示数字类型
		经常被定成：NUMBER（P，S）形式，P 表示数字的总位数，S 表示小数点后面的位数 
	CHAR：表示固定长的字符类型，读取快，但空间利用率低
		经常被定成：CHAR（N），N 表示的占用的字节数,CHAR---()不写时，默认1
	VARCHAR: 表示变长的字符类型
		经常被定成：VARCHAR(N)，N 表示的占用的字节数
	LONG：存储变长字符串，最多达2GB的字符串，有许多限制
	CLOB：存储定长或变长字符串，最多可达4GB，经常用CLOB

	数据库中所有数据类型的默认值为NULL，可以用DEFAULT为某个字段设置一个指定的默认值
	数据库中的字符串字面量时使用单引号的，但是字符串的值时区分大小写的
	例：DEFAULT ‘M’

	NOT NULL ：设置字段不允许为NULL，必须给值。

数值函数

	1：ROUND(n,m)：四舍五入，m为保存的位数，如果是负数则是-1，十位，-2百位
	2：TRUNC（n,m）：截取数字
	3：MOD（n,m）:求余数
	4：CEIL（n）,FLOOR(n):向上取整，向下取整


字符串函数
	
	1：CONCAT('字符串1','字符串2')函数，拼接字符串

	2：|| ：代表拼接符

	3：LENGTH（VARCHAR）：返回字符串的长度

	4：UPPER（'字符串'），LOWER（'字符串'），INITCAP（'字符串'），将字符串转换为大写，小写，首字符大写，
	   对于INITCAP，可以使用空格隔开多个单词，那么每个单词首字母都会大写

	5：TRIM（‘指定字符’ FROM ‘字符’ ），LTRIM（‘字符’，‘指定字符’），RTRIM（字符’，‘指定字符’）：去除当前字符串两边的指定重复字符，
														LTRIM仅去除左侧的，RTRIM则仅去除右侧的。
		注意：只要存在指定字符即去除，与顺序无关

	6：LPAD(字段,位数,‘补位符’)，RPAD（字段,位数,‘补位符’）左补，右补位函数，可以用来对齐
		从左往右截取

	7：SUBSTR（‘think’,2,3）----hin 截取字符串,最后一位不写是截取到末尾，-2是从倒数第二位上开始

	8:INSTR（char1,char2,n,m）：查找给定字符在字符串中的位数
		查找char2，在char1中出现
		n:从第几个字符开始检索，m:为第几次出现，n,m不写时默认为1


伪表：dual 当查询的内容不和任何表中的数据有关系时，可以使用伪表，伪表只会查询出一条记录


日期类型：

	1:DATE：用于定义时间的数据，长度为7个字节
	2:TIMESTAMP：时间戳
时间关键字
	1：SYSDATE：本质是Oracle的内部函数，返回当前的系统时间，精确到秒----    显示格式：DD-MON-RR	
	2：SYSTIMESTAMP：返回的是一个表示时间戳类型的值，包含秒

TO_DATE ：函数可以将字符串按指定的日期格式解析为DATE类型
		'YYYY"年"-MM-DD HH24:MI:SS'	

日期的计算：日期可以与一个数字进行加减法，这相当于加减指定的天，减法，差为相差的天

TO_CHAR：可以将DATE按照给定的格式转换为字符串。

	YY不判定世纪，RR判定世纪

	sys	0-49		50-99
	user	
	
	0-49	本世纪		下世纪
	
	50-99	上世纪		本世纪

LAST_DAY（date）:返回给定日期所在的月底日期

ADD_MONTH（date，i）：对给定的日期加上指定的月，若i为负数则是减去。

MONTHS_BETWEEN(date1,date2):计算两个日期之间相差的月，计算时根据date1-date2得到的

NEXT_DAY（date，char）：返回从明天开始一周之内的指定周几日期，char：周几

LEAST（date1，date2）：求最小值
CREATEST（date1，date2）：求最大值，除了日期外，常用的数字也可以比较大小

EXTRACT（YEAR FROM date）：提取指定日期中指定的时间分量值


NULL：空值，数据不存在

1：插入NULL值,显示和隐式插入
2: 更新NULL，
3：判断是否为NULL  is NULL 或 is NOT NULL  
4：非空约束： NOT NULL


NULL值的运算操作：NULL与任何数字运算劫夺还为NULL，NULL与字符串拼接等于什么都没做

空值函数：NVL（arg1，arg2）：当arg1为NULL时，返回arg2的值，若不为NULL返回本身
	此函数的作用是将NULL值替换为一个非NULL值
























