### 多线程  ：

------

[TOC]

##### 01：进程（process）：是操作系统中能独立运行并作为资源分配的基本单位，由核数决定

1. ###### 进程就是一个运行中的程序																			

   - 程序是一个没有生命的实体，只有处理器赋予程序生命时（操作系统执行之），它才能成为一个活动的实体

2. ###### 进程是一个实体

   - 每一个进程都有它自己的地址空间，一般包括文本区域（text region）、数据区域（data region）和堆栈（stack region）
     - 文本区域：存储处理器执行的代码
     - 数据区域：存储变量和进程执行期间使用的动态分配的内存
     - 堆栈区域：存储着活动过程调用的指令和本地变量

3. ###### 进程：分为系统进程和用户进程

   - 系统进程：用于完成操作系统各种功能的进程
     - 内核态: CPU可以访问内存所有数据, 包括外围设备, 例如硬盘, 网卡；
   -  用户进程：由用户启动的进程
     - 用户态: 只能受限的访问内存, 且不允许访问外围设备. 占用CPU的能力可以被剥夺, CPU资源可以被其他程序获取；

##### 02：线程（thread）：线程是 cpu 调度的最小单位

- 标准的线程由线程ID，当前指令指针(PC），寄存器集合和堆栈组成										
- 进程中包含一组线程，同属一个进程的其它线程共享进程所拥有的全部资源，线程切换开销小
- 线程只能归属于一个进程，并且只能访问该进程所拥有的资源
- 当操作系统创建一个进程后，该进程会**自动申请一个名为主线程的线程（master）**

##### 03：协程（coroutine）：

- **类似于子程序，但是又可以被中断去执行别的子程序，在适当的时候再返回来接着执行**。
- 协程极高的执行效率。因为**子程序切换不是线程切换，而是由程序自身控制**，因此，没有线程切换的开销。
- 不需要多线程的锁机制，因为只有一个线程，也不存在同时写变量冲突，在协程中控制共享资源不加锁，只需要判断状态就好了，所以执行效率比多线程高很多。

##### 04：守护线程 （Daemon）

​	也称守护线程，是一种支持型线程，因为它主要用于**程序中后台调度以及支持性工作**，这意味着，当一个Java虚拟机中不存在非Daemon线程的时候，Java虚拟机将会退出，守护线程则会立即终止；

- Daemon属性需要在启动线程之前设置，不能在启动线程之后设置；
- 设置方式：Thread.setDaemon(true)
- main线程（非Daemon线程）在**启动了线程DaemonRunner**之后随着main方法执行完毕而终止，而此时Java虚拟机中已经没有非Daemon线程，虚拟机需要退出。Java虚拟机中的所有Daemon线程都需要立即终止，因此**DaemonRunner立即终止**，但是DaemonRunner中的finally块并没有执行；

```java
public class Daemon {
    public static void main(String[] args) {
      Thread thread = new Thread(new DaemonRunner(), "DaemonRunner");
      thread.setDaemon(true);
      thread.start();
    }
    static class DaemonRunner implements Runnable {
        @Override
        public void run() {
            try {
              Thread.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            } finally {
              System.out.println("DaemonThread finally run.");
            }
        }
     }
}
```

##### 05：多线程（multi-thread）：

- 在同一个**时间段**，计算机系统中如果**允许两个或两个以上的线程处于运行状态**
- 总进程数<= CPU数量：并行运行
- 总进程数 >  CPU数量：并发运行
- Java程序天生就是多线程程序，因为执行main()方法的是一个名称为main的线程，而执行一个简单的Java程序 不仅仅是main( )的运行，而是main( ) 和多个线程同时在运行；
  - Signal Dispatcher：分发处理发送给JVM信号的线程
  - Finalizer：调用对象finalize方法的线程
  - Reference Handler：清除Reference的线程
  - main线程：用户程序入口

###### 多线程并发原理：

- 感观上多个线程能同时运行的技术，事实上线程并发运行时，OS将CPU时间分为很多时间片段（时间片），尽可能的均匀分配给每一个线程，第一个获取时间片段的线程被CPU运行而其他线程全部等待，这种现象称为并发。
- JVM执行字节码，最终需要转化为汇编指令在CPU上执行，Java中所使用的并发机制依赖于JVM的实现和CPU的指令；

##### 06：线程优先级

- 线程的优先级：线程**默认优先级为5**，范围是1-10，值越大优先级越高；
- 线程默认的优先级都与创建它的父线程具有相同的优先级，在默认情况下，主线程具有普通优先级：5
- 设置线程优先级时，为了保证CPU不会被独占，针对频繁阻塞（休眠或者I/O操作）的线程需要设置较高优先级，而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较低的优先级
- 某些操作系统可能会忽略线程优先级的设定，默认都为一样的优先级
- 现今的操作系统基本采用**时分的形式调度运行的线程**，每个线程会分配若干个时间片，当线程时间片用完了，就会发生线程切换，期待下次时间片的分配，而且线程的切换是由CPU的调度(轮转时间片)控制的，不能被干预，但是可以通过**提高线程的优先级来提高获取时间片的概率**，从而提高CPU在此线程上的执行时间；

##### 07：线程的五种状态

- **NEW：新建状态**，当线程对象对创建后，即进入了新建状态
- **RUNNABLE：就绪状态**，当调用线程对象的start()方法，线程即进入就绪状态
  - 处于就绪状态的线程，说明此线程已经做好了准备，随时等待CPU调度执行
- **RUNNING：运行状态**，当CPU开始调度处于就绪状态的线程时，此时线程才得以真正执行，即进入到运行状态
- **BLOCKED：阻塞状态**， 线程在获取synchronized同步锁失败，它会进入同步阻塞状态直到其进入到就绪状态，才有机会再次被CPU调用以进入到运行状态
  - WAITING：等待状态，进入等待状态的线程需要等待其他线程做出一些特定的动作（通知或中断）
    - 等待阻塞：运行状态中的线程执行wait()方法，使本线程进入到等待阻塞状态
    - 其他阻塞：通过调用线程的sleep()或join()或发出了I/O请求时，线程会进入到阻塞状态
  - TIME-WAITING：超时等待状态，它是可以在指定的时间内返回的线程
    - 超时等待阻塞：调用wait(long timeout)
- **TERMINATED：终止状态**，线程执行完了或者因异常退出了run()方法，该线程结束生命周期

##### 08：构造线程的过程

```java
private void init(ThreadGroup g, Runnable target, String name,long stackSize,
                  AccessControlContext acc) {
    if (name == null) {
        throw new NullPointerException("name cannot be null");
    }
    // 当前线程就是该线程的父线程
    Thread parent = currentThread();
    // 设置线程组
    this.group = g;
    // 将 daemon、priority属性设置为父线程的对应属性
    this.daemon = parent.isDaemon();
    this.priority = parent.getPriority();
    this.name = name.toCharArray();
    this.target = target;
    setPriority(priority);
    // 设置contextClassLoader以及可继承的ThreadLocal，将父线程InheritableThreadLocal复制过来
    if (parent.inheritableThreadLocals != null)
        this.inheritableThreadLocals=ThreadLocal.createInheritedMap(
        parent.inheritableThreadLocals);
    // 分配一个线程ID
    tid = nextThreadID();
}
```

##### 09：中断操作

​	API中声明许多抛出InterruptedException的方法（例如Thread.sleep(long millis)方法）这些方法在抛出InterruptedException之前，Java虚拟机会先将**该线程的中断标识位清除**，然后抛InterruptedException，此时调用isInterrupted()方法将会返回false；

- 通过isInterrupted()来进行判断是否被中断；
- 通过调用静态方法Thread.interrupted() ，对当前线程的**中断标识位进行复位**；

###### 安全的中断任务操作

​	main线程通过**中断操作和cancel()方法均可使CountThread得以终止**，这种通过**标识位或者中断操作的方式**能够使线程在终止时有机会去清理资源，而不是武断地将线程停止；

```java
public class Shutdown {
    public static void main(String[] args) throws Exception {
        Runner one = new Runner();
        Thread countThread = new Thread(one, "CountThread");
        countThread.start();
        // 睡眠1秒，main线程对CountThread进行中断，使CountThread能够感知中断而结束
        TimeUnit.SECONDS.sleep(1);
        countThread.interrupt();
        Runner two = new Runner();
        countThread = new Thread(two, "CountThread");
        countThread.start();
        // 睡眠1秒，main线程对Runner two进行取消，使CountThread能够感知on为false而结束
        TimeUnit.SECONDS.sleep(1);
        two.cancel();
    }
    private static class Runner implements Runnable {
        private long i;
        private volatile boolean on = true;
        @Override
        public void run() {
            while (on && !Thread.currentThread().isInterrupted()) {
                i++;
            }
            System.out.println("Count i = " + i);
        }
        public void cancel() {
            on = false;
        }
    }
}
```

##### 10：线程间通信

​	因为多个线程需要相互配合完成工作，提高任务执行效率

1. ###### 共享内存

   - 在共享内存的并发模型里，**线程之间共享程序的公共状态**，通过写-读内存中的公共状态进行隐式通信；

   - **锁**：在同一个时刻，只能有一个线程处于方法或者同步块中，它保证了线程对变量访问的可见性和排他性；

   - **volatile：**保证所有线程对变量访问的可见性；

   - 管道输入-输出流【进程间管道通信】

     - 主要用于线程之间的数据传输，而传输的媒介为内存

     - 主要包括4种具体实现：PipedOutputStream、PipedInputStream、PipedReader和PipedWriter

       ```java
       // 正是因为这种方式的线程通信方式，CPU一直运行，开销太大，出现了等待-通知模式
       public class Piped {
           public static void main(String[] args) throws Exception {
               PipedWriter out = new PipedWriter();
               PipedReader in = new PipedReader();
               // 将输出流和输入流进行连接，否则在使用时会抛出IOException
               out.connect(in);
               Thread printThread = new Thread(new Print(in), "PrintThread");
               printThread.start();
               int receive = 0;
               try {
                   while ((receive = System.in.read()) != -1) {
                       out.write(receive);
                   }
               } finally {
                   out.close();
               }
           }
           static class Print implements Runnable {
               private PipedReader in;
               public Print(PipedReader in) {
                   this.in = in;
               }
               public void run() {
                   int receive = 0;
                   try {
                       while ((receive = in.read()) != -1) {
                           System.out.print((char) receive);
                       }
                   } catch (IOException ex) {
       
                   }
               }
           }
       }
       ```

2. ###### 消息传递

   - 一个线程修改了一个对象的值，而另一个线程感知到了变化，然后进行相应的操作，整个过程开始于一个线程（生产者），而最终执行又是另一个线程（消费者）

   - 等待通知机制（生产者-消费者模型）：等待通知机制的相关方法是任意Java对象都具有的Object类

     - 是指**一个线程A调用了对象O的wait()方法**进入等待状态，而**另一个线程B调用了对象O的notify()或者notifyAll()方法**，线程A收到通知后从对象O的wait()方法返回，进而执行后续操作。上述**两个线程通过对象O来完成交互**，用来完成等待方和通知方之间的交互工作；
     - wait()：当线程执行 wait() 时，会让出CPU，释放当前对象的锁资源，进入WAITING状态
     - wait(long)：超时等待一段时间，毫秒，超时后，若是没有通知则返回
     - notify()：任意通知一个处于等待获取该对象锁的线程，使其从wait（）中返回，**但返回的前提是该线程获取了对象的锁然后继续往下执行**
     - notifyAll()：通知所有处于等待该对象锁的线程；
  - 调用时**并不会真正释放对象锁**，必须等到synchronized方法或者语法块执行完才真正释放锁；
    
     ```java
     /**
      * @author kangkang.li@qunar.com
      * @date 2021-04-20 16:27
      */
     public class WaitNotify {
         // 共享资源
         static Object lock = new Object();
         public static void main(String[] args) throws Exception {
             Thread waitThread = new Thread(new Wait(), "WaitThread");
             waitThread.start();
             TimeUnit.SECONDS.sleep(1);
             Thread notifyThread = new Thread(new Notify(), "NotifyThread");
             notifyThread.start();
         }
     
         // 消费者，吃饭时排位
         static class Wait implements Runnable {
             @Override
             public void run() {
                 // 加锁，拥有lock的Monitor
                 synchronized (lock) {
                     try {
                         System.out.println(Thread.currentThread()
                                            + " flag is true.wait@ "
                                            + new SimpleDateFormat("HH:mm:ss")
                                            .format(new Date()));
                         // 线程通知并且释放锁资源后才从wait中返回，底层会重新获取锁资源，从当前开始执行
                         lock.wait();
                         System.out.println(Thread.currentThread()
                                            + " restart@ "
                                            + new SimpleDateFormat("HH:mm:ss")
                                            .format(new Date()));
                     } catch (InterruptedException e) {
     
                     }
                     // 条件满足时，完成工作
                     System.out.println(Thread.currentThread()
                                        + " flag is false.running@ "
                                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                 }
             }
         }
     
         // 生产者
         static class Notify implements Runnable {
             @SneakyThrows
             @Override
             public void run() {
                 // 加锁，拥有lock的Monitor
                 synchronized (lock) {
                     // 获取lock的锁，然后进行通知，通知时不会释放lock的锁
                     // 直到当前线程释放了lock后，WaitThread才能从wait方法中返回
                     System.out.println(Thread.currentThread()
                                        + " hold lock. notify @ "
                                        + new SimpleDateFormat("HH:mm:ss").format(new Date()));
                     lock.notifyAll();
                     // 测试是否notify是否会释放锁资源
                     Thread.sleep(10000);
                 }
             }
         }
     }
     ```

   ###### 等待-通知经典范式

   ```java
   // 消费者
   synchronized (对象) {
       对象.wait();
       对应的处理逻辑
   }
   // 生产者
   synchronized (对象) {
       改变条件
       对象.notifyAll();
   }
   ```

##### 11：进程间通信

1. **管道(pipe)：**管道是一种半双工的通信方式，数据只能**单向流动**，而且只能在**具有亲缘关系的进程间使用**
   - 进程的亲缘关系通常是指父子进程关系
   - ⽗进程往管道⾥写，⼦进程从管道⾥读，管道是⽤环形队列实现的，数据从写端流⼊从读端流出。
2. **消息队列(MessageQueue)：**消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点；
3. **共享存储(SharedMemory)：**共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建，但多个进程都可以访问；
4. **信号量 Semaphore(P,V)：**信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段;
5. **信号 ( sinal ) ：** 信号是一种比较复杂的通信方式，用于通知接收进程某个事件已经发生。
6. **套接字( socket ) ：** 套接口也是一种进程间通信机制，与其他通信机制不同的是，它可用于不同机器间的进程通信【socket-serverSocket】。

##### 12：进程调度

1. 先来先服务（FIFS）
   - 并**不能保证紧急的任务优先处理**
2. 短作业优先调度
   - 虽然节省了任务执行时间，但是并不能保证紧急的任务优先处理
3. 优先级调度算法：在线程等待队列中选择优先级最高的来执行
   - 保证了紧急任务的执行，但是多个紧急的任务执行时，并没有考虑他的效率
4. 时间片轮转算法
   - 导致任务可能没有执行完，就会被切换，进程间的切换，唤醒非常浪费资源
5. **多级反馈队列调度算法**：把**时间片轮转与优先级调度相结合**，把进程按优先级分成的队列，先按照优先级调度，优先级相同的，按照时间片轮转
   - **既保证了紧急的任务优先处理，又保证了任务执行的效率**

##### 13：线程调度

1. **时分调度模型：**所有线程轮流使用 CPU 的使用权，平均分配 CPU 的时间片给每个线程占用
2. **抢占式调度模型：**优先让优先级高的线程使用 CPU，如果线程的优先级相同，那么会随机选择一个，优先级高的线程获取的 CPU 时间片相对多一些
   - 默认使用，**Java使用的是抢占式调度模型**；
