### Redis 数据备份与恢复

###### 备份数据

 **SAVE**：（**BGSAVE**） 命令用于创建当前数据库的备份，该命令将在 redis 安装目录中创建dump.rdb文件

###### 恢复数据

**CONFIG GET dir**   ：获取 redis 目录可以使用 

只需将备份文件 (dump.rdb) 移动到 redis 安装目录并启动服务即可

### Redis 分区

分割数据到多个Redis实例的处理过程，因此每个实例只保存key的一个子集

###### 范围分区：

最简单的分区方式是按范围分区，就是映射一定范围的对象到特定的Redis实例，缺点你是需要维护一张映射表，这个表需要被管理

###### Hash分区：

用一个hash函数将key转换为一个数字，比如使用crc32 hash函数。对key foobar执行crc32(foobar)会输出类似93024922的整数，对这个整数取模，将其转化为0-3之间的数字，就可以将这个整数映射到4个Redis实例中的一个了			93024922 % 4 = 2，就是说key foobar应该被存到R2实例中

### 注意：取模操作是取除的余数，通常在多种编程语言中用%操作符实现



## Java使用Redis

1:下载驱动包 **下载 jedis.jar**,确保下载最新驱动包

2：在你的 classpath 中包含该驱动

```java
import redis.clients.jedis.Jedis;
public class RedisStringJava {
    public static void main(String[] args) {
        //连接本地的 Redis 服务
        Jedis jedis = new Jedis("localhost");
        System.out.println("连接成功");
        //设置 redis 字符串数据
        jedis.set("runoobkey", "www.runoob.com");
        // 获取存储的数据并输出
        System.out.println("redis 存储的字符串为: "+ jedis.get("runoobkey"));
    }
}
 // 获取数据并输出
 Set<String> keys = jedis.keys("*"); 
 //列表设置key-value 
 jedis.lpush("site-list", "Runoob");
```



