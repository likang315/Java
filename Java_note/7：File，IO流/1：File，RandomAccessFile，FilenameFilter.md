### IO

------

- IO 通道：操作系统中用来传输读，写的信号
- IO：对磁盘数据读写信号
  - URL："/" ：根目录，"." ：当前目录，"/"：层级的分隔符，默认路径就是当前路径

##### Java IO：

- Java.io   包中 阻塞型 IO
- Java.nio 包中的非阻塞型IO，通常称为 New IO
- IO 流：把内存中的数据写到磁盘上(输出流) 或者把磁盘上的数据读到内存中的操作（输入流）

##### 1：java.io. File：既可以表示一个文件，也可以表示一个目录

###### 作用：

1. 可以访问其文件的一些属性（如：文件名，大小），但是不能访问文件的内容
2. 创建，删除一个文件或目录

###### Constructors：

- File (String pathname) ：通过将给定路径名字符串转换为抽象路径名来创建一个新 File 实例
- File (URI uri)：通过将给定的 file: URI 转换为一个抽象路径名来创建一个新的 File 实例

###### Method：

- boolean exists()  ：测试此抽象路径名表示的文件或目录是否存在 
- boolean createNewFile( ) ：不存在具有此抽象路径名指定名称的文件时，则创建一个新的空文件 
- boolean delete() ：删除此抽象路径名表示的文件或目录
- String getName() ：返回由此抽象路径名表示的文件或目录的名称
- String getPath() ：将此抽象路径名转换为一个路径名字符串 
- long length() ：返回由此抽象路径名表示的文件的长度 
- String[] list()：获取目录下的所有File对象名称所组成的字符串数组

###### java.io 

##### 2：Interface  FilenameFilter（文件过滤器）

```java
@FunctionalInterface
public interface FilenameFilter {
    // dir表示指定目录, name表示指定文件名
    boolean accept(File dir, String name);
}
```

###### 实现 FilenameFilter 接口，重写 accept 函数，使用 endwiths 或 startswith 过滤

```java
// 获取目录下的所有File对象的抽象路径名所组成的文件数组
File[] listFiles() 
// 根据File对象的名称进行过滤
File[] listFiles(FilenameFilter filter) 
@Override
public boolean accept(File dir, String name) {
		return name.endsWith(".java");
		// 通过返回值来控制指定目录下面的文件和文件夹的显示，只显示.java文件
}
```

###### Java.io    

##### 3：Class  RandomAccessFile：

专门用来读取文本数据的，不需要流直接可以读写

RAF：是基于指针进行读写的，即 RAF总是在指针指向的位置读写字节，并且读写后指针会自动向后移动，**给每个该对象包含一个记录指针**，既可以读取数据，也可以写入文件数据

- RandomAccessFile(File file, String mode) ：创建从中读取和向其中写入（可选）的随机访问文件流，该文件由 File 参数指定**，Mode：读写模式，"r" ,"rw"** 

- RandomAccessFile(String name, String mode) ：创建从中读取和向其中写入（可选）的随机访问文件流，该文件具有指定路径名称 

  

- void seek(long pos) ：设置此文件开头测量到的文件指针偏移量，在该位置发生下一个读取或写入操作
- void write（byte[] data）:一次性将给定的字节型数组的所有字节写出 
- void write（byte[] data,int start,int len） : 将给定数组中从下标start开始，连续读取len个字节一次性写出
- int read（byte[] data）:一次性尝试读取给定的字节型数组总长度的字节量（1024 * ...）并存入到数组中， 返回值为实际读取到的字节量，若返回值为 -1，则表示没有读取到任何字节，文件末尾

