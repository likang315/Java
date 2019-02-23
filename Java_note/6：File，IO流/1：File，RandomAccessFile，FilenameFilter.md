IO通道：操作系统中用来传输读，写的信号
IO：对磁盘数据读写信号

# Java IO：	

?	1：Java.io  包中堵塞型IO
?	2：Java.nio 包中的非堵塞型IO，通常称为New IO

Java IO ：java.io 包下的类和接口来支持，用流来描述了IO过程

IO流：操作对象是内存，是外部设备与内存之间数据交互的过程,因为IO流不能操作磁盘上的数据,因此提供File类进行中间的桥梁

URL："." :根目录，"/":层级的分隔符，默认路径就是当前路径

## 1：java.io. File：既可以表示一个文件也可以表示一个目录

?	作用：1：可以访问其文件的一些属性（如：文件名，大小），但是不能访问文件的内容
?      	      	   2：创建，删除一个文件或目录

Constructors：
	File(String pathname)           ------------通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例
	File(URI uri)                   ------------通过将给定的 file: URI 转换为一个抽象路径名来创建一个新的 File 实例 

Method：
	boolean exists() 
        	测试此抽象路径名表示的文件或目录是否存在
	boolean createNewFile() 
          	当且仅当不存在具有此抽象路径名指定名称的文件时，不可分地创建一个新的空文件 	  
	boolean delete() 
          	删除此抽象路径名表示的文件或目录
	String getName() 
          	返回由此抽象路径名表示的文件或目录的名称
	String getPath() 
		将此抽象路径名转换为一个路径名字符串 
	long length() 
		返回由此抽象路径名表示的文件的长度
	
			  
重点掌握方法：
		获取目录下的所有File对象名称所组成的字符串数组
		String[] list()
	        //过滤器，但要实现FilenameFilter接口，重写accept函数，使用endwiths或startswith
		String[] list(FilenameFilter filter)
		

 		获取目录下的所有File对象的抽象路径名所组成的文件数组
		 File[] listFiles()  
		 File[] listFiles(FileFilter filter)     //根据File对象的类型过滤，可以是文件或目录
		 File[] listFiles(FilenameFilter filter) //根据File对象的名称进行过滤

```java
  删除目录其所有子项时用递归
	public void delete(File file)
	{		  
		  if（file.isDirectory）
		  {
			File[] subs = file.listFiles();
			for(File sub:subs)
			{
				delete(sub);	
			}
		     	file.delete();
		  }
	}
		  
```

?		  

## 2：Java.io.RandomAccessFile：专门用来读取文件数据的，不需要流直接可以读写

RAF：是基于指针进行读写的，即RAF总是在指针指向的位置读写字节，并且读写后指针会自动向后移动，给每个该对象包含一个记录指针
     既可以读取数据，也可以写入文件数据

构造方法：
	RandomAccessFile(File file, String mode) 
          创建从中读取和向其中写入（可选）的随机访问文件流，该文件由 File 参数指定，Mode：读写模式，"r" ,"rw"
	RandomAccessFile(String name, String mode) 
          创建从中读取和向其中写入（可选）的随机访问文件流，该文件具有指定名称。 
方法：
	int read（）：每次读取一个字节，读完返回-1
	int read（byte[] data）:一次性尝试读取给定的字节型数组总长度的字节量（1024 * ...）并存入到数组中，
				返回值为实际读取到的字节量，若返回值为 -1，则表示没有读取到任何字节，文件末尾	  
	

void write（byte[] data）:一次性给定的字节型数组的所有字节写出
void write（byte[] data,int start,int len） : 将给定数组中从下标start开始，连续读取len个字节一次性写出
	
void seek(long pos) 
      设置此文件开头测量到的文件指针偏移量，在该位置发生下一个读取或写入操作		  