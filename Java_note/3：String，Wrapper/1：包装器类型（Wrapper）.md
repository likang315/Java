### 包装器类型（wrapper）：

​	为了能将基本类型视为对象来处理,调用相关的方法

```java
//Java中八种基本数据类型对应有八种包装器类型
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



### Java 中 包装类 缓存 (cache)

###### 原理：

**静态内部类IntegerCache，IntegerCache有一个静态的Integer数组，因为有静态代码块的存在，在类加载时就将-128 到 127 的 Integer 对象创建了**，并填充在**cache数组**中，一旦程序调用valueOf 方法，如果 i 的值是在-128 到 127 之间就直接在cache缓存数组

Integer 包装类在**自动装箱的过程中**，是有缓存数组的，对于值 **在-128~127之间的数**，会放在内存中进行重用；对于大于这个范围的数，使用的时候 都会 new 出一个新的对象

java 使用该机制是**为了达到最小化数据频繁创建的，和输入和输出的目的,这是一种优化措施,提高效率**（可以设置系统属性 java.lang.Integer.IntegerCache.high **修改缓冲区上限,默认为127**

其他包装类缓存：Boolean（全部缓存）、Byte（全部缓存）、Character（<= 127缓存）、Short（-128~127缓存）、Long（-128~127 缓存）、**Float（没有缓存）、Double（没有缓存），因为在指定范围内浮点型数据个数是不确定的，整型等个数是确定的，所以可以 Cache**

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    static final Integer cache[];  //缓存数组,存储Integer对象
	//类加载额时候已经加载到cache[] 中
    static {
        int h = 127;
        high = h;
	    //初始化cache数组
        cache = new Integer[(high - low) + 1];
        int j = low;
        for(int k = 0; k < cache.length; k++)
            cache[k] = new Integer(j++);

        // range [-128, 127] must be interned (JLS7 5.1.7)
        assert IntegerCache.high >= 127;
    }
    private IntegerCache() {}
}
```



### Integer：

当我们给一个Integer对象赋一个int 值的时候，会**调用 Integer 类的静态方法 valueOf(int i)**，valueof()原码分析得

直接赋值时，如果整型字面量的值在-128 到 127 之间，那么不会 new 新的 Integer 对象，而是直接从cache[] 中获取 Integer 对象，如超过则new一个新的对象

static字段：(属性)
	static int MAX_VALUE 
	          值为 231－1 的常量，它表示 int 类型能够表示的最大值
	static int MIN_VALUE 
	          值为 －231 的常量，它表示 int 类型能够表示的最小值 
	static int SIZE 
	          用来表示 int 值的比特位数
	static Class<Integer> TYPE 
	          表示基本类型 int 的 Class 实例
构造方法
	Integer(int value) 
	          构造一个新分配的 Integer 对象，它表示指定的 int 值
	Integer(String s) 
	          构造一个新分配的 Integer 对象，它表示 String 参数所指示的 int 值​	

```java
	Integer i1 = new Integer(10); //new 的直接在堆中
	Integer i2 = new Integer("10");
	Integer i3 = 10;		//cache[] 中
	Integer i4 = 10;
	int i=10;

	System.out.println(i1==i2);//false  	比较地址
	System.out.println(i1==i3);//false   	比较地址
	System.out.println(i3==i4);//true	    cache[] 中
	System.out.println(i==i4);//true	    自动调用valueOf(int i)
```

```java
 方法：
 int compare(int x, int y) ---------X>Y 返回1，等于返回0，小于返回 -1
 int compareTo(Integer anotherInteger) ----比较两个Integer对象的大小，调用compare方法

 static String toBinaryString(int i) ---------以二进制无符号整数形式返回一个整数参数的字符串表示形式
 
 String	toString()

 int intValue() 以 int 类型返回该 Integer 的值,拆箱
 static Integer valueOf(int i) 返回一个表示指定的 int 值的 Integer 实例，装箱

 static int parseInt(String s)  -----将满足要求的字符串对象转换成其所对应的基本数据类型
 static String	toString(int i)
 
 static int reverse(int i)  进行数值反转,以补码输出
```



### 自动拆装箱机制（Auto boxing）：

### 默认的进行基本数据与包装器类型之间的一个相互转化过程的一种自动机制

```java
Integer i1 = new Integer(23);
int i2 = 10;
i1 = i2;//装箱----类型提升过程,发生了类型变化
i2 = i1;//拆箱---类型降低过程，类型肯定发生变化
```

**装箱阶段自动替换为了 valueOf 方法，拆箱阶段自动替换为了 xxxValue 方法**



### Float

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

######  int intValue() 

​          将此 Float 值以 int 形式返回（强制转换为 int 类型）。 

######  float floatValue() 

​          返回此 Float 对象的 float 值 

######  double doubleValue() 

​          返回此 Float 对象的 double 值 

######  static float parseFloat(String s) 

​          返回一个新的 float 值，该值被初始化为用指定 String 表示的值

 String toString() 
          返回此 Float 对象的字符串表示形式 

###### static Float valueOf(float f) 

​          返回表示指定的 float 值的 Float 实例。 



### Double 

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

###### double doubleValue() 

​          返回此 Double 对象的 double 值 

###### int intValue() 

​          以 int 形式返回此 Double 的值（通过强制转换为 int 类型）。 

static String toString(double d) 
          返回 double 参数的字符串表示形式 

###### static Double valueOf(double d) 

​          返回表示指定的 double 值的 Double 实例 

static double parseDouble(String s) 
          返回一个新的 double 值，该值被初始化为用指定 String 表示的值 







### Character

字段：自己查看

构造方法摘要 
Character(char value)
          构造一个新分配的 Character 对象，用以表示指定的 char 值

方法：

int compareTo(Character anotherCharacter) 
          根据数字比较两个 Character 对象。 

###### static boolean isDigit(char ch) 

​          确定指定字符是否为数字，是返回ture，否则返回false

###### static boolean isLetter(char ch) 

​          确定指定字符是否为字母。 

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

###### static Character valueOf(char c) 

​          返回一个表示指定 char 值的 Character 实例 

###### char charValue() 

​          返回此 Character 对象的值

 

### Boolean 

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

###### boolean booleanValue() 

​          将此 Boolean 对象的值作为基本布尔值返回

###### static Boolean valueOf(String s) 

​          返回一个用指定的字符串表示值的 Boolean 值。 

###### static boolean parseBoolean(String s) 

​          将字符串参数转换为对应的 boolean 值 







​	 
​	 
​	 
​	 