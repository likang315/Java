### 集合工具类

------

##### 1：Fail-Fast 机制：快速失败

​	java 集合中的一种错误检测机制，**当多个线程对同一个集合的内容进行操作时，就可能会产生 fail-fast 事件**，抛出异常 java.util.ConcurrentModificationException

原理：集合中 **modCount 属性**用来记录List修改的次数：每修改一次(**添加/删除等操作**)，将 modCount+1**，调用 迭代器中 next() 和 remove()时，** 都会判断 "modCount != expectedModCount"，若成立，则抛出ConcurrentModificationException 异常， 产生 fail-fast 事件，因为每一个迭代器对应一个modCount

```java
// 每一个迭代器对应自己的 expectModCount
expectedModCount = modCount;
```

解决方法：若在多线程环境下使用 fail-fast 机制的集合，建议使用 java.util.concurrent 包下的类去取代 java.util 包下的类, 新建Iterator时，将集合中的元素保存到一个新的拷贝数组中，当原始集合的数据改变，拷贝数据中的值也不会变化

##### 2：Fail-safe 机制：安全失败

​	对任何对集合的修改都会在一个复制的集合上进行修改，因此不会抛出ConcurrentModificationException

- fail-safe机制缺点：需要复制大量的集合，产生大量的无效对象，开销大	无法保证读取的数据是目前原始数据结构中的数据
- Lsit 实现类 和 Map 实现类都会产生Fail-Fast事件，而CopyOnWriteArrayList不会产生Fail-Fast事件

##### 3：迭代器（两种）

###### java.util

###### Interface Iterator(迭代器)：用于遍历集合的，除了能读取集合的数据之外，也能数据进行删除操作(3个方法)

三步走：问，取，删 方法： 

- boolean hasNext() ：用于平判断是否有元素可以迭代，若有返回 true，否则false 
- E next() ：返回迭代的下一个元素 
- void remove() ：从迭代器指向的 collection 中移除迭代器返回的当前元素，移除之后会重新给exceptedModCount赋值，值为新的modCount

###### java.util

###### Interface Enumeration：只能读取集合的数据，而不能对数据进行修改(2个方法)

- 本身并没有支持同步，而在 **Hashtable 实现 Enumeration时**，添加了同步
- Iterator 支持 fail-fast机制，当多个线程操作集合时，会抛出异常，而 Enumeration 不支持

##### 4：比较器（内比较器和外比较器）

用匿名内部类写一个比较器，因为只用一次，以后再不会使用，也可以new

###### java.util

###### Interface Comparator: 强行对某个对集合中的元素进行整体排序的比较函数，用来传递比较器------外比较器

实现 comparator 接口，必须重写int compare(Object o1, Object o2)方法	

- int compare(T o1, T o2) -------------比较用来排序的两个参数

###### java.lang

###### Interface Comparable：强行对它的实现类的每个对象进行整体排序-----------内比较器

实现Comparable接口，必须重写int compareTo(Object o)方法	

- int compareTo(T o) ------------ 比较此对象与指定对象进行比较