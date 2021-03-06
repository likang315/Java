### Redis：

------

[TOC]

##### 01：概述

- Redis 是一个开源的使用C语言编写、遵守BSD协议、支持网络、**基于内存亦可持久化的日志型、Key-Value 数据库**，并提供多种语言的API，通常被称为数据结构服务器；
- redis 作为内存数据库，所有数据都**保存在内存中**, 一旦程序停止工作，数据都将丢失，需要我们重新从其他地方加载数据，不过Redis提供了两种方式保存Redis中的数据；
  1. dump.rdb 快照，把内存信息**持久化磁盘**上；
  2. 一种是**存储到AOF文件中**，aof 文件存储的是一条一条存储和修改数据的命令，类似mysql的二进制日志；

###### redis 特性

- Redis 支持**数据的持久化**，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用；
- Redis **数据结构丰富**，不仅仅支持的 key-value 类型的数据，还提供list，set，zset，hash等数据结构的存储；
- Redis 支持数据的备份，即 **master-slave （一主多从）模式的数据备份**；
- Redis 的单个操作都是原子性的，要么成功要么失败，**单个操作是原子性的。多个操作支持事务，但不是原子性**，通过MULTI和EXEC指令包起来；

###### Value 的数据结构【五种】

1. String（字符串）
2. Hash（哈希）
3. List（列表）
4. Set（集合）
5. Zset  (有序集合)

##### 02：String

- Redis 的 String 可以包含任何数据，比如jpg图片或者序列化的对象；
- 字符串是动态字符串，采⽤**预分配冗余空间的⽅式来减少内存的频繁分配**，内部为当前字符串实际分配的空间 capacity ⼀般要⾼于实际字符串⻓度 len
- **扩容方式**：当字符串⻓度⼩于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时⼀次只会多扩 1M 的空间，注意的是字符串**最⼤⻓度为 512M**；

##### 03：Hash

- 是一个 String 类型的 key和 value 的映射表，适合用于存储对象，底层使⽤数组 + 链表
- 每个 hash 可以存储 2^32-1 键值对（40多亿）

###### ReHash

- 是包含两个项的数组，数组中的每个项都是⼀个字典, ⼀般情况下, 字典只使⽤ ht[0] 字典, ht[1] 只在 ht[0] 进⾏
  rehash 的时候使用，作为临时载体；

###### Rehash 步骤

1. 为字典ht[1] 分配空间，这个字段取决于要执行的操作，以及ht[0] 当前所包含的键值对的数量；
2. 如果执行扩展操作，那么ht[1]的大小就是一个大于等于ht[o].used * 2 的2的指数幂；
3. 如果执行收操作，那么htp1]的大小是第一个小于等于ht[0].used * 2 的 2 的指数幂；
4. 将保存在ht[0]中的所有键值时rehash到ht[0]上，即**重新计算key的哈希值和索引直然后将key放在ht[1]对应的位置上**；
5. 当ht[0]包含的所有键值对都迁移到了ht[1]，**释放ht[0]，将ht[1]设置为ht[0]，并在在ht[1]新创建一个空白字典**，为下一次
   rehash做准备；

###### 渐进式ReHash

- 渐进式Rehash操作，**不会阻塞读写操作**，索引变量index（相当于下标），将rehash键值对所需的计算，**均摊到对字典的所有操作中**，从而避免了集中式的rehash带来庞大的计算量；

###### 渐进式Rehash步骤

1. 为ht[1] 分配空间，让上字典同时有ht[O)和ht[i];
2. 在字典中维持一个索引计数器变量rehashidx，并将它设置为0，表示ReHash开始；
3. 在rehash期间，每次对字典执行增删改查时，程序**除了执行指定的操作外还会顺带将ht[0]字典在的rehashdx索引上的所有键值对 rehash 到叫ht[1]**，rehash完成后，rehashidx自增1；
4. 随着字典操作的不断进行，最终有的键值对会被renasn至ht[1]，**这时将rehashidx的值没置为 -1**，表示rehash操作已经完成；
5. 在rehash的过程中，程序优先在叫ht[0]中进行查找，如果没有找到，就会继续去ht[1] 中进行查找，此外，新添加的键值对一律保存在ht[1]中，而ht[0] 不在进行任何添加操作，慢慢变成空表；

##### 04：List

- 可以添加一个元素到**列表的头部或者尾部列表**，最多可存储  2^32 - 1 元素，底层是**双向链表**；

##### 05：Set

- String 类型元素的无序集合；
- 相当于 Java 中的 HashSet，它内部的**键值对是⽆序，并且唯⼀的**，底层实现相当于⼀个特殊的字典，字典中所有的 **value 都是⼀个 NULL**，当集合中的最后⼀个元素被移除后, 数据结构⾃动删除, 内存被回收；


##### 06：Zset

- Redis 中最优特色的数据结构，String 类型元素的**有序集合且唯一**；
- 底层实现是：**跳表+Hash，跳表保证有序，Hash保证的查找高效**；
- 该结构中每个元素都会关联一个**double类型的 score** ，redis 正是通过 score 来为集合中的成员进行从小到大的排序，zset的成员是唯一的，但分数(score)却可以重复，**skipList负责实现高性能排序，Hash负责实现高性能查找**；
- ![](https://github.com/likang315/Middleware/blob/master/04%EF%BC%9ARedis/photos/skipList.png?raw=true)

  - **层：**跳表的Level数组，每次创建一个新的结点时，**随机生成[1, 32] 的值作为数组的长度**，即层的高度；
  - **前进指针：**每一层都有一个指向表尾的指针，level[i].forward ，用于从表头方位节点；
  - **后退指针：**backward 用于从表尾方向访问节点；
  - **分值：**用一个double类型的浮点数，跳表中所有节点都按分值的从小到大排序；
  - **成员对象：**是一个指针，指向对象地址；

##### 07：BitMap

- key-value（字符串位图）

- 可以直接对值得bit位操作，大数据查找；

- 示例

  - ```sh
    # 设置值为big（3个字符，一个字符8bit）
    set hello big
    # 获取二进制形式的第1位，即为0
    getbit hello 0
    # 给位图指定索引设置值
    # setbit key offset vlaue
    # 把hello二进制形式的第8位设置为1，之前的ASCII码为98,现在改为99，即把b改为c
    setbit hello 7 1
    # 获取位图指定索引的值
    getbit key offset
    # 获取位图指定范围(start到end,单位为bit，如果不指定就是获取全部)位值为1的个数
    bitcount key [start end]
    ```

    