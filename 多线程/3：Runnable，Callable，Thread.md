### 创建线程

------

​	JVM执行字节码，最终需要转化为汇编指令在CPU上执行，Java中所使用的并发机制依赖于JVM的实现和CPU的指令

##### 1：继承Thread类，重写run() 来定义线程要执行的任务

​	启动线程指调用start()，而并不是调用run()，当线程的start()被调用后，线程进入Runnable状态，等待获取cpu,一旦获取CPU时间片，run()自动被调用，即运行程序

###### 缺点：

- 由于Java单继承，当继承了 Thread 类后就无法再继承其它类
- 由于继承了 Thread 后重写run()规定了线程执行的任务，这导致线程与任务有一个必然的耦合关系，不利于线程的重用

```java
Thread thread = new Thread() {
    @Override
    public void run() {
			for (int i = 0; i < 100; i++) {
           System.out.println(i);
      }
    }
};
// 调用处于就绪状态，不允许显示创建线程，使用线程池，否则会创建大量同类的线程
thread.start();
```

##### 2：实现 Runnable（Interface），重写run（）方法

```java
// 使用匿名内部类
Runnable runnable = new Runnable() {
  @Override
  public void run() {
      long startTimestamp = System.currentTimeMillis();
    	// ... ,处理我那业务通知其他线程
      this.notifyAll(); 
      long time = System.currentTimeMillis() - startTimestamp;
  }
};
// 执行线程
executorServices.execute(runnable);
```

##### 3：实现 Callable （Interface），重写call()，将作为线程执行体，并且有返回值，可抛出异常

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
      Future<List<String>> futureReceive = future.get(i);
      List<String> futureValue  =
        futureReceive.get(TASK_TIME_OUT,Time.Unit.MILLISECONDS);
      System.out.println(futureValue);
    }
  }
}
```

##### 4：Thread  （Java.lang）

public class Thread extends Object implements Runnable

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
  - 仅仅任意通知一个处于阻塞的线程，不释放锁资源
- void join(long millis) 
  - join( )：默认等待0 毫秒
  - 调用 join() 的线程进入 TIMED_WAITING 状态，等待 join() 所属线程运行结束后再继续运行，底层调用Object.wait()
- static void sleep(long millis) ：线程休眠
  - 在指定的毫秒数内让当前正在执行的线程休眠（暂停执行），不释放锁资源，监控状态继续保持，时间到则重新为就绪状态
- static void yield() ：线程让步
  - 暂停当前正在执行的线程对象，让出时间片，由运行状态到就绪状态，等待获取时间片
- void interrupt ()
  - 中断线程并且抛出一个InterruptedException异常，处理异常，虚拟机不会退出，线程之后的代码会继续执行
- void setPriority(int newPriority) 
  - 更改线程的优先级
- void setDaemon(boolean on) 
  - 设置是否为守护线程，先设置后启动
  - 垃圾回收器（GC）：就是守护线程
  - 守护进程：一直运行的服务端程序，通常在系统后台运行，没有控制终端，不与前台交互，Daemon程序一般作为系统服务使用

##### 5：线程优先级

- 线程的优先级：线程默认优先级为5，范围是1-10，值越大优先级越高
- 线程的切换是由CPU的调度(轮转时间片)控制的，并且线程的调度不能被干预，但是可以通过提高线程的优先级来提高获取时间片的概率
- 线程默认的优先级都与创建它的父线程具有相同的优先级，在默认情况下，主线程具有普通优先级：5



