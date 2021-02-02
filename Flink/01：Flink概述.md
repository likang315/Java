### Flink 概述

------

[TOC]

------

##### 01：Flink 集群剖析（Flink Cluster）

- Flink 运行时由两种类型的进程组成：**一个 JobManager 和一个或者多个 TaskManager**。

###### JobManager

- 职责：协调 Flink 应用程序的分布式执行，它决定何时调度下一个 task（或一组 task）、对完成的 task 或执行失败做出反应、协调 checkpoint、并且协调从失败中恢复等等。这个进程由三个不同的组件组成：
  - **ResourceManager**
    - 负责 Flink 集群中的资源提供、回收、分配 - 它管理 **task slots**，这是 Flink 集群中资源调度的单位；
  - **Dispatcher**
    - 提供了一个 REST 接口，用来**提交 Flink 应用程序执行，并为每个提交的作业启动一个新的 JobMaster**。它还运行 Flink WebUI 用来提供作业执行信息。
  - **JobMaster**
    - 负责管理单个 JobGraph 的执行。Flink 集群中可以同时运行多个作业，每个作业都有自己的 JobMaster。
    - 始终至少有一个 JobManager。高可用（HA）设置中可能有多个 JobManager，其中一个始终是 *leader*，其他的则是 *standby*