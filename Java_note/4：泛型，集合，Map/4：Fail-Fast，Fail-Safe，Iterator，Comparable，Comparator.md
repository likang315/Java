### 1：Fail-Fast  机制：快速失败

java 集合中的一种错误检测机制。当多个线程对同一个集合的内容进行操作时，就可能会产生fail-fast事件，抛出异常java.util.ConcurrentModificationException

原理：集合中 modCount 属性用来记录List修改的次数：每修改一次(添加/删除等操作)，将 modCount+1，调用 next() 和 remove()时， 都会执行 checkForComodification(),若"modCount != expectedModCount"，则抛出ConcurrentModificationException 异常， 产生fail-fast事件

 解决方法：若在多线程环境下使用fail-fast机制的集合，建议使用java.util.concurrent包下的类去取代java.util包下的类,
	   新建Iterator时，将集合中的元素保存到一个新的拷贝数组中，当原始集合的数据改变，拷贝数据中的值也不会变化

### 2：fail-safe 机制：安全失败

fail-safe 任何对集合结构的修改都会在一个复制的集合上进行修改，因此不会抛出ConcurrentModificationException

fail-safe机制有两个问题																					需要复制集合，产生大量的无效对象，开销大															无法保证读取的数据是目前原始数据结构中的数据

###### ArrayList会产生Fail-Fastl事件，而CopyOnWriteArrayList不会产生Fail-Fast事件

### 3：迭代器（两种）

######    java.util

######    Interface Iterator<E>(迭代器)：用于遍历集合的，除了能读取集合的数据之外，也能数据进行删除操作(3个方法)

ArrayList 和 HashMap
三步走：问，取，删
方法：
	 boolean hasNext() ----------  用于平判断是否有元素可以迭代，若有返回 true，否则false 
	 E next()   -----------------  返回迭代的下一个元素
	void remove()   ------------  从迭代器指向的 collection 中移除迭代器返回的最后一个元素

######   java.util

######   Interface Enumeration<E>：只能读取集合的数据，而不能对数据进行修改(2个方法)

本身并没有支持同步，而在Vector、Hashtable实现Enumeration时，添加了同步

Iterator支持fail-fast机制，当多个线程操作集合时，会抛出异常，而Enumeration不支持

### 3：比较器（内比较器和外比较器）

  用匿名内部类写一个比较器，因为只用一次，以后再不会使用，也可以new

######  java.util

######  Interface Comparator<T>:强行对某个对象集合进行整体排序的比较函数，用来传递比较器------外比较器

实现omparator接口，必须重写int compare(Object o1, Object o2)方法										 			  int compare(T o1, T o2) -------------比较用来排序的两个参数

######  java.lang

######  Interface Comparable<T>:强行对它的实现类的每个对象进行整体排序-----------内比较器

 实现Comparable接口，必须重写int compareTo(Object o)方法												  int compareTo(T o) ------------   比较此对象与指定对象进行比较



