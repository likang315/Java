### 1：StringBuffer ：线程安全的可变字符串

​	每个字符串缓冲区都有一定的容量，只要字符串缓冲区所包含的字符序列的长度没有超出此容量，就无需分配新的内部缓冲区数组如果内部缓冲区溢出，则此容量自动增大

###### 构造方法：

- StringBuffer( ) ：构造一个其中不带字符的字符串缓冲区，**初始容量为 16 个字符**
- StringBuffer(String str) ：构造一个字符串缓冲区，并将其内容初始化为**（str + 16）**

```java
public final class StringBuffer  extends AbstractStringBuilder implements java.io.Serializable, CharSequence
{
    private transient char[] toStringCache;
    public StringBuffer() {
        super(16);
    }
    public StringBuffer(String str) {
        super(str.length() + 16);
        append(str);
    }
    
		// 重写AbstractStringBuffer ，每个方法加synchronized 锁
		@Override
    public synchronized char charAt(int index) {
        if ((index < 0) || (index >= count))
            throw new StringIndexOutOfBoundsException(index);
        return value[index];
    }
}
```

###### 方法：

- StringBuffer append(String str)
  - 向字符串缓冲区追加字符串

- delete(int start, int end) 
  - 删除从什么位置到什么位置字符

- deleteCharAt(int index) 
  - 删除什么位置字符
- StringBuffer insert(int offset, String str) 
  - 从什么位置开始插入字符串或字符
- StringBuffer reverse() 
  - 反向替换
- void setCharAt(int index, char ch) 
  - 把后面的字符替在前面的位置替换
- String substring(int start)

### 2：StringBuilder 单线程效率最高

```java
public final class StringBuilder extends AbstractStringBuilder
   implements java.io.Serializable, CharSequence{
    //默认初始容量
    public StringBuilder() {
        super(16);
    }
    // str + 16
    public StringBuilder(String str) {
        super(str.length() + 16);
        append(str);
    }
    //重写AbstractStringBuilder，单线程效率高
		@Override
    public StringBuilder append(String str) {
        super.append(str);
        return this;
    }
}
```

###### 原因：

​	String类型的对象经过操作都只是一个新的对象，会导致系统内存的浪费，而使用StringBuilder类会在字符串生成器里操作，然后用toString 转为String类型 减少了执行时内存的浪费

###### 构造方法：

- StringBuilder() 构造一个其中不带字符的字符串生成器，**初始容量为 16 个字符**
- StringBuilder(CharSequence seq) 构造一个字符串生成器，包含与指定的 CharSequence 相同的字符，seq+16
- StringBuilder(String str) 构造一个字符串生成器，并初始化为str +16 



### 3：扩容机制

###### StringBuffer 和 StringBuilder 一样继承 AbstractStringxxx

根据**当前数组长度的 2倍+2 和 新增加字符串长度+原有数组长度 进行比较，**谁大就是谁

```java
//二倍+2 扩容,通过移位运算符
private int newCapacity(int minCapacity) {
  int newCapacity = (value.length << 1) + 2;
  if (newCapacity - minCapacity < 0) {
    newCapacity = minCapacity; //当前数组大小和小的String拼接成
  }
  return (newCapacity <= 0 || MAX_ARRAY_SIZE - newCapacity < 0)
    ? hugeCapacity(minCapacity) : newCapacity;
}
```



### 4：三者的区别：

###### 1：执行效率 StringBuilder（后出的）> StringBuffer > String

- String 最慢的原因：

  ​	String 为字符串常量final修饰的，而StringBuilder和StringBuffer均为字符串变量，即String对象一旦创建之后该对象是 不可更改的，更改的话就是一个新的String常量，但后两者的对象是变量，是可以更改的

###### 2：在线程安全上

StringBuilder 是线程不安全的，而 StringBuffer 是线程安全的 (**大部分方法加同步锁)**

- String：适用于**少量的字符串操作的情况，线程安全**
- StringBuilder：适用于**单线程下在字符缓冲区进行大量操作的情况**
- StringBuffer：适用**多线程下在字符缓冲区进行大量操作的情况**

### 