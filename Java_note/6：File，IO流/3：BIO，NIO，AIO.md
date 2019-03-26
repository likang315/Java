### 阻塞 (Block)和 非租塞 (NonBlock)：

阻塞和非阻塞是进程在访问数据的时候，**数据是否准备就绪的一种处理方式**，当数据没有准备的时候

**读缓冲区有数据，写缓冲区后空闲字节空间**

**阻塞：**往往需要等待缓冲区中的数据准备好过后才处理其他的事情，否则**一直等待在那里**

**非阻塞：**当我们的进程访问我们的数据缓冲区的时候，如果**数据没有准备好则直接返回，不会等待**，如果数据已经准备好，读写完也直接返回



### 同步 ( Synchronization ) 和 异步 ( Async ) 

同步和异步都是**基于应用程序让操作系统处理IO事件所采用的方式**

**同步：**是应用程序要**直接参与IO读写**的操作，这时我们**不能处理其他**的事情

**异步：**所有的**IO读写交给搡作系统去处理**，应用程序只需要**等待被通知结果**，这时我们**可以处理其他**的事情



### N I O：同步非阻塞性 IO

通常将非阻塞IO的空闲时间用于在其它通道上执行IO操作，所以一个单独的线程现在可以管理多个输入和输出通道

### NIO ：缓冲区Buffer

缓冲区实际上是一个容器对象，其实就是**一个数组**，在NIO库中，所有数据都是用缓冲区处理的。在读取数据时，它是直接读到缓冲区中的； 在写入数据时，它也是写入到缓冲区中的；**任何时候访问 NIO 中的数据，都是将它放到缓冲区中**

**在NIO中，所有的缓冲区类型都继承于抽象类Buffer**，最常用的就是ByteBuffer，对于Java中的基本类型，基本都有一个具体Buffer类型与之相对应

```java
public abstract class Buffer
{
	// mark <= position <= limit <= capacity
    
    //标记：下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )函数更新。
    private int mark = -1;
    //位置：下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )函数更新
    private int position = 0;
    //上界：缓冲区的第一个不能被读或写的元素。或者说,缓冲区中现存元素的计数
    private int limit;
    //缓冲区能够容纳的数据元素的最大数量。这一个容量在缓冲区创建时被设定，并且永远不能改变。
    private int capacity; 
   
}
```



### NIO ：通道 Channel

通道是一个对象，通过它可以读取和写入数据，当然了所有数据都通过Buffer对象来处理

在NIO中，提供了多种通道对象，而**所有的通道对象都实现了Channel接口**

```java
public interface Channel extends Closeable {
    //打开通道
    public boolean isOpen();
	//关闭通道
    public void close() throws IOException;
}
```

###### 使用NIO 读写数据 ，通过channel 再到 Buffer

任何时候读取数据，都不是直接从通道读取，而是从通道读取到缓冲区

1. 从 FileInputStream 获取 Channel
2. 创建 Buffer 缓存
3. 将数据从 Channel 读取到Buffer中 或者 数据不是直接写入通道，而是写入缓冲区 

```java
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileInputProgram {  
     public static void main( String args[] ) throws Exception
     {  
        FileInputStream fin = new FileInputStream("c:\\test.txt");  
        // 获取通道  
        FileChannel fc = fin.getChannel();  
        // 创建缓冲区  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        // 读取数据到缓冲区  
        fc.read(buffer);  
	    //将写模式转换成读模式
        buffer.flip();  
          
        while (buffer.remaining() > 0) 
        {  
            byte b = buffer.get();  
            System.out.print(((char)b));  
        }  
        fin.close();
    }  
}
```



### NIO ：反应堆

事件驱动机制：当有事件到的时候触发反应堆，而不是同步的去监视事件， 反应堆中有一个专门的线程来处理所有的IO事件，并负责分发给相应的线程处理

### NIO： 选择器（Selector)

Java NIO的选择器**允许一个单独的线程来监视多个输入通道**，你可以注册多个通道使用一个选择器，然后使用一个单独的线程来“选择"通道：当某个通道里已经有可以处理的输入，或者选择已准备写入的通道，就会选怿，这种机制，使得一个单独的线程很容易来管理多个通道



### BIO、NIO 和 AIO 的 区别

Java BIO ： 同步阻塞，客户端有连接请求时服务器端就需要启动一个线程进行处理，这个连接不做任何事情

Java NIO ： 同步非阻塞，客户端发送的连接请求都会**注册到多路复用器**上，多路复用器**轮询到连接有I/O请求时才启动一个线程进行处理**

Java AIO： 异步非阻塞，服务器实现模式为一个有效请求一个线程，**客户端的I/O请求都是由OS先完成了再通知服务器应用去启动线程进行处理**



