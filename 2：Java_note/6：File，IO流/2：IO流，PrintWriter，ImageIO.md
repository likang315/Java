### IO操作的步骤（5步）：

1：封装File对象
2：选择io对象
3：加缓冲
4：进行读写操作
5：关闭IO

### IO 流 ：将数据是读入到内存还是从内存写出分为：输入流（读入） +  输出流（写出）

**输入流：**全部是InputStream/Reader的子类 其类名是以InputStream/Reader结尾    进行读的操作
**输出流：**全部是OutputStream/Writer的子类 其类名是以OutputStream/Writer结尾  进行写的操作

字节流经常操作二进制的数据，比如图片，视频，音乐等等，文本文件的使用字符流
	
字节流（低级流)
字节流：全部是InputStream、OutputStream的子类 其类名是以 OutputStream/InputStream 结尾

######  public abstract class InputStream

######  public abstract class OutputStream

![IO流.png](https://github.com/likang315/Java/blob/master/Java_note/6%EF%BC%9AFile%EF%BC%8CIO%E6%B5%81/IO%E6%B5%81.png?raw=true)

### java.io.FileInputStream  类

###### public class FileInputStream extends InputStream

从文件系统中的某个文件中获得输入字节，文件输入流（低级流）

```java
构造方法：		
	FileInputStream(File file) 
     		通过打开一个到实际文件的连接来创建一个文件输入流，该文件通过文件系统中的 File 对象 file 指定
	FileInputStream(String name) 
    		  通过打开一个到实际文件的连接来创建一个文件输入流，该文件通过文件系统中的路径名 name 指定

方法：
	 int read(byte[] b) 
     		 从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中 
	 void close() 
      		关闭此文件输入流并释放与此流有关的所有系统资源
```

### java.io.FileOutputStream类

###### public class FileOutputStream extends OutputStream

FileOutputStream：文件字节输出流是用于将数据写入文件中，低级流 

```java
构造方法：
	FileOutputStream(File file) 
      		创建一个向指定 File 对象表示的文件中写入数据的文件输出流
	FileOutputStream(String name) 
      		创建一个向具有指定名称的文件中写入数据的输出文件流
	FileOutputStream(File file, boolean append) 
		 	创建一个向指定 File 对象表示的文件中写入数据的文件输出流，追加
方法：
	 void write(byte[] b) 
     		将 b.length 个字节从指定 byte 数组写入此文件输出流中
	 void write(byte[] b, int off, int len) 
      		将指定 byte 数组中从偏移量 off 开始的 len 个字节写入此文件输出流
	 void close() 
      		关闭此文件输出流并释放与此流有关的所有系统资源。 

注意：	覆盖写：默认创建的FOS是覆盖写的操作，FOS会先将文件数据全部删除，然后在开始写
	  追加写：在创建FOS时，若指定第二个参数，并且改值为true时，则是追加写，那么内容会被追加到末尾
```



### 字符流（高级流）

字符流：全部是Reader、Writer的子类，其类名是以Reader、Writer结尾，以char为读写单位
字符流使用了字节流读到一个或多个字时，**先去查指定的编码表，将查到的字符返回**

```java
public abstract class Reader  public abstract class Writer
```

转换流：可以将字节流转换为字符流处理
特点：可以按照指定的字符编码集写出字符

### InputStreamReader 类：在字符流封装了字节流

public class InputStreamReader extends Reader
	构造函数：
		InputStreamReader(InputStream in, Charset cs) 
          		创建使用指定字符集的 InputStreamReader

### OutputStreamWriter 类

public class OutputStreamWriter extends Writer
	构造函数：
		OutputStreamWriter(OutputStream out, Charset cs) 
          		创建使用给定字符集的 OutputStreamWriter
		



### 根据封装的数据将 io 分为：节点流(低级流)  +   处理流（高级流）

节点流：真实负责读写数据的流

处理流：封装节点流的，用来处理数据的，不能独立存在，高级流处理其他流就形成了流的连接，叠加的效果

常见的处理流：BufferedInputStream, BufferedOputStream , BufferedReader, BufferedWriter

注意：加缓冲流以后，在进行写的时候一定要flush()

### 缓冲流：

###  BufferedInputStream :在创建 BufferedInputStream 时，会创建一个内部缓冲区，使用其可以加快读写效率

 public class BufferedInputStream extends FilterInputStream

```java
构造方法：
	BufferedInputStream(InputStream in) 
      	 	创建一个 BufferedInputStream 并保存其参数，即输入流 in，以便将来使用
方法：
	int read(byte[] b) 
      		从此字节输入流中开始将各字节读取到指定的 byte 数组中
	void close() 
    		关闭此输入流并释放与该流关联的所有系统资源
```



###  BufferedOutputStream  ：该类实现缓冲的输出流

######  public class BufferedOutputStream extends FilterOutputStream

```java
构造方法：
	BufferedOutputStream(OutputStream out) 
    		创建一个新的缓冲输出流，以将数据写入指定的底层输出流。
方法：
	 void flush() 
      		刷新此缓冲的输出，强制缓冲区的内容一次性写出
	 void write(byte[] b) 
      		将指定 byte 数组中写入此缓冲的输出流
```



###  BufferedReader	:字符缓冲输入流，按行读取字符串

public class BufferedReader extends Reader

构造方法：
	BufferedReader(Reader in)
方法：
	int read(char[] cbuf) 
		将字符读入数组的某一部分
	String readLine() 
		读取一个文本行，全部到取到返回String中，若返回为NULL则表示读取到末尾

###  BufferedWriter :字符缓冲输出流

 public class BufferedWriter extends Writer

```java
方法：
	void write(char[] cbuf, int off, int len)
	void write(String s) 
     		写入字符串的某一部分
	void close() 
   	 		关闭此流，但要先刷新它
	void newLine() 
     		写入一个行分隔符
	void flush() 
          	刷新该流的缓冲
```




###  PrintWriter：具有自动刷新，换行功能的缓冲字符输出流，内部会创建BufferWriter作为缓冲功能的叠加

```java
//构造方法：
	PrintWriter(File file) 
      		使用指定文件创建不具有自动行刷新的新 PrintWriter。
	PrintWriter(String fileName) 
      		创建具有指定文件名称且不带自动行刷新的新 PrintWriterjava
	PrintWriter(OutputStream out, boolean autoFlush) 
      		传入其他字节流，创建新的 PrintWriter，同个第二个参数来确定具不具有行刷新

//方法：所有的println具有自动行刷功能，每当调用时，自动刷新  
	void println() 
      		通过写入行分隔符字符串终止当前行 
	void println(String x) 
      		打印 String，然后换行（终止当前行） 
	void close() 
      		关闭该流并释放与之关联的所有系统资源
```



常用流类：
   **1：ByteArrayInputStream，ByteArrayOutputStream**  

调用 ByteArrayInputStream 或 ByteArrayOutputStream 对象的 close 方法没有任何意义，这两个基于内存的流
只要垃圾回收器清理对象就能够释放资源，不同于其他流
   **2：ImageIO**



