## Redis：基于内存亦可持久化的日志型，Key-Value 数据库

Redis 是一个开源的使用C语言编写、遵守BSD协议、支持网络、可基于内存亦可持久化的日志型、Key-Value数据库，并提供多种语言的API，通常被称为数据结构服务器，因为值（value）可以是 字符串(String), 哈希(Hash), 列表(list), 集合(sets) 和 有序集合(zset) 类型

Redis作为内存数据库，所有数据都**保存在内存中**, 一旦程序停止工作, 数据都将丢失. 需要我们重新从其他地方加载数据. 
不过Redis提供了两种方式保存Redis中的数据

###### 1：dump.rdb 快照，把内存信息持久化磁盘上

###### 2：一种是存在aof文件中，aof 文件存储的是一条一条存储和修改数据的命令，类似于 mysql的二进制日志形式

### 特点

-  Redis 支持数据的持久化，可以将内存中的数据保存在磁盘中，重启的时候可以再次加载进行使用
-  Redis 不仅仅支持简单的key-value类型的数据，同时还提供list，set，zset，hash等数据结构的存储
-  Redis 支持数据的备份，即 master-slave 模式的数据备份
- Redis 的所有操作都是原子性的，意思就是要么成功执行要么失败完全不执行。单个操作是原子性的。多个操作也支持事务，即原子性，通过MULTI和EXEC指令包起来



### Redis支持 vualue 的五种 数据类型：

###### string（字符串），hash（哈希），list（列表），set（集合）及zset  (有序集合)



### String：类型是二进制安全的

redis 的 string 可以包含任何数据。比如jpg图片或者序列化的对象

Redis 的字符串是**动态字符串，是可以修改的字符串**，采⽤**预分配冗余空间的⽅式来减少内存的频繁分配**，内部为当前字符串实际分配的空间 capacity ⼀般要⾼于实际字符串⻓度 len

当字符串**⻓度⼩于 1M 时，扩容都是加倍现有的空间，如果超过 1M，扩容时⼀次只会多扩 1M** 的空间。需要注意的是字符串**最⼤⻓度为 512M**



### Hash（哈希，字典）：是一个键值 (key：value)  对集合

Redis hash 是一个 string 类型的 key和 value 的映射表，hash 特别适合用于存储对象，⽆序字典, 底层使⽤数组 + 链表

######  HMSET, HGET 命令，HMSET  设置了 field:value 对 , HGET 获取对应 key  对应的 value

每个 hash 可以存储 2 32（次方） -1 键值对（40多亿）

```c
redis> HMSET myhash field1 "Hello" field2 "World"
"OK"
redis> HGET myhash field1
"Hello"
redis> HGET myhash field2
"World"
```

#### Hash 的 扩容

属性是包含两个项的数组, 数组中的每个项都⼀个字典, ⼀般情况下, 字典只使⽤ ht[0] 字典, ht[1] 只在 ht[0] 进⾏
rehash 的时候使用

#### Rehash 和 渐进式 Rehash

![](G:\Java and Middleware\7：Redis\Redis\Rehash.png)

渐进式Rehash操作，将rehash键值对所需的计算，**均摊到对字典的所有操作中**，从而避免了集中式的rehash带来庞大的计算量



### List：集合：你可以添加一个元素到列表的头部（左边）或者尾部（右边）,列表最多可存储  **232 - 1** 元素 

###### 底层是双向链表

```c
redis 127.0.0.1:6379> lpush key redis
(integer) 1
redis 127.0.0.1:6379> lpush key mongodb
(integer) 2
redis 127.0.0.1:6379> lpush key rabitmq
(integer) 3
redis 127.0.0.1:6379> lrange key 0 3
1) "rabitmq"
2) "mongodb"
3) "redis"
```



### Set：是  string  类型 的 无序集合

相当于 Java 中的 HashSet, 它内部的键值对是⽆序，并且唯⼀的, 底层实现相当于⼀个特殊的字典, 字典中所有的 **value 都是⼀个 NULL**，当集合中的最后⼀个元素被移除后, 数据结构⾃动删除, 内存被回收

```c
redis 127.0.0.1:6379> sadd key redis
(integer) 1
redis 127.0.0.1:6379> sadd key mongodb
(integer) 1
redis 127.0.0.1:6379> sadd key rabitmq
(integer) 1
redis 127.0.0.1:6379> sadd key rabitmq
(integer) 0
redis 127.0.0.1:6379> smembers key
1) "redis"
2) "rabitmq"
3) "mongodb"
//以上实例中 rabitmq 添加了两次，但根据集合内元素的唯一性，第二次插入的元素将被忽略
```



### zset (有序集合)：string 类型元素的集合，有序且唯一

###### 底层实现是：跳表+Hash

不同的是每个元素都会关联一个**double类型的 score** ，redis正是通过 score 来为集合中的成员进行从小到大的排序，zset的成员是唯一的,但分数(score)却可以重复

![](G:\Java and Middleware\7：Redis\Redis\Zset.png)

```C
zadd key score member    //添加zset
ZRANGEBYSCORE key 0 1000 //查看
```



#####  从层开始查找，比他大往后，比他小往上



