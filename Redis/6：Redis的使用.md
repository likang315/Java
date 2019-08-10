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

###### API

- boolean  exists(String key) ： 判断某个键是否存在
- set(String key,String value)  ：添加或覆盖键值对（key,value） ，返回String类型的OK代表成功
- Set<String> keys(*) ：获取所有key，返回set 无序集合
- del(String key) ：删除键为key的数据项   
- expire(String key,int i)  ：设置键为key的过期时间为i秒    
- int    ttl(String key)  ：获取健为 key 的数据项剩余时间（秒）    
- persist(String key)  ：移除键为key属性项的生存时间限制
- type(String key)  ：查看键为key所对应value的数据类型
- get(String key)  ：获取键为key对应的value

###### Hash

- hmset(String key,Map map)：添加一个Hash
- hset(String key , String key, String value)：向Hash中插入一个元素（K-V）
- hgetAll(String key)：获取指定key的Hash的所有（K-V） 元素
- hkeys（String key）：获取指定key的Hash所有元素的key
- jedis.hvals(String key)：获取指定Key的Hash所有元素 的value
- hdel(String key , String k1, String k2,…)：从指定Key的Hash中删除一个或多个元素
- hlen(String key)：获取指定Key的Hash中元素的个数
- hmget(String key,String K1,String K2)：获取指定Key的Hash中一个或多个元素value

###### list

- lpush(String key, String v1, String v2,....)：添加一个List , 如果已经有该List对应的key, 则按顺序在左边追加
- rpush(String key , String vn) ：key对应list右边插入元素
- lpop(String key) ：key对应list ,左弹出栈一个元素
- rpop(String key) ：key对应list ,右弹出栈一个元素
- lset(String key,int index,String val) ： 修改key对应的list指定下标index的元素
- llen(String key)：获取key对应list的长度
- sort(String key) ：把key对应list里边的元素从小到大排序   