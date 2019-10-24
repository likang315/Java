### Java中的锁

------

​	主要介绍Java并发包中与锁相关的API和组件，以及这些API和组件的使用方式和实现细节

- 如何使用
- 怎么实现

#### 显示获取锁并发编程

##### 1：Interface Lock

​	锁是用来控制多个线程访问共享资源的方式，一般来说，一个锁能够防止多个线程同时访问共享资源（但是有些锁可以允许多个线程并发的访问共享资源，比如读写锁）。

- 在Lock接口出现之前，Java程序是靠synchronized关键字实现锁功能的，而Java SE 5之后，并发包中新增了Lock接口（以及相关实现类）用来实现锁功能，它提供了与synchronized关键字类似的同步功能，只是在使用时需要显式地获取和释放锁，显示使用锁，扩展其功能

```java
Lock lock = new ReentrantLock();
lock.lock();
try {
  // 不要把获取锁的代码放到这，可能会导致获取锁时发生了异常，异常抛出的同时，也会导致锁无故被释放
} finally {
	lock.unlock();
}
```

###### 源码

```java
public interface Lock {
    // 获取锁，调用该方法当前线程将会获取锁，当锁获取成功后，从该方法返回
    void	lock();
    // 可中断的获取锁，即在锁获取的过程中可以中断当前线程
    void	lockInterruptibly();
  	// 尝试非阻塞的获取锁，调用该方法后立即返回，如果能获得锁就返回true，不能就立即返回false
  	boolean	tryLock()
    // 超时等待获取锁，如果超过该时间段还没获得锁，返回false
    boolean	tryLock(long time, TimeUnit unit) throws InterruptedException;
    // 释放锁
    void unlock();
    // 获取等待-通知组件，该组件和当前锁绑定，当前线程只有获得了锁，才能调用该组件的wait(),而调用后当前线程将释放锁
    Condition	newCondition();
}
```

##### 2：队列同步器（AQS）

​	队列同步器AbstractQueuedSynchronizer，是用来构建锁或者其他同步组件的基础框架，它使用了一个int成员变量表示同步状态，通过内置的FIFO队列来完成资源获取线程的排队工作

###### AQS原理

​	同步器的主要使用方式是**继承**，子类通过继承同步器并实现它的抽象方法来管理同步状态，在抽象方法的实现过程中免不了要**对同步状态进行更改**，这时就需要使用同步器提供的3个方法（getState()、setState(int newState)和compareAndSetState(int expect,int update)）来进行操作，因为它们能够保证状态的改变是安全的

###### 同步器和锁的关系

​	同步器是锁的实现者，锁是面向用户使用的

###### 同步器允许重写的方法

- protected boolean tryAcquire(int arg)
  - 独占式的获取同步状态，实现该方法需要查询当前状态并判断同步状态是否符合预期，然后在进行CAS设置同步状态
- protected boolean tryRelease(int arg)
  - 独占式的释放同步状态，等待获取同步状态的线程将有机会获得同步状态
- protected boolean tryAcquireShared(int arg)
  - 共享式的获取同步状态，返回大于等于0的值，表示获取成功，反之获取失败
- protected boolean tryReleaseShared(int arg)
  - 共享式的释放同步状态
- protected boolean isHeldExclusively()
  - 表示是否被当前线程锁独占

```java
/**
* 独占锁的获取和释放
*/
class Mutex implements Lock {
    // 静态内部类，自定义同步器
    private static class Sync extends AbstractQueuedSynchronizer {
      // 是否处于占用状态
      protected boolean isHeldExclusively() {
        return getState() == 1;
      }
      // 当状态为0的时候获取锁
      public boolean tryAcquire(int acquires) {
        		final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            }
            else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0) 
                  	// overflow
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
      }
      // 释放锁，将状态设置为0
      protected boolean tryRelease(int releases) {
      		int c = getState() - releases;
        	if (Thread.currentThread() != getExclusiveOwnerThread())
          	throw new IllegalMonitorStateException();
        	boolean free = false;
        	if (c == 0) {
          	free = true;
          	setExclusiveOwnerThread(null);
       	 	}
        	setState(c);
        	return free;
      }
      // 返回一个Condition，每个condition都包含了一个condition队列
      Condition newCondition() { return new ConditionObject(); }
    }
    // 仅需要将操作代理到Sync上即可
    private final Sync sync = new Sync();
    public void lock() { 
 				// 底层调用的还是重写的tryLock()
      	sync.acquire(1);
    }
    public boolean tryLock() { return sync.tryAcquire(1); }
    public void unlock() { sync.release(1); }
    public Condition newCondition() { return sync.newCondition(); }
    public boolean isLocked() { 
      	// 是否被当前线程所占用
      	return sync.isHeldExclusively();
    }
    public boolean hasQueuedThreads() { return sync.hasQueuedThreads(); }
  	// 如果当前方法被中断，将会抛出InterruptedException后返回
    public void lockInterruptibly() throws InterruptedException {
      sync.acquireInterruptibly(1);
    }
    public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException {
      return sync.tryAcquireNanos(1, unit.toNanos(timeout));
    }
}
```

##### 3：AQS（队列同步器）实现剖析

###### 同步队列

​	同步器依赖内部的**同步队列（一个FIFO双向队列）来完成同步状态的管理**，当前线程获取同步状态失败时，同步器会将当前线程以及等待状态等信息构造成为一个节点（Node）并将其**加入同步队列，同时会阻塞当前线程**，当同步状态释放时，会把首节点中的线程唤醒，使其再次尝试获取同步状态。

###### Node节点

​	同步队列中的节点（Node）用来保存**获取同步状态失败的线程引用**、**等待状态**以及**前驱和后继节点，节点的属性类型**与名称以及描述

- int waitStatus：等待状态
  - static final int CANCELLED =  1，由于在队列中等待线程等待超时或者被中断，需要从队列中取消等待，之后节点进入该状态将不会变化
  - static final int SIGNAL  = -1，当前节点的线程如果释放了同步状态或者被取消，将会通知后继节点，使得后继节点得以继续运行
  - static final int CONDITION = -2，该节点在等待队列中，当其他线程调用signal（）后，该节点将会加入到对同步状态的获取中
  - static final int PROPAGATE = -3，表示下一次共享式同步状态获取将会无条件的传播下去
- Node prev：前驱节点
- Node next：后继节点
- Node nextWaiter：等待队列中的后继节点，如果当前节点是共享的，字段值为SHARED常量
- Thread thread：获取同步状态的线程

###### 同步队列的结构

​	同步器包含了两个节点类型的引用，一个指向头节点，而另一个指向尾节点，当一个线程成功地获取了同步状态，其他线程将无法获取到同步状态，转而被构造成为节点并加入到同步队列中，而这个加入队列的过程必须要保证线程安全，因此同步器提供了一个基于CAS的设置尾节点的方法：compareAndSetTail(Node expect,Nodeupdate)，它需要传递当前线程“认为”的尾节点和当前节点，只有设置成功后，当前节点才正式与之前的尾节点建立关联。

- 同步队列遵循FIFO，首节点是获取同步状态成功的节点，首节点的线程在释放同步状态时，将会唤醒后继节点，而后继节点将会在获取同步状态成功时将自己设置为首节点

![](https://github.com/likang315/Java-and-Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%90%8C%E6%AD%A5%E9%98%9F%E5%88%97%E7%9A%84%E5%9F%BA%E6%9C%AC%E7%BB%93%E6%9E%84.png?raw=true)

###### 独占式同步状态的获取和释放

- 在获取同步状态时，同步器维护一个同步队列，获取状态失败的线程构造成一个Node节点，通过CAS安全的加入到队列的尾部并在队列中进行自旋；
- 当头节点的线程释放了同步状态之后，将会唤醒其后继节点，后继节点的线程被唤醒后需要检查自己的前驱节点是否是头节点，若是获取同步状态，获取成功后将当前节点设置为头结点；
- 在释放同步状态时，同步器调用tryRelease(int arg)方法释放同步状态，然后唤醒头节点的后继节点，继而让后继节点重新尝试获取同步状态；

###### 共享式同步状态获取与释放

- 共享式获取与独占式获取最主要的区别在于同一时刻能否有多个线程同时获取到同步状态

- 以文件的读写为例，如果一个程序在对文件进行读操作，那么这一时刻对于该文件的写操作均被阻塞，而读操作能够同时进行。写操作要求对资源的独占式访问，而读操作可以是共享式访问；

- ```java
  public final void acquireShared(int arg) {
      if (tryAcquireShared(arg) < 0)
      doAcquireShared(arg);
  }
  ```

- 在doAcquireShared(int arg)方法的自旋过程中，如果当前节点的前驱为头节点时，尝试获取同步状态，如果返回值大于等于0，表示该次获取同步状态成功并从自旋过程中退出；

- releaseShared(int arg)在释放同步状态之后，将会唤醒后续处于等待状态的节点。而且必须确保同步状态（或者资源数）线程安全释放，一般是通过循环和CAS来保证的，因为释放同步状态的操作会同时来自多个线程；

###### 独占式超时获取同步状态

- tryAcquireNanos(int arg,long nanosTimeout) 方法在支持响应中断的基础上，增加了**超时**获取的特性
- 针对超时获取，主要需要计算出需要睡眠的时间间隔nanosTimeout，为了防止过早通知，nanosTimeout计算公式为：**nanosTimeout -= now - lastTime**，其中now为当前唤醒时间，lastTime为上次唤醒时间，如果nanosTimeout大于0则表示超时时间未到，需要继续睡眠nanosTimeout纳秒，反之，表示已经超时

![](https://github.com/likang315/Java-and-Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E7%8B%AC%E5%8D%A0%E5%BC%8F%E5%90%8C%E6%AD%A5%E7%8A%B6%E6%80%81%E7%9A%84%E8%8E%B7%E5%8F%96.png?raw=true)

##### 4：重入锁(Lcok)：表示该锁能够支持一个线程对资源的重复加锁

- 重进入：指任意线程在获取到锁之后能够再次获取该锁而不会被锁阻塞
- 可重入锁：指的是可重复递归调用的锁，在外层使用锁之后，在内层仍然可以获得此锁，并且不发生死锁
  - ReentrantLock 和synchronized 都是可重入锁
- 不可重入锁：与可重入锁相反，不可递归调用，递归调用就发生死锁

##### 5：公平锁、非公平锁：多个线程竞争锁时需不需要排队

```java
Lock lock = new ReentrantLock(true);
// 非公平锁，设置优先级，效率高
Lock lock = new ReentrantLock(false);
```

- 公平锁：指线程获取锁的顺序是按照申请锁顺序来的，先lock的先获取锁
- 非公平锁：指的是抢锁机制，先lock的线程不一定先获得锁
- 公平性锁保证了锁的获取按照FIFO原则，而代价是进行大量的线程切换。非公平性锁虽然可能造成线程“饥饿”，但极少的线程切换，保证了其更大的吞吐量【100倍】。

###### ReentrantLock实现重进入需要满足两个特性【nonfairTryAcquire(int acquires)】

1. 线程再次获取锁
   - 锁需要去**识别获取锁的线程是否为当前占据锁的线程**，如果是，则再次成功获取。
2. 锁的最终释放
   - 线程重复n次获取了锁，随后在第n次释放该锁后，其他线程能够获取到该锁。锁的最终释放要求锁对于获取进行计数自增，计数表示当前锁被重复获取的次数，而锁被释放时，计数自减，当计数等于0时表示锁已经成功释放。

###### ReentrantLock实现公平锁【tryAcquire(int acquires)】

​	唯一不同的是判断条件多了hasQueuedPredecessors()方法，即加入了**同步队列中当前节点是否有前驱节点**的判断，如果该方法返回true，则表示有线程比当前线程更早地请求获取锁，因此需要等待前驱线程获取并释放锁之后才能继续获取锁。

##### 6：读写锁

​	读写锁在同一时刻可以允许多个读线程访问，但是在写线程访问时，所有的读线程和其他写线程均被阻塞。读写锁维护了一对锁，一个读锁和一个写锁，通过**分离读锁和写锁**，使得并发性和吞吐量相比一般的排他锁较好

- 共享的缓存结构（读大于写的场景）
- 在读写锁的未出现之前为了保证可见性使用等待通知机制，使读操作可以读到写之后的数据，而出现读写锁后，写锁释放后，所有操作即可执行
- Java并发包提供读写锁的实现是ReentrantReadWriteLock
- 支持重进入，读线程在获取读锁之后可以再次获取读锁，而写线程在获取写锁之后可以再次获取写锁和读锁
- **锁降级**：遵循先获取写锁，再获取读锁，然后释放写锁的次序，则写锁能够降级为读锁

###### 读写锁的接口

- ```java
  /**
  * 提供获取读写锁的方法
  */
  public interface ReadWriteLock {
      Lock readLock();
      Lock writeLock();
  }
  ```

- 其实现类为ReentrantReadWriteLock，其中ReadLock和WriteLock是两个内部类，实现了Lock接口

  ```java
  /**
  * 读写锁的使用示例
  */
  public class Cache {
      static Map<String, Object> map = new HashMap<String, Object>();
      static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
      static Lock r = rwl.readLock();
      static Lock w = rwl.writeLock();
      // 获取一个key对应的value
      public static final Object get(String key) {
          r.lock();
          try {
            	return map.get(key);
          } finally {
            	r.unlock();
          }
      }
      // 设置key对应的value，并返回旧的value
      public static final Object put(String key, Object value) {
          w.lock();
          try {
            	return map.put(key, value);
          } finally {
            	w.unlock();
          }
      }
  }
  ```

##### 7：剖析ReentrantReadWriteLock

1. 读写状态的设计
2. 写锁的获取与释放
3. 读锁的获取
4. 释放以及锁降级

###### 读写状态的设计

​	读写锁的自定义同步器需要在**同步状态（一个整型变量）上维护多个读线程和一个写线程的状态**，如果在一个整型变量上维护多种状态，就一定需要**“按位切割使用”**这个变量，读写锁将变量切分成了两个部分，高16位表示读，低16位表示写。

- 通过位运算快速确定读写状态的值。
  - 假设当前同步状态值为S，写状态等于S & 0x0000FFFF（将高16位全部抹去），读状态等于S >>> 16（无符号补0右移16位）。当写状态增加1时，等于S+1，当读状态增加1时，等于S+(1<<16)，是 S+0x00010000。
  - 若是读写状态都有值，则表示该线程已经获取了写锁，且重进入获取了读锁，若只有读状态有值，则以获取读锁。

###### 写锁的获取与释放

​	写锁是一个支持重进入的排它锁。如果当前线程已经获取了写锁，则增加写状态。如果当前线程在获取写锁时，读锁已经被获取（读状态不为0）或者该线程不是已经获取写锁的线程，则当前线程进入等待状态。

```java
protected final boolean tryAcquire(int acquires) {
  Thread current = Thread.currentThread();
  int c = getState();
  // 独占式锁的数量
  int w = exclusiveCount(c);
  if (c != 0) {
    // 存在读锁或者当前获取线程不是已经获取写锁的线程
    if (w == 0 || current != getExclusiveOwnerThread())
      return false;
    if (w + exclusiveCount(acquires) > MAX_COUNT)
      throw new Error("Maximum lock count exceeded");
    setState(c + acquires);
    return true;
  }
  if (writerShouldBlock() || !compareAndSetState(c, c + acquires)) {
    return false;
  }
  setExclusiveOwnerThread(current);
  return true;
}
```

###### 读锁的获取和释放

​	读锁是一个支持重进入的共享锁，它能够被多个线程同时获取，在没有其他写线程访问（或者写状态为0）时，读锁总会被成功地获取，而所做的也只是（线程安全的）增加读状态。如果当前线程已经获取了读锁，则增加读状态。如果当前线程在获取读锁时，写锁已被其他线程获取，则进入等待状态。

- 读锁的每次释放（线程安全的，可能有多个读线程同时释放读锁）均减少读状态，减少的值是（1<<16）。

```java
protected final int tryAcquireShared(int unused) {
  for (;;) {
    int c = getState();
    // 获取之后同步状态的值
    int nextc = c + (1 << 16);
    if (nextc < c)
      throw new Error("Maximum lock count exceeded");
    if (exclusiveCount(c) != 0 && owner != Thread.currentThread())
      return -1;
    // 依靠CAS保证增加读状态线程安全
    if (compareAndSetState(c, nextc))
      return 1;
  }
}
```

###### 锁降级









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

- 

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

