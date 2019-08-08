### Redis 命令

------

##### 1：安装 Reids

1. 下载：https://github.com/MSOpenTech/redis/releases，下载Redis压缩包
2. 用 cd 命令切换目录到 C:\redis  运行，启动服务端： 
   - redis-server.exe  ：执行此命令，redis.conf 是一个默认的配置文件
3. 另启一个 cmd 窗口，原来的不要关闭，不然就无法访问服务端了，切换到 redis 目录下运行: 
   - redis-cli.exe -h 127.0.0.1 -p 6379
4. 设置键值对：set myKey abc 
5. 取出键值对：get myKey

##### 2：Redis 配置信息

Redis 的配置文件位于 Redis 安装目录下，文件名为 redis.conf

- 通过 CONFIG 命令查看或设置配置项
  - CONFIG  GET  CONFIG_SETTING_NAME  ：获取指定 CONFIG_SETTING_NAME的值，配置信息
  - CONFIG  GET  *     ：* 号获取所有配置项
  - CONFIG  SET   CONFIG_SETTING_NAME    NEW_CONFIG_VALUE  ：使用 CONFIG set 修改配置信息

##### 3：连接Redis服务

- redis-cli：该命令会连接本地的 redis 服务
- ping：用于检测Redis服务是否启动
- AUTH password：验证密码是否正确
- QUIT：关闭当前连接
- SELECT index：切换到指定的数据库
- redis-cli -h host -p port -a password ：远程连接Redis 服务

##### 4：Redis 键 (key)：用于管理 redis 的键

- 如果键被删除成功，命令执行后输出 (integer) 1，否则将输出 (integer) 0

- DEL key：用于在 key 存在时删除  key

- DUMP key：用于序列化 给定 key ，并返回被序列化的值，如果 key 不存在，那么返回 nil 

- ###### EXISTS key：用于检查给定 key 是否存在，若 key 存在返回 1 ，否则返回 `0`

- ###### Expire key second ：命令用于设置 key 的过期时间，key 过期后将不再可用，单位以秒计

- Expire key timestamp ：命令用于以 UNIX 时间戳(unix timestamp)格式设置 key 的过期时间，key 过期后将不再可用

- KEYS pattern ： 查找所有符合给定模式 pattern 的 key ，表达式

- MOVE key  db：命令用于将当前数据库的 key 移动到给定的数据库 db 当中， redis默认使用数据库 0

- PERSIST key ： 用于移除给定 key 的过期时间，使得 key 永不过期

- TTL key：以秒为单位返回 key 的剩余过期时间

- ###### PTTL key：以毫秒为单位返回 key 的剩余过期时间

- RANDOMKEY ：从当前数据库中随机返回一个 key ，用于测试数据库为不为空，为空时，返回 nil 

- RENAME key newkey：修改可以的值

- ###### Type key：返回存储值得类型

- INFO ：获取 Redis 服务器的各种信息和统计数值

- CLIENTLIST：获取连接到服务器的客户端内连接列表

- FLUSHDB：删除当前数据库的所有key

- FLUSHALL ：删除所有数据库的所有key

##### 5：Redis 数据备份于恢复

- SAVE（BGSAVE） ：命令用于创建当前数据库的备份，该命令将在 redis 安装目录中**创建dump.rdb文件**
- 数据回复只需将备份文件 (dump.rdb) 移动到 redis 安装目录并启动服务即可
- CONFIG GET dir    ：获取 redis 安装目录可以使用 

##### 6：Redis 字符串(String)

- SET key value ：设置key的值
- GET key  ：返回指定 Key 的值
- GESET key value ：将 key 的值设为新的 value ，并返回 key 的旧值(old value)

##### 7：Redis 哈希(Hash)

- HSET key filed value ：将哈希表 key 中的字段 field 的值设为 value 
- HMSET key field1 value1 [field2 value2 ]  ：同时将多个 field-value (域-值)对设置到哈希表 key 中
- HGET key filed ：获取存储hash表中指定的字段值
- HMGET key field1 [field2] ：获取所有给定字段的值
- HKEYS key ：获取所有哈希表中的字段
- HLEN key ：获取哈希表中字段的数量

```shell
redis> HMSET myhash field1 "Hello" field2 "World"
"OK"
redis> HGET myhash field1
"Hello"
redis> HGET myhash field2
"World"
```

##### 8：Redis 列表（List）

- LPUSHX key value ：将一个值插入到已存在的列表头部
- LINDEX key index ：通过索引获取列表中的元素，可能存在相同的key
- LLEN key：获取列表长度

```shell
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

##### 9：Redis 集合(Set)

- SADD key member1 [member2] ：向集合添加一个或多个成员
- SREM key member1 [member2]：移除集合中一个或多个成员
- sismember key member：判断 member 元素是否是集合 key 的成员 

```shell
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
# 以上实例中 rabitmq 添加了两次，但根据集合内元素的唯一性，第二次插入的元素将被忽略
```

##### 10：Redis 有序集合 (zset)

- ZADD key score1 member1 [score2 member2]
  - 向有序集合添加一个或多个成员，或者更新已存在成员分数
- ZCARD key：获取有序集合的成员数

```shell
zadd key score member     # 添加zset
ZRANGEBYSCORE key 0 1000  # 查看
```