### 数据管道 & ETL

------

[TOC]

------

##### 01：概述

- Apache Flink 的一种常见应用场景是 **ETL（抽取、转换、加载）**管道任务。从一个或多个数据源获取数据，进行一些转换操作和信息补充，将结果存储起来。

##### 02：无状态的转换

###### map() 算子

1. `MapFunction` 只适用于**一对一**的转换：对每个进入算子的流元素，`map()` 将仅输出一个转换后的元素；

2. 需要实现MapFunction接口或者实现RichMapFunction类；

3. 重写map()方法，输入为接口key，输出为value；

   ```java
   MapFunction<inputType, outputType>
   ```

###### flatMap()  算子，多个元素输出

1. 使用接口中提供的 `Collector` ，`flatmap()` 可以输出你想要的**任意数量的元素，也可以一个都不发**；
2. 实现接口FlatMapFunction<inputType, outputType> 或者继承 RichFlatMapFunction ；
3. 重写flatMap（）；

```java
public static class DemoMapper implements FlatMapFunction<Input, Output> {
  @Override
  public void flatMap(Input input, Collector<Output> out) throws Exception {
    // 业务处理逻辑
    out.collect(Output);
  }
}
```

##### 03：KeyedStreams

###### keyBy（）

- 将一个流根据其中的一些属性来进行分区，每个 `keyBy` 会通过 shuffle 来为数据流进行重新分区。总体来说这个开销是很大的，它涉及网络通信、序列化和反序列化。

- ```java
  rides
     	.flatMap(new StudentMapper())
      .keyBy(student -> student.name);
  ```

###### 通过计算得到键

- KeySelector 不仅限于从事件中抽取键。你也可以按想要的方式计算得到键值，只要最终结果是确定的，并且实现了 `hashCode()` 和 `equals()`；

###### 隐式的状态

- 只要应用中有状态，你就应该考虑**状态（某个属性）**的大小。如果键值的数量是无限的，那 Flink 的状态需要的空间也同样是无限的。

###### reduce（）

- 一个更通用的算子可以用来实现你的自定义聚合；

##### 04：有状态的转换

###### Rich Functions

- `FilterFunction`， `MapFunction`，和 `FlatMapFunction`。这些都是单一抽象方法模式。
- 例如：`RichFlatMapFunction`，其中增加了以下方法
  - `open(Configuration c)`
    - 仅在算子初始化时调用一次。可以用来初始化一些静态数据等。
  - `close()`
  - `getRuntimeContext()`
    - 提供了一个访问途径，最明显的，它是你**创建和访问 Flink 状态的途径**；

###### Keyed State

- Flink 支持几种不同方式的 keyed state，最简单的一个，叫做 `ValueState`。意思是对于 *每个键* ，Flink 将存储一个单一的对象 ，例如：Boolean；
- 自定义的Mapper通过 `open()` 方法定义 `ValueState<Boolean>` 建立了**管理状态的使用**；

###### 清理状态

- 键空间是无界的时候将发生内存溢出，因此在键无限增长的应用中，清除再也不会使用的状态是很必要的，可以通过在状态对象上调用clear（）来实现；
- 可以用定时器自动实现；

###### Non-keyed State

- 在没有键的上下文中我们也可以使用 Flink 管理的状态。这也被称作算子的状态。
- 它包含的接口是很不一样的，这个特性最常用于 source 和 sink 的实现。

##### 05：Connected Streams

- 想要更调整转换的某些功能，比如数据流的阈值、规则或者其他参数。Flink 支持这种需求的模式称为 *connected streams* ，一个单独的算子有两个输入流，实现流的的关联；
- 这里注意**两个流的键必须一致**才能连接。 `keyBy` 的作用是将流数据分区，当 keyed stream 被连接时，他们必须按相同的方式分区。这样保证了两个流中所有键相同的事件发到同一个实例上。
- **RichCoFlatMapFunction**：是一种可以被用于一对连接流的 `FlatMapFunction`，并且它可以调用 rich function 的接口。这意味着它可以是有状态的。
- 在 Flink 运行时中，`flatMap1` 和 `flatMap2` 在连接流有新元素到来时被调用 ，在示例中，`control` 流中的元素会进入 `flatMap1`，`streamOfWords` 中的元素会进入 `flatMap2`。这是由两个流连接的顺序决定的，本例中为 `control.connect(datastreamOfWords)`。
- 认识到你无法控制 `flatMap1` 和 `flatMap2` 的调用顺序是很重要的。这两个输入流是相互竞争的关系，Flink 运行时将根据从一个流或另一个流中消费的事件做它要做的。