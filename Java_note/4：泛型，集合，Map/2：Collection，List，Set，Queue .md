## 集合框架：（Java.util）

## List + Set +Map +工具类（Arrays ，Collections，Iterator，Enumeration）



### 1：集合：一个解决数组固定长度缺陷的动态容器，java 定义的一组管理对象的类库

java.util
Interface Collection<E>

Collection：集合类的根接口，Collection 表示一组对象的引用，这些对象也称为 collection 的元素，泛型决定集合中元素为对象型

Java 中 没有提供这个接口的直接的实现类，但是却让其被继承产生了两个接口，就是 Set 和 List

![Framework.png](https://github.com/likang315/Java/blob/master/Java_note/4%EF%BC%9A%E6%B3%9B%E5%9E%8B%EF%BC%8C%E9%9B%86%E5%90%88%EF%BC%8CMap/Framework.png?raw=true)



###   Collection 的优化子接口：List（接口）+    Set（接口) +  Queue（队列接口）

######    List：：序列，可以包含重复元素，提供了按索引访问

######    Set：      集合，不包含重复的元素

###### Deque：双向队列，Queue 的子接口



**方法：**

###### int size() ---------------------  返回此 collection 中的元素数

###### boolean isEmpty() --------------  如果此 collection 不包含元素，则返回 true

###### boolean	add(E e)   -------------  往集合中添加元素

###### boolean remove(Object o) -------  从此 collection 中移除指定元素的单个实例，如果存在的话（可选操作）

###### void clear() -------------------  移除此 collection 中的所有元素（可选操作）

###### Iterator<E> iterator() ---------  返回集合的迭代器，是实现了Iterator 的实现类

###### boolean contains(Object o) -----  如果此 collection 包含指定的元素，则返回 true 

###### Object[] toArray() -------------  返回包含此 collection 中所有元素的数组





### 2：Collections ：集合的工具类

java.util
Class Collections ：集合的工具类，和数组（Arrays）一样，方法基本一样    

static <T> extends Comparable<? super T>> void  sort(List<T> list) 
   	根据**元素的自然顺序** 对指定列表按升序进行排序 
static <T> void sort(List<T> list, Comparator<? super T> c) 
	根据**指定比较器**产生的顺序对指定列表进行排序
static <T extends Object & Comparable<? super T>> T	**max(Collection<? extends T> coll)**   返回最大值
static <T extends Object & Comparable<? super T>> **T	min(Collection<? extends T> coll)**    返回最小值
static void   reverse(List<?> list)	返回有序集合



###### 使集合类变成线程安全的类

######     static <T> List<T> synchronizedList(List<T> list) 

返回指定列表支持的同步（线程安全的）列表

######     static <K,V> Map<K,V> synchronizedMap(Map<K,V> m) 

返回由指定映射支持的同步（线程安全的）映射

######     static <T> Set<T>  synchronizedSet(Set<T> s) 

 返回指定 set 支持的同步（线程安全的）set



#### Collection.sort(List<T> list, Comparator<? super T> ：底层用Timsort：归并+插入

#### Arrays.sort()：归并+快排+插入

如果数组长度**大于等于 286** 且 连续性好 （连续升序和连续降序）的话，就用**归并排序**，如果大于等于286 且 连续性不好的话就用 **双轴快速排序** 。如果长度小于286且大于等于47的话就用双轴快速排序，如果长度**小于47**的话就用**插入排序**



### 3：List（接口）：序列，有序可重复，允许 多个 null 存在

```java
public interface List<E> extends Collection<E>
```

Method：

###### void add(int index, E element) ----------------  在列表的指定位置插入指定元素（可选操作）

###### E get(int index)      -------------------------  返回列表中指定位置的元素

###### E set(int index, E element) -------------------  用指定元素替换列表中指定位置的元素（可选操作）

###### int indexOf(Object o) -------------------------  返回此列表中第一次出现的指定元素的索引

List<E>	subList(int fromIndex, int toIndex) ---  返回含有两个边界的子序列集合

###### Iterator<E> iterator() ------------------------  返回按适当顺序在列表的元素上进行迭代的迭代器





### 4：Interface Set<E>：无序，不重复.并且最多包含一个  null  元素，不能通过下标操作，其实底层实现依赖 map

```java
public interface Set<E> extends Collection<E> 
```

方法：同list，除了下标操作的方法
  



### 5：Interface Queue<E>：队列，FIFO，实现了Collection接口

###### boolean offer(E e)  ：添加一个元素并返回true       如果队列已满，则返回false

###### E poll() ： 获取并移除此队列的头，如果此队列为空，则返回 null 

实现类：**Linkedlist**,PriorityQueue

```java
//add()和remove()方法在失败的时候会抛出异常(不推荐)
Queue<String> queue = new LinkedList<String>();
//添加元素
queue.offer("a");
```



### 6：public class Stack<E> extends Vector<E> ：栈，继承 了数组实现的集合, 2倍扩容

###### boolean  empty()     ：判断栈为不为空

###### E   pop()    ：出栈，如果是空栈，会抛出异常：EmptyStackException

###### E   push  (E item)  ：入栈

###### int  search(Object o) ：返回某元素在栈中的位置,计数从1开始

```java
Stack<Integer> st = new Stack<Integer>();
ast.push(8);
System.out.println("stack: " + st);
```



### 7：普通集合、同步（线程安全）的集合、并发集合（JUC）

普通集合通常性能最高，但是不保证多线程的安全性和并发的可靠性

线程安全集合仅仅是给集合添加了synchronized(同步锁)，严重牺牲了性能，而且对并发的效率就更低了

并发集合则通过内部使用**锁分段技术**，**不仅保证了多线程的安全又提高的并发时的效率**
ConcurrentHashMap、ConcurrentLinkedQueue



### 8：Arrays 类中的  asList 方法 

 * 当使用 asList() 方法时，数组就和列表链接在一起了，**当更新其中之一时，另一个将自动获得更新**

 * asList得到的List 是的**没有 add()  和 remove() ** 

   通过查看Arrays类的源码可以知道,**asList返回的List是Array中的实现的 内部类**,而该类并没有定义add和remove方法.另外,为什么修改其中一个,另一个也自动 获得更新了,因为asList获得 **List实际引用的 就是 数组** 







