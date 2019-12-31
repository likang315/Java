### 使用Redis

------

##### 1：Java

- 导入Jar 包： Jedis.jar  确保下载最新驱动包

  ```xml
  <!-- https://mvnrepository.com/artifact/redis.clients/jedis -->
  <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>3.0.1</version>
  </dependency>
  ```

- 实例化此类，使用

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

##### 2：API

###### String

- boolean  **exists**(String key) ： 判断某个键是否存在
- Long **expire**(String key, int seconds) 
  - 设置过期时间吗，单位为秒；
  -  返回1表示成功设置过期时间，返回0表示key不存在，重复会覆盖；
- set(String key，String value)  
  - 添加或覆盖键值对（key,value） ，返回String类型的OK代表成功
-  String **setex**(String key, int seconds, String value);
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
  - 如果该key对应的值是一个Hash表，则返回对应字段的值。 如果不存在该字段，或者key不存在，则返回一个"nil"值；
- Map<String, String> **hgetAll**(String key);
  - 获取指定key的Hash的所有（K-V） 元素
- Set<String> hkeys(String key);
  - 获取指定key的Hash所有元素的key
- List<String> hvals(String key);
  - 获取指定Key的Hash所有元素 的value
- List<String> hvals(String key);
  - 从指定Key的Hash中删除一个或多个元素
- Long hlen(String key);
  - 获取指定Key的Hash中元素的个数

###### list

- lpush(String key, String v1, String v2,....)：添加一个List , 如果已经有该List对应的key, 则按顺序在左边追加
- rpush(String key , String vn) ：key对应list右边插入元素
- lpop(String key) ：key对应list ,左弹出栈一个元素
- rpop(String key) ：key对应list ,右弹出栈一个元素
- lset(String key,int index,String val) ： 修改key对应的list指定下标index的元素
- llen(String key)：获取key对应list的长度
- sort(String key) ：把key对应list里边的元素从小到大排序   