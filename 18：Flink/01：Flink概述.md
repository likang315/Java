### Flink 概述

------

[TOC]

------

##### 01：专业术语

- Flink Cluster

  - 一般情况下，Flink 集群是由一个 Flink JobManager  和一个或多个 Flink TaskManager 进程组成的分布式系统。

- **Event**

  - Event 是对应用程序建模的域的状态更改的声明。它可以同时为流或批处理应用程序的 input 和 output，也可以单独是 input 或者 output 中的一种。Event 是特殊类型的 Record;

- **Operator**

  - **算子执行某种操作**，该操作通常由 Function 执行。Source 和 Sink 是数据输入和数据输出的特殊算子。

- **Operator Chain**

  - 算子链由两个或多个连续的 Operator 组成，**两者之间没有任何的重新分区**。同一算子链内的算子可以彼此直接传递 record，而无需通过序列化或 Flink 的网络栈。

- **Partition**

  - 分区是整**个数据流或数据集的独立子集**。通过**将每个 Record 分配给一个或多个分区，来把数据流或数据集划分为多个分区**。在运行期间，Task 会消费数据流或数据集的分区。改变数据流或数据集分区方式的转换通常称为**重分区**。

- **Record**

  - Record 是数据集或**数据流的组成元素**。Operator 和 Function 接收 record 作为输入，并将 record 作为输出发出。

- **Sub-Task**

  - Sub-Task 是负责处理数据流 Partition 的 Task。”Sub-Task”强调的是**同一个 Operator 或者 Operator Chain具有多个并行的 Task** 。

- **Task**

  - 它是**基本的工作单元**，由 Flink 的 runtime 来执行。Task 正好**封装了一个 Operator 或者 Operator Chain 的 *parallel instance***。

- Flink TaskManager

  TaskManager 是 [Flink Cluster](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/concepts/glossary.html#flink-cluster) 的工作进程。[Task](https://ci.apache.org/projects/flink/flink-docs-release-1.12/zh/concepts/glossary.html#task) 被调度到 TaskManager 上执行。TaskManager 相互通信，只为在后续的 Task 之间交换数据。

##### 02：Flink 集群剖析（Flink Cluster）

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

###### TaskManagers

- 也称为 worker：**执行作业流的 task，并且缓存和交换数据流**。
- 必须始终至少有一个 TaskManager。在 **TaskManager 中资源调度的最小单位是 task slot**。TaskManager 中 **task slot 的数量表示并发处理 task 的数量**。请注意一个 task slot 中可以执行多个算子；

##### 02：Tasks 和算子链

- 对于分布式执行，Flink 将算子的 subtasks *链接*成 *tasks*。**每个 task 由一个线程执行**。将算子链接成 task 是个有用的优化;

##### 03：Task Slots 和资源

- 每个 worker（TaskManager）都是一个 JVM 进程，可以在**单独的线程中执行一个或多个 subtask**。为了控制一个 TaskManager 中接受多少个 task，就有了所谓的 **task slots**（至少一个）。
- **每个 *task slot* 代表 TaskManager 中资源的固定子集**。
  - 例如，具有 3 个 slot 的 TaskManager，会将其托管内存 1/3 用于每个 slot。分配资源意味着 subtask 不会与其他作业的 subtask 竞争托管内存，而是具有一定数量的保留托管内存。
  - 通过调整 task slot 的数量，用户可以定义 subtask 如何互相隔离。**每个 TaskManager 有一个 slot，这意味着每个 task 组都在单独的 JVM 中运行**。具有多个 slot 意味着更多 subtask 共享同一 JVM。同一 JVM 中的 task 共享 TCP 连接（通过多路复用）和心跳信息。它们还可以共享数据集和数据结构，从而减少了每个 task 的开销。
- 默认情况下，**Flink 允许 subtask 共享 slot，即便它们是不同的 task 的 subtask，只要是来自于同一作业即可**。结果就是一个 slot 可以持有整个作业管道。
- ![TaskSlot](/Users/likang/Code/Git/Middleware/Flink/photos/TaskSlot.jpg)

##### 04：Flink 应用程序执行

- Flink 应用程序是从其 `main()` 方法产生的一个或多个 Flink 作业的任何用户程序，这些作业的执行可以在本地 JVM（LocalEnvironment）中进行，或具有多台机器的集群的远程设置（``RemoteEnvironment）中进行；
- Flink 应用程序的作业可以被提交到长期运行的 **Flink Session 集群，专用的 Flink Job 集群 或 Flink Application 集群**。这些选项之间的差异主要与**集群的生命周期和资源隔离保证**有关。

###### Flink Session 集群

- **集群生命周期**：
  - 在 Flink Session 集群中，**客户端连接到一个预先存在的、长期运行的集群**，该集群可以接受多个作业提交。即使所有作业完成后，集群（和 JobManager）仍将继续运行直到手动停止 session 为止。因此，**Flink Session 集群的寿命不受任何 Flink 作业寿命的约束**。
- **资源隔离**：
  - TaskManager slot 由 ResourceManager 在**提交作业时分配，并在作业完成时释放**。由于所有作业都共享同一集群，因此在集群资源方面存在一些竞争 — 例如提交工作阶段的网络带宽。
- **其他注意事项**：
  - 拥有一个预先存在的集群可以**节省大量时间申请资源和启动 TaskManager**。有种场景很重要，作业执行时间短并且启动时间长会对端到端的用户体验产生负面的影响；

###### Flink Job 集群

- **集群生命周期**：
  - 在 Flink Job 集群中，可用的集群管理器（例如 YARN 或 Kubernetes）用于**为每个提交的作业启动一个集群，并且该集群仅可用于该作业**。客户端首先从集群管理器**请求资源启动 JobManager**，然后将作业提交给在这个进程中运行的 **Dispatcher**。然后根据作业的资源请求惰性的分配 **TaskManager**。一旦作业完成，Flink Job 集群将被拆除。
- **资源隔离**：
  - JobManager 中的**致命错误仅影响在 Flink Job 集群中运行的一个作业**。
- **其他注意事项**：
  - 由于 ResourceManager 必须应用并等待外部资源管理组件来启动 TaskManager 进程和分配资源，因此 Flink Job 集群更**适合长期运行、具有高稳定性要求且对较长的启动时间不敏感的大型作业**。

###### Flink Application 集群

- **集群生命周期**：
  - Flink Application 集群是专用的 Flink 集群，仅从 Flink 应用程序执行作业，并且 `main()`方法在集群上而不是客户端上运行。提交作业是一个单步骤过程：无需先启动 Flink 集群，然后将作业提交到现有的 session 集群；相反，**将应用程序逻辑和依赖打包成一个可执行的作业 JAR 中，并且集群入口（`ApplicationClusterEntryPoint`）负责调用 `main()`方法来提取 JobGraph**。
- **资源隔离**：
  - 在 Flink Application 集群中，ResourceManager 和 Dispatcher 作用于**单个的 Flink 应用程序**，相比于 Flink Session 集群，它提供了**更好的隔离**。

##### 05：流处理

- 数据的产生原本就是流式的，因此分析数据时，可以围绕 有界流（bounded）或 无界流（unbounded）两种模型来组织处理数据。
- **批处理：**是有界数据流处理的范例。在这种模式下，你可以选择在计算结果输出之前输入整个数据集，这也就意味着你可以对**整个数据集的数据**进行排序、统计或汇总计算后再输出结果。
- **流处理：**正相反，其涉及无界数据流。至少理论上来说，它的**数据输入永远不会结束**，因此程序必须持续不断地对到达的数据进行处理。
- 在 Flink 中，应用程序由用户自定义**算子**转换而来的**流式 dataflows** 所组成。这些流式 dataflows 形成了有向图，以一个或多个**源**（source）开始，并以一个或多个**汇**（sink）结束。
- 通常，程序代码中的 **transformation 和 dataflow 中的算子（operator）之间是一一对应的**。但有时也会出现一个 transformation 包含多个算子的情况，Flink 应用程序可以消费来自**消息队列或分布式日志**这类流式数据源的实时数据，也可以从各种的数据源中消费有界的历史数据。同样，Flink 应用程序生成的结果流也可以发送到各种数据汇中。
- ![Flink_DataStream](/Users/likang/Code/Git/Middleware/Flink/photos/Flink_DataStream.jpg)

##### 06：DataFlows

- Flink 程序本质上是分布式并行程序。
- 在程序执行期间，一个流有**一个或多个流分区**（Stream Partition），每个算子**有一个或多个算子子任务**（Operator Subtask）。每个子任务彼此独立，并在不同的线程中运行，或在不同的计算机或容器中运行,算子子任务数就是其对应**算子的并行度**。
- ![Flink_Parallelism](/Users/likang/Code/Git/Middleware/Flink/photos/Flink_Parallelism.jpg)
- Flink 算子之间可以通过**一对一（直传）模式或重新分发模式传输数据**
  - **一对一模式：**（例如上图中的 *Source* 和 *map()* 算子之间）可以保留元素的分区和顺序信息。这意味着 *map()* 算子的 subtask[1] 输入的数据以及其顺序与 *Source* 算子的 subtask[1] 输出的数据和顺序完全相同，即**同一分区的数据只会进入到下游算子的同一分区**。
  - **重新分发模式：**（例如上图中的 *map()* 和 *keyBy/window* 之间，以及 *keyBy/window* 和 *Sink* 之间）则会更改数据所在的流分区。当你在程序中选择使用不同的 *transformation*，每个*算子子任务*也会根据不同的 transformation 将数据发送到不同的目标子任务。例如：*keyBy()*（通过散列键重新分区）、*broadcast()*（广播）或 *rebalance()*（随机重新分发）。

##### 07：有状态流处理

- Flink 中的算子可以是**有状态的**，并且Flink 应用程序可以在分布式群集上并行运行，其中每个算子的各个并行实例会在单独的线程中独立运行，并且通常情况下是会在不同的机器上运行。
- 有状态算子的并行实例组在存储其对应状态时通常是**按照键（key）进行分片存储的**。每个并行实例算子负责处理一组特定键的事件数据，并且这组键对应的状态会保存在本地。
- Flink 应用程序的状态访问都在本地进行，因为这有助于其提高吞吐量和降低延迟。通常情况下 Flink 应用程序都是将**状态存储在 JVM 堆上**，但如果状态太大，我们也可以选择将其**以结构化数据格式存储在高速磁盘中**。

##### 08：通过状态快照实现的容错

- 通过状态快照和流重放两种方式的组合，Flink 能够提供可容错的，精确一次计算的语义。
- 这些状态快照在执行时**会获取并存储分布式 pipeline 中整体的状态**，它会将数据源中消费数据的**偏移量记录**下来，并将整个 job graph 中算子获取到该数据（**记录的偏移量对应的数据**）时的状态记录并存储下来。当发生故障时，**Flink 作业会恢复上次存储的状态**，重置数据源从状态中记录的上次消费的偏移量开始重新进行消费处理。而且**状态快照在执行时会异步获取状态并存储，并不会阻塞正在进行的数据处理逻辑**。