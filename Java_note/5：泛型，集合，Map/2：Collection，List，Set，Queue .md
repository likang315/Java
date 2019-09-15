### 集合框架：(Java.util)

------

- List + Set + Map + 工具类（Arrays ，Collections，Iterator，Enumeration）

##### 1：集合：解决数组固定长度缺陷的动态容器，java 定义的一组管理对象的类库

- java.util Interface.Collection
- 集合类的根接口，Collection存储一组对象的引用，这些对象也称为 collection 的元素，泛型决定集合中元素的数据类型
- Java 中 没有提供这个接口的直接的实现类，但是却让其被继承产生了两个接口，就是 Set 和 List

![](https://github.com/likang315/Java-and-Middleware/blob/master/Java_note/5%EF%BC%9A%E6%B3%9B%E5%9E%8B%EF%BC%8C%E9%9B%86%E5%90%88%EF%BC%8CMap/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9/Framework.png?raw=true)

##### 2：List + Set + Queue

- List：序列，可以包含重复元素，提供了按索引方式访问
- Set： 集合，不包含重复的元素
- Deque：双向队列，Queue 的子接口

- int size() --------------------- 返回此 collection 中的元素数
- boolean isEmpty() -------------- 如果此 collection 不包含元素，则返回 true
- boolean	add(E e) ------------- 往集合中添加元素
- boolean remove(Object o) ------- 从此 collection 中移除指定元素的单个实例，如果存在的话（可选操作）
- void clear() ------------------- 移除此 collection 中的所有元素（可选操作）
- Iterator iterator() --------- 返回集合的迭代器，是实现了Iterator 的实现类
- boolean contains(Object o) ----- 如果此 collection 包含指定的元素，则返回 true
- Object[] toArray() ------------- 返回包含此 collection 中所有元素的数组

##### 3：Collections ：集合的工具类

###### java.util Class Collections ：集合的工具类，和数组（Arrays）一样，方法基本一样

- static extends Comparable> void sort(List list) 
  - 根据元素的自然顺序 对指定列表按升序进行排序 
- public static <T extends Object & Comparable<? super T>> T max(Collection<? extends T> coll)
  - 返回集合中的最大值

###### 使集合类变成线程安全的类

- static List synchronizedList(List list)
  - 返回指定列表支持的同步（线程安全的）列表
- static Map<K,V> synchronizedMap(Map<K,V> m)
  - 返回由指定映射支持的同步（线程安全的）映射
- static Set synchronizedSet(Set s)
  - 返回指定 set 支持的同步（线程安全的）set

##### 4：Collections.sort(List list, Comparator<? super T> ：转换成数组，调用Arrays.sort（）

```java
@SuppressWarnings({"unchecked", "rawtypes"})
public static <T> void sort(List<T> list, Comparator<? super T> c) {
	// Collections 底层调用的是List的sort
  list.sort(c);
}
// 底层调用的是Arrays.sort
default void sort(Comparator<? super E> c) {
  Object[] a = this.toArray();
  Arrays.sort(a, (Comparator) c);
  ListIterator<E> i = this.listIterator();
  for (Object e : a) {
    i.next();
    i.set((E) e);
  }
}
// Arrays.sort(a, (Comparator) c); 归并 + TimSort
public static <T> void sort(T[] a, Comparator<? super T> c) {
        if (c == null) {
            sort(a);
        } else {
            if (LegacyMergeSort.userRequested)
                legacyMergeSort(a, c);
            else
              // TimSort：就是找到已经排好序数据的子序列，然后对剩余部分排序，然后合并起来
                TimSort.sort(a, 0, a.length, c, null, 0, 0);
        }
}
```

###### Arrays.sort()：

```java
public static void sort(int[] a) {
  // 双轴快排:基于两个轴来进行比较
	DualPivotQuicksort.sort(a, 0, a.length - 1, null, 0, 0);
}
```



##### 5：Interface List：序列，有序可重复，允许多个null存在

```java
public interface List<E> extends Collection<E>
```

- void add(int index, E element) ---------------- 在列表的指定位置插入指定元素（可选操作）
- E get(int index) ------------------------- 返回列表中指定位置的元素
- E set(int index, E element) ------------------- 用指定元素替换列表中指定位置的元素（可选操作）
- int indexOf(Object o) ------------------------- 返回此列表中第一次出现的指定元素的索引
- List subList(int fromIndex, int toIndex) --- 返回含有两个边界的子序列集合
- Iterator iterator() ------------------------ 返回按适当顺序在列表的元素上进行迭代的迭代器

##### 6：Interface Set：无序，不重复，并且最多包含一个 null 元素，不能通过下标操作，其实底层实现依赖 map

```java
public interface Set<E> extends Collection<E> 
// 方法：同list，除了下标操作的方法
```

##### 7：Interface Queue：队列，FIFO，实现了 Collection 接口

- boolean offer(E e) ：入队，添加一个元素并返回true，如果队列已满，则返回false
- E poll() ：出队，获取并移除此队列的头，如果此队列为空，则返回 null
- 实现类：**Linkedlist**，PriorityQueue

```java
// add()和 remove()方法在失败的时候会抛出异常(不推荐)
Queue<String> queue = new LinkedList<String>();
// 入队 
if（!queue.offer("a")）{
  return "队列已满";
}
//出队
String str = queue.poll();
```

##### 8：public class Stack extends Vector ：栈，2倍扩容

- boolean empty() ：判断栈为不为空
- E pop() ：出栈，如果是空栈，会抛出异常：EmptyStackException
- E push (E item) ：入栈
- int search(Object o) ：返回某元素在栈中的位置,计数从1开始

```java
Stack<Integer> stack = new Stack<Integer>();
stack.push(8);
```

##### 9：普通集合、同步（线程安全）的集合、并发集合（JUC）

- 普通集合通常性能最高，但是不保证并发的可靠性
- 线程安全集合仅仅是给集合添加了synchronized(同步锁)，严重牺牲了性能，换回可靠性
- 并发集合则通过内部使用**锁分段技术**，**不仅保证了多线程的安全又提高的并发时的性能**
  - ConcurrentHashMap
  - ConcurrentLinkedQueue

##### 10：集合数组互转

###### 1：数组转集合

- 当使用 Arrays.asList() 方法时，数组就和列表指向同于个引用，当更新其中之一时，另一个将自动获得更新，因为 asList 获得 ArrayList 实际引用的 就是 数组
- asList 得到的 List 是的没有 add() 和 remove() ，得到的集合自身不能修改

###### 2：集合转数组

- 调用集合的 toArray() ，实际上，把集合中的元素拷贝到一个新的数组中

```java
// 数组转集合
public static <T> List<T> asList(T... a) {
  			//可变长参数本质上就是数组
        return new ArrayList<>(a);
}
// 集合转数组
public Object[] toArray() {
  	//ArrayList 本质就是数组序列 Object[] elementData
    return Arrays.copyOf(elementData, size);
}
```



