### CAS、AQS

------

##### 1：CAS（Compare and swap）：比较与交换

​	是一种无锁算法，乐观锁的 一种实现

###### 无锁编程：

​	即不使用锁的情况下实现多线程之间的变量同步，也就是在没有线程被阻塞的情况下实现变量的同步，所以也叫非阻塞同步

###### CAS 算法涉及到三个操作数：

1. 读写的内存值 （当前值）V
2. 进行比较的值 （预期值）A
3. 要写入的新值（新值）     B

- 当且仅当 V 的值等于 A 时，CAS通过原子方式用新值B来更新V的值（“比较+更新”是一个原子操作），若有一方失败，不会执行任何操作，“更新”是一个不断重试的操作
- 通过 自旋  CAS 的方式来实现原子操作

###### Java中 java.util.concurrent.atomic 包下相关类就是 CAS的实现，这些类存在的目的是对相应的数据进行原子操作

- AtomicBoolean：可以用原子方式更新的 `boolean` 值
- AtomicInteger：可以用原子方式更新的 `int` 值
- AtomicLong：可以用原子方式更新的 `long` 值
- AtomicIntegerFieldUpdater<T>：基于反射的实用工具，可以对指定类的指定 volatile int 字段进行原子更新
- AtomicIntegerArray：数组类型

```java
// value 是 AtomicLong对应的当前值，保证可见性，不保证原子性
private volatile long value;
// 内存位置的值
private static final long valueOffset;

// 返回AtomicLong对应的long值
public final long get() {
  return value;
}
// 比较AtomicLong的原始值是否与expect相等，若相等的话，则设置AtomicLong的值为update。
public final boolean compareAndSet(long expect, long update) {
  // 当前值变为预期值，会重新读取当前值进行比较
  return unsafe.compareAndSwapLong(this, valueOffset, expect, update);
}
/*
	使用一个死循环，先得到当前的值value，然后再把当前的值加一，加完之后使用cas原子操作让当前值加一处理正确。当然cas原子操作不一定是成功的，所以做了一个死循环，当cas操作成功的时候返回数据。这里由于使用了cas原子操作，所以不会出现多线程处理错误的问题*/
public final long incrementAndGet() {
  for (;;) {
    // 获取AtomicLong当前对应的long值
    long current = get();
    // 将current + 1
    long next = current + 1;
    // 通过CAS函数，更新current的值，若更新失败，则一直更新
    if (compareAndSet(current, next))
      return next;
  }
}
```

- 例：线程A得到current为1，线程B也得到current为1；线程A的next值为2，进行cas操作并且成功的时候，将value修改成了2；这个时候线程B也得到next值为2，当进行cas操作的时候由于expected值已经是2，而不是1了；所以cas操作会失败，下一次循环的时候得到的current就变成了2；也就不会出现多线程处理问题了

###### 示例：

​	Java中，i++ 等类似操作并不是线程安全的，因为  i++可分为三个独立的操作：获取变量当前值，为该值+1，然后写回新的值，在没有额外资源可以利用的情况下，只能使用加锁才能保证读-改-写这三个操作时“原子性”的。但是利用加锁的方式来实现该功能的话，代码将比较简单

```java
synchronized (lock) {
 	i++;  
}
```

##### 2：CAS 仍然存在三大问题：

###### 1：ABA 问题：

- CAS需要在操作值的时候检查内存值是否发生变化，没有发生变化才会更新内存值。但是如果内存值原来是A，后来变成了B，然后又变成A，那么CAS进行检查时会发现值没有发生变化，但是实际上是有变化的
- 解决思路就是在变量前面添加版本号，每次变量更新的时候都把版本号加一，这样变化过程就从“A－B－A”变成了“1A－2B－3A”
- compareAndSet（）：**AtomicStampedReference 类解决ABA问题**，具体封装在 compareAndSet() 中
- 首先检查当前引用和当前标志与预期引用和预期标志是否相等，如果都相等，则以原子方式将引用值和标志的值更新为给定的值

```java
// 使用 CAS 实现线程安全计数器  
for (;;) {
  int i = ai.get();  
  // 如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值  
  boolean suc = ai.compareAndSet(i, ++i);  
  if (suc) {  
    break;  
  }  
}
```

###### 2：循环时间长，开销大

​	自旋CAS（不成功就一直循环执行直到成功为止）如果长时间不成功，会给CPU带来非常大的执行开销

- 如果JVM能支持处理器提供的**pause指令**那么效率会有一定的提升
- pause 指令有两个作用
  1. 它可以延迟流水线执行指令（de-pipeline）,使CPU不会消耗过多的执行资源，延迟的时间取决于具体实现的版本，在一些处理器上延迟时间是零
  2. 它可以避免在退出循环的时候因内存顺序冲突（memoryorder violation）而引起CPU流水线被清空（CPU pipeline flush），从而提高CPU的执行效率

###### 3：只能保证一个共享变量的原子操作

​	当对一个共享变量执行操作时，可以使用CAS的方式来保证原子操作，但是对多个共享变量操作时，循环CAS就无法保证操作的原子性

- 使用锁
- 将多个共享变量合并成一个共享变量来操作、
  - 比如有两个共享变量i＝2,j=a，合并一下ij=2a，然后用CAS来操作 ij

##### 2：AQS（AbustactQueuedSynchronizer）：

​	它是一个 Java 提高底层同步的工具类，用一个 int类型的变量 表示同步状态，并提供了一系列的CAS操作来管理这个同步状态

###### 同步队列：

​	是 AQS 重要的组成部分，它是一个双端队列，遵循FIFO原则，主要作用是用来存放阻塞的线程，当一个线程尝试获取锁时，如果已经被占用，那么当前线程就会被构造成一个Node节点放入到同步队列的尾部，队列的头节点是已经成功获取锁的节点，当头节点线程释放锁时，会唤醒后面的节点并释放当前头节点的引用

##### 3：独享锁的获取和释放

![](https://github.com/likang315/Java-and-Middleware/blob/master/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E5%A4%9A%E7%BA%BF%E7%A8%8B/%E7%8B%AC%E5%8D%A0%E9%94%81%E7%9A%84%E8%8E%B7%E5%8F%96%E4%B8%8E%E9%87%8A%E6%94%BE.png?raw=true)

###### 获取锁

1. 调用入口方法acquire(arg)
2. 调用模版方法tryAcquire(arg)尝试获取锁，若成功则返回，若失败则走下一步
3. 将当前线程构造成一个Node节点，并利用CAS将其加入到同步队列到尾部，然后该节点对应的线程进入自旋状态，自旋时，首先判断其前驱节点是否为头节点 && 是否成功获取同步状态，两个条件都成立，则将当前线程的节点设置为头节点，如果不是，则利用LockSupport.park(this)将当前线程挂起 ,等待被前驱节点唤醒

###### 释放锁

1. 调用入口方法release(arg)
2. 调用模版方法tryRelease(arg)释放同步状态
3. 获取当前节点的下一个节点，利用 LockSupport.unpark(currentNode.next.thread) 唤醒后继节点

##### 4：共享锁的获取和释放

###### 获取锁

1. 调用acquireShared(arg)入口方法
2. 进入tryAcquireShared(arg)模版方法获取同步状态，如果返回值>=0，则说明同步状态(state)有剩余，获取锁成功直接返回
3. 如果 tryAcquireShared(arg) 返回值 <0，说明获取同步状态失败，向队列尾部添加一个共享类型的Node节点，随即该节点进入自旋状态
4. 自旋时，首先检查前驱节点是否为 头节点 & tryAcquireShared() 是否 >=0 (即成功获取同步状态)
   - 如果是，则说明当前节点可执行，同时把当前节点设置为头节点，并且唤醒所有后继节点
   - 如果否，则利用 LockSupport.unpark(this) 挂起当前线程，等待被前驱节点唤醒

###### 释放锁

1. 调用 releaseShared(arg) 模版方法，释放同步状态												
2. 如果释放成，则遍历整个队列，利用LockSupport.unpark(nextNode.thread)唤醒所有后继节点

##### 5：独享锁和共享锁在实现上的区别

- 独享锁的同步状态值为1，即同一时刻只能有一个线程成功获取同步状态，共享锁的同步状态 >1，取值由上层同步组件确定
- 独享锁队列中头节点运行完成后通知它的直接后继节点，共享锁队列中头节点运行完成后通知它后面的所有后继节点
- 共享锁中会出现多个线程（即同步队列中的节点）同时成功获取同步状态的情况，独享锁不可能存在