### Hadoop 

------

[TOC]

------

##### Hadoop 单机搭建

##### Hadoop 集群搭建

- 集群里的一台机器被指定为 NameNode，另一台不同的机器被指定为JobTracker。这些机器是masters。余下的机器即作为DataNode*也*作为TaskTracker。这些机器是slaves。

###### 配置文件

- 对Hadoop的配置通过conf/目录下的两个重要配置文件完成，HADOOP_HOME：指代安装的根路径
  1. hadoop-default.xml - 只读的默认配置。
  2. *hadoop-site.xml* - 集群特有的配置。
  3. conf/hadoop-env.sh中的变量可以为集群特有的值，也可以对bin/目录下的Hadoop脚本进行控制；

##### 集群配置

- 要配置Hadoop集群，你需要设置Hadoop守护进程的**运行环境**和Hadoop守护进程的**运行参数**；
- Hadoop守护进程指NameNode/DataNode 和JobTracker/TaskTracker。
- conf/hadoop-env.sh ： 配置守护线程的运行环境

###### Hadoop的机架感知

- NameNode和JobTracker通过调用管理员配置模块中的API来获取集群里**每个slave的机架id**。该API将slave的DNS名称（或者IP地址）转换成机架id。使用哪个模块是通过配置项topology.node.switch.mapping.impl来指定的。

###### 日志

- Hadoop使用Apache log4j来记录日志，它由Apache Commons Logging框架来实现。编辑conf/log4j.properties文件可以改变Hadoop守护进程的日志配置（日志格式等）;

###### 启动Hadoop

- 启动Hadoop集群需要启动HDFS集群和Map/Reduce集群；
- 格式化一个新的分布式文件系统：
  - bin/hadoop namenode -format
- 在分配的NameNode上，运行下面的命令启动HDFS：
  - bin/start-dfs.sh
  - bin/start-dfs.sh脚本会参照NameNode上${HADOOP_CONF_DIR}/slaves文件的内容，在所有列出的slave上启动DataNode守护进程。
- 在分配的JobTracker上，运行下面的命令启动Map/Reduce：
  - bin/start-mapred.sh
  - bin/start-mapred.sh脚本会参照JobTracker上${HADOOP_CONF_DIR}/slaves文件的内容，在所有列出的slave上启动TaskTracker守护进程。

##### 停止Hadoop

- 在分配的NameNode上，执行下面的命令停止HDFS：
  - bin/stop-dfs.sh
  - bin/stop-dfs.sh脚本会参照NameNode上${HADOOP_CONF_DIR}/slaves文件的内容，在所有列出的slave上停止DataNode守护进程。
- 在分配的JobTracker上，运行下面的命令停止Map/Reduce：
  - bin/stop-mapred.sh
  - bin/stop-mapred.sh脚本会参照JobTracker上${HADOOP_CONF_DIR}/slaves文件的内容，在所有列出的slave上停止TaskTracker守护进程。

