## 线程同步：

###### 同步执行：多个线程必须排队执行

###### 异步执行：多个线程可以同时进行

### Synchronized ，ReentrantLock和 volatile 变量，CAS 实现同步机制

多线程并发访问同一资源时，就会形成“抢”的现象，由于线程切换实际不确定，可能导致执行代码顺序的混乱，
严重时会导致系统瘫痪

实例：上厕所，上锁

![](G:\Java\Java_note\9：多线程\锁.png)

加入同步锁来避免在该线程没有完成操作之前，被其他线程的调用，从而避免了多线程的并发性

### synchronized 是Java中的关键字，是一种同步锁，通常称为重量级锁

Synchronized 锁 原理

###### synchronized 关键字是通过字节码指令来实现的

###### synchronized 关键字编译后会在同步块前后形成monitorenter和monitorexit两个字节码指令

###### 执行monitorenter指令时需要先获得对象的锁（每个对象有一个监视器锁monitor），如果这个对象没被锁或者当前线程已经获得此锁（也就是重入锁），那么锁的计数器+1。如果获取失败，那么当前线程阻塞，直到锁被对另一个线程释放

###### 执行monitorexit指令时，计数器减一，当为 0 的时候锁释放



###### synchronized 修饰方法时，该方法为同步方法，即：多个线程不能同时进入方法内部执行

对于方法而言，synchronized 锁会在一个线程调用该方法时将该方法所属对象加锁，其他线程在执行此方法时
由于执行此方法的线程没有释放锁，所以只能在方法外阻塞，直到持有同步锁的线程将方法执行完毕，释放锁，此线程获取同步锁，所以，解决多线程并发问题的办法就是讲“抢”变为“排队”

同一个锁对象可以产生互斥作用，不同锁对象不能产生互斥作用

```java
public synchronized void method()
{
}
```

注意：

1：synchronized关键字不能继承，若需要同步，在子类的重写方法添加synchronized关键字

2：在定义接口方法时不能使用synchronized关键字，不能被继承

3：构造方法不能使用synchronized关键字，但可以使用synchronized代码块来进行同步

###### synchronized修饰的静态方法锁定的是这个类的所有对象

```java
public static synchronized  method()
{
}
```

注意：若修饰静态方法：属于类的，具有同步效果，按顺序执行，与对象无关
	

### synchronized 块（同步块）：

要求多个线程对该块内的代码排队执行，但是前提条件是同步监视器对象，即：要求多个线程看到的必须是同一个对象（上锁对象），可以有效的缩小同步范围，并保证并发安全的同时尽可能的提高效率

##### this 锁

```java
synchronized(this) //监视器对象
{
	需要同步的代码
}
```

##### synchronized 块 this锁 之间具有同步性

即：当一个线程访问对象的一个synchronized  同步块，其他线程仍对同一个对象中所有的其他synchronized 同步块的访问将被阻塞，因为在**执行synchronized代码块时会锁定当前的对象**，只有执行完该代码块才能释放该对象锁，下一个线程才能执行并锁定该对象

###### 当一个线程访问对象的一个synchronized(this)同步代码块时，另一个线程仍然可以访问该对象中的非synchronized(this)同步代码块



##### 非this

```java
synchronized() //监视器对象
{
	需要同步的代码
}
```

当有一个明确的对象作为锁时

```java
public void  method3(SomeObject obj)
{
   //obj 锁定的对象
   synchronized(obj)
   {
      // todo
   }
}
```

当没有明确的对象作为锁，只是想让一段代码同步时，可以创建一个特殊的对象来充当锁：

```java
public void method()
{
    private Byte[] local= new Byte[0];
    synchronized(local)
    {
        //todo...
    }
}
```



##### synchronized 可以作用于类，通过反射，class锁

```java
synchronized(ClassName.class)
{
         // todo
}
```

synchronized 作用于一个类<T>时，是给这个类<T>加锁，T的所有对象用的是同一把锁

### 线程安全的API

###### StringBuffer ，Vector，HashTable，ConcurrentHashMap线程安全的类

将给定集合转换为线程安全的集合：用Collection的工具类 Collections
static <T> List<T> synchronizedList(List<T> list) 
      	返回指定列表支持的同步（线程安全的）列表 
static <T> Set<T>  synchronizedSet(Set<T> s) 
      	返回指定 set 支持的同步（线程安全的）set	
static <K,V> Map<K,V>  synchronizedMap(Map<K,V> m) 
      	返回由指定映射支持的同步（线程安全的）映射

##### API手册上说明：

就算是线程安全的集合那么其中对于元素的操作，如add，remove方法都不予迭代器遍历做互斥，需要自行维护互斥关系，fail-fast机制

### 并发编程的三个特性

###### 原子性，有序性

###### 可见性：指当多个线程访问同一个变量时，一个线程修改了这个变量的值，其他线程能够立即看得到修改的值

当一个变量被volatile修饰后，当一个线程修改共享变量后他会立即被更新到主内存中，其他线程读取共享变量时，会直接从主内存中读取。当然，synchronize和Lock都可以保证可见性。synchronized和Lock能保证同一时刻只有一个线程获取锁然后执行同步代码，并且在释放锁之前会将对变量的修改刷新到主存当中。因此可以保证可见性

##### 

### 重入锁（Lock）：实现线程同步

重进入：是指任意线程在获取到锁之后能够再次获取该锁而不会被锁阻塞

###### 可重入锁：指的是可重复递归调用的锁，在外层使用锁之后，在内层仍然可以获得此锁，并且不发生死锁（前提得是同一个对象或者class），ReentrantLock 和synchronized 都是可重入锁

###### 不可重入锁：与可重入锁相反，不可递归调用，递归调用就发生死锁



### 为什么支持重复加锁？

因为源码中用变量 c 来保存当前锁被获取了多少次，故在释放时，对 c 变量进行减操作，只有 c 变量为 0 时，才算锁的最终释放。所以可以 lock 多次，同时 unlock 也必须与 lock 同样的次数

### public  interface  Lock

```java
void	lock()  // 能获得锁就返回true，不能的话一直等待获得锁
void	lockInterruptibly()
//如果两个线程分别执行以上两个方法，但此时中断这两个线程，前者不会抛出异常，而后者会抛出异常
    
Condition	newCondition() //返回锁的状态
    
boolean	tryLock()
boolean	tryLock(long time, TimeUnit unit)
//tryLock能获得锁就返回true，不能就立即返回false，tryLock(long timeout,TimeUnit unit)，可以增加时间限制，如果超过该时间段还没获得锁，返回false
    
void	unlock()  //释放锁
```

### java.util.concurrent.locks  

### Class ReentrantLock：可重入锁

###### 是可重入、实现了Lock接口的锁，它与使用synchronized方法和块具有相同的基本行为和语义，并且扩展了其能力  

```java
public class MyService {
    private final reentrantlock lock=new reentrantlock（）；
    public void testMethod() {
        lock.lock(); //先获得锁，再处理业务
        for (int i = 0; i < 5; i++) {
            System.out.println("ThreadName=" + Thread.currentThread().getName()
                    + (" " + (i + 1)));
        }
        lock.unlock();
    }
}
```

```java
//创建Condition对象来使线程wait或者唤醒，必须先执行lock.lock方法获得锁
public class MyService {
    private final reentrantlock lock=new reentrantlock（）；
    private Condition condition = lock.newCondition();
    public void testMethod() {
        try {
            lock.lock();
            System.out.println("开始wait");
            condition.await();
            for (int i = 0; i < 5; i++) {
                System.out.println("ThreadName=" + Thread.currentThread().getName()
                        + (" " + (i + 1)));
            }
        } catch (InterruptedException e) {
            // TODO 自动生成的 catch 块
            e.printStackTrace();
        }
        finally
        {
            lock.unlock();
        }
    }
}
```

###### condition对象的signal()方法可以唤醒wait线程，ReentrantLock类可以唤醒指定条件的线程，而object的wait()的唤醒是随机的

Condition类的signal 方法和Object类的notify 方法等效													Condition类的signalAll 方法和Object类的notifyAll 方法等效

### Lock的 公平锁和 非公平锁：

```java
Lock lock=new ReentrantLock(true);//公平锁
Lock lock=new ReentrantLock(false);//非公平锁，设置优先级，使用效率高
```

###### 公平锁：指的是线程获取锁的顺序是按照加锁顺序来的，而非公平锁指的是抢锁机制，先lock的线程不一定先获得锁

公平锁是严格的以FIFO的方式进行锁的竞争，但是非公平锁是无序的锁竞争，刚释放锁的线程很大程度上能比较快的获取到锁，队列中的线程只能等待，所以非公平锁可能会有“饥饿”的问题。但是重复的锁获取能减小线程之间的切换，而公平锁则是严格的线程切换，这样对操作系统的影响是比较大的，所以非公平锁的吞吐量是大于公平锁的，这也是为什么JDK将非公平锁作为默认的实现

### 读写锁：Lock类有读锁和写锁，读读共享，写写互斥，读写互斥

###### 读锁，此时多个线程可以或得读锁

###### 写锁，此时只有一个线程能获得写锁

```java
public void read() {
        try {
            try {
                lock.readLock().lock();
                System.out.println("获得读锁" + Thread.currentThread().getName()
                        + " " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.readLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void write() {
        try {
            try {
                lock.writeLock().lock();
                System.out.println("获得写锁" + Thread.currentThread().getName()
                        + " " + System.currentTimeMillis());
                Thread.sleep(10000);
            } finally {
                lock.writeLock().unlock();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
```



### 乐观锁与悲观锁：

悲观锁：是一种独占锁，而 synchronized 就是一种独占锁，synchronized 会导致其它，所有未持有锁的线程阻塞，而去等待持有锁的线程释放锁

乐观锁：每次不加锁而是假设没有冲突而去完成某项操作，如果因为冲突失败就重试，直到成功为止。而乐观锁用到的机制就是CAS



### CAS（Compare And Set）: 原子操作，解决多线程并行情况下使用锁造成性能损耗的一种机制

###### CAS操作包含三个操作数：内存位置（V）、预期值（A），新值(B）

如果内存位置的值与预期值相匹配，那么处理器会自动将该位置值更新为新值。否则，处理器不做任何操作。无论哪种情况，它都会在CAS指令之前返回该位置的值

CAS 有效地说明了“我认为位置V应该包含值A；如果包含该值，则将B放到这个位置；否则，不要更改该位置，只告诉我这个位置现在的值即可

在java 中可以通过 **锁 和循环  CAS 的方式来实现原子操作**，Java中 java.util.concurrent.atomic 包相关类就是 CAS的实现

​	AtomicBoolean：可以用原子方式更新的 `boolean` 值

​	AtomicInteger：可以用原子方式更新的 `int` 值

​	AtomicLong：可以用原子方式更新的 `long` 值

​	AtomicIntegerFieldUpdater<T>：基于反射的实用工具，可以对指定类的指定 `volatile int` 字段进行原子更新

###### atomic 包中的类可将 `volatile` 值、字段和数组元素的概念扩展到那些也提供原子条件更新操作的类

 Java中，i++ 等类似操作并不是线程安全的，因为  i++可分为三个独立的操作：获取变量当前值，为该值+1，然后写回新的值。在没有额外资源可以利用的情况下，只能使用加锁才能保证**读-改-写**这三个操作时“原子性”的。但是利用加锁的方式来实现该功能的话，代码将非常复杂及难以维护

```java
synchronized (lock) {  
 i++;  
}  
```

###### compareAndSet方法：如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值，不是锁的常规替换方法，仅当对象的重要更新限定于单个 变量时才应用它

```java
/** 使用CAS实现线程安全计数器 */  
    private void safeCount() {  
        for (;;) {  
            int i = ai.get();  
            // 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值  
            boolean suc = ai.compareAndSet(i, ++i);  
            if (suc) {  
                break;  
            }  
        }  
    }  
```

CAS虽然很高效的解决原子操作，但是CAS仍然存在三大问题：**ABA问题、循环时间长开销大、只能保证一个共享变量的原子操作**

### ABA问题：

AtomicStampedReference 来解决**ABA问题**，这个类的 compareAndSet方法作用是首先检查当前引用是否等于预期引用，并且当前标志是否等于预期标志（版本号），如果全部相等，则以原子方式将该引用和该标志的值设置为给定的更新值

### 循环时间长开销大：

自旋CAS如果长时间不成功，会给CPU带来非常大的执行开销。如果JVM能支持处理器提供的pause指令那么效率会有一定的提升，pause指令有两个作用，第一它可以延迟流水线执行指令（de-pipeline）,使CPU不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零。第二它可以避免在退出循环的时候因内存顺序冲突（memoryorder violation）而引起CPU流水线被清空（CPU pipeline flush），从而提高CPU的执行效率

### 只能保证一个共享变量的原子操作：

当对一个共享变量执行操作时，我们可以使用循环CAS的方式来保证原子操作，但是对多个共享变量操作时，循环CAS就无法保证操作的原子性，这个时候就可以用锁，或者有一个取巧的办法，就是把多个共享变量合并成一个共享变量来操作。比如有两个共享变量i＝2,j=a，合并一下ij=2a，然后用CAS来操作ij。从Java1.5开始JDK提供了AtomicReference类来保证引用对象之间的原子性，你可以把多个变量放在一个对象里来进行CAS操作



### volatile：是Java提供的一种轻量级的同步机制

###### volatile：更轻量级，它不会引起线程上下文的切换和调度，但是volatile 变量的同步性较差（有时它更简单并且开销更低），而且其使用也更容易出错

###### 1：保证可见性，不保证原子性

volatile变量更新时，JMM会把该线程本地内存中的变量强制刷新到主内存中去，这个写会操作会导致其他线程中的缓存无效

###### 2：禁止指令重排 

用volatile修饰的共享变量，在编译时，会在指令序列中插入内存屏障来禁止特定类型的处理器重排序

##### 内存屏障

加入volatile关键字时，会多出一个lock前缀指令，lock前缀指令实际上相当于一个内存屏障（也成内存栅栏）

###### 内存屏障会提供3个功能：

1：它确保指令重排序时不会把其后面的指令排到内存屏障之前的位置，也不会把前面的指令排到内存屏障的后面，即在执行到内存屏障这句指令时，在它前面的操作已经全部完成
2：它会强制将对缓存的修改操作立即写入主存															3：如果是写操作，它会导致其他CPU中对应的缓存行无效

### 重排序

指编译器和处理器为了优化程序性能而对指令序列进行重新排序的一种手段

###### 重排序需要遵守一定规则：

重排序操作不会对存在数据依赖关系的操作进行重排序

重排序是为了优化性能，但是不管怎么重排序，单线程下程序的执行结果不能被改变

##### 多线程中的非同步问题主要出现在对域的读写上，如果让域自身避免这个问题，则就不需要修改操作该域的方法。用final域，有锁保护的域和 volatile 域可以避免非同步的问题








