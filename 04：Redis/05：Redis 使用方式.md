### Redis 使用方式

------

[TOC]

##### 01：Java

- 导入Jar 包： Jedis.jar  确保下载最新驱动包

  ```xml
  <!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
  <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>3.0.1</version>
  </dependency>
  ```

- 示例

```java
import redis.clients.jedis.Jedis;
import redis.clients.jedis,JedisPool;
public class RedisTest {
  	@Resource
  	private JedisPool jedisPool;
    public static void main(String[] args) {
        // 连接本地的 Redis 服务
        Jedis jedis = Jedis.getResource();
        jedis.set("runoobkey", "www.runoob.com");
      	if (jedis.exists("runnoobkey")) {
          System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
        }
        System.out.println("数据不存在");
    }
}
```

##### 02：Jedis API

###### String

- boolean  **exists**(String key) ： 判断某个键是否存在
- Long **expire**(String key, int seconds) 
  - 设置过期时间吗，单位为秒；
  -  返回1表示成功设置过期时间，返回0表示key不存在，重复会覆盖；
- set(String key，String value)  
  - 添加或覆盖键值对（key,value） ，返回String类型的OK代表成功
-  **Long setnx(String key，String value)**
  - key存在的情况下，不操作redis内存；也就是返回值是0；  
-  **String setex(String key, int seconds, String value);**
  - 设置值和过期时间，秒为单位；
- Set<String> keys(*) ：获取所有key，返回set 无序集合
- **get**(String key)  ：获取键为key对应的value
- **del**(String key) ：删除键为key的数据项   
- **type**(String key)  ：查看键为key所对应value的数据类型
- expire(String key，int i)  ：设置键为key的过期时间为i秒    
- int  ttl(String key)  ：获取健为 key 的数据项剩余时间（秒）    
- persist(String key)  ：移除键为key属性项的生存时间限制

###### Hash

- String **hmset**(String key, Map<String, String> hash);
  - 设置多个字段和值，如果字段存在，则覆盖；
- List<String> **hmget**(String key, String... fields);
  - 获取多个字段的值，若字段不存在，则其值为nil；
- Long hset(String key, String field, String value);
  - 向Hash中插入一个元素（K-V），如果key不存在，则创建一个新的hash表；
- String hget(String key, String field);
  - hkey对应的值是一个Hash表，则返回对应字段的值。 如果不存在该字段，或者key不存在，则返回一个"nil"值；
- Map<String, String> **hgetAll**(String key);
  - 获取指定key的Hash的所有（K-V） 元素
- Set<String> hkeys(String key);
  - 获取指定key的Hash所有元素的key
- List<String> hvals(String key);
  - 获取指定Key的Hash所有元素 的value
- Long **hdel**(String key, String... field);
  - 删除hash中指定字段，成功返回1，失败返回0；
- Long hlen(String key);
  - 获取指定Key的Hash中元素的个数

###### List

- Long lpush(String key, String v1, String v2,....)
  - 所有指定的值依次插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key对应的值不是一个 list 的话，那么会返回一个错误；
- **Long rpush(String key, String... string)**
  - 向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。 当 key保存的不是一个列表，那么会返回一个错误。
- Long llen(String key);
  - 返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。 当存储在 key里的值不是一个list的话，会返回error；
- **List<String> lrange(String key, long start, long end);**
  - 返回存储在 key 的列表里指定范围内的元素。 start 和 end是偏移量；
- String lindex(String key, long index);
  - 返回列表中索引对应的值，超出索引的范围返回nil；
- lset(String key,int index,String val) 
  -  修改key对应的list指定下标index的元素的值；

###### Set

- Long sadd(String key, String... member);
  - 添加一个或多个指定的member元素到集合的 key中，指定的元素member，如果已经在集合key中存在则忽略，如果集合key不存在，则新建集合key，并添加member元素到集合key中；
- Set<String> smembers(String key);
  - 返回key集合所有的元素
- Long srem(String key, String... member);
  - 移除key集合中指定的元素，如果指定的元素不是key集合中的元素则忽略，如果key集合不存在，则被视为一个空的集合，该命令返回0.
- Long scard(String key)
  - 返回集合中元素的数量；

###### ZSet

- Long zadd(String key, double score, String member);
  - 添加指定的成员到key对应的有序集合中，每个成员都有一个分数。你可以指定多个分数/成员组合。如果一个指定的成员已经在对应的有序集合中了，那么其分数就会被更新成最新的并且该成员会重新调整到正确的位置，以确保集合有序；
- **Long zadd(String key, Map<String, Double> scoreMembers);**
  - 一个key对应多个成员组合；
- Set<String> zrange(String key, long start, long end);
  - 返回有序集key中，指定区间内的成员。其中成员按score值递增(从小到大)来排序。具有相同score值的成员按字典序来排列；
- Long zrem(String key, String... member);
  - 从集合中删除指定member元素，当key存在，但是其不是有序集合类型，就返回一个错误；
- Long zrank(String key, String member);
  - **返回有序集key中成员member的排名**。其中有序集成员按score值递增(从小到大)顺序排列。排名以0为底，score值最小的成员排名为0；
- Long zcard(String key);
  - 返回key的有序集元素个数；
- Double zscore(String key, String member);
  - 返回有序集key中，成员member的score值；

##### 03：RedisUtils

```java

```



