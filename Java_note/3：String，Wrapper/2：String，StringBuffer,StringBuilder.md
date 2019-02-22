## java.lang ：java程序的基础类，不需要导包，JVM自动导包了

### jav.lang.String	(immutable)

字符串的排序：是根据字典编排的顺序排序的，数字在字母之前，大写字母在小写字母的前面

##### java中的字符串：String, StringBuffer,StringBuilder（都被用来符辅助String）

public final class String extends Object implements Serializable, Comparable<String>, CharSequence

charSequence:是一个只读序列接口

##### String :Java中字符串是不变对象，不管是运算还是拼接产生新的结果都是一个新的String对象的产生

```java
/** The value is used for character storage. */
private final char value[];

/** Cache the hash code for the string */
private int hash; // Default to 0
public String(char value[]) {
        this.value = Arrays.copyOf(value, value.length);
}

String 是用value[]存储的，而这个属性是final修饰的，是不可变的，而且是用private修饰的，没有get，set方法，确保外部无法修改，而且String的方法都没有直接对value进行直接的修改
```

##### String对 +  的重载支持其实就是使用了StringBuilder以及他的append、toString两个方法

##### 字符串拼接几种方式区别

###### 1.直接用“+”号 

使用了StringBuilder以及他的append、toString两个方法

###### 2.使用String的方法concat 

concat其实就是申请一个char类型的buf数组，将需要拼接的字符串都放在这个数组里，最后再转换成String对象

###### 3.使用StringBuilder的append 

###### 4.使用StringBuffer的pend

append的方法都是调用父类AbstractStringBuilder的append方法，只不过StringBuffer是的append方法加了sychronized关键字，因此是线程安全的。append代码如下，他主要也是利用char数组保存字符

##### rerplace,replaceAll,replaceFirst()的区别

String replace(char oldChar, char newChar)  ----用新的字符或者字符串替换旧的字符或者字符串					（可以用来删除所有空格），基于字符串的替换

String replaceAll(String regex, String replacement) ---用新的字符串替换该字符串中的所在目标位置     					基于正则表达式的替换，replaceAll("\\d", "*") ：把一个字符串所有的数字字符都换成星号

String	replaceFirst(String regex, String replacement)  只匹配第一个相匹配字符串，基于正则表达式的

##### String.valueof()和Integer.toString()的不同

Integer.toString() 将i转换位buf符号数组，然后new String(buf, true)													String.valueof()方法

```java
public static String valueOf(Object obj) {
        return (obj == null) ? "null" : obj.toString();
}
public static String valueOf(char data[]) {
    return new String(data);
}
```

##### Switch 对String的支持

```java
public void test(String status) {
            switch (status) {
            case "killed":
                break;
            case "alive": 
            	break;
```



##### 本质：字符型数组

```java
 构造方法：
	String()
		初始化一个新创建的 String 对象，使其表示一个空字符序列
	String(String original) 
		初始化一个新创建的 String 对象，使其表示一个与参数相同的字符序列
	String(StringBuffer buffer) 
		分配一个新的字符串，它包含字符串缓冲区参数中当前包含的字符序列
	String(StringBuilder builder)
		分配一个新的字符串，它包含字符串生成器参数中当前包含的字符序列
```

```java
 方法：
	int length()  -----返回该字符串的长度
	boolean isEmpty()  ----判断字符串是否为空
	char[] toCharArray()  ---将String返回char类型数组
	int compareTo(String anotherString) ---比较字符串的大小

	String concat(String str)   -----想该字符串末尾拼接字符串等同+号操作
	boolean contains(CharSequence s)  -----判断是否包含目标字符串		
	String trim() ---去掉该字符串的前后为wightspace,占位符
	boolean equals(Object anObject)  ---判断该字符串与目标字符串内容是否相等，被重写
	char charAt(int index)  ---根据索引值获取相应的字符,从0开始	

	String toLowerCase() ---全部转换成小写
	String toUpperCase()  --全部转换成大写
	boolean  endsWith(String suffix)  ---判断以目标字符串结尾
	boolean  startsWith(String suffix);---判断该字符串是否已目标字符串开始

	int indexOf(int ch)  ---返回指定字符在本字符串中第一次出现的索引值的地址（ascall 码值）
	int indexOf(int ch, int fromIndex)---  从指定位置开始检索目标字符返回第一次出现索引值的地址
	int indexOf(String str) ----  返回指定子字符串在此字符串中第一次出现处的索引的地址
   int indexOf(String str, int fromIndex) ----返回指定子字符串在此字符串中第一次出现处索引的地址，从指定的索引始 
	int lastIndexOf(String s) ----返回指定字符串在本字符串中最后一次出现的索引值的地址
	int lastIndexOf(String s, int fromIndex) ----从指定位置开始检索目标字符串返回最后一次出现的位置 
	
	String[] split(String sign)  ----使用指定的隔离字符进行分离，返回容纳子字符串对象的数组
	split(String sign,int limit)----根据指定的分隔符对字符串进行拆分，并且限定拆分的次数，总是limit-1次 
```



```java
		String str=("abc,dfg,dfg,sfd,efg");
		String[] p=str.split(",");
		for(String i:p) {
			System.out.println(i);
		}
```

```
	String substring(int beginIndex) ----从指定位置开始截取子字符串
	String substring(int beginIndex, int endIndex) --从指定位置开始截取到指定的位置，但不包含结束的位置	  static String valueOf(int i)   -----将int参数返回String对象，或者和空字符串拼接
	static int parseInt(String s)  -----将满足要求的字符串对象转换成其所对应的基本数据类型
```



### StringBuffer :线程安全的可变字符串，字符串缓冲区安全地用于多个线程

######  public final class StringBuffer  extends AbstractStringBuilder implements java.io.Serializable, CharSequence

每个字符串缓冲区都有一定的容量。只要字符串缓冲区所包含的字符序列的长度没有超出此容量，就无需分配新的内部缓冲区数组如果内部缓冲区溢出，则此容量自动增大
	 	 
构造方法：
StringBuffer() 
          构造一个其中不带字符的字符串缓冲区，初始容量为 16 个字符	 
	 
StringBuffer(CharSequence seq) 
         构造一个字符串缓冲区，它包含与指定的 CharSequence 相同的字符+16

StringBuffer(String str) 
          构造一个字符串缓冲区，并将其内容初始化为指定的字符串内容，+16
	 
方法：
StringBuffer append(String str) 
	向字符串缓冲区追加字符串

delete(int start, int end) 
     	删除从什么位置到什么位置字符

deleteCharAt(int index) 
        删除什么位置字符

StringBuffer insert(int offset, String str) 
      从什么位置开始插入字符串或字符

StringBuffer reverse() 
	反向替换

void setCharAt(int index, char ch) 
          把后面的字符替在前面的位置替换

String substring(int start) 

String toString()  

StringBuffer replace(int start, int end, String str)  

int length()  



### StringBuilder： 用在字符串缓冲区被单个线程使用的时候

###### public final class StringBuilder  extends AbstractStringBuilder implements java.io.Serializable, CharSequence

String类型的对象经过操作都只是一个新的对象，会导致系统内存的浪费，而使用StringBuilder类会在字符串生成器里操作，然后用toString 转为String类型 减少了执行时内存的浪费

构造方法：
StringBuilder() 
          构造一个其中不带字符的字符串生成器，初始容量为 16 个字符

StringBuilder(CharSequence seq) 
          构造一个字符串生成器，包含与指定的 CharSequence 相同的字符

StringBuilder(String str) 
          构造一个字符串生成器，并初始化为指定的字符串内容。

方法：
 StringBuilder append(String str)  

 char charAt(int index) 

 StringBuilder delete(int start, int end) 

 StringBuilder deleteCharAt(int index)  

 StringBuilder insert(int offset, String str)  

 int lastIndexOf(String str)  

 int length()  

 StringBuilder replace(int start, int end, String str) 

 StringBuilder reverse() 

 String substring(int start) 

 String toString() 

 

## 三者的区别：

####   1：执行效率   StringBuilder （后出的）> StringBuffer > String

　　	String最慢的原因：

　　	String为字符串常量final修饰的，而StringBuilder和StringBuffer均为字符串变量，即String对象一旦创建之后该对象是
	不可更改的，更改的话就是一个新的String常量，但后两者的对象是变量，是可以更改的

####   2:在线程安全上 

StringBuilder是线程不安全的，而StringBuffer是线程安全的(调用方法时加入同步锁)

String：适用于少量的字符串操作的情况，线程安全

StringBuilder：适用于单线程下在字符缓冲区进行大量操作的情况

StringBuffer：适用多线程下在字符缓冲区进行大量操作的情况

### 字符串池（字符串常量池）：为了避免大量创建相同String对象，浪费资源问题

  机制：直接赋值创建一个String时，首先检查字符串池是否有字面量值相等的字符串，如果有，则不再创建，直接返回字符串池中对象的引用，若没有，则创建，然后放到字符串池中，并且返回新建对象的引用。而new 时会检查常量池中是否有字面量相等的字符串，若有不会把对象放入常量池中，若没有则放入

调用intern（）方法，会检查常量池中是否有和当前的对象字面量相同的引用对象，若有，则返回字符串池中的对象，若没有则放到字符串池中，并返回当前对象

#####  public native String intern();


