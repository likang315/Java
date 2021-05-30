### 线程池

------

[TOC]

##### 01：线程池

- 降低资源消耗，**提高响应速度**，提高线程可管理性；
- **重复利用已经创建的线程，减少创建线程和销毁线程的开销**，不需要等到线程创建就能立即执行；
- 使用线程池可以对线程**统一分配，调优和监控**；
- 当我们的应用需要创建大量线程或者发现线程会频繁的创建和销毁时就应当考虑使用线程池来维护线程；

##### 02：线程池原理

![](https://github.com/likang315/Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E7%BA%BF%E7%A8%8B%E6%B1%A0.png?raw=true)

1. 向线程池提交任务后，处理任务流程;
2. 判断核心线程池（corePoolSize：核心线程数）是否都处于工作状态，若没有，则拿一个已有的线程执行任务，若是，则进入下一个流程；
3. 查看阻塞队列（BlockingQueue）是否已满，未满的话，将任务存储在队列里，若满的话，进入下一个流程；
4. 查看线程池（maximumPoolSize：最大线程数量）中的线程是否都处于工作状态，若没有，创建线程执行任务，若线程池满了，则按照某种拒绝策略（handler）处理无法执行的任务；

##### 03：ThreadPoolExcutor

- ThreadPoolExcutor 执行excute( ) 示意图
- 在步骤一、三创建线程都会要求**获取全局锁**，要尽可能的避免获取全局锁

![](https://github.com/likang315/Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/ThreadPoolExcutor.png?raw=true)

##### 04：创建线程池的参数

- ###### 创建线程池【推荐的方式】,七大参数

  ```java
  public  new ThreadPoolExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit,
                            BlockingQueue<Runnable> workQueue,
                            ThreadFactory threadFactory,
                            RejectedExecutionHandler handler)
  ```

- ###### corePoolSize

  - 核心线程池的大小
  - 构建线程池后，并不会立即创建线程，当执行任务时，如果当前线程数如果小于corePoolSize，则创建一个线程，当前线程数等于corePoolSize，会将任务放入队列中；

- ###### workQueue：任务队列（4种，属于阻塞队列）

  - ArrayBlockingQueue：
    -  基于数组结构的有界阻塞队列，按FIFO排序任务
  - LinkedBlockingQuene：
    - 基于链表结构的有界阻塞队列，按FIFO排序任务，**吞吐量高于ArrayBlockingQuene**；
    - ExecutorService newFixedThreadPool( ) 使用此队列
      - Executors.newFixedThreadpool（固定线程数）:禁用
    - ThreadPoolTaskExecutor 默认也是此队列；
      - LinkedBlockingQueue时有一个大小限制，其默认为Integer.MAX_VALUE；
      - LinkedBlockingQueue不接受null值，当添加null的时候，会直接抛出NullPointerException；
  - SynchronousQuene：
    - 没有容量的阻塞队列，每个插入操作必须等到另一个线程调用移除操作，否则插入操作一直处于阻塞状态，吞吐量通常要高于LinkedBlockingQuene
    - ExecutorService newCachedThreadPool( )  使用此队列
  - priorityBlockingQuene：
    - 具有优先级的无界阻塞队列

- ###### maximumPoolSize：

  - 线程池最大数量
  - 如果任务队列已满时，并且创建的线程数小于最大线程数，则会创建线程执行任务；
  - 若使用无界队列则此参数没有意义

- ###### keepAliveTime：

  - 线程存活时间，工作线程空闲后，保持存活的时间；
  - 默认情况下，如果当前线程数大于corePoolSize，并且存在线程如果没有任务执行，当空闲的时间大于keepAliveTime时，会终止该线程，直到线程数不超过corePoolSize；

- ###### TimeUnit：

  - 线程活动保持时间的单位
  - 毫秒：MILLISECONDS
  - 微妙：MICROSECONDS
  - 纳秒：NANOSECONDS

- ###### rejectedExecutionHandler：拒绝策略（4种，类）

  - AbortPolicy：直接抛出异常，默认策略；
  - CallerRunsPolicy：只用调用者所在的线程来执行任务；
  - DiscardOldestPolicy：丢弃阻塞队列中靠最前的任务，并执行当前任务；
  - DiscardPolicy：不处理，丢弃掉；
  
- ###### ThreadFactory：

  - 用于设置创建线程的工厂，通过线程工厂可以使创建的线程具有意义的名字；

  ```java
  new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build();
  ```

##### 05：操作线程池

- execute( )：
  - 用于提交**不需要返回值**的任务，所以无法判断是否被线程池执行成功
  - 输入的任务一个 Runnable 的实例
- submit( ):
  - 提交**需要有返回值**的任务，线程池会返回一个 Future 对象，通过Future 可以知道线程是否执行成功
  - Future 的get( ) 方法：阻塞当前线程直到任务完成
    
    - get（long timeOut，TimeUnit unit）：阻塞timeout时间，返回，任务可能为执行完成，但是超时了；
    
    - ```java
      Future<Object> future = excutor.submit(task);
      try {
          Object obj = future.get();
      } catch (InterruptedException e) {
          // 处理中断异常
      } catch (ExceptionException e) {
          // 处理无法执行的任务异常
      } finally {
          // 关闭线程池
          excutor.shutdown();
      }
      ```
- **shutdown( )：**
  
  - 将线程池的状态设置为SHUTDOW，然后遍历工作线程，调用interupt（），**中断没有正在执行任务的线程，若有现成正在执行任务，等待任务执行完毕**；
- **shutdownNow( )：**
  
  - 将线程池的状态设置为STOP，**中断所有工作线程**，不区分是否正在执行任务
- isShutdown( )：
  
  - 只要调用关闭方法中任意一个就会返回ture，当所有任务执行完成后，才表示线程池关闭成功
- **isTerminaed( ):**
  
  - 线程池关闭成功，才会返回True

##### 06：线程池的状态

```java
static final int RUNNING    = 0;
static final int SHUTDOWN   = 1;
static final int STOP       = 2;
static final int TERMINATED = 3;
```

- **RUNNING：**创建线程池后，初始状态为RUNNING
- SHUTDOWN：调用shutdown（）后，线程池处于SHUTDOWN状态
- **STOP：**调用shutdownNow（）后 ，线程池处于STOP状态
- **TERMINATED：**当线程关闭成功时，状态为TERMINATED

##### 07：合理的配置线程池

1. **CPU密集型任务**应配置尽可能少的线程，如配置 **N cpu + 1** 个线程的线程池；
2. 由于**IO密集型任务**线程并不是一直在执行任务，则应配置尽可能多的线程，如 **2 * N cpu**；
3. 依赖数据库连接池的任务，因为线程提交SQL后需要等待数据库返回结果，等待的时间越长，则CPU空闲时间就越长，那么线程数应该设置得越大，这样才能更好地利用CPU；
4. 建议使用有界队列，可能会撑爆内存；

##### 08：线程池的监控

- 必需对线程池进行监控，方便在出现问题时，可以根据线程池的使用状况快速定位问题。可以通过线程池提供的参数进行监控；
- taskCount：线程池需要执行的任务数量。
- completedTaskCount：线程池在运行过程中**已完成的任务数量**，小于或等于taskCount；
- **largestPoolSize**：线程池里曾经创建过的最大线程数量。通过这个数据**可以知道线程池是否曾经满过**；
- getPoolSize：线程池的线程数量。如果线程池不销毁的话，线程池里的线程不会自动销毁；



