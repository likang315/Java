### 正则表达式、URL

------

##### 1：正则表达式 (Regular Expression)：是一种文本模式，包括普通字符和特殊字符

​	正则表达式定义了字符串的模式，可以用来搜索、编辑或处理文本

###### 通配符：

1. ?：通配符匹配文件名中的 0 个或 1 个字符
2. *： 通配符匹配零个或多个字符
3. .： 任何字符 
4. +：一次或一次以上
5. {最小，最大次} ：最小值必须确定，最大值可以不确定

-  \s+ ：匹配多个空格字符
-  \+  ：出现多次
-  \\  ：要插入一个正则表达式的反斜线，所以其后的字符具有特殊的意义，转义字符
- （） ：分组，整体

##### String 类中正则表达式的应用

- boolean matches(String regex) 
  - 判断此字符串是否匹配给定的正则表达式
- String[] split(String regex) 
  - 根据给定正则表达式的匹配拆分此字符串 
- String replaceAll(String regex, String replacement) 
  - 使用给定的 replacement 替换此字符串所有匹配给定的正则表达式的子字符串 

###### java.util.regex

##### Pattern 

​	正则表达式的编译表示形式，先编译，然后得到匹配器，再匹配

```java
public final class Pattern
```

- 字符串的正则表达式必须首先被编译为此类的实例，然后可将得到的模式用于创建 Matcher（匹配器），依照正则表达式，该对象可以与任意字符序列匹配
- static Pattern compile(String regex) 
  - 将给定的正则表达式编译到模式中
- static Matcher matcher(CharSequence input) 
  - 创建匹配编译后模式的匹配器，但并不比较是否匹配

###### java.util.regex.Matcher

##### Matcher：

通过解释 Pattern 对 characterSequence 执行匹配操作的引擎，调用Pattern.matcher( ) 方法从模式创建匹配器

- boolean matches() 
  - 尝试将整个区域与模式匹配
- boolean lookingAt() 
  - 尝试将从头开始的输入序列与该模式匹配 
- Pattern pattern() 
  - 返回由此匹配器解释的模式
- boolean find() 
  - 尝试查找与该模式匹配的输入序列的下一个子序列
-  boolean find(int start) 
  - 重置此匹配器，然后尝试查找匹配该模式、从指定索引开始的输入序列的下一个子序列

###### Java.net.URL

##### 2：URL

​	类 URL 代表一个统一资源定位符，它是指向互联网中“唯一的资源”

###### 构造方法：

- URL(String spec) 
  - 根据 String 表示形式创建 URL 对象

###### 方法：

- URLConnection openConnection() 
  - 返回一个 URLConnection 对象， 和URL 建立连接

###### java.net.URLConnection

##### URLConnection  

- 应用程序和 URL 之间的通信链接，此类的实例可用于读取和写入此 URL 引用的资源
- InputStream getInputStream() 
  - 返回从此打开的连接读取的输入流
    	