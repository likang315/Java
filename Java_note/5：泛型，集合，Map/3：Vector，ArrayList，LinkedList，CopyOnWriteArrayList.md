### List（序列）：可以通过下标操作

### 1：Class Vector <E>：

数组序列，Vector 线程安全，加了同步锁，效率低

###### 扩容机制：

初始容量为10 ，就是根据 capacityIncrement（容量增长因子）确认扩容大小的，若capacityIncrement = 0 则扩大一倍，否则在原来容量的基础上加上 capacityIncrement ，当然这个容量的最大范围为 Integer.MAX_VALUE 即，2^32 - 1，所以 Vector 并不是可以无限扩充的

###### 扩容过程：

申请 newCapacity 新空间，然后将原空间内容拷贝过来,并释放原空间，vector 并不是在原空间之后的相邻地址增加新空间，vector 的 空间是线性连续分配的

```java
public abstract class AbstractList<E> extends AbstractCollection<E> implements List<E> {
    //定义在 AbstractList
    protected transient int modCount = 0;	
}

public class Vector<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
  // 保存Vector中元素的数组
  protected Object[] elementData;
  // 实际元素的数量
  protected int elementCount;
  //自增因子
  protected int capacityIncrement;
  public Vector() {
    this(10);
  }

  //可以指定初始容量和容量增长因子（默认为：0）
  public Vector(int initialCapacity, int capacityIncrement) {}

  //扩容机制
  private void grow(int minCapacity) {
    //当前容器大小
    int oldCapacity = elementData.length; 
    //新容量
    int newCapacity = oldCapacity + ((capacityIncrement > 0) ?capacityIncrement : oldCapacity);

    if (newCapacity - minCapacity < 0)
      newCapacity = minCapacity;
    if (newCapacity - MAX_ARRAY_SIZE > 0)
      newCapacity = hugeCapacity(minCapacity);
    elementData = Arrays.copyOf(elementData, newCapacity);
  }
    
  //判断是否超出最大范围
  private static int hugeCapacity(int minCapacity){
    if (minCapacity < 0)
      throw new OutOfMemoryError();
    return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
  }

  //添加元素
  public synchronized boolean add(E e) {
    modCount++; //Fail-Fast机制
    ensureCapacityHelper(elementCount + 1);
    elementData[elementCount++] = e;
    return true;
  }
}
```





### 2：Class ArrayList<E>：

本质是 Object[]  数组，线程不安全，但是查找效率高，通过索引查询

###### 扩容机制

构造方法默认生成的空数组，并且第一次添加数据时，数组的容量会从0扩容成10，而后的数组扩容按照当前容量的1.5倍进行扩容，初始容量可以指定，但是扩容方式不可以（移位确定的）

```java
public class ArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable
{
		  //初始容量为10
	    private static final int DEFAULT_CAPACITY = 10;
	    //空集合数据
	    private static final Object[] EMPTY_ELEMENTDATA = {};
    	private static final Object[] DEFAULTCAPACITY_EMPTY_ELEMENTDATA = {};
		  //当前集合数据
    	transient Object[] elementData; 
	    private int size;

	  //指定初始容量构造方法
	  public ArrayList(int initialCapacity) {
            if (initialCapacity > 0) {
                this.elementData = new Object[initialCapacity];
            } else if (initialCapacity == 0) {
                this.elementData = EMPTY_ELEMENTDATA;
            } else {
                throw new IllegalArgumentException("Illegal Capacity: "+initialCapacity);
            }
    }

	   //1.5倍 扩容
	   private void grow(int minCapacity) {
            int oldCapacity = elementData.length;
            //1.5倍扩容
            int newCapacity = oldCapacity + (oldCapacity >> 1);
            if (newCapacity - minCapacity < 0)
                newCapacity = minCapacity;
            //最大为 Integer.MAX_VALUE
            if (newCapacity - MAX_ARRAY_SIZE > 0)
                newCapacity = hugeCapacity(minCapacity);
            elementData = Arrays.copyOf(elementData, newCapacity);
    	}

	   //索引 remove：删除就是移动
	   public E remove(int index) {
	   	      //检查是否越界
            rangeCheck(index);
            //Fail-Fast机制，继承 AbstractList 的属性
            modCount++; 
            E oldValue = elementData(index);

            int numMoved = size - index - 1;
            //删除就是移动，index处整体前移一个空间
            if (numMoved > 0)
                System.arraycopy(elementData, index+1, elementData, index,
                                 numMoved);
            elementData[--size] = null; //长度-1
            return oldValue;
    }
    	
    	// remove第一个对象
    	public boolean remove(Object o) {
            if (o == null) {
                for (int index = 0; index < size; index++)
                    if (elementData[index] == null) {
                        fastRemove(index);
                        return true;
                    }
            } else {
                for (int index = 0; index < size; index++)
                    if (o.equals(elementData[index])) {
                        fastRemove(index);
                        return true;
                    }
            }
            return false;
    	}
    	//添加一个集合
    	public boolean addAll(Collection<? extends E> c) {
        Object[] a = c.toArray();
        int numNew = a.length;
        ensureCapacityInternal(size + numNew);  // Increments modCount
        System.arraycopy(a, 0, elementData, size, numNew);
        size += numNew;
        return numNew != 0;
     }

    	//清空元素，依次赋 null
    	public void clear() {
            modCount++;
            for (int i = 0; i < size; i++)
                elementData[i] = null;
            size = 0;
    	}
    	//直接判断长度
    	public boolean isEmpty() {
        return size == 0;
      }
}
```

- boolean add(E e)    ：将指定的元素添加到此列表的尾部
- void add(int index, E element) ：将指定的元素插入此列表中的指定位置,先调用ensureCapacity（size+1),然后拷贝数组，赋值
- E remove(int index)    ：移除此列表中指定位置上的元素 
- boolean remove(Object o)   ：移除此列表中首次出现的指定元素（如果存在）
- E set(int index, E element) ： 用指定的元素替代此列表中指定位置上的元素
- E get(int index)  ：返回此列表中指定位置上的元素
- int indexOf(Object o)   ：返回此列表中首次出现的指定元素的索引，若列表不包含元素,返回-1 
- int size()    ：返回此列表中的元素数
- boolean isEmpty()    ：如果此列表中没有元素，则返回 true 
- void clear()    ：移除此列表中的所有元素。本质是所有元素赋值NUll，size=0
- boolean contains(Object o) ----------------------  如果此列表中包含指定的元素，则返回 true 
- Iterator<E> iterator()      ---------------------  返回按适当顺序在列表的元素上进行迭代的迭代器



### 3：Class LinkedList<E> 

链表序列，增加，删除时效率高 ，非线程安全的类

###### 实现原理

通过双向链表去实现的，它的顺序访问会非常高效，而随机访问效率比较低，可以通过下标来访问

当我们调用 get(int index) 时，首先会比较 index和 “双向链表 size 长度的1/2”；若前者大，则从链表头开始往后查找，直到 index位置；否则，从链表末尾开始向前查找，直到 index 位置

```java
public abstract class AbstractSequentialList<E> extends AbstractList<E> {}

public class LinkedList<E> extends AbstractSequentialList<E> implements List<E>, Deque<E>, Cloneable, java.io.Serializable {
  //Node 结点的个数
  transient int size = 0;
  //指向头结点
  transient Node<E> first;
  //指向尾节点
  transient Node<E> last;

  public LinkedList() {
  }
  //添加集合中的所有元素
  public LinkedList(Collection<? extends E> c) {
    this();
    addAll(c);
  }

  //添加元素尾部，可以添加到指定位置
  public boolean add(E e) {
    linkLast(e);
    return true;
  }

  void linkLast(E e) {
    final Node<E> l = last; //指向链表尾部
    final Node<E> newNode = new Node<>(l, e, null); //以尾部为前驱节点创建一个新节点
    last = newNode;//将链表尾部指向新节点
    if (l == null)//如果链表为空，那么该节点既是头节点也是尾节点
      first = newNode;
    else//链表不为空，那么将该结点作为原链表尾部的后继节点
      l.next = newNode;
    size++;//增加尺寸
    modCount++; //Fast-Fail机制
  }

  //通过索引得到Node
  public E get(int index) {
    //检查边界,看是否越界
    checkElementIndex(index);
    //只返回值
    return node(index).item;
  }
  Node<E> node(int index) {
    //判断索引和长度一半的大小相比
    if (index < (size >> 1)) {
      Node<E> x = first;
      for (int i = 0; i < index; i++)
        x = x.next;
      return x;
    } else {
      Node<E> x = last;
      for (int i = size - 1; i > index; i--)
        x = x.prev;
      return x;
    }
  }


  //remove 第一个元素指定对象，还可以通过索引删除
  public boolean remove(Object o) {
    //如果删除对象为null
    if (o == null) {
      //从前向后遍历
      for (Node<E> x = first; x != null; x = x.next) {
        //一旦匹配，调用unlink()方法将该节点从链表中移除并且返回true
        if (x.item == null) {
          unlink(x);
          return true;
        }
      }
    } else {
      //从前向后遍历
      for (Node<E> x = first; x != null; x = x.next) {
        //一旦匹配，调用unlink()方法和返回true
        if (o.equals(x.item)) {
          unlink(x);
          return true;
        }
      }
    }
    return false;
  }
}

  private static class Node<E> {
    //只返回Node 节点的值
    E item;
    Node<E> next;
    Node<E> prev;

    Node(Node<E> prev, E element, Node<E> next) {
      this.item = element;
      this.next = next;
      this.prev = prev;
    }
  }

}
```

###### 方法：

- void addFirst(E e) --------------    将指定元素插入此列表的开头
- void addLast(E e) ---------------   将指定元素添加到此列表的结尾
- E remove(int index)																			
- E set(int index, E element)
- E get(int index)  
- ListIterator<E> listIterator(int index)   -------- 返回此列表中的元素的列表迭代器（按适当顺序），从列表中指定位置开始 



### 4：java.util.concurrent   Class   CopyOnWriteArrayList：

​	  写时复制的容器，指向新的引用，读多写少的场景

```java
public class CopyOnWriteArrayList<E>  implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
	  //非公平的可重入锁
    final transient ReentrantLock lock = new ReentrantLock();
    //保证可见性，但是不保证一致性
    private transient volatile Object[] array;
    
    final Object[] getArray() {
        return array;
    }
    final void setArray(Object[] a) {
        array = a;
    }
    public CopyOnWriteArrayList() {
        setArray(new Object[0]);
    }
    //返回当前线程对应的 array
    final Object[] getArray() {
        return array;
    }
  
    @SuppressWarnings("unchecked")
    private E get(Object[] a, int index) {
        return (E) a[index];
    }
  
    //读，直接取
    public E get(int index) {
        return get(getArray(), index);
    }
  
    
    //插入新元素，赋值新数组
    public boolean add(E e) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
        	//赋给一个新的数组
            Object[] elements = getArray();
            int len = elements.length;
            Object[] newElements = Arrays.copyOf(elements, len + 1);
            newElements[len] = e;
            //把新的数组的引用赋给 array
            setArray(newElements);
            return true;
        } finally {
            lock.unlock();
        }
    }
    //删除元素，赋值新数组
    public E remove(int index) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] elements = getArray();
            int len = elements.length;
            E oldValue = get(elements, index);
            int numMoved = len - index - 1;
            if (numMoved == 0)
                setArray(Arrays.copyOf(elements, len - 1));
            else {
                Object[] newElements = new Object[len - 1];
                System.arraycopy(elements, 0, newElements, 0, index);
                System.arraycopy(elements, index + 1, newElements, index,
                                 numMoved);
                setArray(newElements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }
    }
    
  //更改引用，没有引用的数组直接被 GC 回收
	public void clear() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            setArray(new Object[0]);
        } finally {
            lock.unlock();
        }
  }

}												
```

- 实现了List接口
- 内部持有一个 ReentrantLock lock = new ReentrantLock();可重入锁
- 底层是用 volatile transient 声明的数组Object[] array
- **读写分离，写时复制出一个新的数组，**完成插入、修改或者移除操作后将数组的引用指向新数组，如果有**线程并发写，则通过锁来控制**
- 如果有线程并发的读，不需要加锁，则**分 3 种情况**：
  1、如果写操作未完成，那么直接读取原数组的数据
  2、如果写操作完成，但是引用还未指向新数组，那么也是读取原数组数据
  3、如果写操作完成，并且引用已经指向了新的数组，那么直接从新数组中读取数据



### 5：ConcurrentSkipListMap：

提供了一种线程安全的并发访问的排序映射表。内部是 SkipList（跳表）结构实现，在理论上能够O(log(n))时间内完成查找、插入、删除操作

SkipList：插入、查找为O(logn)，但常数项比红黑树要大；底层结构为链表，可无锁实现；数据天然有序



