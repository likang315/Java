### Java的并发容器

------

##### 1：ConcurrentHashMap

​		参考集合笔记

##### 2：ConcurrentLinkedQueue

​	ConcurrentLinkedQueue是一个基于链表的非阻塞无界线程安全队列

###### 如果要实现一个线程安全的队列有两种方式：

1. 使用阻塞算法
   - 使用阻塞算法的队列可以**用一个锁（入队和出队用同一把锁）或两个锁**（入队和出队用不同的锁）等方式来实现。
2. 使用非阻塞算法
   - 非阻塞的实现方式则可以使用**循环CAS的方式**来实现

###### ConcurrentLinkedQueue 结构

​	ConcurrentLinkedQueue由head节点和tail节点组成，每个节点（Node）由节点元素（item）和指向下一个节点（next）的引用组成，节点与节点之间就是通过这个next关联起来，从而组成一张链表结构的队列。默认情况下head节点存储的元素为空，tail节点等于head节点。

###### 入队操作

​	整个入队过程主要做两件事情：第一是定位出尾节点；第二是使用CAS算法将入队节点设置成尾节点的next节点，如不成功则重试，入队方法永远返回ture。

###### 出队操作

​	出队操作，简单的使用CAS操作把当前节点的item值设置为null，然后通过重新设置头节点让该元素从队列里面摘除，被摘除的节点就成了孤立节点，这个节点会被在GC的时候会被回收掉。

```java
public class ConcurrentLinkedQueue<E> extends AbstractQueue<E>
        implements Queue<E>, java.io.Serializable {
   	private transient volatile Node<E> head;
    private transient volatile Node<E> tail;
		// 初始化空队列
    public ConcurrentLinkedQueue() {
        head = tail = new Node<E>(null);
    }
    public boolean offer(E e) {
        // e为null则抛出空指针异常
        checkNotNull(e);
        // 构造Node节点
        final Node<E> newNode = new Node<E>(e);
        // 从尾节点进行插入
        for (Node<E> t = tail, p = t;;) {
            // p.next为tail的后一个节点
            Node<E> q = p.next;
            // 如果q==null说明p是尾节点，则执行插入
            if (q == null) {
                // 使用CAS设置p节点的next节点，并发入队
                if (p.casNext(null, newNode)) {
                    // cas成功，则说明新增节点已经被放入链表，然后设置当前尾节点
                    if (p != t)
                        casTail(t, newNode);  // Failure is OK
                    return true;
                }
            }
            // 由于poll操作移除元素后有可能会把head变为自引用（默认值），所以要重新把head赋给p
            else if (p == q)
                p = (t != (t = tail)) ? t : head;
            else
                // 并发插入时，寻找尾节点
                p = (p != t && t != (t = tail)) ? t : q;
         } 
		}

		public E poll() {
        // continue 标记
        restartFromHead:
        // 无限循环
        for (;;) {
            for (Node<E> h = head, p = h, q;;) {
                // 保存当前节点值
                E item = p.item;
                // 当前节点有值则cas变为null
                if (item != null && p.casItem(item, null)) {
                    // cas成功标志当前节点以及从链表中移除
                    if (p != h) 
                        updateHead(h, ((q = p.next) != null) ? q : p);
                    return item;
                }
                // 当前队列为空则返回null
                else if ((q = p.next) == null) {
                    updateHead(h, p);
                    return null;
                }
                // 自引用了，则重新找新的队列头节点
                else if (p == q)
                    continue restartFromHead;
                else
                    p = q;
            }
        }
 		}
}
```

##### 3：阻塞队列

阻塞队列（BlockingQueue）是一个支持两个附加操作的队列，分别是支持阻塞的插入和移除方法，阻塞队列常用于生产者和消费者的场景，阻塞队列就是生产者用来存放元素、消费者用来获取元素的容器。

1. 支持阻塞的插入方法：意思是当队列满时，队列会阻塞插入元素的线程，直到队列不满。
2. 支持阻塞的移除方法：意思是在队列为空时，获取元素的线程会等待队列变为非空。

###### 普通队列的四种处理方式

当阻塞队列不可用或者是普通队列时，这两个附加操作提供了4种处理方式

| 方法/处理方式 | 抛出异常    | 返回特殊值 | 一直阻塞 | 超时退出             |
| ------------- | ----------- | ---------- | -------- | -------------------- |
| 插入方法      | add(e)      | offer(e)   | put(e)   | offer（e,time,unit） |
| 移除方法      | remove()    | poll()     | take（） | poll(time,unit)      |
| 检查方法      | element（） | peek()     | 不可用   | 不可用               |

- put和take分别尾首含有字母t，一直阻塞，offer和poll都含有字母o，返回特殊值。

##### 4：Java中的阻塞队列

1. ###### ArrayBlockingQueue：用数组实现的有界阻塞队列，其按照先入先出对元素进行排序

   - 默认情况下不保证线程公平的访问队列，所谓公平访问队列是指阻塞的线程，可以按照阻塞的先后顺序访问队列，即先阻塞线程先访问队列。非公平性是对先等待的线程是非公平的，当队列可用时，阻塞的线程都可以争夺访问队列的资格，有可能先阻塞的线程最后才访问队列。为了保证公平性，通常会降低吞吐量。
   - 可以创建一个公平性的阻塞队列，只是吞吐量会相应的降低，其实现原理是利用可重入锁

2. ###### LinkedBlockingQueue：是一个用链表实现的有界阻塞队列

   - 此队列的默认和最大长度为Integer.MAX_VALUE。此队列按照先进先出的原则对元素进行排序。

3. ###### PriorityBlockingQueue：是一个支持优先级的无界阻塞队列

   - 默认情况下元素采取自然顺序升序排列。也可以自定义类实现compareTo()方法来指定元素排序规则，或者初始化PriorityBlockingQueue时，指定构造参数Comparator来对元素进行排序。需要注意的是不能保证同优先级元素的顺序。

4. ###### DelayQueue：是一个支持延时获取元素的无界阻塞队列

   - 队列使用PriorityQueue来实现。队列中的元素必须实现Delayed接口，在创建元素时可以指定多久才能从队列中获取当前元素。只有在延迟期满时才能从队列中提取元素。
   - DelayQueue非常有用，可以将DelayQueue运用在以下应用场景。
     1. **缓存系统的设计**：可以用DelayQueue保存缓存元素的有效期，使用一个线程循环查询DelayQueue，一旦能从DelayQueue中获取元素时，表示缓存有效期到了。
     2. **定时任务调度**：使用DelayQueue保存当天将会执行的任务和执行时间，一旦从DelayQueue中获取到任务就开始执行，比如TimerQueue就是使用DelayQueue实现的。

5. ###### SynchronousQueue：是一个不存储元素的阻塞队列

   - 每一个put操作必须等待一个take操作，否则不能继续添加元素，队列本身不存储元素，非常适合传递性场景。 

6. ###### LinkedTransferQueue：是一个由链表结构组成的无界阻塞TransferQueue队列

   - 相对于其他阻塞队列，LinkedTransferQueue多了tryTransfer和transfer方法
     1. transfer方法
        - 如果当前有消费者正在等待接收元素（消费者使用take()方法或带时间限制的poll()方法时）transfer方法可以把生产者传入的元素立刻transfer（传输）给消费者。如果没有消费者在等待接收元素（让CPU自旋），transfer方法会将元素存放在队列的tail节点，并等到该元素被消费者消费了才返回。
     2. tryTransfer方法
        - 用来**试探**生产者传入的元素是否能直接传给消费者。如果没有消费者等待接收元素，则返回false。

7. **LinkedBlockingDeque**：是一个由链表结构组成的双向阻塞队列。

   - 所谓双向队列指的是可以从队列的两端插入和移出元素。双向队列因为多了一个操作队列的入口，在多线程同时入队时，也就减少了一半的竞争。
   - 在初始化LinkedBlockingDeque时可以**设置容量防止其过度膨胀**。另外，双向阻塞队列可以运用在“工作窃取”模式中。

##### 5：阻塞队列的实现原理

​	使用通知模式实现。所谓通知模式，就是当生产者往满的队列里添加元素时会阻塞住生产者，当消费者消费了一个队列中的元素后，会通知生产者当前队列可用。通过查看JDK源码发现ArrayBlockingQueue使用了Condition来实现

```java
public E take() throws InterruptedException {
    final ReentrantLock lock = this.lock;
    lock.lockInterruptibly();
    try {
        // 消费者的线程一直处于等待状态，直到它被生产者通知
        while (count == 0)
        notEmpty.await();
        return extract();
    } finally {
    		lock.unlock();
    }
}
private void insert(E x) {
    items[putIndex] = x;
    putIndex = inc(putIndex);
    ++count;
  	// 生产者通知
    notEmpty.signal();
}
```









