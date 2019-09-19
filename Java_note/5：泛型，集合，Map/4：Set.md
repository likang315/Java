### Set (Map)：集合

------

​	Set 集合元素唯一、无序并且只能通过迭代器，再通过for each循环获取元素

##### 1：Class  HashSet<E> ：散列集

​	元素唯一，因为Map.key 唯一，但不有序，线程不安全类

```java
public class HashSet<E> extends AbstractSet<E> implements Set<E>, Cloneable, java.io.Serializable {
	private transient HashMap<E,Object> map;
	// 作为 value 固定存储
	private static final Object PRESENT = new Object();
	// 底层是 Map
  public HashSet() {
    map = new HashMap<>();
  }
  // 指定初始容量，加载因子
  public HashSet(int initialCapacity, float loadFactor) {
    map = new HashMap<>(initialCapacity, loadFactor);
  }
  // 专门用来重写，给linkedHashSet 用的
  HashSet(int initialCapacity, float loadFactor, boolean dummy) {
        map = new LinkedHashMap<>(initialCapacity, loadFactor);
  }  
	// 操作的都是 map
  public boolean add(E e) {
    return map.put(e, PRESENT) == null;
  }
  public Iterator<E> iterator() {
    return map.keySet().iterator();
  }
}
```

###### 构造函数：

- HashSet () ：构造一个新的空 set，本质 HashMap 实例的默认初始容量是 16，加载因子是 0.75
- HashSet (int initialCapacity)：构造一个新的空 set，其底层 HashMap 实例具有指定的初始容量和默认的加载因子（0.75），如果容量超过初始容量，则创建新的容量的集合，并且把原来的数据复制进去

###### 方法：

- boolean add(E e) ：如果此 set 中尚未包含指定元素，则添加指定元素
- boolean isEmpty() ：做判断条件，之前做判空处理
- int size()   ：返回此 set 中的元素的数量，为空时，返回0
- Iterator<E>  iterator() ：返回对此 set 中元素进行迭代的迭代器

##### 2：Class  LinekdHashSet

​	继承 HashSet，源码更少、更简单，唯一的区别是LinkedHashSet 内部使用的是 LinkedHashMap，这样做的意义就是LinkedHashSet中的元素唯一，而且有序

###### 实现原理

底层采用双向链表实现，可以保证元素的插入顺序，又因为是HashSet的子类，所以插入的元素不能重复

```java
public class LinkedHashSet<E> extends HashSet<E> implements Set<E>, Cloneable, java.io.Serializable {
  // 浮点型 0.几 0 可以省略
  public LinkedHashSet() {
    super(16, .75f, true);
  }
	// 调用HashSet的 LinkHashMap 构造方法 
  public LinkedHashSet(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor, true);
  }
  //初始容量
  public LinkedHashSet(Collection<? extends E> c) {
    super(Math.max(2*c.size(), 11), .75f, true);
    addAll(c);
  }
}
```

##### 3：Class  TreeSet<E>：树集

​	保证 Set 集合的**元素唯一, 而且有序（元素值大小有序）**，底层是 TreeMap，元素被排序后放入该容器元素的类，必须实现 Comparator<T>  ，因为在元素进行排序时需要按照此原则本质是一个  TreeMap

```java
public class TreeSet<E> extends AbstractSet<E> implements NavigableSet<E>, Cloneable, java.io.Serializable {
			// 当前对象的引用
      private transient NavigableMap<E,Object> m;
	    // 作为Value值固定存储
      private static final Object PRESENT = new Object();
      // 按照可以的自然顺序排序
      public TreeSet() {
        this(new TreeMap<E,Object>());
      }
      // 根据外比较器进行排序
      public TreeSet(Comparator<? super E> comparator) {
        this(new TreeMap<>(comparator));
      }
      public boolean add(E e) {
        // 返回的当前被插入的值
        return m.put(e, PRESENT) == null;
      }
}
```

######  方法：

- boolean add(E e) ：将指定的元素添加到此 set（如果该元素尚未存在于 set 中)
- void clear()  ：移除此 set 中的所有元素
- boolean contains(Object o) ：如果此 set 包含指定的元素，则返回 true
- boolean remove(Object o)  ：将指定的元素从 set 中移除（如果该元素存在于此 set 中）
- Iterator<E> descendingIterator() ：返回在此 set 元素上按降序进行迭代的迭代器
- Iterator<E> iterator()    ： 返回在此 set 中的元素上按升序进行迭代的迭代器 

