### Map< K，V>

------

##### 1：Interface Map<K,V>：

- 一种映射关系，将**键映射到值的对象**，存储键和值这样的双列数据的集合
- 映射不能包含重复的键，每个键最多只能映射到一个值，K - 映射键的类型，V - 映射值的类型

```java
public interface Map<K,V> {
    V get(Object key);
		V put(K key, V value);
	  boolean containsKey(Object key);
	  ...
    // Java 8
	  interface Entry<K,V> {
        K getKey();
        V getValue();
    }
}
```

- V put(K key, V value) ：将指定的值与此映射中的指定键关联，想象成放入了集合

- V get(Object key)： 返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null

- V remove(Object key) ：如果存在一个键的映射关系，则将其从此映射中移除,返回值为被删除的value

- void clear() ：从此映射中移除所有映射关系

- boolean isEmpty() ：如果此映射未包含键-值映射关系，则返回 true 

- int size()：返回此映射中的键-值映射关系数

  

- Set<Map.Entry<K,V>>   entrySet() ：返回此映射中包含的映射关系的 Set 集合

- Set keySet() ：返回此映射中只包含的键的 Set 集合

- Collection values() ：返回此映射中包含的值的collection集合

- boolean containsKey(Object key) ：如果此映射包含指定键的映射关系，则返回 true 
- boolean containsValue(Object value) ：如果此映射将一个或多个键映射到指定值，则返回 true

##### 2：Class HashMap<K,V>：

​	基于哈希表的 Map 接口的实现类，非线程安全的类，并且不保证映射的顺序，但是查找高效，允许 null 值 和 null 键的存在

###### 1：实现原理

- JDK1.7：数组+链表
- JDK1.8：数组+链表+红黑树，引入红黑树来提高查找效率   

```java
public class HashMap<K,V> extends AbstractMap<K,V> implements Map<K,V>, Cloneable, Serializable {
	  // 存储 key-value 键值对
    transient Node<K,V>[] table;
    // map大小
    transient int size;
    // Fail-fast
    transient int modCount;
	  // 阈值.扩容
    int threshold;
    // 加载因子
    final float loadFactor;
    // 默认的加载因子
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
 	  // 初始容量 16
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;  

    public HashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR; 
    }
    // 静态内部类 Node<K,V>
    static class Node<K,V> implements Map.Entry<K,V> {
   		// rehash时不会再计算hash
      final int hash; 
      final K key;
      V value;
      Node<K,V> next;
      public final int hashCode() {
        return Objects.hashCode(key) ^ Objects.hashCode(value);
      }
      .....
    }
  // 指定初始容量和加载因子
  public HashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 0)
      throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
    if (initialCapacity > MAXIMUM_CAPACITY)
      initialCapacity = MAXIMUM_CAPACITY;
    if (loadFactor <= 0 || Float.isNaN(loadFactor))
      throw new IllegalArgumentException("Illegal load factor: " + loadFactor);
    this.loadFactor = loadFactor;
    this.threshold = tableSizeFor(initialCapacity);
  }
  // 指定的新的阈值
  static final int tableSizeFor(int cap) {
    int n = cap - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
  }
}
```

###### 2：hash冲突

​	通过 key的 hashCode() 值，再进行位与运算产生分布相对均匀的位置，如果存储的对象对多了，就有可能不同的对象所算出来的hash值是相同的，这就出现了所谓的 hash 冲突

- HashMap 底层是通过链表来解决hash冲突的，**拉链法（桶的深度）** ，开放地址法，再哈希法


###### 3：影响 HashMap 的性能：Capacity 和 loadFactor

​	当节点数大于 (threshold) 阀值就需要扩容，这个值的计算方式是 **capacity * load factor**

###### 4：扩容机制：当前容量X2，在扩大容量时须要再hash

​	产生一个新的数组把原来的数组赋值过去，在原来数组的区间基础上的按照索引存储

- 扩容时，索引(下标)改变
- newTab[e.hash & (newCap - 1)] = e; 虽然Hash值不变，但是用的是新的容量相与

###### 5：Hash()

​	用了异或，移位等运算，对 key.hashcode() 进一步进行计算来保证最终获取的存储位置**尽量分布均匀**

```java
static final int hash(Object key) {
  int h;
  // 无符号右移，低12位和高12位异或，取低12位的，对null键特殊处理
  return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
}
```

###### 6：HashMap 为什么初始容量为 16或者2的幂次方

1. 为了降低hash值的碰撞，在 HashMap 的索引， 公式: index = e.hash & (newCap - 1)
   - 反观长度16或者其他2的幂次方, newCap - 1 的值是所有最后二进制位全为1,这种情况下,index 的结果等同于hash值的后几位的值，只要输入的 hashcode 本身分布均匀,hash算法的结果就是均匀的，HashMap的默认长度为16,是为了降低 hash 碰撞的几率
2. 移位运算符，对于二进制运算非常快

###### 7：什么时候用红黑树什么时候用链表

- 在桶元素（桶的深度）超过8个并且表长超过 64 会将链表转化为红黑树（两个条件），当红黑树中元素小于6个时、会将红黑树转化为链表
- 因为红黑树需要进行左旋，右旋操作， 而单链表不需要，如果元**素小于8个**，查询成本高，新增成本低，如果**元素大于8个**，查询成本低，新增成本高

###### 8：为什么要引入红黑树

​	因为之前hashmap底层结构是数组加链表，但是当数据大到一定程度的时候，用链表存储也比较长，难以查询，红黑树的查找效率高，相当于二分

###### 9：JDK1.7 扩容时，头插造成环形链表 (高并发时)

​	并发时，当两个线程同时进行put的操作时，刚好要扩容，一个线程刚扩容就休眠，另一个线程执行扩容，再hash 完时，另一个线程继续，此时就导致环形链表

![](https://github.com/likang315/Java-and-Middleware/blob/master/Java_note/5%EF%BC%9A%E6%B3%9B%E5%9E%8B%EF%BC%8C%E9%9B%86%E5%90%88%EF%BC%8CMap/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9/HashMap%E5%BE%AA%E7%8E%AF%E9%93%BE%E8%A1%A8.png?raw=true)

###### 10：put(K key, V value) 操作（ JDK1.8 ）

```java
public V put(K key, V value) {
    return putVal(hash(key), key, value, false, true);
}
final V putVal(int hash, K key, V value, boolean onlyIfAbsent, boolean evict){
    Node<K,V>[] tab;
  	Node<K,V> p;
  	int n, i;
    // table 是否为null 或者 length等于0, 如果是则调用resize()进行第一次初始化
    if ((tab = table) == null || (n = tab.length) == 0)
        n = (tab = resize()).length;
    // 通过 hash 值计算索引位置, 如果 table 表该索引位置节点为空则新增一个
    if ((p = tab[i = (n - 1) & hash]) == null)
        tab[i] = newNode(hash, key, value, null);
    else { 
        // table 表该索引位置不为空
        Node<K,V> e; K k;
        // 判断p节点的hash值和key值是否跟传入的hash值和key值相等
        if (p.hash == hash && ((k = p.key) == key || (key != null && key.equals(k)))) 
            e = p;
        // 判断 p 节点是否为TreeNode, 如果是则调用红黑树的 putTreeVal 方法查找目标节点
        else if (p instanceof TreeNode) 
            e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);
        else  {	
            //走到这代表p节点为普通链表节点,遍历此链表, binCount 用于统计节点数
            for (int binCount = 0; ; ++binCount) {  
                // p.next 为空代表不存在目标节点则新增一个节点插入链表尾部，并发出现问题
                if ((e = p.next) == null) {
                    p.next = newNode(hash, key, value, null);
                    // 计算节点是否超过8个, 减一是因为循环是从p节点的下一个节点开始的
                    if (binCount >= TREEIFY_THRESHOLD - 1)
                        treeifyBin(tab, hash); // 如果超过8个，调用treeifyBin方法将该链表转换为红黑树
                    break;
                }
                // e节点的hash值和key值都与传入的相等, 则e即为目标节点,跳出循环
                if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k)))) 
                    break;
                p = e;   //将p指向下一个节点
             }
        }
        // e不为空则代表根据传入的hash值和key值查找到了节点,将该节点的value覆盖,返回oldValue
        if (e != null) { 
            V oldValue = e.value;
            if (!onlyIfAbsent || oldValue == null)
                e.value = value;
            afterNodeAccess(e); // 用于LinkedHashMap
            return oldValue;
        }
    }
    ++modCount;
    if (++size > threshold) // 插入节点后超过阈值则进行扩容
        resize();
    afterNodeInsertion(evict);  // 用于LinkedHashMap
    return null;
}
// 新建 Node 节点
Node<K,V> newNode(int hash, K key, V value, Node<K,V> next) {
	return new Node<>(hash, key, value, next);
}
```

1. 第一次put 的时候，判断是否为null 或者长度为 0，则会触发下面的 resize() **初始就是第一次 resize 和后续的扩容有些不一样，因为这次是数组从 null 初始化到默认的 16 或自定义的初始容量**
2. 判断**第一个结点是否为空**，为空则插入
3. 判断**第一个结点的 hash 和 key 是否相等** ，若相等，直接替换新的value
4. 判断是否为**红黑树的结点**，若为红黑树则调用红黑树的 putTreeVal 方法
5. 遍历链表，**插入到尾部(尾插)**，同时判断是否为第九个结点，转换为红黑树
6. 判断是否超出阈值，超出则相应 resize( ) ;

###### 11：get(Object key) 操作

```java
public V get(Object key) {
        Node<K,V> e;
        return (e = getNode(hash(key), key)) == null ? null : e.value;
}

final Node<K,V> getNode(int hash, Object key) {
    Node<K,V>[] tab;
    Node<K,V> first, e; int n; K k;
  	//判断桶不为 null 
    if ((tab = table) != null && (n = tab.length) > 0 && (first = tab[(n - 1) & hash]) != null) {
       //判断是不是桶的第一个结点，先 hash，在 key
       if (first.hash == hash && ((k = first.key) == key || (key != null && key.equals(k))))
                return first;
        if ((e = first.next) != null){
             //判断是不是红黑树的结点
             if (first instanceof TreeNode)
                return ((TreeNode<K,V>)first).getTreeNode(hash, key);
             //遍历链表，找对应的结点
             do {
                  if (e.hash == hash && ((k = e.key) == key || (key != null && key.equals(k))))
                        return e;
                } while ((e = e.next) != null);
        }
     }
     return null;
}
```

1. 计算 **key 的 hash 值**，根据 hash 值找到**对应数组索引**: hash & (length-1)
2. 判断**数组该位置处的元素是否刚好就是我们要找的**，如果不是，走第三步
3. **判断该元素类型是否是 TreeNode**，如果是，用红黑树的方法取数据，如果不是，走第四步
4. **遍历链表，直到找到相等**

###### 12：resize（）操作

```java
final Node<K,V>[] resize() {
    Node<K,V>[] oldTab = table;
    int oldCap = (oldTab == null) ? 0 : oldTab.length;
    int oldThr = threshold;
    int newCap, newThr = 0;
    //计算新表的容量和阈值
    if (oldCap > 0) {// 旧table不为空
        if (oldCap >= MAXIMUM_CAPACITY) {   // 旧table的容量超过最大容量值
            threshold = Integer.MAX_VALUE;  // 设置阈值为Integer.MAX_VALUE
            return oldTab;
        }
        // 如果容量*2< 最大容量并且 >=16, 则将阈值设置为原来的两倍
        else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY &&
                 oldCap >= DEFAULT_INITIAL_CAPACITY)   
            newThr = oldThr << 1; 
    }
    else if (oldThr > 0)   //旧表的容量为 0,  旧表表的阈值大于0
        newCap = oldThr;	 // 则将新表的容量设置为旧表的阈值 
    else {	
        // 旧表的容量为0, 旧表的阈值为0, 则为空表，设置默认容量和阈值
        newCap = DEFAULT_INITIAL_CAPACITY; 
        newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
    }
    
    if (newThr == 0) {  // 如果新表的阈值为空, 则通过新的容量*负载因子获得阈值
        float ft = (float)newCap * loadFactor;
        newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                  (int)ft : Integer.MAX_VALUE);
    }
    threshold = newThr; // 将当前阈值赋值为刚计算出来的新的阈值
//-------------------------------------------------------------------------------------------------
    
    @SuppressWarnings({"rawtypes","unchecked"})
    // 定义新表,容量为刚计算出来的新容量，出现get（） 问题，oldTab 可能被垃圾回收器回收
    Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];
    table = newTab; // 将当前的表赋值为新定义的表
    
    //如果老表不为空, 则需遍历将节点赋值给新表
    if (oldTab != null) {  
        for (int j = 0; j < oldCap; ++j) {
            Node<K,V> e;
            if ((e = oldTab[j]) != null) {  // 将索引值为j的老表头节点赋值给e
                oldTab[j] = null; // 将老表的节点设置为空, 以便垃圾收集器回收空间
                // 如果e.next为空, 则代表老表的该位置只有1个节点, 
                // 通过hash值计算新表的索引位置, 直接将该节点放在该位置
                if (e.next == null) 
                  //扩容后索引位置不一样
                    newTab[e.hash & (newCap - 1)] = e;
                else if (e instanceof TreeNode)
                	 // 调用treeNode的hash分布(跟下面最后一个else的内容几乎相同)
                    ((TreeNode<K,V>)e).split(this, newTab, j, oldCap); 
                
                else { 
                    Node<K,V> loHead = null, loTail = null; // 存储跟原索引位置相同的节点
                    Node<K,V> hiHead = null, hiTail = null; // 存储索引位置为:原索引+oldCap的节点
                    Node<K,V> next;
                    
                    do {
                        next = e.next;
                 //如果e的hash值与老表的容量进行与运算为0,则扩容后的索引位置跟老表的索引位置一样
                        if ((e.hash & oldCap) == 0) {   
                            if (loTail == null) // 如果loTail为空, 代表该节点为第一个节点
                                loHead = e; // 则将loHead赋值为第一个节点
                            else    
                                loTail.next = e;    // 否则将节点添加在loTail后面
                            loTail = e; // 并将loTail赋值为新增的节点
                        }
                 //如果e的hash值与老表的容量进行与运算为1,则扩容后的索引位置为:老表的索引位置＋oldCap
                        else {  
                            if (hiTail == null) // 如果hiTail为空, 代表该节点为第一个节点
                                hiHead = e; // 则将hiHead赋值为第一个节点
                            else
                                hiTail.next = e;    // 否则将节点添加在hiTail后面
                            hiTail = e; // 并将hiTail赋值为新增的节点
                        }
                    } while ((e = next) != null);
                    
                    if (loTail != null) {
                        loTail.next = null; // 最后一个节点的next设为空
                        newTab[j] = loHead; // 将原索引位置的节点设置为对应的头结点
                    }
                    if (hiTail != null) {
                        hiTail.next = null; // 最后一个节点的next设为空
                        newTab[j + oldCap] = hiHead; // 将索引位置为原索引+oldCap的节点设置为头结点
                    }
                }
            }
        }
    }
    return newTab;
}
```

1. 判断**旧表的容量为0, 旧表的阈值为0**, 若是，则为空表，设置默认容量和阈值
2.  若**旧表的容量为 0, 旧表的阈值大于0**，则新表的容量为旧表的阈值
3. 若**旧表不为空**，并且 容量*2< 最大容量并且 >=16, 则将阈值设置为原来的两倍
4.  把将当前阈值赋值为刚计算出来的新的阈值，以上都是计算出容量和阈值
5. 遍历旧表的容量，把每个桶对应的元素，判断是不是红黑树结点，若是，调用红黑树结点对应的方法
6. 若不是，按照链表的方式，放入新的桶中，每一个元素新的index 可能不一样，不一样，就作为头结点放入，若一样，就和旧表样的Node放入
7. 遍历完旧表，返回newTab

- V get(Object key) ：返回指定键所映射的值；如果此映射不包含该键的映射关系，则返回 null
- V put(K key, V value) ：将指定的值与此映射中的指定键关联
- V remove(Object key) ：如果存在一个键的映射关系，则将其从此映射中移除
- int size() ：返回此映射中的键-值映射关系数，包含链表上的结点

###### 13：并发时可能导致的问题 (都是扩容的问题) ：

1.  put 扩容时，可能会导致某个元素没有给挂在链表上，导致丢失，因为已经第一个线程已经 new了一个新的结点，第二个线程来时判断他为空，也 new 了一个结点，但只挂了一个,**导致丢失**
2. get（index）元素时可能为空，因为扩容时，把新 new的数组赋给旧的数组，而这时还没再hash计算挂链，这是一个线程来读，可能会导致读到元素为空



### 3：Class LinkedHashMap<K,V>：

为了解决 hashmap 不保证映射顺序的（无序）问题，迭代顺序

![](https://github.com/likang315/Java-and-Middleware/blob/master/Java_note/5%EF%BC%9A%E6%B3%9B%E5%9E%8B%EF%BC%8C%E9%9B%86%E5%90%88%EF%BC%8CMap/%E6%96%B0%E5%BB%BA%E6%96%87%E4%BB%B6%E5%A4%B9/LinkedHashMap.jpg?raw=true)

```java
public class LinkedHashMap<K,V> extends HashMap<K,V> implements Map<K,V> {
	  //用于指向双向链表的头部
    transient LinkedHashMap.Entry<K,V> head;
    //用于指向双向链表的尾部
    transient LinkedHashMap.Entry<K,V> tail;
    //用来指定 LinkedHashMap 的迭代顺序，true 则表示按照基于访问的顺序来排列，意思就是最近使用的entry，放在链表的最末尾,false则表示按照插入顺序来，插入到尾部,默认为 False
    final boolean accessOrder;
    public LinkedHashMap(int initialCapacity,float loadFactor,boolean accessOrder) {
   		super(initialCapacity, loadFactor) ;
   		this.accessOrder = accessOrder ;
    }
  //取值
  public V get(Object key) {
    Node<K,V> e;
    //调用 HashMap 的 getNode的方法
    if ((e = getNode(hash(key), key)) == null)
      return null;
    //在取值后对参数 accessOrder 进行判断，如果为true，执行afterNodeAccess
    if (accessOrder)
      afterNodeAccess(e);  //将最近使用的Entry，放在链表的最末尾
    return e.value;
  }
  //移除头结点
  void afterNodeInsertion(boolean evict) { 
    LinkedHashMap.Entry<K,V> first;
    if (evict && (first = head) != null && removeEldestEntry(first)) {
      K key = first.key;
      removeNode(hash(key), key, null, false, true);
    }
  }
  //移除此结点到尾部
  void afterNodeAccess(Node<K,V> e) { 
    LinkedHashMap.Entry<K,V> last;
    if (accessOrder && (last = tail) != e) {
      LinkedHashMap.Entry<K,V> p =
        (LinkedHashMap.Entry<K,V>)e, b = p.before, a = p.after;
      p.after = null;
      if (b == null)
        head = a;
      else
        b.after = a;
      if (a != null)
        a.before = b;
      else
        last = b;
      if (last == null)
        head = p;
      else {
        p.before = last;
        last.after = p;
      }
      tail = p;
      ++modCount;  //Fail-fast
    }
  }
	//静态内部类
  static class Entry<K,V> extends HashMap.Node<K,V> {
    //用于维护双向链表
    Entry<K,V> before, after;
    Entry(int hash, K key, V value, Node<K,V> next) {
      super(hash, key, value, next);
    }
  }

  abstract class LinkedHashIterator {
    //记录下一个Entry
    LinkedHashMap.Entry<K,V> next;
    //记录当前的Entry
    LinkedHashMap.Entry<K,V> current;
    //记录是否发生了迭代过程中的修改
    int expectedModCount;

    LinkedHashIterator() {
      //初始化的时候把head给next
      next = head;
      //每一个迭代器对应自己的 expectModCount
      expectedModCount = modCount;   //Fail-Fast
      current = null;
    }

    public final boolean hasNext() {
      return next != null;
    }

    //采用的是链表方式的遍历方式
    final LinkedHashMap.Entry<K,V> nextNode() {
      LinkedHashMap.Entry<K,V> e = next;
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      if (e == null)
        throw new NoSuchElementException();
      //记录当前的Entry
      current = e;
      //直接拿after给next
      next = e.after;
      return e;
    }

    public final void remove() {
      Node<K,V> p = current;
      if (p == null)
        throw new IllegalStateException();
      if (modCount != expectedModCount)
        throw new ConcurrentModificationException();
      current = null ;
      K key = p.key;
      removeNode(hash(key), key, null, false, false);
      //给当前的ExpectedModCount 重新赋值
      expectedModCount = modCount;
    }
  }
}
```

即 LinkedHashMap 它是在原来的基础上维护了一个双向链表保证有序的HashMap,每次 **put 进来 Entry映射关系，除了将其保存到哈希表中对应的位置上之外，还会将其插入到双向链表的尾部**，内部类额外增加的两个属性来维护的一个双向链表**：before、After **是用于维护Entry插入的先后顺序的



### 4：Class Hashtable<K,V> ：

​	HashMap 的升级版，并发,多线程的情况下，使用 Hashmap 进行 put 操作会引起死循环,导致CPU利用率接近100%

```java
public class Hashtable<K,V> extends Dictionary<K,V> implements Map<K,V>, Cloneable, java.io.Serializable
```

1. 线程安全(synchronized)和非线程安全的

   Hashtable 是线程安全给每个方法加了同步锁，所以在单线程环境下它比HashMap要慢，效率低

2. 支不支持 null 值和 null 键

   HashTable 不支持null值和null键 ，而HashMap是因为对null做了特殊处理，将 null 的hashCode值定为了0，从而将其存放在哈希表的第0个bucket中

3. 遍历方式不同：

   HashMap的迭代器(Iterator)是fail-fast迭代器，而 Hashtable 是 enumerator迭代器

4. 初始容量和扩容机制不同

   HashTable的初始容量是11，HashMap的初始容量是16.两者的填充因子默认都是0.75 **HashMap扩容时 ：当前容量X2 **，在扩容时须要重新计算hash **Hashtable扩容时：当前容量X2+1**



### 5：java.util.concurrent   Class   ConcurrentHashMap：

HashTable 容器使用 synchronized 来保证线程安全，但在线程竞争激烈的情况下 HashTable 的效率非常低下的，当一个线程访问HashTable的同步方法时，其他线程访问HashTable的同步方法时，可能会进入阻塞或轮询状态

```java
public class ConcurrentHashMap<K,V> extends AbstractMap<K,V>  implements ConcurrentMap<K,V>, Serializable {
  private static final long serialVersionUID = 7249069246763182397L;
	//刨析源码
  	
}

```

###### 1：JDK1.7 实现 ConcurrentHashMap 的锁分段技术

将数据分成一段一段的存储，然后给每一段数据配一把锁（Segment），当一个线程占用锁访问其中一个段数据的时候，其他段的数据也能被其他线程访问，提高了并发性

HashTable 容器在并发环境下表现出效率低下的原因，是因为**所有访问HashTable的线程都必须竞争同一把锁**，如果容器里有多把锁每一把锁用于锁容器其中一部分数据，那么当多线程访问容器里不同数据段的数据时，线程间就不会存在锁竞争，从而可以有效的提高并发访问效率，这就是 ConcurrentHashMap 所使用的锁分段技术结构 由Segment(段) 数组结构 和 HashEntry 数组结构组成

###### 2：Segment 锁

是一种可重入锁，在 ConcurrentHashMap 里扮演锁的角色，HashEntry 则用于存储键值的映射关系

Segment 的结构 和 HashMap 类似，是一种数组和链表结构， 一个 Segment 里包含一个 HashEntry 数组，每个HashEntry 是一个链表结构的元素,每个 Segment 守护着一个HashEntry数组里的元素,当对HashEntry数组的数据进行修改时，必须首先获得它对应的 Segment 锁

默认是 16，也就是说 ConcurrentHashMap 有 16 个 Segments，所以理论上，这个时候，最多可以同时支持 16 个线程并发写，只要它们的操作分别分布在不同的 Segment 上。这个值可以在初始化的时候设置为其他值，但是一旦初始化以后，它是不可以扩容的

###### 3：构造函数进行初始化的，那么初始化完成后：

- Segment 为 16个，不可以扩容
- Segment[ i ] 的默认大数组大小为 2，负载因子是 0.75，得出初始阈值为 1.5，也就是以后插入第一个元素不会触发扩容，插入第二个会进行第一次扩容
- segment 不能扩容，扩容的是 segment 内部的数组 HashEntry[] 进行扩容，扩容后，容量为原来的 2 倍

###### 4：JDK1.8 实现 ConcurrentHashMap

Node 数组+链表+红黑树 的数据结构来实现，并发控制使用Synchronized和CAS来操作，整个看起来就像是优化过且线程安全的HashMap，虽然在JDK1.8中还能看到 Segment 的数据结构，但是已经简化了属性，只是为了兼容旧版本

### 6：java.util  Class   WeakHashMap：

​	当某个“弱键”不再正常使用时（弱引用），会被从 WeakHashMap 中被自动移除，被垃圾回收器所回收



### 7：Interface SortedMap<K,V>：

​	map 的子接口，增加了排序的功能(comparator), TreeMap 实现了它的继承接口



### 8：Class TreeMap<K,V>：

TreeMap 基于 **红黑树（Red-Black tree）实现**，该映射根据**其键的自然顺序进行排序**，或者根据**创建映射时提供的 Comparator 进行排序**，具体取决于使用的构造方法

- 默认大小：size = 0；
- TreeMap 基本操作 containsKey、get、put 和 remove 的时间复杂度是 log(n) ，TreeMap是**非同步**的，它的iterator()，返回的**迭代器是fail-fastl**的
- TreeMap 不支持null键，但是 支持null值，排序时，每个结点都是一个Entry<K,V>
  - TreeMap()：使用键的自然顺序构造一个新的、空的树映射
  - TreeMap(Comparator<? super K> comparator) ：构造一个新的、空的树映射，该映射根据给定比较器进行排序 
  - SortedMap<K,V>   subMap(K fromKey, K toKey)  ：返回键值的范围从 fromKey（包括）到 toKey（不包括）的部分视图
  - SortedMap<K,V> tailMap(K fromKey) ：返回此映射的部分Entry，其键大于等于 fromKey

```java
// ToDo(likang):待深入红黑树时吗，在以此为例对照学习
public class TreeMap<K,V> extends AbstractMap<K,V>  implements NavigableMap<K,V>, Cloneable, java.io.Serializable {
	// extends AbstractMap ：表明其是一个MaP
  // implements NavigableMap<K,V> :实现了此接口，意味着支持一系列导航方法，返回有序的key集合
  // 实现了Cloneable接口，意味着它能被克隆
  // 实现了java.io.Serializable，意味着它支持序列化
  
  // 使用该构造函数，TreeMap 中的元素按照自然排序进行排列
  public TreeMap() {
    comparator = null;
  }
	// 指定排序器
  public TreeMap(Comparator<? super K> comparator) {
    this.comparator = comparator;
  }
	// 添加Map
  public TreeMap(Map<? extends K, ? extends V> m) {
    comparator = null;
    putAll(m);
  }
  // 比较器
	private final Comparator<? super K> comparator;
	// 红黑树的根节点
  private transient Entry<K,V> root;
  // 红黑树的数量
  private transient int size = 0;
	// 修改红黑树结构的数量
 	private transient int modCount = 0;
  // 返回TreeMap中是否保护“键(key)”
	public boolean containsKey(Object key) {
  	return getEntry(key) != null;
  }
  // 返回TreeMap中是否保护"值(value)"
  public boolean containsValue(Object value) {
 		// getFirstEntry() 是返回红黑树的第一个节点
    // successor(e) 是获取节点e的后继节点
    for (Entry<K,V> e = getFirstEntry(); e != null; e = successor(e))
      if (valEquals(value, e.value))
        return true;
    return false;
  }
	// 获取后继节点
  static <K,V> TreeMap.Entry<K,V> successor(Entry<K,V> t) {
    if (t == null)
      return null;
    else if (t.right != null) {
      Entry<K,V> p = t.right;
      while (p.left != null)
        p = p.left;
      return p;
    } else {
      Entry<K,V> p = t.parent;
      Entry<K,V> ch = t;
      while (p != null && ch == p.right) {
        ch = p;
        p = p.parent;
      }
      return p;
    }
  }
  // 获取前驱节点
  static <K,V> Entry<K,V> predecessor(Entry<K,V> t) {
    if (t == null)
      return null;
    else if (t.left != null) {
      Entry<K,V> p = t.left;
      while (p.right != null)
        p = p.right;
      return p;
    } else {
      Entry<K,V> p = t.parent;
      Entry<K,V> ch = t;
      while (p != null && ch == p.left) {
        ch = p;
        p = p.parent;
      }
      return p;
    }
  }

  // 获取TreeMap中键为key的节点
  final Entry<K,V> getEntry(Object key) {
    // 若"比较器"为null，则通过getEntryUsingComparator()获取"健"为key的节点
    if (comparator != null)
      return getEntryUsingComparator(key);
    if (key == null)
      throw new NullPointerException();
    Comparable<? super K> k = (Comparable<? super K>) key;
    // 将p设为根节点，开始比较大小遍历
    Entry<K,V> p = root;
    while (p != null) {
      int cmp = k.compareTo(p.key);
      // 若“p的key” < key，则p=“p的左孩子”
      if (cmp < 0)
        p = p.left;
      // 若“p的key” > key，则p=“p的右孩子”
      else if (cmp > 0)
        p = p.right;
      // 若“p的key” = key，则返回节点p
      else
        return p;
    }
    return null;
  }
  

}
```

###### NavigableMap<K,V> ：一些方法

```java
// 获取TreeMap中不小于key的最小的节点，若不存在(即TreeMap中所有节点的键都比key小)，就返回null
final Entry<K,V> getCeilingEntry(K key) {
  Entry<K,V> p = root;
  while (p != null) {
    int cmp = compare(key, p.key);
    // 情况一：若“p的key” > key。
    // 若 p 存在左孩子，则设 p=“p的左孩子”；
    // 否则，返回p
    if (cmp < 0) {
      if (p.left != null)
        p = p.left;
      else
        return p;
      // 情况二：若“p的key” < key。
    } else if (cmp > 0) {
      // 若 p 存在右孩子，则设 p=“p的右孩子”
      if (p.right != null) {
        p = p.right;
      } else {
        // 若 p 不存在右孩子，则找出 p 的后继节点，并返回
        // 这里返回的 “p的后继节点”有2种可能性：第一，null；第二，TreeMap中大于key的最小的节点
        //   理解这一点的核心是，getCeilingEntry是从root开始遍历的
        //   若getCeilingEntry能走到这一步，那么，它之前“已经遍历过的节点的key”都 > key
        //   能理解上面所说的，那么就很容易明白，为什么“p的后继节点”又2种可能性了
        Entry<K,V> parent = p.parent;
        Entry<K,V> ch = p;
        while (parent != null && ch == parent.right) {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
      // 情况三：若“p的key” = key。
    } else
      return p;
  }
  return null;
}

// 获取TreeMap中不大于key的最大的节点，若不存在(即TreeMap中所有节点的键都比key小)，就返回null
final Entry<K,V> getFloorEntry(K key) {
  Entry<K,V> p = root;
  while (p != null) {
    int cmp = compare(key, p.key);
    if (cmp > 0) {
      if (p.right != null)
        p = p.right;
      else
        return p;
    } else if (cmp < 0) {
      if (p.left != null) {
        p = p.left;
      } else {
        Entry<K,V> parent = p.parent;
        Entry<K,V> ch = p;
        while (parent != null && ch == parent.left) {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
    } else
      return p;

  }
  return null;
}
// 获取TreeMap中大于key的最小的节点，和getCeilingEntry等于时的处理不一样，若不存在，就返回null
final Entry<K,V> getHigherEntry(K key) {
  Entry<K,V> p = root;
  while (p != null) {
    int cmp = compare(key, p.key);
    if (cmp < 0) {
      if (p.left != null)
        p = p.left;
      else
        return p;
    } else {
      if (p.right != null) {
        p = p.right;
      } else {
        Entry<K,V> parent = p.parent;
        Entry<K,V> ch = p;
        while (parent != null && ch == parent.right) {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
    }
  }
  return null;
}
// 获取TreeMap中小于key的最大的节点，若不存在，就返回null
final Entry<K,V> getLowerEntry(K key) {
  Entry<K,V> p = root;
  while (p != null) {
    int cmp = compare(key, p.key);
    if (cmp > 0) {
      if (p.right != null)
        p = p.right;
      else
        return p;
    } else {
      if (p.left != null) {
        p = p.left;
      } else {
        Entry<K,V> parent = p.parent;
        Entry<K,V> ch = p;
        while (parent != null && ch == parent.left) {
          ch = parent;
          parent = parent.parent;
        }
        return parent;
      }
    }
  }
  return null;
}

```

### 9：Map的遍历（三种）：转成 Set 集合，用迭代器遍历

- 遍历 key 键，利用 Set **keySet()** ，返回的 set 集合 
- 遍历所有的value，利用 Collection **values()** ，返回collection集合 
- 遍历键值对，利用 Set<Map.Entry<K,V>> **entrySet()** 方法，返回每一组键值对的set集合， entry.getKey(), entry.getValue()



