### 多线程  ：

------

##### 1：进程（process）：是操作系统中能独立运行并作为资源分配的基本单位，由核数决定

1. ###### 进程就是一个运行中的程序																			

   - 程序是一个没有生命的实体，只有处理器赋予程序生命时（操作系统执行之），它才能成为一个活动的实体

2. ###### 进程是一个实体

   - 每一个进程都有它自己的地址空间，一般包括文本区域（text region）、数据区域（data region）和堆栈（stack region）
     - 文本区域：存储处理器执行的代码
     - 数据区域：存储变量和进程执行期间使用的动态分配的内存
     - 堆栈区域：存储着活动过程调用的指令和本地变量

3. ###### 进程：分为系统进程和用户进程

   - 系统进程：用于完成操作系统各种功能的进程
     - 内核态: CPU可以访问内存所有数据, 包括外围设备, 例如硬盘, 网卡. CPU也可以将自己从一个程序切换到另一个程序
   - 用户进程：由用户启动的进程
     - 用户态: 只能受限的访问内存, 且不允许访问外围设备. 占用CPU的能力被剥夺, CPU资源可以被其他程序获取

##### 2：线程（thread）：线程是 cpu 调度的最小单位

- 标准的线程由线程ID，当前指令指针(PC），寄存器集合和堆栈组成										
- 进程中包含一组线程，同属一个进程的其它线程共享进程所拥有的全部资源，线程切换开销小
- 线程只能归属于一个进程，并且只能访问该进程所拥有的资源
- 当操作系统创建一个进程后，该进程会自动申请一个名为主线程的线程（master）

##### 3：协程：

​	其执行过程更类似于子程序，或者说不带返回值的函数调用，在单线程里实现多任务的调度，并在单线程里维持多个任务间的切换

##### 4：多线程：在同一个时间段，计算机系统中如果允许两个或两个以上的线程处于运行状态

- 总进程数<= CPU数量：并行运行																			
- 总线程数>   CPU数量：并发运行
- Java程序天生就是多线程程序，因为执行main()方法的是一个名称为main的线程，而执行一个简单的Java程序 不仅仅是main( )的运行，而是main( ) 和多个线程同时在运行
  - Signal Dispatcher：分发处理发送给JVM信号的线程
  - Finalizer：调用对象finalize方法的线程
  - Reference Handler：清除Reference的线程
  - main线程，用户程序入口

###### 多线程并发原理：

​	感观上多个线程能同时运行的技术,事实上线程并发运行时，OS将CPU时间分为很多时间片段（时间片），尽可能的均匀分配给每一个线程，获取时间片段的线程被CPU运行而其他线程全部等待，这种现象称为并发

##### 5：线程优先级

- 线程的优先级：线程默认优先级为5，范围是1-10，值越大优先级越高
- 线程默认的优先级都与创建它的父线程具有相同的优先级，在默认情况下，主线程具有普通优先级：5
- 设置线程优先级时，为了保证CPU不会被独占，针对频繁阻塞（休眠或者I/O操作）的线程需要设置较高优先级，而偏重计算（需要较多CPU时间或者偏运算）的线程则设置较低的优先级
- 某些操作系统可能会忽略线程优先级的设定，默认都为一样的优先级
- 现今的操作系统基本采用时分的形式调度运行的线程，每个线程会分配若干个时间片，当线程时间片用完了，就会发生线程调度，期待下次时间片的分配，而且线程的切换是由CPU的调度(轮转时间片)控制的，不能被干预，但是可以通过提高线程的优先级来提高获取时间片的概率，从而提高CPU在此线程上的执行时间

##### 6：线程的五种状态

- NEW：新建状态，当线程对象对创建后，即进入了新建状态
- RUNNABLE：就绪状态，当调用线程对象的start()方法，线程即进入就绪状态
  - 处于就绪状态的线程，说明此线程已经做好了准备，随时等待CPU调度执行
- RUNNING：运行状态，当CPU开始调度处于就绪状态的线程时，此时线程才得以真正执行，即进入到运行状态
- BLOCKED：阻塞状态， 线程在获取synchronized同步锁失败，它会进入同步阻塞状态直到其进入到就绪状态，才有机会再次被CPU调用以进入到运行状态
- WAITING：等待状态，进入等待状态的线程需要等待其他线程做出一些特定的动作（通知或中断）
  - 等待阻塞：运行状态中的线程执行wait()方法，使本线程进入到等待阻塞状态
  - 其他阻塞：通过调用线程的sleep()或join()或发出了I/O请求时，线程会进入到阻塞状态
- TIME-WAITING：超时等待状态，它是可以在指定的时间内返回的线程
  - 超时等待阻塞：调用wait(long timeout)
- TERMINATED：终止状态，线程执行完了或者因异常退出了run()方法，该线程结束生命周期

##### 6：Daemon线程

​	也称守护线程，是一种支持型线程，因为它主要用于**程序中后台调度以及支持性工作**，这意味着，当一个Java虚拟机中不存在非Daemon线程的时候，Java虚拟机将会退出，守护线程则会立即终止

- Daemon属性需要在启动线程之前设置，不能在启动线程之后设置
- 设置方式：Thread.setDaemon(true)
- main线程（非Daemon线程）在启动了线程DaemonRunner之后随着main方法执行完毕而终止，而此时Java虚拟机中已经没有非Daemon线程，虚拟机需要退出。Java虚拟机中的所有Daemon线程都需要立即终止，因此DaemonRunner立即终止，但是DaemonRunner中的finally块并没有执行

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

##### 7：构造线程的过程

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
    // 将daemon、priority属性设置为父线程的对应属性
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

##### 8：中断操作

​	API中声明许多抛出InterruptedException的方法（例如Thread.sleep(long millis)方法）这些方法在抛出InterruptedException之前，Java虚拟机会先将该线程的中断标识位清除，然后抛InterruptedException，此时调用isInterrupted()方法将会返回false

- 通过isInterrupted()来进行判断是否被中断
- 通过调用静态方法Thread.interrupted() ，对当前线程的中断标识位进行复位

###### 安全的中断任务操作

​	main线程通过中断操作和cancel()方法均可使CountThread得以终止，这种通过标识位或者中断操作的方式能够使线程在终止时有机会去清理资源，而不是武断地将线程停止

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

##### 9：线程间通信

​	因为多个线程需要相互配合完成工作，提高任务执行效率

1. 共享内存
   - 在共享内存的并发模型里，线程之间共享程序的公共状态，通过写-读内存中的公共状态进行隐式通信
   - 锁：多个线程需要同时访问同一个方法时，谁获取了锁（synchronized），谁先执行
2. 消息传递
   - wait / notify机制：Object 类的方法
     - wait()：当线程执行 wait() 时，会把当前的锁资源释放，让出CPU（时间片），进入就绪状态
     - notify()：任意唤醒一个处于等待获取该对象锁的线程，然后继续往下执行，直到执行完退出对象锁锁住的区域（synchronized块）后再释放锁
     - notifyAll()：会唤醒所有处于等待该对象锁的线程





##### 6：线程的调度算法

- 先来先服务（FIFS）
  - 并不能保证紧急的任务优先处理
- 短作业优先调度
  - 虽然节省了任务执行时间，但是并不能保证紧急的任务优先处理
- 优先级调度算法：在线程等待队列中选择优先级最高的来执行
  - 保证了紧急任务的执行，但是多个紧急的任务执行时，并没有考虑他的效率
- 时间片轮转算法
  - 导致任务可能没有执行完，就会被切换，线程间的切换，唤醒非常浪费资源
- 多级反馈队列调度算法：把时间轮转与优先级调度相结合，把进程按优先级分成的队列，先按照优先级调度，优先级相同的，按照时间片轮转
  - 既保证了紧急的任务优先处理，又保证了任务执行的效率

##### 7：两种线程调度模型：Java使用的是抢占式调度模型

1. 分时调度模型：所有线程轮流使用 CPU 的使用权，平均分配 CPU 的时间片给每个线程占用
2. 抢占式调度模型：优先让优先级高的线程使用 CPU，如果线程的优先级相同，那么会随机选择一个，优先级高的线程获取的 CPU 时间片相对多一些
   - 默认使用

##### 10：进程间通信方式

1. 管道(pipe)：管道是一种半双工的通信方式，数据只能单向流动，而且只能在具有亲缘关系的进程间使用
   - 进程的亲缘关系通常是指父子进程关系
2. 消息队列(MessageQueue)：消息队列是由消息的链表，存放在内核中并由消息队列标识符标识。消息队列克服了信号传递信息少、管道只能承载无格式字节流以及缓冲区大小受限等缺点
3. 共享存储(SharedMemory)：共享内存就是映射一段能被其他进程所访问的内存，这段共享内存由一个进程创建，但多个进程都可以访问
4. 信号量 Semaphore(P,V)：信号量是一个计数器，可以用来控制多个进程对共享资源的访问。它常作为一种锁机制，防止某进程正在访问共享资源时，其他进程也访问该资源。因此，主要作为进程间以及同一进程内不同线程之间的同步手段