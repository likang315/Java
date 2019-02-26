### java.lang

## Class ThreadLocal<T>

用来提供线程内部的局部变量。这种变量在多线程环境下访问(通过get或set方法访问)时能保证各个线程里的变量相对独立于其他线程内的变量

ThreadLocal实例通常来说都是 **private static** 类型的，用于关联线程和线程的上下文

##### 方法：

protected  T`  `initialValue() 

​	函数在调用`get`函数的时候会第一次调用，但是如果一开始就调用了`set`函数，则该函数不会被调用

T      get()

void`  `remove()

void`  `set(T value)

static <S> ThreadLocal<S>`  `withInitial(Supplier<? extends S> supplier)



1：通过getMap()获取每个子线程Thread 持有自己的**ThreadLocalMap实例,** 因此它们是不存在并发竞争的。可以理解为每个线程有自己的变量副本

2：ThreadLocalMap中Entry[]数组存储数据，初始化长度16，后续每次都是2倍扩容。主线程中定义了几个变量，Entry[]才有几个key

 3：Entry的key是对ThreadLocal的弱引用，当抛弃掉ThreadLocal对象时，垃圾收集器会忽略这个key的引用而清理掉ThreadLocal对象， 防止了内存泄漏

### ThreadLocalMap 是ThreadLocal 的一个内部类

ThreadLocalMap是一个定制的哈希映射，仅适用于维护线程本地值。ThreadLocalMap类是包私有的，允许在Thread类中声明字段。为了帮助处理非常大且长时间的使用，哈希表entry使用了对键的弱引用，有助于GC回收

##### key为当前ThreadLocal对象，value则是对应线程的变量副本

