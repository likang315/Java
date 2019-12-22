### Executor 框架

应用程序通过Excutor框架控制上层的调度，而下层的调度由操作系统内核控制且不受应用程序的控制

------

 从JDK 1.5 开始，Java把工作单元和执行机制分离开，任务提交和任务执行分离开

- 工作单元：Runnable，Callable
- 执行机制：Excutor框架提供

ExecutorService 接口有两个关键的实现类，ThreadPoolExecutor、ScheduledThreadPoolExecutor

##### 1：ThreadPoolExecutor

​	是线程池的核心实现类，用来执行被提交的任务

##### 2：ScheduledThreadPoolExecutor

​	可以给定的延迟后执行任务，或定期执行命令

##### 3：Executors

- static ExecutorService newFixedThreadPool(int Threads) 
  - 创建一个可重用固定线程数的线程池，以共享的无界队列方式来运行这些线程 
- static ExecutorService newSingleThreadExecutor() 
  - 单线程线程池，队列使用的无限制大小的链表阻塞队列
- static ExecutorService newCachedThreadPool()
  - 无界线程池，无论多少任务，直接运行，如果线程池长度超过处理需要，可灵活回收空闲线程，若无可回收，则新建线程

