### Hive

------

[TOC]

------

​	Hive是基于Hadoop的一个数据仓库工具，将结构化的数据文件映射为一张数据库表，并提供类SQL查询功能。

##### 01：Hadoop 和 MapReduce 概述

- Map Reduce 是一种计算模型，该模型可将大型数据处理**任务分解**成很多单个的、 可以 在服务器集群中并行执行的任务。这些任务的计算结果可以**合并在一起来计算最终的结果**。

- MapReduce这个术语来自于两个基本的数据转换操作： map过程和reduce过程。 

  - **map操作：**将集合中的元素从一种形式转换成另一种形式。
  - **reduce过程：**将值的集合转换成一个值（例如对一组数值求和或求平均值），或者转换成另 一个集合。这个 Reducer最终会产生一个键－值对。
  - 在MapReduce计算框架中， 某个键的所有键值对都会被分发到同一个 reduce操作中。 

- Hadoop 基础配置

  - Hadoop决定如果将提交的**job分解成多个独立的map和 reduce任务(task) 来执行**，它就会对这些task进行调度并为其分配合适的资源，决定将某个task 分配到集群中哪个位置（如果可能，通常是这个**task 所要处理的数据所在的位置**，这样可以**最小化网络开销**）。它会监控每一个task 以确保其成功完成，并重启一些失败的task。

- Hadoop分布式文件系统（HDFS)

  - 管理着集群中的数据。每个数据块(block) 都会被冗余多份（通常默认会冗余3份），这样可以保证不会因单个硬盘或服务器的损坏导致数据丢失。通常是**64MB**或是这个值的若干倍。

- ###### 示例

  - Word Count算法【重要】

  ```java
  // 学习使用Java 编写 MapReduce 程序
  public class WordCount {
    public static class Map extends Mapper<LongWritable, Text, Text, IntWritable> {
      private final static IntWritable one = new IntWritable(1);
      private Text word = new Text();
      public void map(LongWritable key, Text value, Context context) throws
        IOException, InterruptedException {
          String line = value.toString();
          StringTokenizer tokenizer = new StringTokenizer(line);
          while (tokenizer.hasMoreTokens()) {
            word.set(tokenizer.nextToken());
            context.write(word, one);
          }
      }
   	}   
   public static class Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
     public void reduce(Text key, Iterable<IntWritable> values, Context context)
        throws IOException, InterruptedException {
          int sum = 0;
         for (IntWritable val : values) {
           sum += val.get();
         }
       	 context.write(key, new IntWritable(sum));
     }
   }   
   public static void main(String[] args) throws Exception {
      Configuration conf = new Configuration();
      Job job = new Job(conf, "wordcount");
      job.setOutputKeyClass(Text.class);
      job.setOutputValueClass(IntWritable.class);
      job.setMapperClass(Map.class);
      job.setReducerClass(Reduce.class);
      job.setInputFormatClass(TextInputFormat.class);
      job.setOutputFormatClass(TextOutputFormat.class);
       
      FileInputFormat.addInputPath(job, new Path(args[0]));
      FileOutputFormat.setOutputPath(job, new Path(args[1])); 
      job.waitForCompletion(true);
   }  
  }
  // 使用HQL进行相同的运算
  CREATE TABLE docs (line STRING); 
  LOAD DATA INPATH 'docs' OVERWRITE INTO TABLE docs;
  CREATE TABLE word_counts AS 
  SELECT word, count(l) AS count FROM 
  (SELECT explode(split(line, '\s')) AS word FROM docs) w GROUP BY word 
  ORDER BY word; 
  ```

##### 02：Hadoop 生态中的Hive

- ![Hive组成模块](/Users/likang/Code/Git/Middleware/Hive/photos/Hive组成模块.png)
- Hive 发行版中附带的模块有CLI，一个称为Hive网页界面(HWI)的简单网页界面，以及可通过JDBC 、ODBC和一个Thrift服务器进行编程访问的几个模块。
- 所有的命令和查询都会进入到Driver （驱动模块）， 通过该模块对输入进行解析编译，对需求的计算进行优化， 然后按照指定的步骤执行（通常是启动多个MapReduce 任务 (job)来执行）。 当需要启动MapReduce 任务 (job)时，Hive本身是不会生成Java MapReduce 算法程序的。想相反，Hive通过一 的XML文件驱动 执行内置的、 原生的Mapper和Reducer 模块。
- Hive通过和JobTracker通信来初始化MapReduce任务(job)，而不必部署在 JobTracker 所在的管理节点上执行





