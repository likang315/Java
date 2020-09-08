### Caffeine

------

- Caffeine是基于JAVA 1.8 Version的高性能缓存库;
- **是JVM缓存（堆缓存）**；
  - 类似于全局变量集合一样，只不过具有一些集合不具有的缓存特性，例：定时删除；
- 读写性能完全秒杀其他缓存；

##### 01：导入 Jar 包

```xml
<dependency>
  <groupId>com.github.ben-manes.caffeine</groupId>
  <artifactId>caffeine</artifactId>
  <version>2.8.5</version>
</dependency>
```

##### 02：示例

```java
public static void main(String[] args) {
    try {
        // 定义缓存
        Cache<String, Object> cache = Caffeine.newBuilder()
          // 最后一次写入缓存后开始计时，指定的时间后过期
          .expireAfterWrite(1000 * 10, TimeUnit.MILLISECONDS)
          // 缓存的最大条数
          .maximumSize(100).build();

        cache.put("hello", "1234");
        System.out.println(cache.getIfPresent("hello"));
        // 过期自动删除
        Thread.sleep(1000 * 11);
        System.out.println(cache.getIfPresent("hello"));
    } catch (InterruptedException e) {
      	log.error("InterruptedException {}", e);
    }
}
```

##### 03：填充策略

1. 手动加载；
2. 同步加载；
3. 异步加载；

###### 同步加载

```java
LoadingCache<String, Object> loadingCache = Caffeine.newBuilder()
  			.initialCapacity(1)
        .maximumSize(100)
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .build(new CacheLoader<String, String>() {
						// 默认的数据加载实现，当调用get取值的时候，如果key没有对应的值，就调用这个方法进行加载
        		@Override
        		public String load(String key)  {
        			return "";
         		}
         });
    
String key = "name";
Object graph = loadingCache.get(key);
```

###### 异步加载

```java
AsyncLoadingCache<String, Object> asyncLoadingCache = Caffeine.newBuilder()
  .maximumSize(10_000)
  .expireAfterWrite(10, TimeUnit.MINUTES)
  // 自定义一个方法
  .buildAsync(key -> createExpensiveGraph(key));
// Or: Build with a asynchronous computation that returns a future
// .buildAsync((key, executor) -> createExpensiveGraphAsync(key, executor));

String key = "name";
// 查询并在缺失的情况下使用异步的方式来构建缓存
CompletableFuture<Object> graph = asyncLoadingCache.get(key);
```

##### 04：驱逐策略

1. 基于大小（size-based）；
2. 基于时间（time-based）；
3. 基于引用（reference-based）；

###### 基于大小（size-based）

```java
// 根据缓存的计数进行驱逐
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
  	// 指定缓存大小
    .maximumSize(10000)
    .build(key -> createExpensiveGraph(key));

// 根据缓存的权重来进行驱逐
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
  	// 指定缓存最大权重值
    .maximumWeight(10000)
    .weigher((Key key, Graph graph) -> graph.vertices().size())
    .build(key -> createExpensiveGraph(key));
```

###### 基于时间（Time-based）

```java
// 基于固定的到期策略进行退出
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .expireAfterAccess(5, TimeUnit.MINUTES)
    .build(key -> createExpensiveGraph(key));
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build(key -> createExpensiveGraph(key));
```

###### 三种定时的驱逐策略

- expireAfterAccess(long, TimeUnit):在最后一次访问或者写入后开始计时，在指定的时间后过期。假如一直有请求访问该key，那么这个缓存将一直不会过期；
- expireAfterWrite(long, TimeUnit): 在最后一次写入缓存后开始计时，在指定的时间后过期；
- expireAfter(Expiry): 自定义策略，过期时间由Expiry实现独自计算；

###### 基于引用（reference-based）

```java
// 当key和value都没有引用时驱逐缓存，弱引用
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .weakKeys()
    .weakValues()
    .build(key -> createExpensiveGraph(key))
    
// 当垃圾收集器需要释放内存时驱逐，弱引用
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .softValues()
    .build(key -> createExpensiveGraph(key));
```

- **注意：**AsyncLoadingCache不支持弱引用和软引用；

##### 05：移除监听器

```java
Cache<Key, Graph> graphs = Caffeine.newBuilder()
  .removalListener((Key key, Graph graph, RemovalCause cause) ->
                   System.out.printf("Key %s was removed (%s)%n", key, cause))
  .build();
```

- 为缓存指定一个删除侦听器，以便在删除数据时执行某些操作；
- RemovalListener 可以获取到key、value和RemovalCause（删除的原因）；
- **注意**：由RemovalListener抛出的任何异常都会被记录（使用Logger）并不会抛出；

##### 06：刷新（Refresh）

```java
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
    .maximumSize(10_000)
    // 指定在创建缓存或者最近一次更新缓存后经过固定的时间间隔，刷新缓存
    .refreshAfterWrite(1, TimeUnit.MINUTES)
    .build(key -> createExpensiveGraph(key));
```

- 刷新：是通过LoadingCache.refresh(key)方法来指定，并通过调用CacheLoader.reload方法来执行，刷新key会异步地为这个key加载新的value，并返回旧的值（如果有的话）。
- 驱逐：会阻塞查询操作直到驱逐作完成才会进行其他操作。

##### 07：Writer

```java
LoadingCache<Key, Graph> graphs = Caffeine.newBuilder()
  .writer(new CacheWriter<Key, Graph>() {
    @Override public void write(Key key, Graph graph) {
      // write to storage or secondary cache
    }
    @Override public void delete(Key key, Graph graph, RemovalCause cause) {
      // delete from storage or secondary cache
    }
  })
  .build(key -> createExpensiveGraph(key));
```

- 当CacheWriter与CacheLoader结合使用时，所有对缓存的读写操作都可以通过Writer进行传递。Writer可以把操作缓存和操作外部资源扩展成一个同步的原子性操作。并且在缓存写入完成之前，它将会阻塞后续的更新缓存操作，但是读取（get）将直接返回原有的值。如果写入程序失败，那么原有的key和value的映射将保持不变，如果出现异常将直接抛给调用者。

- ###### 分层（Layering）

  - CacheWriter可能用来集成多个缓存进而实现多级缓存。
  - 多级缓存的加载和写入可以使用系统外部高速缓存。这允许缓存使用一个小并且快速的缓存去调用一个大的并且速度相对慢一点的缓存。典型的off-heap、file-based和remote 缓存。

##### 08：统计（Statistics）

```java
Cache<Key, Graph> graphs = Caffeine.newBuilder()
    .maximumSize(10_000)
    .recordStats()
    .build();
```

- 使用Caffeine.recordStats()，您可以打开统计信息收集。Cache.stats() 方法返回提供统计信息的CacheStats，如：
  - hitRate()：返回命中与请求的比率；
  - hitCount(): 返回命中缓存的总数；
  - evictionCount()：缓存逐出的数量；
  - averageLoadPenalty()：加载新值所花费的平均时间；

##### 09：Clean up

- 缓存的删除策略使用的是惰性删除和定时删除，但是我也可以自己调用cache.cleanUp()方法手动触发一次回收操作。cache.cleanUp()是一个同步方法；

##### 10：示例代码







