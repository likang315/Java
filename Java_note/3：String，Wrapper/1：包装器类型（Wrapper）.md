### 包装器、自动拆装箱机制

------

##### 1：包装器类型（wrapper）：

​	将基本类型包装为对象来处理

```java
// Java中八种基本数据类型对应有八种包装器类型
byte                Byte
short            		Short
int 							 	Integer (特殊)
long 								Long
float								Float
double					 		Double
char								Character（特殊）
boolean							Boolean
```

- public final class Integer extends Number implements Comparable<Integer> 

##### 2：自动拆装箱机制（Auto boxing）：

- 默认的进行基本数据与包装器类型之间的一个相互转化过程的自动机制
- 原理：装箱阶段自动替换为了 valueOf( )，拆箱阶段自动替换为了 xxxValue 方法

```java
Integer i1 = new Integer(23);
int i2 = 10;
// 装箱，类型提升过程，发生了类型变化
i1 = i2;
// 拆箱，类型降低过程，类型肯定发生变化
i2 = i1;
```

##### 3：包缓存：所有包装类的缓存 (cache)

###### 缓存机制：

包装类在**自动装箱的过程中**，是有缓存数组的，对于值 **在-128~127之间的数**，会放在内存中进行重用；对于大于这个范围的数，使用的时候 都会 new 出一个新的对象

```java
Integer i = 10;  //装箱
int n = i;   //拆箱
```

###### 原理（Integer 举例）：

​	静态内部类 IntegerCache，IntegerCache 有一个**静态的Integer cahce[] 数组**，因为有静态代码块的存在，在类初始化时就将 -128 到 127 的 Integer 对象创建存储到数组中，一旦程序调用valueOf 方法，如果 i 的值是在-128 到 127 之间就直接使用cache缓存数组

###### 机制目的：

为了使最小化数据不会频繁创建，这是一种优化措施,提高效率，可以设置系统属性修改上限 

- java.lang.Integer.IntegerCache.high 修改缓冲区上限,默认为127
- 其他包装类缓存：Boolean（全部缓存）、Byte（全部缓存）、Character（<= 127缓存）、Short（-128~127缓存）、Long（-128~127 缓存）、**Float（没有缓存）、Double（没有缓存），因为在指定范围内浮点型数据个数是不确定的，整型等个数是确定的，所以可以 Cache**

```java
private static class IntegerCache {
    static final int low = -128;
    static final int high;
    //缓存数组,存储Integer对象
    static final Integer cache[]; 
	  //类初始化时候已经加载到cache[]中
    static {
        int h = 127;
        high = h;
	      // 初始化cache数组
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

##### 4：包装器类型

##### Integer：

​	当我们给一个 Integer 对象赋一个int 值的时候，会调用 Integer 类的静态方法 valueOf(int i)，valueof( ) 与源码分析得直接赋值时，如果整型字面量的值在-128 到 127 之间，那么不会 new 新的 Integer 对象，而是直接从cache[] 中获取 Integer 对象，如超过则new一个新的对象

###### static字段：(属性)

- static int MAX_VALUE
  -  值为 2^31-1 的常量，它表示 int 类型能够表示的最大值
- static int MIN_VALUE
  - 值为 －2^31 的常量，它表示 int 类型能够表示的最小值
- static int SIZE
  - 用来表示 int 值的比特位数
- static Class<Integer> TYPE 
  - 表示基本类型 int 的 Class 实例

###### 构造方法

- Integer(int value) 
  - 构造一个新分配的 Integer 对象，它表示指定的 int 值

- Integer(String s)
  - 构造一个新分配的 Integer 对象，它表示 String 参数所指示的 int 值​	

```java
	Integer i1 = new Integer(10); // new 的直接在堆中
	Integer i2 = new Integer("10");
	Integer i3 = 10;		//cache[] 中
	Integer i4 = 10;
	int i = 10;

	System.out.println(i1==i2);//false  	比较地址
	System.out.println(i1==i3);//false   	比较地址
	System.out.println(i3==i4);//true	    cache[] 中
	System.out.println(i==i4); //true,基本数据类型比值，自动调用valueOf(int i)
```

######  方法：

- int compare(int x, int y) ：X>Y 返回1，等于返回 0，小于返回 -1

- int compareTo(Integer anotherInteger) ：比较两个Integer对象的大小，调用compare方法

- static String toBinaryString(int i) ：以二进制无符号整数形式返回一个整数参数的字符串表示形式

- String	toString（）

- ###### int intValue() ：以 int 类型返回该 Integer 的值,拆箱

- ###### static Integer valueOf(int i)：返回一个表示指定的 int 值的 Integer 实例，装箱

- ###### static int parseInt(String s) ：将满足要求的字符串对象转换成其所对应的基本数据类型

- static String toString(int i)

- static int reverse(int i)  进行数值反转,以补码输出

##### Character

###### 构造方法

Character(char value)
          构造一个新分配的 Character 对象，用以表示指定的 char 值

###### 方法：

int compareTo(Character anotherCharacter) 
          根据数字比较两个 Character 对象

###### static boolean isDigit(char ch) 

​          确定指定字符是否为数字，是返回ture，否则返回false

###### static boolean isLetter(char ch) 

​          确定指定字符是否为字母。 

###### static Character valueOf(char c) 

​          返回一个表示指定 char 值的 Character 实例 

###### char charValue() 

​          返回此 Character 对象的值s 

##### Boolean 

###### 字段：

- static Boolean FALSE 
  - 对应基值 false 的 Boolean 对象 
- static Boolean TRUE 
  - 对应基值 true 的 Boolean 对象
- static Class<Boolean> TYPE 
  - 表示基本类型 boolean 的 Class 对象 

###### 构造方法：

Boolean(String s) 
          如果 String 参数不为 null 且在忽略大小写时等于 "true"，则分配一个表示 true 值的 Boolean 对象

###### 方法：

###### boolean booleanValue() 

​          将此 Boolean 对象的值作为基本布尔值返回

###### static Boolean valueOf(String s) 

​          返回一个用指定的字符串表示值的 Boolean 值

###### static boolean parseBoolean(String s) 

​          将字符串参数转换为对应的 boolean 值
