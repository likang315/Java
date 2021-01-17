### DataStream_API

------

[TOC]

------

##### 01：Flink 的序列化器

- Flink 的 Java 和 Scala DataStream API 可以将**任何可序列化的对象转化为流**。Flink 自带的序列化器有
  - 基本类型，即 String、Long、Integer、Boolean、Array
  - 复合类型：Tuples、POJOs 和 Scala case classes
- 而且 Flink 会交给 Kryo 序列化其他类型。也可以将其他序列化器和 Flink 一起使用。

##### 02：Java tuples 和 POJOs

###### Tuples

- 对于 Java，Flink 自带有 `Tuple0` 到 `Tuple25` 类型。

- ```java
  Tuple2<String, Integer> person = Tuple2.of("Fred", 35);
  // zero based index!  
  String name = person.f0;
  Integer age = person.f1;
  ```

###### POJOs

- 如果满足以下条件，Flink 将数据类型识别为 POJO 类型（并允许“按名称”字段引用）：
  - 该类是公有且独立的（没有非静态内部类）;
  - 该类有公有的无参构造函数;
  - 类（及父类）中所有的所有不被 static、transient 修饰的属性要么是公有的（且不被 final 修饰），要么是包含公有的 getter 和 setter 方法，这些方法遵循 Java bean 命名规范;

###### 示例

```java
public static void main(String[] args) throws Exception {
  final StreamExecutionEnvironment env =
    StreamExecutionEnvironment.getExecutionEnvironment();
  DataStream<Person> flintstones = env.fromElements(
                new Person("Fred", 35),
                new Person("Wilma", 35),
                new Person("Pebbles", 2));
	DataStream<Person> adults = flintstones.filter((FilterFunction<Person>) person ->
                                               person.age >= 18);
	adults.print();
  env.execute();
}

public class Person {
    public String name;  
    public Integer age;  
    public Person() {};  
    public Person(String name, Integer age) {  
      this.name = name;
      this.age = age;
    };  
}  
```

##### 03：Stream的执行环境

- 每个 Flink 应用都需要有执行环境，流式应用需要用到`StreamExecutionEnvironment`。
- DataStream API 将你的应用构建为一个 **job graph**，并附加到 `StreamExecutionEnvironment` 。当调用 `env.execute()` 时**此 graph 就被打包并submit 到 JobManager 上，后者对作业并行处理并将其子任务分发给 Task Manager 来执行**。每个作业的并行子任务将在 *task slot* 中执行。
- 注意，如果没有调用 execute()，应用就不会运行，相当于job 没有提交；
- ![Flink_Stream_env](/Users/likang/Code/Git/Middleware/Flink/photos/Flink_Stream_env.jpg)

##### 04：基本的 stream source

1. 构建元素
   - `StreamExecutionEnvironment` 上有一个 `fromCollection(Collection)` 方法；
   - `StreamExecutionEnvironment` 上还有一个 `fromElements(...)` 方法；
2. Socket
   - DataStream<String> lines = env.socketTextStream("localhost", 9999)；
   - DataStream<String> lines = env.readTextFile("file:///path");
3. 常用的数据源是那些**支持低延迟，高吞吐并行读取以及重复的数据源**
   - kafka；
   - kinesis；





