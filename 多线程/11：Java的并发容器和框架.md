### Java的并发容器和框架

------

##### 1：ConcurrentLinkedQueue

​	ConcurrentLinkedQueue是一个基于链接节点的无界线程安全队列.

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







```java
public class ConcurrentLinkedQueue<E> extends AbstractQueue<E>
        implements Queue<E>, java.io.Serializable {
        
   	private transient volatile Node<E> head;
    private transient volatile Node<E> tail;
		// 初始化一空队列
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

		

		
}
```

