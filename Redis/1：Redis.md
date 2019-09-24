### Redis：基于内存亦可持久化的日志型，Key-Value 数据库

------

​	Redis 是一个开源的使用C语言编写、遵守BSD协议、支持网络、可基于内存亦可持久化的日志型、Key-Value 数据库，并提供多种语言的API，通常被称为数据结构服务器

​	redis 作为内存数据库，所有数据都**保存在内存中**, 一旦程序停止工作, 数据都将丢失. 需要我们重新从其他地方加载数据，不过Redis提供了两种方式保存Redis中的数据

1. dump.rdb 快照，把内存信息持久化磁盘上
2. 一种是存在aof文件中，aof 文件存储的是一条一条存储和修改数据的命令，类似于 mysql的二进制日志形式

###### redis：特点

- Redis 支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用
- Redis 数据结构丰富，不仅仅支持的 key-value 类型的数据，还提供list，set，zset，hash等数据结构的存储
- Redis 支持数据的备份，即 master-slave （一主多从）模式的数据备份
- Redis 的单个操作都是原子性的，要么成功执行要么失败，单个操作是原子性的。多个操作支持事务，但不是原子性，通过MULTI和EXEC指令包起来

###### Redis支持 vualue 的五种 数据类型：

​	string（字符串），hash（哈希），list（列表），set（集合），zset  (有序集合)

##### 1：String：类型是二进制

- Redis 的 String 可以包含任何数据，比如jpg图片或者序列化的对象
- 字符串是动态字符串，采⽤**预分配冗余空间的⽅式来减少内存的频繁分配**，内部为当前字符串实际分配的空间 capacity ⼀般要⾼于实际字符串⻓度 len
- 扩容方式：当字符串⻓度⼩于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时⼀次只会多扩 1M 的空间，注意的是字符串**最⼤⻓度为 512M**

##### 2：Hash：一个键值对集合 (key：value)  

- 是一个 String 类型的 key和 value 的映射表，适合用于存储对象，底层使⽤数组 + 链表
- 每个 hash 可以存储 2^32-1 键值对（40多亿）

###### Hash 的扩容

​	是包含两个项的数组，数组中的每个项都⼀个字典, ⼀般情况下, 字典只使⽤ ht[0] 字典, ht[1] 只在 ht[0] 进⾏
rehash 的时候使用，作为临时载体

###### 渐进式Rehash：不会阻塞读写操作，索引变量index（相当于下标）

- 渐进式Rehash操作，将rehash键值对所需的计算，**均摊到对字典的所有操作中**，从而避免了集中式的rehash带来庞大的计算量

![Rehash.png](https://github.com/likang315/Java-and-Middleware/blob/master/Redis/Redis/Rehash.png?raw=true)

##### 3：List：序列

- 可以添加一个元素到列表的头部或者尾部列表，最多可存储  2^32 - 1 元素，底层是双向链表

##### 4：Set：String 类型元素的无序集合

​	相当于 Java 中的 HashSet，它内部的键值对是⽆序，并且唯⼀的，底层实现相当于⼀个特殊的字典，字典中所有的 **value 都是⼀个 NULL**，当集合中的最后⼀个元素被移除后, 数据结构⾃动删除, 内存被回收

##### 5：zset：String 类型元素的有序集合且唯一

- 底层实现是：跳表+Hash，跳表保证有序，Hash保证的查找高效
- 不同的是每个元素都会关联一个**double类型的 score** ，redis 正是通过 score 来为集合中的成员进行从小到大的排序，zset的成员是唯一的，但分数(score)却可以重复
- 从层开始查找，比他大往后，比他小往上

![](https://github.com/likang315/Java-and-Middleware/blob/master/Redis/Redis/Zset.png?raw=true)



