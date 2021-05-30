### Executor 框架

------

[TOC]

##### 01：概述

 从 JDK 1.5 开始，Java把**工作单元**和**执行机制**分离开，任务提交和任务执行分离开。

- 工作单元：Runnable，Callable
- 执行机制：Excutor 框架提供

##### 02：Executor 两级调度模型

- 在上层，Java多线程程序通常把应用分解为若干个任务，然后使用用户级的调度器（Executor框架）将这些任务映射为固定数量的线程；
- 在底层，操作系统内核将这些线程映射到硬件处理器上；

##### 03：Executor 组成【三部分】

![](/Users/likang/Code/Git/Java-and-Middleware/多线程/多线程/executor使用示意图.png)

1. 任务

   - 包括被执行任务需要实现的接口：Runnable接口或Callable接口；

2. 任务的执行

   1. 包括任务执行机制的核心接口Executor，以及继承自Executor的ExecutorService接口；

   2. Executor框架有两个关键类实现了ExecutorService接口

      - ThreadPoolExecutor

      - ScheduledThreadPoolExecutor

3. 异步计算的结果

   - 包括接口Future和实现Future接口的FutureTask类；

##### 04：Executor API

1. ThreadPoolExecutor
2. ScheduledThreadPoolExecutor
3. Future接口
4. Runnable接口、Callable接口
5. Executors 

   - Executors可以创建3种类型的ThreadPoolExecutor 【禁止这种方式创建】
     - FixedThreadPool
       - 适用于为了满足资源管理的需求，而需要限制当前线程数量的应用场景，它适用于负载比较重的服务器;
     - SingleThreadExecutor
       - 适用于需要保证顺序地执行各个任务；并且在任意时间点，不会有多个线程是活动的应用场景;
     - CachedThreadPool
       - 大小无界的线程池，适用于执行很多的短期异步任务的小程序，或者是负载较轻的服务器;

   ```java
   // 创建固定线程数的FixedThreadPool的API
   public static ExecutorService newFixedThreadPool(int nThreads)
     
   // 创建使用单个线程的SingleThreadExecutor的API
   public static ExecutorService newSingleThreadExecutor()
     
   // 创建一个会根据需要创建新线程的CachedThreadPool的API
   public static ExecutorService newCachedThreadPool()
   ```

2. ###### ScheduledThreadPoolExecutor

   - ScheduledThreadPoolExecutor
     - 适用于需要多个后台线程**执行周期任务**，同时为了满足资源管理的需求而需要限制后台线程的数量的应用场景；
   - SingleThreadScheduledExecutor
     - 适用于需要**单个后台线程执行周期任务**，同时需要保证顺序地执行各个任务的应用场景；

   ```java
   public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize)
   public static ScheduledExecutorService newSingleThreadScheduledExecutor()
   ```
   
3. ###### Future接口

   - 实现Future接口的FutureTask类用来表示异步计算的结果，获取异步执行的结果；

4. ###### Executors 

   - 工厂类，用于创建线程池的；

##### 05：ThreadPoolExecutor 详解

线程池预热：初始化线程corePoolSize

1. ###### FixedThreadPool

   ```java
   public static ExecutorService newFixedThreadPool(int nThreads) {
     return new ThreadPoolExecutor(nThreads, nThreads,
                                   0L, TimeUnit.MILLISECONDS,
                                   new LinkedBlockingQueue<Runnable>());
   }
   ```

   - 当线程池中的线程数大于corePoolSize时，keepAliveTime为多余的空闲线程等待新任务的最长时间，超过这个时间后多余的线程将被终止。这里把keepAliveTime设置为0L，意味着多余的空闲线程会被立即被终止；
   - FixedThreadPool使用无界队列LinkedBlockingQueue作为线程池的工作队列（容量：Integer.MAX_VALUE）
   - 使用无界队列时maximumPoolSize，keepAliveTime，拒绝策略都是一个无效参数；

2. ###### SingleThreadExecutor

   ```java
   public static ExecutorService newSingleThreadExecutor() {
     return new FinalizableDelegatedExecutorService
       (new ThreadPoolExecutor(1, 1,
                               0L, TimeUnit.MILLISECONDS,
                               new LinkedBlockingQueue<Runnable>()));
   }
   ```

   - 使用无界队列LinkedBlockingQueue作为线程池的工作队列（队列的容量为Integer.MAX_VALUE）

3. ###### CachedThreadPool

   ```java
   public static ExecutorService newCachedThreadPool() {
     return new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                                   60L, TimeUnit.SECONDS,
                                   new SynchronousQueue<Runnable>());
   }
   ```

   1. 执行SynchronousQueue.offer（Runnable task）。如果当前maximumPool中有空闲线程正在执行SynchronousQueue.poll（keepAliveTime，TimeUnit.NANOSECONDS），那么主线程执行offer操作与空闲线程执行的poll操作配对成功，**主线程把任务交给空闲线程执行**，execute()方法执行完成；否则执行下面的步骤2；
   2. 当初始maximumPool为空，或者maximumPool中当前没有空闲线程时，将没有线程执行SynchronousQueue.poll（keepAliveTime，TimeUnit.NANOSECONDS）。这种情况下，步骤1将失败；此时CachedThreadPool会创建一个新线程执行任务，execute()方法执行完成；
   3. 在步骤2中新创建的线程将任务执行完后，会执行SynchronousQueue.poll（keepAliveTime，TimeUnit.NANOSECONDS）。这个poll操作会让空闲线程最多在SynchronousQueue中等待60秒钟。如果60秒钟内主线程提交了一个新任务（主线程执行步骤1）），那么这个空闲线程将执行主线程提交的新任务；否则，这个空闲线程将终止。由于空闲60秒的空闲线程会被终止，因此长时间保持空闲的CachedThreadPool 不会使用任何资源；

##### 06：ScheduledThreadPoolExecutor 详解

1. 当调用ScheduledThreadPoolExecutor的scheduleAtFixedRate()方法或者scheduleWithFixedDelay()方法时，会向ScheduledThreadPoolExecutor的DelayQueue添加一个实现了RunnableScheduledFuture接口的ScheduledFutureTask；
2. 线程池中的线程从**DelayQueue**中获取ScheduledFutureTask，然后执行任务；

###### 原理

- 定时任务先执行 corn，计算出任务的执行时间，放入延迟队列中，假如队列中有多个定时任务，按照延迟时间最小堆排序，把延迟是时间最小的放到队列的头部，有一个工作者线程轮询获取任务（持有时间器），判断delayTime 是否为0 ，若是执行，否则丢弃任务，继续等待；

##### 07：Future 接口

1. ###### FutureTask  

   - FutureTask的实现基于AbstractQueuedSynchronizer ；

```java
public class FutureTask<V> implements RunnableFuture<V> {
	 // RunnableFuture<V> extends Runnable, Future<V>
}
```

- get ( ) ：执行get方法时，若是处于未启动或者已启动状态时，将会导致**调用线程阻塞，等待获取线程结果；**
- V get(long timeout, TimeUnit unit) ：最长等待timeout时间，否则抛出超时异常；
- boolean isDone() ：判断task是否执行完成；
- cancel( )：取消正在执行任务的线程；

![](https://github.com/likang315/Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/FutureTask.png?raw=true)

- 可以把FutureTask交给Executor执行；
- 也可以通过ExecutorService.submit（…）方法返回一个FutureTask，然后执行FutureTask.get()方法或FutureTask.cancel（…）方法；

##### 08：CompletableFuture

- Future 对于结果的获取却是很不方便，只能通过阻塞或者轮询的方式得到任务的结果，**阻塞的方式显然和我们的异步编程的初衷相违背，轮询的方式又会耗费无谓的 CPU 资源**，而且也不能及时地得到计算结果;
- CompletableFuture提供了非常强大的Future的扩展功能，并且提供了**函数式编程**的能力，可以通过回调的方式处理计算结果，也提供了转换和组合 CompletableFuture 的方法；
- 它可能代表一个明确完成的Future，也有可能代表一个完成阶段（ CompletionStage ），它支持在计算完成以后触发一些函数或执行某些动作；

```java
public class CompletableFuture<T> implements Future<T>, CompletionStage<T> 
```

- getNow(T valueIfAbsent) ：如果结果已经计算完则返回结果或者抛出异常，否则返回 valueIfAbsent 值
- join()：阻塞等待线程执行完成后，返回计算的结果或者抛出一个unchecked异常(CompletionException)

###### 创建、获取CompletableFuture对象

```java
public static CompletableFuture<Void> runAsync(Runnable runnable);
public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor);
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier);
public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor);
```

- 以 Async 结尾并且没有指定 Executor 的方法会使用 ForkJoinPool.commonPool( ) 作为它的线程池异步执行;
- runAsync()，它以 Runnable 函数式接口类型为参数，所以 CompletableFuture 的计算结果为空;
- `supplyAsync()`方法以`Supplier<U>`函数式接口类型为参数,`CompletableFuture`的计算结果类型为`U`;

```java
final CompletableFuture<String> future = CompletableFuture.supplyAsync(
  new Supplier<String>() {
    @Override
    public String get() {
        // 异步任务逻辑...
        return "result";
    }
}, executor);

final CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
    // 异步任务执行逻辑...
    return "result";
}, executor);
```

###### 任务完成、或者抛出异常时的处理

```java
public CompletableFuture<T> 	whenComplete(
  BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T> 	whenCompleteAsync(
  BiConsumer<? super T,? super Throwable> action)
public CompletableFuture<T> 	whenCompleteAsync(
  BiConsumer<? super T,? super Throwable> action, Executor executor)
public CompletableFuture<T>   exceptionally(Function<Throwable,? extends T> fn)
```

- 当任务处理完成时，将触发action；

- 它的类型是`BiConsumer<? super T,? super Throwable>`，它可以处理正常的计算结果，或者异常情况；

- 方法不以`Async`结尾，意味着`Action`使用相同的线程执行，而`Async`可能会使用其它的线程去执行;

- 当`Action`执行完毕后它的结果返回原始的`CompletableFuture`的计算结果或者返回异常；

- ###### 异常处理：

  - 当原始的 CompletableFuture 抛出异常的时候，exceptionally 方法返回一个新的CompletableFuture，就会触发新的 CompletableFuture 的计算，调用function计算值，返回新的计算结果；

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(Main::getMoreData);
// 第一个参数 v 为 future返回的结果, e 为异常类型
Future<Integer> f = future.whenComplete((v, e) -> {
  System.out.println(v);
  System.out.println(e);
});
System.out.println(f.get());
```

###### 执行结果转换

```java
public <U> CompletableFuture<U> handle(BiFunction<? super T,Throwable,? extends U> fn)
public <U> CompletableFuture<U> handleAsync(
  BiFunction<? super T,Throwable,? extends U> fn)
public <U> CompletableFuture<U> handleAsync(
  BiFunction<? super T,Throwable,? extends U> fn, Executor executor)
```

- 返回CompletableFuture对象的值和原来的CompletableFuture计算的值不同；
- 当原先的CompletableFuture的值计算完成或者抛出异常的时候，会触发这个CompletableFuture对象的计算，结果由`BiFunction`参数计算而得，因此**这组方法兼有`whenComplete`和转换的两个功能**；
- 不以Async结尾的方法由原来的线程计算，以Async结尾的方法由默认的线程池`ForkJoinPool.commonPool()`或者指定的线程池`executor`运行；

###### 执行结果转换，但是不能处理异常【禁用】

```java
public <U> CompletableFuture<U> 	thenApply(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U> 	thenApplyAsync(Function<? super T,? extends U> fn)
public <U> CompletableFuture<U> 	thenApplyAsync(
  	Function<? super T,? extends U> fn, Executor executor)
```

- 阻塞直到原来的CompletableFuture计算完后，将结果传递给函数`fn`，将`fn`的结果作为新的`CompletableFuture`计算结果。因此它的功能相当于将`CompletableFuture<T>`转换成`CompletableFuture<U>`；
- 它们与`handle`方法的区别在于`handle`方法会处理正常计算值和异常，因此它可以屏蔽异常，避免异常继续抛出。**而`thenApply`方法只是用来处理正常值，因此一旦有异常就会抛出**；

```java
CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
    return 100;
});
CompletableFuture<String> f = future.handleAsync((v, e) -> {
    return v * 10;}).handle((v, e) -> v.toString());
```

###### 用来组合多个CompletableFuture

```java
// 当所有的CompletableFuture都执行完后执行计算
public static CompletableFuture<Void> allOf(CompletableFuture<?>... cfs)
// 当任意一个CompletableFuture执行完后就会执行计算，计算的结果相同
public static CompletableFuture<Object> anyOf(CompletableFuture<?>... cfs)
```

- 可变长参数用于输入多个异步任务;

- ```java
  Random rand = new Random();
  CompletableFuture<Integer> future1 = CompletableFuture.supplyAsync(() -> {
      try {
          Thread.sleep(10000 + rand.nextInt(1000));
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      return 100;
  });
  CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
      try {
          Thread.sleep(10000 + rand.nextInt(1000));
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
      return "abc";
  });
  // 该返回值一般不使用，等待异步线程执行完，执行下一步操作
  // CompletableFuture.allOf(future1,future2);
  CompletableFuture<Object> f =  CompletableFuture.anyOf(future1,future2);
  System.out.println(f.get());
  ```

###### 将多个 CompletableFuture 的结果存到一个List中【封装】

```java
public static <T> CompletableFuture<List<T>> sequence(
  List<CompletableFuture<T>> futures) {
  	CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(
      futures.toArray(new CompletableFuture[futures.size()]));
  	return allDoneFuture.thenApply(v -> futures.stream().map(CompletableFuture::join)
                                   .collect(Collectors.<T>toList()));
}
public static <T> CompletableFuture<Stream<T>> sequence(
  Stream<CompletableFuture<T>> futures) {
 	 List<CompletableFuture<T>> futureList = futures
     .filter(f -> f !=null).collect(Collectors.toList());
  return sequence(futureList);
}
```



