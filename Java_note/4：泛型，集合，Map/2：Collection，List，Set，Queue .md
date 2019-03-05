## 集合框架

### 1:集合：一个解决数组固定长度缺陷的动态容器，java定义的一组管理对象的类库

java.util
Interface Collection<E>

  Collection：集合类的根接口，Collection 表示一组对象的引用，这些对象也称为 collection 的元素，泛型决定集合中元素为对象型

  Java中没有提供这个接口的直接的实现类。但是却让其被继承产生了两个接口，就是Set和List

###   Collection的优化子接口：List（接口）+ Set（接口) +（Queue--队列接口）	

#####    List：是一个有序的集合，可以包含重复的元素，提供了按索引访问的方式

#####    Set：无序的，不包含重复的元素

   方法：
boolean	add(E e)   -------------  往集合中添加元素

boolean remove(Object o) -------  从此 collection 中移除指定元素的单个实例，如果存在的话（可选操作）
void clear() -------------------  移除此 collection 中的所有元素（可选操作）
boolean isEmpty() --------------  如果此 collection 不包含元素，则返回 true

int size() ---------------------  返回此 collection 中的元素数
boolean contains(Object o) -----  如果此 collection 包含指定的元素，则返回 true 

Object[] toArray() -------------  返回包含此 collection 中所有元素的数组
Iterator<E> iterator() ---------  返回集合的迭代器，是实现了Iterator 的实现类

### 2：Collections

java.util
Class Collections ：集合的工具类，和数组（Arrays）一样，方法基本一样    

static <T> extends Comparable<? super T>> void  sort(List<T> list) 
    根据元素的自然顺序 对指定列表按升序进行排序 
static <T> void sort(List<T> list, Comparator<? super T> c) 
根据指定比较器产生的顺序对指定列表进行排序
static <T extends Object & Comparable<? super T>> T	max(Collection<? extends T> coll)   按照自然顺序，返回最大值
static <T extends Object & Comparable<? super T>> T	min(Collection<? extends T> coll)   按照自然顺序，返回最小值
static void	reverse(List<?> list)	返回有序集合

###### 使集合类变成线程安全的类

######     static <T> List<T> synchronizedList(List<T> list) 

返回指定列表支持的同步（线程安全的）列表

######     static <K,V> Map<K,V> synchronizedMap(Map<K,V> m) 

返回由指定映射支持的同步（线程安全的）映射

######     static <T> Set<T>  synchronizedSet(Set<T> s) 

 返回指定 set 支持的同步（线程安全的）set

### 3：List（接口）：序列，有序可重复，允许null存在

java.util 
Interface List<E>

Method：
void add(int index, E element) ----------------  在列表的指定位置插入指定元素（可选操作）

E get(int index)      -------------------------  返回列表中指定位置的元素
E set(int index, E element) -------------------  用指定元素替换列表中指定位置的元素（可选操作） 		
int indexOf(Object o) -------------------------  返回此列表中第一次出现的指定元素的索引

List<E>	subList(int fromIndex, int toIndex) ---  返回含有两个边界的子序列集合
Iterator<E> iterator() ------------------------  返回按适当顺序在列表的元素上进行迭代的迭代器

boolean equals(Object o) ----------------------  比较指定的对象(列表)与列表是否相等 

##### List（序列，线性表）接口的实现类：

######    Class Vector<E>：   数组序列,Vector线程安全效率低，以适应创建 Vector 后进行添加或移除项的操作

######    Class ArrayList<E>：数组序列,本质都是Object[]数组，只不过ArrayList线程不安全效率高，对序列整体进行操作，用数组实现的

   构造方法：

###### ArrayList<E>() --------- 构造一个初始容量为 10 的空列表,1.5被扩容

ArrayList<E>(int initialCapacity) -------- 构造一个具有指定初始容量的空列表

   方法：
	boolean add(E e)               将指定的元素添加到此列表的尾部
        void add(int index, E element) 将指定的元素插入此列表中的指定位置,先调用ensureCapacity（size+1),然后拷贝数组，赋值
 	boolean addAll(int index, Collection<? extends E> c)从指定的位置开始，将指定 collection 中的所有元素插入到此列表中 
		

E remove(int index)          --------------------  移除此列表中指定位置上的元素 
boolean remove(Object o)    ---------------------  移除此列表中首次出现的指定元素（如果存在）

E set(int index, E element) ---------------------  用指定的元素替代此列表中指定位置上的元素

E get(int index)           ----------------------  返回此列表中指定位置上的元素。			
int indexOf(Object o)     -----------------------  返回此列表中首次出现的指定元素的索引，若列表不包含元素,返回-1 

int size()                   --------------------  返回此列表中的元素数
boolean isEmpty()             -------------------  如果此列表中没有元素，则返回 true 
void clear()               ----------------------  移除此列表中的所有元素。本质是所有元素赋值NUll，size修改为0
boolean contains(Object o) ----------------------  如果此列表中包含指定的元素，则返回 true 

void trimToSize()           ---------------------  将此 ArrayList 实例的容量调整为列表的当前大小
Iterator<E> iterator()      ---------------------  返回按适当顺序在列表的元素上进行迭代的迭代器。 
Object[] toArray()          ---------------------  按适当顺序（从第一个到最后一个元素）返回包含此列表中
							   所有元素的数组	

######   Class LinkedList<E>：链表序列，因此在增加，删除时效率高 ，主要执行删除，插入，单个集合元素的操作，不是线程安全的 

###### 		       			        用双向链表实现的，线程不安全类，没有加 synchronized

双向链表：优点，增加删除，用时间很短，但是因为没有索引，对索引的操作，比较麻烦，只能循环遍历，但是每次循环的时候，都会先判断一下，这个索引位于链表的前部分还是后部分，每次都会遍历链表的一半 ，而不是 全部遍历

构造方法：
	LinkedList() ------------- 构造一个空链表 
     	LinkedList(Collection<? extends E> c) -------- 构造一个包含指定 collection 中的元素的列表，
						       这些元素按其 collection 的迭代器返回的顺序排列
    	
方法：
void addFirst(E e) --------------   将指定元素插入此列表的开头
void addLast(E e) ---------------   将指定元素添加到此列表的结尾
	

E remove(int index)																					E set(int index, E element)

E get(int index)  --------------   返回指定下标的元素 
E getFirst()     ---------------    返回此列表的第一个元素
E getLast()     -----------------   返回此列表的最后一个元素 

ListIterator<E> listIterator(int index)   -------- 返回此列表中的元素的列表迭代器（按适当顺序），从列表中指定位置开始 

##### CopyOnWriteArrayList：写时复制的容器，读多写少的场景																				public class CopyOnWriteArrayList<E>  implements List<E>, RandomAccess, Cloneable, java.io.Serializable

- 实现了List接口
- 内部持有一个ReentrantLock lock = new ReentrantLock();可重入锁
- 底层是用volatile transient 声明的数组 array
- 读写分离，写时复制出一个新的数组，完成插入、修改或者移除操作后将数组的引用指向新数组，如果有线程并发的写，则通过锁来控制，防止多个副本混淆数据
- 如果有线程并发的读，不需要加锁，则分几种情况：
  1、如果写操作未完成，那么直接读取原数组的数据
  2、如果写操作完成，但是引用还未指向新数组，那么也是读取原数组数据
  3、如果写操作完成，并且引用已经指向了新的数组，那么直接从新数组中读取数据

##### ConcurrentSkipListMap：

提供了一种线程安全的并发访问的排序映射表。内部是SkipList（跳表）结构实现，在理论上能够O(log(n))时间内完成查找、插入、删除操作

SkipList：插入、查找为O(logn)，但常数项比红黑树要大；底层结构为链表，可无锁实现；数据天然有序

### 4：Interface Set<E>：无序，不重复.并且最多包含一个 null 元素，不能通过下标操作，其实底层实现依赖 map

   方法：同list，除了下标操作的方法
         Iterator<E> iterator() ---------返回在此 set 中的元素上进行迭代的迭代器

  Set集合的实现类：（两个都不是线程安全的类，没有给方法添加同步锁）

#####   Class HashSet<E>(散列集)：只能保证Set集合元素唯一，但不能保证有序，由哈希表（实际上是一个 HashMap 实例）支持,并没有增加方法，线程不安全类

  构造函数:
	HashSet()---------------- 构造一个新的空 set，本质HashMap 实例的默认初始容量是 16，加载因子是 0.75
	HashSet(int initialCapacity)---构造一个新的空 set，其底层 HashMap 实例具有指定的初始容量和默认的加载因子（0.75）如果容量超过初始容量，则创建新的容量的集合，并且把原来的数据复制进去，并且删除原始的集合	
  方法:
	 boolean add(E e) ------- ---- 如果此 set 中尚未包含指定元素，则添加指定元素
	 boolean isEmpty() ----------- 做判断条件，之前做判空处理
	 int size()       ------------ 返回此 set 中的元素的数量，为空时，返回0
 	 Iterator<E> iterator() ------ 返回对此 set 中元素进行迭代的迭代器

##### LinekdHashSet

LinkedHashSe t继承自HashSet，源码更少、更简单，唯一的区别是LinkedHashSet内部使用的是LinkedHashMap,这样做的意义或者好处就是LinkedHashSet中的元素顺序且唯一是可以保证的，也就是说遍历序和插入序是一致的

#####  Class TreeSet<E>(树集):

保证Set集合的元素唯一,而且有序，底层是TreeMap，元素被排序放入该容器元素的类，必须实现Comparable<T>或Comparator<T>,因为在元素进行排序时需要按照此原则本质是一个TreeMap

 构造函数：
	TreeSet() ------------- 构造一个新的空 set，该 set 根据其元素的自然顺序进行排序
    	TreeSet(Comparator<? super E> comparator) -------构造一个新的空 TreeSet，它根据指定比较器进行排序
			
 方法：
	boolean add(E e) ------------------- 将指定的元素添加到此 set（如果该元素尚未存在于 set 中)
        void clear()      ------------------ 移除此 set 中的所有元素
	boolean contains(Object o) --------- 如果此 set 包含指定的元素，则返回 true
 	boolean remove(Object o)   --------- 将指定的元素从 set 中移除（如果该元素存在于此 set 中）
      	Iterator<E> descendingIterator() --- 返回在此 set 元素上按降序进行迭代的迭代器
       	Iterator<E> iterator()    ---------- 返回在此 set 中的元素上按升序进行迭代的迭代器 
       	int size()     --------------------- 返回 set 中的元素数
          

 TreeSet类中跟HashSet类一样也没有get()方法来获取指定位置的元素，所以也只能通过迭代器方法来获取 for each循环
 	

### 5：Interface Queue<E>：队列，FIFO，实现了Collection接口

boolean offer(E e)  添加一个元素并返回true       如果队列已满，则返回false

E poll() 
      获取并移除此队列的头，如果此队列为空，则返回 null 

E peek() 
      获取但不移除此队列的头；如果此队列为空，则返回 null

实现类：Linkedlist,PriorityQueue

```java
//add()和remove()方法在失败的时候会抛出异常(不推荐)
Queue<String> queue = new LinkedList<String>();
//添加元素
queue.offer("a");
```

### public class Stack<E> extends Vector<E> ：栈，继承 了数组实现的集合,2倍扩容

###### boolean  empty()     ：判断栈为不为空

###### E   pop()    ：出栈，如果是空栈，会抛出异常：EmptyStackException

###### E   push  (E item)  ：入栈

###### int  search(Object o) ：返回某元素在栈中的位置,计数从1开始

```java
Stack<Integer> st = new Stack<Integer>();
ast.push(8);
System.out.println("stack: " + st);
```



### 6：普通集合、同步（线程安全）的集合、并发集合

普通集合通常性能最高，但是不保证多线程的安全性和并发的可靠性

线程安全集合仅仅是给集合添加了synchronized(同步锁)，严重牺牲了性能，而且对并发的效率就更低了

并发集合则通过内部使用锁分段技术，不仅保证了多线程的安全又提高的并发时的效率
ConcurrentHashMap、ConcurrentLinkedQueue、ConcurrentLinkedDeque

### 7：Arrays类中的asList方法 

 * 该方法对于基本数据类型的数组支持并不好,当数组是基本数据类型时不建议使用 

 *  当使用asList()方法时，数组就和列表链接在一起了.，当更新其中之一时，另一个将自动获得更新

 *  asList得到的数组是的没有add和remove方法的 

   

   通过查看Arrays类的源码可以知道,asList返回的List是Array中的实现的 内部类,而该类并没有定义add和remove方法.另外,为什么修改其中一个,另一个也自动 获得更新了,因为asList获得List实际引用的就是数组 





