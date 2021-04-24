### Redis 特性

------

##### 1：Redis 分区

分割数据到多个Redis实例的处理过程，因此每个实例只保存key的一个子集

###### 范围分区：

最简单的分区方式是按范围分区，就是映射一定范围的对象到特定的Redis实例，缺点你是需要维护一张映射表，这个表需要被管理

###### Hash分区：

用一个hash函数将key转换为一个数字，比如使用crc32 hash函数，返回一个整数，对这个整数取模，将其转化为0-3之间的数字，就可以将这个整数映射到4个Redis实例中的一个了

- 注意：取模操作是取除的余数，通常在多种编程语言中用  % 操作符实现

##### 2：发布订阅(pub/sub)：是一种消息通信模式

- 发布者(pub)发送消息，订阅者(sub)接收消息，订阅任意数量的频道
- 当有新消息通过 PUBLISH 命令发送给频道 channel1 时， 这个消息就会被发送给订阅它的所有订阅者
- Psubscribe pattern ：订阅一个或多个符合给定模式的频道
- SUBSCRIBE chanfnel [channel ...]：订阅给定的一个或多个频道的信息
- PUBLISH channel message：将信息发送到指定的频道
- UNSUBSCRIBE [channel [channel ...]：指退订给定的频道

##### 3：Redis 事务

1. 批量操作在发送 EXEC 命令前被放入队列缓存
2. 收到 EXEC 命令后进入事务执行，事务中任意命令执行失败，其余的命令依然被执行，没有原子性
3. 在事务执行过程，其他客户端提交的命令请求不会插入到事务执行命令序列中，隔离性
4. 单个 Redis 命令的执行是原子性的，但 Redis 没有在事务上增加任何维持原子性的机制，所以 Redis 事务的执行并不是原子性的 
   - Multi：标记一个事务块的开始
   
   - Exec： 执行所有事务块内的命令
   
   - discard ：取消事务，放弃执行事务块内的所有命令
   
   - watch key [key ...]：监视一个(或多个) key ，如果在事务执行之前这个(或这些) key 被其他命令所改动，那么事务将被打断
   
   - unwatch： 取消 WATCH 命令对所有 key 的监视
   
   - ```shell
     127.0.0.1:6379> multi
     OK
     127.0.0.1:6379> set a 3
     QUEUED
     127.0.0.1:6379> lpop a
     QUEUED
     127.0.0.1:6379> exec
     1) OK
     2) (error) WRONGTYPE Operation against a key holding the wrong kind of value
     ```
   
     

