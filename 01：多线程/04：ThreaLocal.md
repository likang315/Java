### ThreadLocal <T>

------

[TOC]

##### 01：概述

- 即**线程变量，**一个以当前线程的 ThreadLocal 对象为键—该对象的值为值的存储结构，这个结构被附在线程上，也就是说每个线程都可以根据 threadLocalMap 查询到绑定在这个线程上该变量的值。
- 被其修饰的变量在多线程环境下访问时，能**保证各个线程里的变量相对独立于其他线程内的变量**。
- **泛型中存储的是共享变量，ThreadLocalMap 的 Entry[ ] 中，key为当前线程的 Threadlocal 变量，value为当前线程的共享变量值。**
- 锁一般是以时间换空间，而ThreadLocal是以空间换时间。
- ThreadLocal 实例通常来说都是 private static 类型的，用于关联线程和线程的上下文。

##### 02：方法：只有以下四个方法可以被重写

- ThreadLocal< S > withInitial(Supplier<? extends S> supplier)
  - 用于创建线程变量；
- protected initialValue() 
  - 在调用 get() 的时候会第一次调用该方法，但是如果一开始就调用了set()，则该函数不会被调用；
- T   get()
- void`  `set(T value)
- void remove()

##### 03：ThreadLocalMap【源码剖析】

- ThreadLocal 的一个内部类，是一个定制的哈希映射，仅适用于维护线程本地值，**哈希表Entry使用了对键的弱引用**，有助于GC回收；

###### 特性：

1. 通过 getMap() 获取**每个线程 Thread 持有自己的ThreadLocalMap实例**, 因此它们是不存在并发竞争的，可以理解为每个线程有自己的变量副本；
2. ThreadLocalMap 中 Entry[] 数组存储数据，初始化长度16，后续每次都是2倍扩容。主线程中定义了几个变量，Entry[]才有几个key；
3. Entry 的 key是对 **当前线程ThreadLocal的弱引用**，当抛弃掉ThreadLocal对象时，垃圾收集器会忽略这个key的引用而清理掉 ThreadLocal 对象，**防止内存泄漏**；
4. 如果 Threadlocal 在线程中还被**使用的时候他是一个强引用（new  ThreadLocal）**，而作为强引用的时候，他在ThreadLocalMap中的key这个弱引用就不会被GC收掉；

```java
public class ThreadLocal<T> {
    // 若没有调用set()时，第一次调用get()方法，自动调用
    protected T initialValue() {
        return null;
    }
    // 获取当前线程的ThreadLocalMap
    ThreadLocalMap getMap(Thread t) {
        return t.threadLocals;
    }
    // 获取当前线程的存储的value
    public T get() {
        Thread t = Thread.currentThread();
        // 获取当期线程自己的map
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            // this:当前线程的threadlocal对象
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                // 将线程的T返回，只有这个T是共享的
                @SuppressWarnings("unchecked")
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }

    // 设置相应的值
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

    private void set(ThreadLocal<?> key, Object value) {
        Entry[] tab = table;
        int len = tab.length;
        int i = key.threadLocalHashCode & (len-1);
        for (Entry e = tab[i]; e != null; e = tab[i = nextIndex(i, len)]) {
            ThreadLocal<?> k = e.get();
            if (k == key) {
                e.value = value;
                return;
            }

            if (k == null) {
                replaceStaleEntry(key, value, i);
                return;
            }
        }
        tab[i] = new Entry(key, value);
        int sz = ++size;
        if (!cleanSomeSlots(i, sz) && sz >= threshold)
            rehash();
    }

    // innner Class
    static class ThreadLocalMap {
        // value 是弱引用
        static class Entry extends WeakReference<ThreadLocal<?>> {
            Object value;
            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
        private Entry[] table;
        ThreadLocalMap(ThreadLocal<?> firstKey, Object firstValue) {
            table = new Entry[INITIAL_CAPACITY];
            // 通过标存储相同的key，不同的值
            int i = firstKey.threadLocalHashCode & (INITIAL_CAPACITY - 1);
            table[i] = new Entry(firstKey, firstValue);
            size = 1;
            setThreshold(INITIAL_CAPACITY);
        }
    }
}
```

##### 04：示例：

- SImpleDateFormat：线程不安全的

```java
// 创建线程变量，这个变量的初始值在调用get() 时被定义
private static ThreadLocal<SimpleDateFormat> localDateFormat =
    ThreadLocal.withInitial(() -> new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));

for (int i = 0; i < 5; i++) {
    executorService.submit(() -> {
        for (int j = 0; j < 100; j++) {
            Date date;
            try {
                // 获取每个线程对应的该变量
                date = localDateFormat.get().parse(dateString);
                log.info("parsed date:{}", date);
            } catch (Exception e) {
                log.warn("got exception while parsing {}", dateString, e);
            }
        }
    });
}
```

