### Redis 命令：在 Redis 客户端执行命令



### 安装 Reids

1：**cmd** 窗口  使用 cd 命令切换目录到 **C:\redis**  运行： 

##### redis-server.exe    redis.windows.conf   ， **redis.conf** 是一个默认的配置文件

2：另启一个 cmd 窗口，原来的不要关闭，不然就无法访问服务端了。切换到 redis 目录下运行: 

##### redis-cli.`exe -h 127.0.0.1 -p 6379`

3：设置键值对：**set myKey abc** ，取出键值对：**get myKey**



### Redis 配置

Redis 的配置文件位于 Redis 安装目录下，文件名为 redis.conf，

##### 通过 CONFIG 命令查看或设置配置项

###### CONFIG   GET    CONFIG_SETTING_NAME     //获取指定 CONFIG_SETTING_NAME的值，配置信息

###### CONFIG   GET  *       // * 号获取所有配置项

##### 使用 CONFIG set 修改配置信息

CONFIG  SET    CONFIG_SETTING_NAME    NEW_CONFIG_VALUE



###### 1：连接Redis服务：

 **redis-cli**：该命令会连接本地的 redis 服务

 **ping**：用于检测Redis服务是否启动

**AUTH password**：验证密码是否正确

**ECHO message**  ：打印字符串

**QUIT**  ：关闭当前连接

**SELECT index** ：切换到指定的数据库

###### 2：在远程服务上执行命令

 **redis-cli -h host -p port -a password**     //连接到主机为 127.0.0.1，端口为 6379 ，密码为 mypass 的 redis 服务上

### Redis 键 (key)：用于管理 redis 的键

如果键被删除成功，命令执行后输出 **(integer) 1**，否则将输出 **(integer) 0**

###### DEL key：用于在 key 存在时删除  key

###### DUMP key：用于序列化 给定 key ，并返回被序列化的值，如果 key 不存在，那么返回 nil 

###### EXISTS key：用于检查给定 key 是否存在，若 key 存在返回 1 ，否则返回 `0`

###### Expire key second ：命令用于设置 key 的过期时间，key 过期后将不再可用，单位以秒计

###### Expire key timestamp ：命令用于以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间，key 过期后将不再可用

######  KEYS pattern ： 查找所有符合给定模式 pattern 的 key ，表达式

###### MOVE key  db：命令用于将当前数据库的 key 移动到给定的数据库 db 当中， redis默认使用数据库 0

###### PERSIST key ： 用于移除给定 key 的过期时间，使得 key 永不过期

###### TTL key：以秒为单位返回 key 的剩余过期时间

###### PTTL key：以毫秒为单位返回 key 的剩余过期时间

###### RANDOMKEY ：从当前数据库中随机返回一个 key ，用于测试数据库为不为空，为空时，返回 nil 

###### RENAME key newkey：修改可以的值

###### Type key：返回存储值得类型



#### Redis 字符串(String)

SET key value 设置key的值

GET key ：返回指定 Key 的值

GESET key value：将给定 key 的值设为 value ，并返回 key 的旧值(old value)

#### Redis 哈希(Hash)

HSET key filed value：将哈希表 key 中的字段 field 的值设为 value 

HMSET key field1 value1 [field2 value2 ] ：同时将多个 field-value (域-值)对设置到哈希表 key 中

HGET key filed：获取存储hash表中指定的字段值

HMGET key field1 [field2]：获取所有给定字段的值

HKEYS key：获取所有哈希表中的字段

HLEN key：获取哈希表中字段的数量

#### Redis 列表（List）

LPUSHX key value：将一个值插入到已存在的列表头部

LINDEX key index ：通过索引获取列表中的元素

LLEN key：获取列表长度

#### Redis 集合(Set)

SADD key member1 [member2] ：向集合添加一个或多个成员

SREM key member1 [member2]：移除集合中一个或多个成员

SISMEMBER key member：判断 member 元素是否是集合 key 的成员

#### Redis 有序集合( zset)

ZADD key score1 member1 [score2 member2]:向有序集合添加一个或多个成员，或者更新已存在成员的分数

ZCARD key：获取有序集合的成员数

### 发布订阅(pub/sub)：是一种消息通信模式

###### 发送者(pub)发送消息，订阅者(sub)接收消息，订阅任意数量的频道

当有新消息通过 PUBLISH 命令发送给频道 channel1 时， 这个消息就会被发送给订阅它的所有订阅者

Psubscribe pattern ：订阅一个或多个符合给定模式的频道

SUBSCRIBE channel [channel ...]：订阅给定的一个或多个频道的信息

PUBLISH channel message：将信息发送到指定的频道

UNSUBSCRIBE [channel [channel ...]：指退订给定的频道

### Redis 事务

- 批量操作在发送 EXEC 命令前被放入队列缓存
- ###### 收到 EXEC 命令后进入事务执行，事务中任意命令执行失败，其余的命令依然被执行
-  在事务执行过程，其他客户端提交的命令请求不会插入到事务执行命令序列中

单个 Redis 命令的执行是原子性的，但 Redis 没有在事务上增加任何维持原子性的机制，所以 Redis 事务的执行并不是原子性的 

MULTI：标记一个事务块的开始

EXEC： 执行所有事务块内的命令

DISCARD ：取消事务，放弃执行事务块内的所有命令

UNWATCH： 取消 WATCH 命令对所有 key 的监视

WATCH key [key ...]：监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断

### Redis 服务器命令主要是用于管理 redis 服务

 INFO ：获取 Redis 服务器的各种信息和统计数值

 Bgrewriteaof ：用于异步执行一个 AOF（Append Only File） 文件重写操作

CLIENTLIST：获取连接到服务器的客户端内连接列表

CLIENT KILL [ip:port\] [ID client-id] ：关闭客户端连接

CLIENT SETNAME connection-name：设置当前连接的名称

FLUSHDB：删除当前数据库的所有key

FLUSHALL ：删除所有数据库的所有key

