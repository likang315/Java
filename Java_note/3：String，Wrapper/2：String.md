### String

------

##### 1：String 

​	不可变对象，本质字符型数组 ，不管是运算还是拼接产生新的结果都是一个新的 String 对象的产生

```java
//charSequence:只读序列接口
public final class String implements java.io.Serializable, Comparable<String>, CharSequence {
				//基定了不可变的因素，无get，set方法
        private final char value[];
        private int hash; //Default to 0
        //版本号
        private static final long serialVersionUID = -6849794470754667710L;
        //没什么用，因为String不可变
				public String() {
						this.value = "".value;				
				}
        public String(char value[]) {
            this.value = Arrays.copyOf(value, value.length);
        }
}
// String 的 不可变原因
String 是用 value[] 存储的，而这个属性是final修饰的，是不可变的，而且是用 private 修饰的，没有get，set方法，确保外部无法修改，而且String的方法中都没有直接对value进行修改,多种原因共同保证了它的不可变性
```

###### 构造方法： 	

- String() 初始化一个新创建的 String 对象，使其表示一个空字符序列
- String(String original) 初始化一个新创建的 String 对象，使其表示一个与参数相同的字符序列 
- String(StringBuffer buffer) 分配一个新的字符串，它包含字符串缓冲区参数中当前包含的字符序列 
- String(StringBuilder builder) 分配一个新的字符串，它包含字符串生成器参数中当前包含的字符序列

###### 方法：

- int length() -----返回该字符串的长度

- boolean isEmpty() ----判断字符串是否为空

- char[] toCharArray() ---将String返回char类型数组

- char charAt(int index) ---根据索引值获取相应的字符,从0开始	

- boolean contains(CharSequence s) -----判断是否包含目标字符串 

- String trim() ---去掉该字符串的前后为wightspace,占位符 

- boolean equals(Object anObject) ---判断该字符串与目标字符串内容是否相等，被重写

- String toLowerCase() ---全部转换成小写 

- String toUpperCase() --全部转换成大写 

- boolean endsWith(String suffix) ---判断以目标字符串结尾 

- boolean startsWith(String suffix);---判断该字符串是否已目标字符串开始

- int indexOf(int ch) ---返回指定字符在本字符串中第一次出现的索引值的地址（ascall 码值） 

- int indexOf(String str, int fromIndex) ----返回指定子字符串在此字符串中第一次出现处索引的地址，从指定的索引始 

- int lastIndexOf(String s) ----返回指定字符串在本字符串中最后一次出现的索引值的地址

- String substring(int beginIndex) 

##### 2：String 的拼接方式，四种

​	原理都是new一个新的数组，然后拷贝过去

###### 1：直接用“+”号

​		使用了StringBuilder 以及他的 append、toString 两个方法

###### 2：使用 String 的方法 concat

```java
// concat申请一个 char 类型的 buf []，将需要拼接的字符串都放在这个数组里，最后再转换成 String 对象
public String concat(String str) {
        int otherLen = str.length();
        if (otherLen == 0) {
            return this;
        }
        int len = value.length;
        //把原数组拷贝到新数组中
        char buf[] = Arrays.copyOf(value, len + otherLen);
        str.getChars(buf, len);
        return new String(buf, true);
}

void getChars(char dst[], int dstBegin) {
  			//value 指str.value
        System.arraycopy(value, 0, dst, dstBegin, value.length);
}
```

###### 3：StringBuilder 的 append（String str）

```java
// 创建一个新数组，拷贝过去
public AbstractStringBuilder append(String str) {
    if (str == null) str = "null";
    int len = str.length();
    ensureCapacityInternal(count + len);
    str.getChars(0, len, value, count);
    count += len;
    return this;
}
```

###### 4：StringBuffer的apend

​	append（）都是调用父类AbstractStringBuilder的append方法，只不过StringBuffer是的append方法加了sychronized关键字，因此是线程安全的

##### 3：rerplace，replaceAll，replaceFirst() 的区别

- String replace(char oldChar, char newChar) 
  - 用新的字符或者字符串替换旧的字符或者字符串（可以用来删除所有空格），基于字符串的替换
- String replaceAll(String regex, String replacement) 
  - 用新的字符串替换该字符串中的所在目标位置的替换，基正则表达式的 
  - replaceAll("\d", "*") ：把一个字符串所有的数字字符都换成星号
- String replaceFirst(String regex, String replacement) 
  - 只匹配第一个相匹配字符串，基于表达式的

##### 4：String.valueof( ) 和 Integer.toString() 的不同

Integer.toString() 将 i 转换为 buf 符号数组，然后 new String(buf, true)

```java
public static String valueOf(Object obj) {
    return (obj == null) ? "null" : obj.toString();
}
public static String valueOf(char data[]) {
    return new String(data);
}
```

##### 5：Switch对String的支持

```java
public void test(String status) {
  switch (status) {
    case "killed":
      break;
    case "alive": 
      break;
    default ：
  }
}
```

##### 6：String 的分割

- String[] split(String sign) ----使用指定的隔离字符进行分离，返回容纳子字符串对象的数组
- split(String sign,int limit)----根据指定的分隔符对字符串进行拆分，并且限定拆分的次数，总是limit-1次

```java
String str = "abc,dfg,dfg,sfd,efg";
String[] p = str.split(",");
for(String i:p) {
  System.out.println(i);
}
```

##### 7：equals() 重写后

```java
public boolean equals(Object anObject) {
  // 先比较地址
  if (this == anObject) {
    return true;
  }
  // 再比较是不是 String类型的数据
  if (anObject instanceof String) {
    String anotherString = (String) anObject;
    int n = value.length;
    //比较Char[] 长度，然后倒叙比较
    if (n == anotherString.value.length) {
      char v1[] = value;
      char v2[] = anotherString.value;
      int i = 0;
      while (n-- != 0) {
        if (v1[i] != v2[i])
          return false;
        i++;
      }
      return true;
    }
  }
  return false;
}
```

##### 8：字符串池（字符串常量池）：为了避免大量创建相同 String 对象，浪费资源问题

机制：直接赋值创建一个String时，首先检查字符串池是否有字面量值相等的字符串，如果有，则不再创建，直接返回字符串池中对象的引用，若没有，则创建，然后放到字符串池中，并且返回新建对象的引用，但是如果是new 的则直接在堆中分配

- 调用 intern（）方法，会检查常量池中是否有和当前的对象**字面量相同的引用对象**，若有，则返回字符串池中的对象，若没有则放到字符串池中，并返回当前对象
- public native String intern();

```java
String s1="123";
String s2="123";
String s = new String ("123");

System.out.println(s1==s2); //true  ，常量池
System.out.println(s==s1);  //false ，堆中分配
System.out.println(s1.equals(s)); //true ，比较内容
```

##### 9：字符串的排序

​		根据字典编排的顺序排序的，数字在字母之前，大写字母在小写字母的前面

##### 10：format (String format，object... args)/SimpleDateFormat

​	使用指定的格式字符串，和参数返回一个格式化的字符串

- 日期格式化: 
  1. %tb--月份的简称 
  2. %tB--月份的全--称
  3.  %tA--星期几的全称 
  4. %ta--星期几的简称 
  5. %tY--年份---2006 
  6. %td--一个月中的第几天
- 时间格式化：
  1. %tH---24时制的小时 
  2. %tM---分钟 
  3. %tS---秒数
  4. %tL---毫秒 
  5. %tP---上午或者下午

时间和日期的格式化组合： %tF--年-月-日格式 %tT---时：分：秒 %tc---包括全部日期和时间信息

```java
public static String format(String format, Object... args) {
  return new Formatter().format(format, args).toString();
}

Date date = new Date();
String year = String.format("%tc", date);
System.out.println(year);
```


