### 常用Java类

------

###### java.lang.Object

##### 1：Object：

- Object 类是所有 Java 中类的顶级父类
- 普通类继承了 Object 类才可以称为一个Java类
- 类中提供了最基本的**9个方法（加上重载是11个方法**）

1. ###### toString(): 输出对象时，默认自动调用，也可手动调用

   - 在打印查看一个对象的内部封装数据（属性）时，默认打印的是
   - 完整限定名@对象哈希值的十六进制表示：com.xzy.obj.Person@82764b
   - 用来默认的解析对象的内部结构，当前的这个默认解析是Object类给我们的一种实现，需重写成我们想要解析的格式

2. ###### boolean equals(Object obj) ：

   - 如若没有重写，则是继承 Objecet 类的，根据源码看，还是 ==，比较的是地址(引用数据类型)，若重写则比较的是内容，判断两个对象是否相等
   - 引用数据类型是否相等我们必须比较其封装的内容是否完全相同，重写之后先比较地址是否相同，若相同直接返回ture，若不相等，则比较内容是否相等，相等返回 ture

```JAVA
public boolean equals(Object obj) {
		return (this == obj)；
}
```

###### 四个特性：

- 自反性：对于任意不为nul的引用值x，x.equals(x)一定是true
- 对称性：对于任意不为null的引用值x和y，当且仅当x.equals(y)是true时，y.equals(x)也是true
- 传递性：对于任意不为null的引用值x，y和z，如果x.equals(y)是true，同时y.equals(z)是true，那么x.equals(z)一定是true
- 一致性：对于任意不为null的引用值x和y，如果用于 equals 比较的对象信息没有被修改的话，多次调用时x.equals(y) 要么一致地返回true，要么一致地返回 false

3. ###### hashCode( ): 哈希值

   - 对象调用该方法时，返回的是将该对象的内部地址转换成的一个整数，在一个程序中没有改变对象中封装的数据，则对象多次调用该方法时产生同一个整数（哈希码）

   - ###### hashCode() 与 equals() 机制 

   - 若两个对象 equals( )相等（重写后），先比较地址，若地址相同肯定内容相同直接返回，若地址不同，再比较内容，内容相同则为true

   - hashCode ( ) 若不重写，则根据对象返回的哈希值，若重写，则根据内容值返回Hash值

   - 对于eqauls方法和hashCode方法是这样规定的： 

     1. 如果两个对象的 equals( )相等，那么它们的hashCode( ) 一定要相等，确保通过equals(Object obj)方法判断结果为true的两个对象具备相等的hashcode()返回值
     2. 如果两个对象的hashCode相等，它们的equals() 并不一定相等

4. ###### Class<?> getClass() 

   - 返回此 Object 的运行时 class 对象

5. ###### protected  void finalize() 

   - 当垃圾回收器确定不存在对该对象的更多引用时，由对象的垃圾回收器调用此方法

6. ###### protected  native Object clone( ) 

   - 创建并返回此对象的一个副本，如果某个对象已经有一些有效值了，并且需要一个和它一样的对象，clone（）默认是浅拷贝

   - 按照惯例重写的时候一个要将protected修饰符修改为public，这是JDK所推荐的做法

   - 重写的clone() ，必须实现 Cloneable 接口。虽然这个接口并没有什么方法，但是必须实现该标志接口,否则将会在运行期间，抛出：CloneNotSupportedException 异常

   - ###### 浅拷贝和深拷贝的区别是：

     - 在拷贝对象的一些属性时时，创不创建新的属性对象，不创建直接将原对象的引用值拷贝给新的对象，则是浅拷贝，反之，创建新的对象为深拷贝
     - 实现深拷贝
       - 如果想要深拷贝一个对象，这个对象必须要实现 Cloneable接口，重写clone方法，并且在 clone 方法内部，把该对象引用的其他对象也要 clone 一份，这就要求这个被引用的对象必须也要实现Cloneable接口并且重写clone方法
       - 序列化也可以拷贝为深拷贝

###### java.lang.System

##### 2：System 

- 属性：
  - static PrintStream err 
    - “标准”错误输出流
  - static InputStream in 
    - “标准”输入流 
  - static PrintStream out 
    - “标准”输出流 
- 方法：
  - static void arraycopy(Object src, int srcPos, Object dest, int destPos, int length) 
    -  从指定源数组中复制一个数组，复制从指定的位置开始，到目标数组的指定位置结束
  - static void exit(int status) 
    - 终止当前正在运行的 Java 虚拟机
  - static void gc() 
    - 运行垃圾回收器

###### java.util.Scanner

##### 3：Scanner 类：

​	扫描器，从指定源开始扫描，等待键盘输入，创建对象新建扫描器，开始扫描

```java
public class ScannerDemo {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        // 从键盘接收数据
        // next方式接收字符串
        System.out.println("next方式接收：");
        // 判断是否还有输入，只判断一回
        if（scan.hasNext（）） {
            String str1 = scan.next();
            System.out.println("输入的数据为：" + str1);
        }
        scan.close();
    }
  			//死循环了，输入一下输出一下
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()) {
            String str = sc.nextLine();
            System.out.println(str);
        }
        sc.close();
}
```

###### next() 与 nextLine() 区别

- next()，nextInt().....都一样:
  - 一定要读取到有效字符后才可以开始输入，在未读取到有效字符时，空格，tab，Enter键均舍去
  - 只有输入有效字符后才将其后面输入的空白（三个键）作为结束符
  - next() 不能得到带有空格的字符串

- nextLine()：
  - 以Enter为结束符，可以获得空格，Tab键
  - 读取完之在它的结束符之前自动加\n，给输入的流换了个行，下一次读取时自动从新的一行开始扫描

###### java.lang.Math

##### 4：Math 

（无构造）在这个类中提供大量的关于数学运算的 static 方法	

- static double E ： 比任何其他值都更接近 e（即自然对数的底数）的 double 值
- static double PI ：比任何其他值都更接近 pi（即圆的周长与直径之比，圆周率）的 double 值
- static double abs(double a) 
  - 返回 double 值的绝对值
- static double sqrt(double a) 
  - 返回正确舍入的 double 值的正平方根
- static double cbrt(double a) 
  - 返回 double 值的立方根
- static double exp(double a) 
  -  返回 e 的 double 次幂的值
- static double pow(double a, double b) 
  - 返回第一个参数的第二个参数次幂的值
- static double max(double a, double b) 
  - 返回两个 double 值中较大的一个
- static double min(double a, double b) 
  - 返回两个 double 值中较小的一个
- static double random() 
  - 返回带正号的 double 值，该值大于等于 0.0 且小于 1.0，左闭右开
- static int round(float a) 
  - 返回最接近参数的 int
- static double signum(double d) 
  - 返回参数的符号函数；如果参数为 0，则返回 0；如果参数大于 0，则返回 1.0；如果参数小于 0，则返回 -1.0 

###### java.util.Random

##### 5：Class Random：

​	如果用相同的种子创建两个 Random 实例，则对每个实例进行相同的方法调用序列，它们将生成并返回相同的随机数，种子即构造参数

- 构造方法：
  ​			public Random(）   -------- 创建一个新的随机数生成器
  ​			public Random(long d)------- 使用单个 long 种子创建一个新的随机数生成器
- 方法：
  - int nextInt()
    - 返回下一个伪随机数，它是此随机数生成器的序列中均匀分布的 int 值
  - int nextInt(int n) 
    - 返回一个伪随机数，它是取自此随机数生成器序列的、在 0（包括）和指定值（不包括）之间均匀分布的 int 值，左闭右开
- 注意：伪随机数如果种子数相同，那么在相同的随机次数下，产生的随机数是相同的，其有一个nextDouble()产生含0.几的随机小数，其也是Math类的Random()底层实现

###### java.math.BigInteger

#####  6：Class BigInteger：大整数

构造方法：
​		BigInteger(int numBits, Random rnd) 
​      			构造一个随机生成的 BigInteger，它是在 0 到 (2^numBits - 1)（包括）范围内均匀分布的值

​		BigInteger(String val) 
​			将 BigInteger 的十进制字符串表示形式转换为 BigInteger
方法：
​		 BigInteger add(BigInteger val) 
​				返回其值为(this + val) 的 BigInteger
​		 BigInteger subtract(BigInteger val) 
   					 返回其值为 (this - val) 的 BigInteger 
 			
​		 BigInteger divide(BigInteger val) 
​					返回其值为 (this / val) 的 BigInteger

​		 BigInteger multiply(BigInteger val) 
​					返回其值为 (this * val) 的 BigInteger

​		 int compareTo(BigInteger val) 
​    			  将此 BigInteger 与指定的 BigInteger 进行比较

​		 BigInteger gcd(BigInteger val) 
​      			  返回一个 BigInteger，其值是 abs(this) 和 abs(val) 的最大公约数

​		 String toString() 
 			          返回此 BigInteger 的十进制字符串表示形式

#####       Calss BigDecimal ：

用来对超过16位有效位的数进行精确的运算，双精度浮点型变量 double 可以处理16位有效数

- BigDecimal 由任意精度的整数非标度值 和 32 位的整数标度 (scale) 组成
- 如果为零或正数，则标度是小数点后的位数
- 如果为负数，则将该数的非标度值乘以 10 的负 scale 次幂，因此，BigDecimal 表示的数值是 (unscaledValue × 10-scale)
- 舍入模式：MathContext 类用来设置舍入模式，精度

###### java.util.Date

#####  7：Class Date：获取当前的瞬时时间精确到毫秒 

- 使用 Calendar 类实现日期和时间字段之间转换，使用 DateFormat 类来格式化和解析日期字符串
- 方法：	
  ​		 boolean after(Date when) 
  ​				  测试此日期是否在指定日期（when）之后 
  ​		 boolean before(Date when) 
  ​				  测试此日期是否在指定日期(when)之前
  ​		 int compareTo(Date anotherDate)
  ​				  比较两个日期的顺序
  ​		 boolean equals(Object obj) 
  ​    				  比较两个日期的相等性

###### java.text.DateFormat:  日期/时间格式化子类的抽象类

#####  Calss DateFormat 

- static DateFormat getDateInstance() ：获取日期格式器，该格式器具有默认语言环境的默认格式化风格 
- static DateFormat getTimeInstance() ：获取时间格式器，该格式器具有默认语言环境的默认格式化风格 

###### java.text.SimpleDateFormat

##### Class SimpleDateFormat：与语言环境有关的方式来格式化和解析日期的具体类

- 构造方法：
  - SimpleDateFormat ()：用默认的模式和默认语言环境的日期格式符号构造 SimpleDateFormat
  - SimpleDateFormat (String pattern)：用给定的模式和默认语言环境的日期格式符号构造 
    - new SimpleDateFormat ("yyyy/MM/dd hh:mm:ss");
- 方法：
  - void applyPattern(String pattern) ： 将给定模式字符串应用于此日期格式 
  - String.format(String.valueOf(date) ：将一个 Date 格式化为日期/时间字符串

###### java.util.Calendar

#####  8：Class Calendar 类：

```java
public abstract class Calendar
```

- 是一个抽象类，它为特定瞬间与一组诸如 YEAR、MONTH、DAY_OF_MONTH、HOUR 等日历字段之间的转换提供了一些方法，并为操作日历字段提供了一些方法
- Calendar 提供了一个类方法 getInstance（），以获得此类型的一个通用的对象
- void set (int year, int month, int date, int hourOfDay, int minute, int second) 
  - 设置字段 YEAR、MONTH、DAY_OF_MONTH、HOUR、MINUTE 和 SECOND 的值 
- void setTime (Date date) ：使用给定的 Date 设置此 Calendar 的时间

###### java.util.ResourceBoundle

##### 9：ResourceBundle：用于加载本地化资源文件的方便类

   info_en_US.properties

- static ResourceBundle getBundle(String baseName)														
  - 使用指定的基本名称、默认的语言环境和调用者的类加载器获取资源包
  - baseName - 资源包的基本名称，是一个完全限定类名
- static ResourceBundle getBundle(String baseName, Locale locale)
- String getString(String key) 
  - 从此资源包或它的某个父包中根据给定键的字符串，获取它的属性值

```java
//注意：一定要将 properties 文件在classpath目录下
ResourceBundle rb1 = ResourceBundle.getBundle("info");
System.out.println("name = " + rb1.getString("name"));
System.out.println("password = " + rb1.getString("password"));
```

##### 10：MessageFormat：用来格式化一个消息，通常是一个字符串

```java
public class MessageFormat extends Format {
  public static String format(String pattern, Object ... arguments) {
        MessageFormat temp = new MessageFormat(pattern);
        return temp.format(arguments);
  }
}
// 格式化字符串，常用于打印日志
String s2 = MessageFormat.format("oh, {0} is 'a' pig {1}", "lisi","!");
System.out.println(s2);
```

​	  



