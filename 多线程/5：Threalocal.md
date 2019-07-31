###### java.lang

##### Class ThreadLocal<T>

即线程变量，一个以ThreadLocal对象为键、任意对象为值的存储结构，这个结构被附在线程上

- 被其修饰的变量在多线程环境下访问时，能保证各个线程里的变量相对独立于其他线程内的变量
- 锁一般是以时间换空间，而ThreadLocal是以空间换时间
- ThreadLocal 实例通常来说都是 private static final 类型的，用于关联线程和线程的上下文

###### 方法：（只有这4个可以被重写）

- protected initialValue() 
  - 函数在调用 get() 的时候会第一次调用，但是如果一开始就调用了set()，则该函数不会被调用
- T   get()
- void`  `set(T value)
- void remove()

###### ThreadLocalMap：ThreadLocal 的一个内部类

- 是一个定制的哈希映射，仅适用于维护线程本地值，哈希表entry使用了对键的弱引用，有助于GC回收

###### 特点：

1. 通过getMap()获取每个子线程Thread 持有自己的ThreadLocalMap实例, 因此它们是不存在并发竞争的，可以理解为每个线程有自己的变量副本
2. ThreadLocalMap 中 Entry[] 数组存储数据，初始化长度16，后续每次都是2倍扩容。主线程中定义了几个变量，Entry[]才有几个key
3. Entry 的 key是对 当前线程ThreadLocal的弱引用，当抛弃掉ThreadLocal对象时，垃圾收集器会忽略这个key的引用而清理掉ThreadLocal对象， 防止了内存泄漏

###### key 当前线程的 ThreadLocal 对象，value则是对应线程的变量副本

```java
public class ThreadLocal<T> {
  Entry(ThreadLocal<?> k, Object v)

    public T get() {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null) {
      ThreadLocalMap.Entry e = map.getEntry(this);
      if (e != null) {
        @SuppressWarnings("unchecked")
        T result = (T)e.value;
        return result;
      }
    }
    return setInitialValue();
  }

  public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
      map.set(this, value);
    else
      createMap(t, value);
  }
  public void remove() {
    ThreadLocalMap m = getMap(Thread.currentThread());
    if (m != null)
      m.remove(this);
  }
  ThreadLocalMap getMap(Thread t) {
    return t.threadLocals;
  }
}
```



