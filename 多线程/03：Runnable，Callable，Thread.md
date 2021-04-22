###  构建线程

------

[TOC]

##### 01：继承Thread类，重写run() 

- 启动线程指调用start()，而并不是调用run()，当线程的start()被调用后，线程进入Runnable状态，等待获取cpu,一旦获取CPU时间片，run()自动被调用，即运行程序；


###### 缺点：

- 由于Java单继承，当继承了 Thread 类后就无法再继承其它类
- 由于继承了 Thread 后重写run()规定了线程执行的任务，这导致线程与任务有一个必然的耦合关系，不利于线程的重用；

```java
Thread thread = new Thread() {
    @Override
    public void run() {
			for (int i = 0; i < 100; i++) {
           System.out.println(i);
      }
    }
};
// 调用处于就绪状态，不允许显示创建线程，使用线程池，否则会创建大量同类的线程；
thread.start();
```

##### 02：实现 Runnable 接口，重写run（）方法

```java
// 使用匿名内部类
Runnable runnable = new Runnable() {
  @Override
  public void run() {
      long startTimestamp = System.currentTimeMillis();
    	// 处理我那业务通知其他线程
      this.notifyAll(); 
      long time = System.currentTimeMillis() - startTimestamp;
  }
};
// 执行线程
executorServices.execute(runnable);
```

##### 03：实现 Callable 接口，重写call()，并且有返回值，可抛出异常

```java
import java.util.concurrent.Callable;  
import java.util.concurrent.ExecutionException;  
import java.util.concurrent.FutureTask;
public class CallableThreadTest implements Callable<List<String>> {
  // 每个线程最多执行1s
  private static final long TASK_TIME_OUT = 1000ms;
  // 实例化此类时，就可以携带参数
  private String name;

  @Override  
  public List<String> call() throws Exception {
    // 学习携带参数的方法
    return new ArrayList<String>.add(name);  
  }

  // 学习回收结果的方法
  public static void main(String[] args) {  
    // 存储每个线程执行任务的返回值
    List<List<String>> futureTask = new ArrayList<>();
    CallableThreadTest task = new CallableThreadTest("param");  
    Future<List<String>> future = executorService.submit(task);
    futureTask.add(future);

    // 取值过程
    for (int i = 0; i < futureTask.size(); i++) {
      Future<List<String>> futureReceive = futureTask.get(i);
      List<String> futureValue  =
        futureReceive.get(TASK_TIME_OUT, Time.Unit.MILLISECONDS);
      System.out.println(futureValue);
    }
  }
}
```

###### java.lang

##### 04：Class  Thread

​	public class Thread  implements Runnable

###### 属性：

| Modifier and Type | Field and Description           |
| :---------------- | :------------------------------ |
| static int        | MAX_PRIORITY：返回最大优先级    |
| static int        | MIN_PRIORITY：返回最小优先级    |
| static int        | NORM_PRIORITY：返回默认的优先级 |
| static class      | Thread.state：返回线程的状态类  |

###### 构造方法：

- Thread() ：分配新的 Thread 对象 
- Thread(Runnable target) ：分配新的 Thread 对象
- Thread(Runnable target, String name) ：线程名

###### 方法:

- void notify()：通知线程
  
  - 仅仅任意通知一个处于阻塞的线程，不释放锁资源，执行体运行完成之后释放。
- void join(long millis) 
  - join( )：默认等待 0 毫秒
  
  - 执行调用 join() 的线程进入 TIMED_WAITING 状态，等待 join() 所属线程运行结束后再继续运行，底层调用Object.wait()
  
  - 如果一个线程A执行了thread.join()语句，其含义是：**当前线程A等待thread线程终止之后才从thread.join()返回**。线程Thread除了提供join()方法之外，还提供了join(long millis)和join(longmillis,int nanos)两个具备超时特性的方法，如果线程thread在给定的超时时间里没有终止，那么将会从该超时方法中返回；
  
    ```java
    // 创建了10个线程，编号0~9，每个线程调用前一个线程的join()方法，也就是线程0结束了，线程1才能从join()方法中返回，而线程0需要等待main线程结束
    public class Join {
        public static void main(String[] args) throws Exception {
            Thread previous = Thread.currentThread();
            for (int i = 0; i < 10; i++) {
                // 每个线程拥有前一个线程的引用，需要等待前一个线程终止，才能从等待中返回
                Thread thread = new Thread(new Domino(previous), String.valueOf(i));
                thread.start();
                previous = thread;
            }
            System.out.println(Thread.currentThread().getName() + " terminate.");
        }
        static class Domino implements Runnable {
            private Thread thread;
            public Domino(Thread thread) {
              	this.thread = thread;
            }
            @Override
            public void run() {
                try {
                  thread.join();
                } catch (InterruptedException e) {
                }
                System.out.println(Thread.currentThread().getName() + " terminate.");
            }
        }
    }
    ```
  
- **static** void sleep(long millis) ：线程休眠
  
  - 在指定的毫秒数内让当前正在执行的线程休眠（暂停执行），不释放锁资源，监控状态继续保持，时间到则重新为就绪状态
  
  - ```java
    TimeUnit.SECONDS.sleep(5);
    Thread.sleep(5);
    ```
- static void yield() ：线程让步
  
  - 暂停当前正在执行的线程对象，让出时间片，由运行状态到就绪状态，等待获取时间片；
- void interrupt ()
  
  - 中断线程并且抛出一个InterruptedException异常，处理异常，虚拟机不会退出，线程之后的代码会继续执行
- void setPriority(int newPriority) 
  
  - 更改线程的优先级
- void setDaemon(boolean on) 
  - 设置是否为守护线程，先设置后启动
  - 垃圾回收器（GC）：就是守护线程

###### java.util.concurrent

##### 05：Class  CountDownLatch [a wait for all]

- **倒计时锁**，是通过一个计数器来实现的，**计数器的初始值是线程的数量**。每当一个线程执行完毕后，计数器的值就-1，当计数器的值为0时，表示所有线程都执行完毕，然后**在闭锁上等待的线程就可以恢复工作**了。
- 比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，实现此功能
- CountDownLatch(int count)
  - 构造count数量的倒计时锁
- public void await() throws InterruptedException；
  - 执行await()方法的线程会被挂起，它会等待直到count值为0才继续执行。
- boolean   await(long timeout, TimeUnit unit) 
  - 增加了超时返回的功能
- void   countDown()
  - 将count值减1。

```java
public class Main {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            Runnable runnable = new CountRunnable(latch);
            pool.execute(runnable);
        }
    }
}

class CountRunnable implements Runnable {
    private CountDownLatch latch;
    public CountRunnable(CountDownLatch latch) {
        this.latch = latch;
    }
    @Override
    public void run() {
        try {
            synchronized (latch) {
                latch.countDown();
                System.out.println("thread counts = " + (countDownLatch.getCount()));
            }
            // 每个线程都在等待...其实就是省了通知
            latch.await();
            System.out.println("concurrency counts =" + (100-countDownLatch.getCount()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
```

###### java.util.concurrent

##### 06：Class CyclicBarrier [all wait for all]

​	**循环屏障**，可以让一组线程达到一个屏障时被阻塞，直到最后一个线程达到屏障时，所有被阻塞的线程才能继续执行，可以循环使用此同步屏障；

- CyclicBarrier就像一扇门，默认情况下关闭状态，堵住了线程执行的道路，直到所有线程都就位，门才打开，让所有线程一起通过；
- CyclicBarrier实现主要基于ReentrantLock

###### 构造方法

- CyclicBarrier(int parties)
  - 其参数表示同步屏障拦截的线程数量，每个线程调用await( )，告诉CyclicBarrier已经到达屏障位置，线程被阻塞;
- CyclicBarrier(int parties, Runnable barrierAction)
  - 其中barrierAction任务会在所有线程到达屏障后执行

###### 方法：

- int   await( )
  - 当线程执行await( )时，内部变量count减1，如果count！= 0，说明有线程还未到屏障处，是该线程处于等等待状态
- int   await(long timeout, TimeUnit unit)
  - 指线程等待的超时时间，当出现等待超时的时候，当前线程会被释放，但会像其他线程传播出BrokenBarrierException异常。
- int   getNumberWaiting()
  - 获取当前同步屏障还需等待的数量
- int   getParties()
  - 获取同步屏障需要等待的总线程数
- boolean   isBroken( )
  - 查询同步屏障是否被穿透，某些等待的线程由于中断或者超时，已经继续不处于等待状态了
- void`  `reset( ) 
  - 重置同步屏障处于初始化状态

```java
public class MyThread extends Thread {
    private CyclicBarrier cyclicBarrier;
    private String name;
    public MyThread(CyclicBarrier cyclicBarrier, String name) {
        super();
        this.cyclicBarrier = cyclicBarrier;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            System.out.println(name + "赛前活动...");
            System.out.println(name + "准备完毕！等待发令枪");
            try {
                // 阻塞直到所需线程都来时，再执行
                cyclicBarrier.await();
              
                System.out.println("111111111111111111111111");
                // 再次使用循环屏障
                cyclicBarrier.await();
             
            } catch (BrokenBarrierException e) {            
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(5, new Runnable() {
            @Override
            public void run() {
                System.out.println("发令枪响了，Run....");
            }
        });
        for (int i = 0; i < 5; i++) {
            new MyThread(barrier, "运动员" + i + "号").start();
        }
    }
}
```

##### 07：CyclicBarrier与CountDownLatch的区别

1. CountDownLatch 允许一个或多个线程等待一些特定的操作完成，而**这些操作是在其它的线程中**进行的，也就是说会出现 **等待的线程** 和 **被等的线程** 这样分明的角色
3. CountDownLatch 是**一次性使用的**，也就是说latch门闩只能只用一次，一旦latch门闩被打开就不能再次关闭，将会一直保持打开状态；

###### java.util.concurrent

##### 08：Semaphore

​	用于**限制访问某些资源的线程数目，它维护了一个许可证集合**，有多少资源需要限制就维护多少许可证集合，假如这里有N个资源，那就对应于N个许可证，同一时刻也只能有N个线程访问。一个线程获取许可证就调用acquire方法，用完了释放资源就调用release方法。

- Semaphore是资源的互斥而不是资源的同步，在同一时刻是无法保证同步的，但是却可以保证资源的互斥；
- Semaphore底层是由AQS和Uasafe实现的；

###### 方法：

- acquire()
  - 获取信号量的许可证
- release()
  - 释放许可证
- acquire(int permits)
  - 从此信号量获取给定数目的许可，在提供这些许可证前一直将线程阻塞，或者线程已被中断。
- release(int permits)
  - 释放给定数目的许可，将其返回到信号量。这个是对应于上面的方法，一个学生占几个窗口完事之后还要释放多少，**要是没有资源就释放的话，会自动+1**；
- availablePermits()
  - 返回此信号量中当前可用的许可数。
- reducePermits(int reduction)
  - 根据指定的缩减量减小可用许可的数目
- hasQueuedThreads()
  - 查询是否有线程正在等待获取资源。
- getQueueLength()
  - 返回正在等待获取的线程的估计数目。该值仅是估计的数字。
- tryAcquire(int permits, long timeout, TimeUnit unit)
  - 如果在给定的等待时间内此信号量有可用的所有许可，并且当前线程未被中断，则从此信号量获取给定数目的许可，超时抛出异常。
- acquireUninterruptibly(int permits)
  - 从此信号量获取给定数目的许可，在提供这些许可前一直将线程阻塞。

```java
// 三个许可证
private static Semaphore semaphore = new Semaphore(3);
public static void main(String[] args) {
  for (int i = 0; i < 10; i++) {
    new Student(Integer.toString(i), semaphore).start();
  }
}

static class Student extends Thread {

  private String name;
  private Semaphore semaphore;

  public Student(String name, Semaphore semaphore) {
    this.name = name;
    this.semaphore = semaphore;
  }

  @Override
  public void run() {
    try {
      semaphore.acquire();
      System.out.println(name + "获取取餐的许可");
      TimeUnit.MICROSECONDS.sleep(10003);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      System.out.println(name + "取餐完毕，释放窗口资源");
      semaphore.release();
    }
  }
}
```

##### 09：综合示例

```java
/**
 * 实例化3个线程，一个线程打印a，一个打印b，一个打印c，三个线程同时执行，要求打印出6个连着的abc
 * 信号量的传递...
 * @author kang.li
 * @date 2020-08-04 14:51
 */
@Slf4j
public class Question1 {

    /**
     * 需要等待的线程数，后面的线程用于测试输出
     */
    private static final CyclicBarrier BARRIER = new CyclicBarrier(3, () -> log.info("start print........."));

    /**
     * 初始化线程数
     */
    private static final ExecutorService POOL = new ThreadPoolExecutor(3,
            5,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingDeque<>());

    /**
     * 控制A线程顺序
     */
    private static Semaphore firstSemaphore = new Semaphore(1);
    /**
     * 控制B线程顺序
     */
    private static Semaphore secondSemaphore = new Semaphore(0);
    /**
     * 控制C线程顺序
     */
    private static Semaphore thirdSemaphore = new Semaphore(0);

    /**
     * 循环屏障使用次数
     */
    private static final int COUNT = 6;

    /**
     * 初始化打印线程
     */
    public static void init_A() {
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < COUNT; i++) {
                try {
                    BARRIER.await();
                    firstSemaphore.acquire();
                    log.info("A");
                    secondSemaphore.release();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error("Question1.init_A: " + e.getMessage());
                }
            }

        }, POOL);
    }

    /**
     * 控制B线程顺序
     */
    public static void init_B() {
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < COUNT; i++) {
                try {
                    BARRIER.await();
                    secondSemaphore.acquire();
                    log.info("B");
                    thirdSemaphore.release();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error("Question1.init_B: " + e.getMessage());
                }
            }

        }, POOL);
    }

    /**
     * 控制C线程顺序
     */
    public static void init_C() {
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < COUNT; i++) {
                try {
                    BARRIER.await();
                    thirdSemaphore.acquire();
                    log.info("C");
                    firstSemaphore.release();
                } catch (InterruptedException | BrokenBarrierException e) {
                    log.error("Question1.init_C: " + e.getMessage());
                }
            }

        }, POOL);
    }

    /**
     * 初始化三个打印线程
     */
    public static void main(String[] args) {
        init_A();
        init_B();
        init_C();
    }

}
```

