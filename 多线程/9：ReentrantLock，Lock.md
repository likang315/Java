##### 1：并发编程的三个特性

1. 原子性：在一个原子操作中，cpu 不可以在中途暂停然后再调度，即不可被中断的操作
2. 有序性：在本线程内观察，所有操作都是有序的
3. 可见性：指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即得到修改的值
   - 当一个变量被 volatile 修饰后，当一个线程在私有内存中修改此共享变量后，共享变量会立即被更新到主内存中，同时使其他线程缓存的此变量无效，其他线程读取共享变量时，会直接从主内存中读取
   - synchronized和Lock都可以保证可见性
     - 锁的happens-before规则保证释放锁和获取锁的两个线程之间的内存可见性

##### 2：乐观锁与悲观锁：

- 分类依据：线程要不要锁住资源
- 悲观锁：假设最坏的情况，自己每次取数据时都认为其他线程会修改，因此在获取数据的时候会先加锁，确保数据不会被别的线程修改，当其他线程想要访问数据时，都需要阻塞挂起，等待持有锁的线程释放锁
  - synchronized
  - Lock 的实现类
- 乐观锁：认为自己在使用数据时不会有别的线程修改数据，所以不会添加锁，只是在更新数据的时候去判断之前有没有别的线程更新了这个数据。如果这个数据没有被更新，当前线程将自己修改的数据成功写入。如果数据已经被其他线程更新，则根据不同的实现方式执行不同的操作（例如报错或者自动重试）
  - 通过使用无锁编程来实现
    - CAS算法，Java 原子类（Automic）中的递增操作就通过CAS自旋实现的

```java
// 悲观锁:synchronized
public synchronized void testMethod() {
	// 操作同步资源
}
// Lock 的实现类：ReentrantLock
private ReentrantLock lock = new ReentrantLock();
public void modifyPublicResources() {
	lock.lock();
	// 操作同步资源
	lock.unlock();
}

// 乐观锁：需要保证多个线程使用的是同一个AtomicInteger
private AtomicInteger atomicInteger = new AtomicInteger(); 
// 执行自增
atomicInteger.incrementAndGet();
```

##### 4：重入锁(Lcok)：根据一个线程中的多个流程能不能获得同一把锁

- 重进入：指任意线程在获取到锁之后能够再次获取该锁而不会被锁阻塞
- 可重入锁：指的是可重复递归调用的锁，在外层使用锁之后，在内层仍然可以获得此锁，并且不发生死锁（前提得是同一个对象或者class）
  - ReentrantLock 和synchronized 都是可重入锁
- 不可重入锁：与可重入锁相反，不可递归调用，递归调用就发生死锁

```java
public class Solution {
  // 同步方法，同一个对象锁
  public synchronized void firstInvoke() {
    System.out.println("方法1执行...");
    doOthers();
  }
	// 同一个对象锁
  public synchronized void secondInvoke() {
    System.out.println("方法2执行...");
  }
}
```

###### 为什么支持重复加锁 ？

​	因为源码中用变量 c (计数器)来保存当前锁被获取了多少次，故在释放时，对 c 变量进行减操作，只有 c 变量为 0 时，才算锁的最终释放。所以可以 lock() 多次，同时 unlock 也必须与 lock 同样的次数

##### 显示获取锁并发编程

##### 5：Lock (JUC)

```java
public interface Lock {
    // 获得当前线程的对象锁，如果不能获取等待直到获取成功返回
    void	lock();
    void	lockInterruptibly();
    // 用于让线程 wait 或者 唤醒
    Condition	newCondition();
    boolean	tryLock()
    boolean	tryLock(long time, TimeUnit unit);
    // 能获得锁就返回true，不能就立即返回false，tryLock(long timeout,TimeUnit unit)，可以增加等待时间限制时间限制，如果超过该时间段还没获得锁，返回false
    // 释放锁
    void unlock();
}
```

##### 6：Condition （JUC）

```java
public interface Condition {
  // 处于等待状态，中断时，会抛出异常
  void await() throws InterruptedException;
  // 让其等待，中断时，不会抛出异常
  void awaitUninterruptibly();
  // 任意唤醒一个处于等待状态的线程
  void signal();
  // 唤醒所有处于等待状态的线程
  void signalAll();
}
```

##### 7：ReentrantLock：

​	是可重入锁、实现了Lock接口的锁，与synchronized作用一致，并且扩展了其能力

###### 原理：

- ReenTrantLock 是一种自旋锁，通过循环调用CAS操作来实现加锁


```java
public class Test {
    private final Reentrantlock lock = new Reentrantlock()；
    public void testMethod() {
        lock.lock(); // 先获得锁，再处理业务
        for (int i = 0; i < 5; i++) {
            System.out.println("ThreadName =" + Thread.currentThread().getName()
                     + (i + 1));
        }
        lock.unlock();
    }
}
```

```java
// 创建Condition对象来使线程 wait 或者唤醒，必须先执行 lock.lock() 获得锁
public class MyService {
    private final Reentrantlock lock = new Reentrantlock（）；
    private Condition condition = lock.newCondition();
    public void testMethod() {
        try {
            lock.lock();
            System.out.println("开始wait...");
            condition.await();
            for (int i = 0; i < 5; i++) {
                System.out.println("ThreadName=" + Thread.currentThread().getName()
                       + (i + 1));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}
```

##### 8：ReentranLock 和 Synchronized 的区别

###### 1：ReentrantLock 可以实现公平锁

```java
// Lock 实现公平锁
public ReentrantLock(boolean fair) {
	sync = fair ? new FairSync() : new NonfairSync();
}
Lock lock = new ReentrantLock(true);
```

###### 2：ReentrantLock 可中断响应

​	当使用 synchronized 实现锁时，阻塞在锁上的线程除非获得锁否则将一直等待下去，而ReentrantLock给我们提供了一个可以中断响应的获取锁的方法lockInterruptibly()，该方法可以用来解决死锁问题，被中断的线程将抛出异常释放已获得锁，而另一个线程将获取锁后正常结束

###### 3：获取锁时限时等待

​	ReentrantLock 还提供了获取锁限时等待的方法

- tryLock(long time, TimeUnit unit)
  - 传入时间参数：表示等待指定的时间
  - 无参则表示立即返回锁申请的结果，true表示获取锁成功,false表示获取锁失败

##### 9：公平锁、非公平锁：多个线程竞争锁时需不需要排队

```java
Lock lock = new ReentrantLock(true);
// 非公平锁，设置优先级，效率高
Lock lock = new ReentrantLock(false);
```

- 公平锁：指的是线程获取锁的顺序是按照申请锁顺序来的，而非公平锁指的是抢锁机制，先lock的线程不一定先获得锁
- 公平锁：严格的以FIFO的方式进行锁的竞争，但是非公平锁是无序的锁竞争，刚释放锁的线程很大程度上能比较快的获取到锁，队列中的线程只能等待，所以非公平锁可能会有“饥饿”的问题
- 重复的锁获取能减小线程之间的切换，而公平锁则是严格的线程切换，这样对操作系统的开销是比较大的，所以非公平锁的吞吐量是大于公平锁的，这也是为什么JDK将非公平锁作为默认的实现

##### 10：共享锁、独享锁 ：多个线程能不能共享同一把锁

​	通过AQS来实现的，通过实现不同的方法，来实现独享或者共享

- 独享锁(排他锁)：该锁一次只能被一个线程所持有，synchronized 和 JUC 中 Lock的实现类基本都是互斥锁
- 共享锁：指该锁可被多个线程所持有，如果线程T对数据A加上共享锁后，则其他线程只能对A再加共享锁，不能加排它锁，获得共享锁的线程只能读数据，不能修改数据
  - ReadWriteLock，其读锁是共享锁，其写锁是独享锁

##### 11：ReadWriteLock：管理一组锁，一个是只读的锁，一个是写锁

```java
public interface ReadWriteLock {
    // Returns the lock used for reading
    Lock readLock();
    // Returns the lock used for writing
    Lock writeLock();
}
```

##### 12：ReentrantReadWriteLock

- ReadLock和WriteLock是靠内部类Sync实现的锁，Sync是AQS的一个子类
- 读锁是共享锁，写锁是独享锁。读锁的共享锁可保证并发读非常高效，而读写、写读、写写的过程互斥，因此读锁和写锁是分离的
- 当有读锁时，写锁就不能获得，而当有写锁时，除了获得写锁的这个线程可以获得读锁外，其他线程不能获得读锁

```java
public class ReentrantReadWriteLock implements ReadWriteLock, java.io.Serializable {
  // Inner class providing readlock
  private final ReentrantReadWriteLock.ReadLock readerLock;
  // Inner class providing writelock
  private final ReentrantReadWriteLock.WriteLock writerLock;
  //Sync是AQS的一个子类
  final Sync sync;
  
	// Inner class
  public static class ReadLock implements Lock, java.io.Serializable {
    private final Sync sync;
    // 实现的Lock接口的方法
    
    // 返回获取了几次读锁，读锁特有的
    public String toString() {
            int r = sync.getReadLockCount();
            return super.toString() +
                "[Read locks = " + r + "]";
    }
  }
}
```

