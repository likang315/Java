### IO 流

------

##### 1：IO 操作的步骤（5步）：同步阻塞型 IO

1. 封装File对象
2. 选择io对象
3. 加缓冲
4. 进行读写操作
5. 关闭IO

![](https://github.com/likang315/Java-and-Middleware/blob/master/Java_note/7%EF%BC%9AFile%EF%BC%8CIO%E6%B5%81/photots/IO%E6%B5%81.png?raw=true)

##### 2：IO 流 ：

依据将数据是读入到内存还是从内存写出分为：输入流（读入）+ 输出流（写出）

- **输入流：**全部是 InputStream/Reader 的子类 其类名是以 InputStream/Reader 结尾，进行读的操作 
- **输出流：**全部是 OutputStream/Writer 的子类 其类名是以OutputStream/Writer结尾，进行写的操作

##### 3：字符流、字节流

- 字符流：操作二进制的数据，比如图片，视频，音乐等等，文本文件的使用
- 字节流（低级流) ：全部是InputStream、OutputStream 的子类 其类名是以 OutputStream/InputStream 结尾
  - public abstract class InputStream
  - public abstract class OutputStream

###### java.io 

##### 4：Class  FileInputStream

```java
public class FileInputStream extends InputStream{
	// 从文件系统中的某个文件中获得输入字节，文件输入流
}
```

###### 构造方法：		

- FileInputStream(File file) 
  - 通过File ，打开一个到实际文件的连接来创建一个文件输入流
- FileInputStream(String name) 
  - 通过文件路径，打开一个到实际文件的连接来创建一个文件输入流

###### 方法：

- int read(byte[] b)
  - 从此输入流中将最多 b.length 个字节的数据读入一个 byte 数组中 
- void close()
  - 关闭此文件输入流并释放与此流有关的所有系统资源

###### 	java.io    

##### 	Class  FileOutputStream

```java
public class FileOutputStream extends OutputStream{
		//文件字节输出流是用于将数据写入文件中，低级流
}
```

###### 构造方法：

- FileOutputStream(String name) 
        	  创建一个向具有指定名称的文件中写入数据的输出文件流
- FileOutputStream (File file, boolean append) 
  		 	创建一个向指定 File 对象表示的文件中写入数据的文件输出流，允许追加

###### 方法：

- void write(byte[] b) 
     - 将 b.length 个字节从指定 byte 数组写入到指定输出流对应的文件中
- void close() 
        - 关闭此文件输出流并释放与此流有关的所有系统资源

###### 注意：	

- 覆盖写：默认创建的 FOS 是覆盖写的操作，FOS会先将文件数据全部删除，然后在开始写
- 追加写：在创建FOS时，若指定第二个参数，并且改值为true时，则是追加写，那么内容会被追加到末尾

##### 5：Reader、Writer

```java
// Reader、Writer的子类，以char为读写单位
// 字符流使用字节流读到一个或多个字时，先去查指定的编码表，将查到的字符返回
public abstract class Reader {}
public abstract class Writer{}
```

###### Java.io   

#####  6：Class  InputStreamReader ：在字符流封装了字节流

```java
public class InputStreamReader extends Reader 	{
		
}
```

-  InputStreamReader(InputStream in, Charset cs) 
  - 创建使用指定字符集的 InputStreamReader

######       Java.io   

###### Class  OutputStreamWriter ：

```java
public class OutputStreamWriter extends Writer  {

}
```

##### 7：根据封装的数据将 io 分为：节点流(低级流) + 处理流（高级流）

- 节点流：真实负责读写数据的流

- 处理流：封装了节点流的，用来处理数据的，不能独立存在，高级流处理其他流就形成了流的连接

- 常见的处理流：BufferedInputStream, BufferedOputStream , BufferedReader, BufferedWriter

- ###### 注意：加缓冲流以后，在进行写的时候一定要flush()

##### 8：缓冲流

###### BufferedInputStream :

​	在创建 BufferedInputStream 时，会创建一个内部缓冲区，使用其可以加快读写效率

```java
public class BufferedInputStream extends FilterInputStream{
		// 创建一个 BufferedInputStream 并保存其参数，即输入流 in，以便将来使用
    BufferedInputStream(InputStream in) {}
    int read(byte[] b) 
    		从输入流中将各字节读取到指定的 byte 数组中
    void close() 
    		关闭此输入流并释放与该流关联的所有系统资源
}
```

###### BufferedOutputStream ：

```java
public class BufferedOutputStream extends FilterOutputStream {
		// 创建一个新的缓冲输出流，以将数据写入指定的底层输出流
    BufferedOutputStream (OutputStream out) {}
    void flush() 
      	刷新此缓冲的输出，强制缓冲区的内容一次性写出，使用缓冲流时，必须使用此强制刷新
    void write(byte[] b) 
      	将指定 byte 数组中写入此缓冲的输出流
}
```

###### BufferedReader：字符缓冲输入流，按行读取字符串

```java
public class BufferedReader extends Reader{
		BufferedReader(Reader in) 
    int read(char[] cbuf) 
    		将字符读入数组的某一部分 
    String readLine() 
    		读取一个文本行，全部读取到返回String中，若返回为NULL则表示读取到末尾
}
```

###### BufferedWriter : 字符缓冲输出流

```java
public class BufferedWriter extends Writer{
		void write(String s) 
     		写入字符串的某一部分
		void close() 
   	 		关闭此流，但要先刷新它
		void newLine() 
     		写入一个行分隔符	
		void flush() 
        刷新该流的缓冲
}
```

##### 9：PrintWriter：

​	具有**自动刷新，换行功能**的缓冲字符输出流，内部会创建 BufferedWriter 作为缓冲功能的叠加

```java
  PrintWriter(File file) 
      		使用指定文件创建不具有自动行刷新的新 PrintWriter
	PrintWriter(String fileName) 
      		创建具有指定文件名称且不带自动行刷新的新 PrintWriterjava
	PrintWriter(OutputStream out, boolean autoFlush) 
      		传入其他字节流，创建新的 PrintWriter，同个第二个参数来确定具不具有行刷新

//方法：所有的 println 具有自动行刷功能，每当调用时，自动刷新  
	void println() 
      		通过写入行分隔符字符串终止当前行 
	void println(String x) 
      		打印 String，然后换行（终止当前行） 
	void close() 
      		关闭该流并释放与之关联的所有系统资源
```

##### 10：常用流类

- ##### ByteArrayInputStream，ByteArrayOutputStream

调用 ByteArrayInputStream 或 ByteArrayOutputStream 对象的 close 方法没有任何意义，这两个基于内存的流 只要垃圾回收器清理对象就能够释放资源，不同于其他流 

- ##### ImageIO

  专门用来读取图片的流

##### 11：示例

```java
public static  void main(String[] args) {
    File fileI = new File("F:\\1.txt");
    File fileO = new File("F:\\1233.txt");
    // 选择加字节流还是字符流
    FileInputStream fi = null;
    FileOutputStream fo = null;
    // 加缓冲流
    BufferedInputStream bs = null;
    BufferedOutputStream bo = null;
    try {
        fi = new FileInputStream(fileI);
        fo = new FileOutputStream(fileO);
        bs = new BufferedInputStream(fi);
        bo = new BufferedOutputStream(fo);
        // 读写,按行读取时注意加入行分隔符
        byte[] b = new byte[1024];
        int i = bs.read(b);
        while (i != -1) {
          bo.write(b);
          bo.flush(); 		//强制刷新缓冲区
          i = bs.read(b);	//重新读取
    		}
    } catch (IOException e) {
      e.printStackTrace();
    }finally{
      bo.close();
    }
}
```

