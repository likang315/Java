## Set 集合的实现类(Map):



### Class  HashSet<E>(散列集)：只能保证 Set 集合元素唯一，但不能保证有序，由哈希表（实际上是一个 HashMap 实例）支持,并没有增加方法，线程不安全类

构造函数:
	HashSet()---------------- 构造一个新的空 set，本质HashMap 实例的默认初始容量是 16，加载因子是 0.75
	HashSet(int initialCapacity)---构造一个新的空 set，其底层 HashMap 实例具有指定的初始容量和默认的加载因子（0.75）如果容量超过初始容量，则创建新的容量的集合，并且把原来的数据复制进去，并且删除原始的集合	
  方法:
	 boolean add(E e) ------- ---- 如果此 set 中尚未包含指定元素，则添加指定元素
	 boolean isEmpty() ----------- 做判断条件，之前做判空处理
	 int size()       ------------ 返回此 set 中的元素的数量，为空时，返回0
 	 Iterator<E> iterator() ------ 返回对此 set 中元素进行迭代的迭代器

### LinekdHashSet

LinkedHashSet 继承自HashSet，源码更少、更简单，唯一的区别是LinkedHashSet内部使用的是LinkedHashMap,这样做的意义或者好处就是LinkedHashSet中的元素顺序且唯一是可以保证的，也就是说遍历序和插入序是一致的

### Class TreeSet<E>(树集):

保证 Set 集合的元素唯一, 而且有序，底层是 TreeMap，元素被排序放入该容器元素的类，必须实现Comparable<T>或Comparator<T>,因为在元素进行排序时需要按照此原则本质是一个TreeMap

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
 	

