### 包装器类型（wrapper）：

​	为了能将基本类型视为对象来处理,调用相关的方法，都有一个常量池，直接赋其基本数据类型时，查看范围在-128~127，都是final类

```java
Java中八种基本数据类型对应有八种包装器类型

byte                        		  Byte
short            					 Short
int 								Integer(特殊)
long 								Long
float								Float
double								Double
char								Character（特殊）
boolean								Boolean
```

**public final class Integer extends Number implements Comparable<Integer>** 

Integer：当我们给一个Integer对象赋一个int 值的时候，会调用 Integer 类的静态方法 valueOf，valueof()原码分析得
	 如果整型字面量的值在-128 到 127 之间，那么不会 new 新的 Integer 对象，而是直接引用常量池中的 Integer 对象，如超过 则new一个新的对象

```java
	static字段：(属性)
	static int MAX_VALUE 
	          值为 231－1 的常量，它表示 int 类型能够表示的最大值
	static int MIN_VALUE 
	          值为 －231 的常量，它表示 int 类型能够表示的最小值 
	static int SIZE 
	          用来表示 int 值的比特位数
	static Class<Integer> TYPE 
	          表示基本类型 int 的 Class 实例
```


​		
```java
	构造方法
	Integer(int value) 
	          构造一个新分配的 Integer 对象，它表示指定的 int 值
	Integer(String s) 
	          构造一个新分配的 Integer 对象，它表示 String 参数所指示的 int 值

	Integer i1 = new Integer(10);
	Integer i2 = new Integer("10");
	Integer i3 = 10;
	Integer i4 = 10;
	int i=10;

	System.out.println(i1==i2);//false  	比较地址
	System.out.println(i1==i3);//false   	比较地址
	System.out.println(i3==i4);//true	常量池
	System.out.println(i==i4);//true
```



### 自动拆装箱机制（Auto boxing）：

### 默认的进行基本数据与包装器类型之间的一个相互转化过程的一种自动机制

```java
​	Integer i1 = new Integer(23);
​	int i2 = 10;
​	i1 = i2;//装箱----类型提升过程,发生了类型变化
​	i2 = i1;//拆箱---类型降低过程，类型肯定发生变化
```




```java
 方法：
 int compare(int x, int y) ---------X>Y 返回1，等于返回0，小于返回 -1
 int compareTo(Integer anotherInteger) ----比较两个Integer对象的大小，调用compare方法

 static String toBinaryString(int i) ---------以二进制无符号整数形式返回一个整数参数的字符串表示形式
 static String	toString(int i)
 String	toString()

 int intValue() 以 int 类型返回该 Integer 的值,拆箱
 static Integer valueOf(int i) 返回一个表示指定的 int 值的 Integer 实例，装箱

 static int parseInt(String s)  -----将满足要求的字符串对象转换成其所对应的基本数据类型
 
 static int reverse(int i)  进行数值反转,以补码输出
```


​	 

## Double 

字段：
	
static double MAX_VALUE 
          保存 double 类型的最大正有限值的常量，最大正有限值为 (2-2（-52）指数)·21023（指数）

static double MIN_VALUE 
          保存 double 类型的最小正非零值的常量，最小正非零值为 2(-1074)指数

static int SIZE 
          用于表示 double 值的位数

static Class<Double> TYPE 
          表示基本类型 double 的 Class 实例


构造方法：

Double(double value) 
          构造一个新分配的 Double 对象，它表示基本的 double 参数。 
Double(String s) 
          构造一个新分配的 Double 对象，表示用字符串表示的 double 类型的浮点值。 

方法：
int compareTo(Double anotherDouble) 
          对两个 Double 对象所表示的数值进行比较

double doubleValue() 
          返回此 Double 对象的 double 值 

int intValue() 
          以 int 形式返回此 Double 的值（通过强制转换为 int 类型）。 

static String toString(double d) 
          返回 double 参数的字符串表示形式 

static Double valueOf(double d) 
          返回表示指定的 double 值的 Double 实例 

static double parseDouble(String s) 
          返回一个新的 double 值，该值被初始化为用指定 String 表示的值 



## Float

字段：
static float MAX_VALUE 
          保存 float 类型的最大正有限值的常量，即 (2-2-23)·2127。 

static float MIN_VALUE 
          保存 float 类型数据的最小正非零值的常量，即 2-149。 

static int SIZE 
          表示一个 float 值所使用的位数。 

static Class<Float> TYPE 
          表示 float 基本类型的 Class 实例。 

构造方法:

Float(double value) 
          构造一个新分配的 Float 对象，它表示转换为 float 类型的参数。 
Float(float value) 
          构造一个新分配的 Float 对象，它表示基本的 float 参数。 
Float(String s) 
          构造一个新分配的 Float 对象，它表示用字符串表示的 float 类型的浮点值。 

方法：

 int compareTo(Float anotherFloat) 
          比较两个 Float 对象所表示的数值 

 int intValue() 
          将此 Float 值以 int 形式返回（强制转换为 int 类型）。 

 float floatValue() 
          返回此 Float 对象的 float 值 

 double doubleValue() 
          返回此 Float 对象的 double 值 

 static float parseFloat(String s) 
          返回一个新的 float 值，该值被初始化为用指定 String 表示的值

 String toString() 
          返回此 Float 对象的字符串表示形式 

static Float valueOf(float f) 
          返回表示指定的 float 值的 Float 实例。 





## Character

字段：自己查看

构造方法摘要 
Character(char value)
          构造一个新分配的 Character 对象，用以表示指定的 char 值

方法：
char charValue() 
          返回此 Character 对象的值。 

int compareTo(Character anotherCharacter) 
          根据数字比较两个 Character 对象。 

static boolean isDigit(char ch) 
          确定指定字符是否为数字，是返回ture，否则返回false

static boolean isLetter(char ch) 
          确定指定字符是否为字母。 

static boolean isLetterOrDigit(char ch) 
          确定指定字符是否为字母或数字。 

static boolean isLowerCase(char ch) 
          确定指定字符是否为小写字母。 

static boolean isUpperCase(char ch) 
          确定指定字符是否为大写字母。 

static boolean isWhitespace(char ch) 
          确定指定字符是否为空白字符。  

String toString() 
          返回表示此 Character 值的 String 对象

static Character valueOf(char c) 
          返回一个表示指定 char 值的 Character 实例 

​	 

## Boolean 

字段：
	static Boolean FALSE 
          对应基值 false 的 Boolean 对象。 
	static Boolean TRUE 
          对应基值 true 的 Boolean 对象。 
	static Class<Boolean> TYPE 
	   表示基本类型 boolean 的 Class 对象。 

构造方法：
Boolean(String s) 
          如果 String 参数不为 null 且在忽略大小写时等于 "true"，则分配一个表示 true 值的 Boolean 对象

方法：
boolean booleanValue() 
          将此 Boolean 对象的值作为基本布尔值返回
static Boolean valueOf(String s) 
          返回一个用指定的字符串表示值的 Boolean 值。 
static boolean parseBoolean(String s) 
          将字符串参数转换为对应的 boolean 值 











​	 
​	 
​	 
​	 