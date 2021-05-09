### jstat 命令详解

------

[TOC]

##### 01：Jstat

- 是JDK自带的工具，位于Java/bin 目录下，主要利用JVM内建的指令对**Java应用程序的资源和性能进行实时的命令行的监控**，包括Heap size和垃圾回收状况的监控；
- 使用时需加上进程的ID【所部署的应用所属的ID】；

1. 使用参数
   - jstat ``-``<option> [-t] [ -h<lines>] <vmid> [<interval> [<count>]]
2. 参数
   - **option：**参数选项
     - **-class 用于查看类加载情况的统计**
     - -compiler 用于查看HotSpot中即时编译器编译情况的统计
     - **-gc 用于查看JVM中堆的垃圾收集情况的统计**
     - **-gccapacity 用于查看新生代、老生代及永久代的存储容量情况**
     - -gcmetacapacity 显示metaspace的大小
     - -gcnew 用于查看新生代垃圾收集的情况
     - -gcnewcapacity 用于查看新生代存储容量的情况
     - -gcold 用于查看老生代及持久代垃圾收集的情况
     - -gcoldcapacity 用于查看老生代的容量
     - -gcutil 显示垃圾收集信息
     - -gccause 显示垃圾回收的相关信息（通-gcutil）,同时显示最后一次仅当前正在发生的垃圾收集的原因
     - -printcompilation 输出JIT编译的方法信息
   - **-t：**可以在打印的列**加上Timestamp列**，用于显示系统运行的时间
   - **-h：**可以在周期性数据输出的时候，指定输出多少行以后输出一次**表头**
   - **vmid：**Virtual Machine ID（ 进程的 pid）
   - **interval：**每次执行间隔时间，单位为毫秒
   - **count：**用于指定输出多少次记录，缺省则会一直打印
     - jstat -gccapacity -t -h5 3346 1000  -h5：每5行显示一次表头，1000：每1秒钟显示一次，单位为毫秒

##### 02：示例

###### 类加载统计

- jstat -compiler 3346
- 统计JVM中加载的类的数量与size；
  - Loaded：加载类的数量
  - Bytes：加载类的size，单位为Byte
  - Unloaded：卸载类的数目
  - Bytes：卸载类的size，单位为Byte
  - Time：加载与卸载类花费的时间

###### 编译统计

- jstat -compiler 3346
- 用于查看HotSpot中即时编译器编译情况的统计
  - Compiled：编译任务执行数量
  - Failed：编译任务执行失败数量
  - Invalid：编译任务执行失效数量
  - Time：编译任务消耗时间
  - FailedType：最后一个编译失败任务的类型
  - FailedMethod：最后一个编译失败任务所在的类及方法

###### 垃圾回收统计

- jstat -gc 3346
- 用于查看JVM中堆的垃圾收集情况的统计
  - S0C：年轻代中第一个survivor（幸存区）的容量 （字节）
  - S1C：年轻代中第二个survivor（幸存区）的容量 (字节)
  - S0U：年轻代中第一个survivor（幸存区）目前已使用空间 (字节)
  - S1U：年轻代中第二个survivor（幸存区）目前已使用空间 (字节)
  - EC：年轻代中Eden（伊甸园）的容量 (字节)
  - EU：年轻代中Eden（伊甸园）目前已使用空间 (字节)
  - OC：Old代的容量 (字节)
  - OU：Old代目前已使用空间 (字节)
  - MC：metaspace(元空间)的容量 (字节)
  - MU：metaspace(元空间)目前已使用空间 (字节)
  - CCSC：当前压缩类空间的容量 (字节)
  - CCSU：当前压缩类空间目前已使用空间 (字节)
  - **YGC：从应用程序启动到采样时年轻代中gc次数**
  - YGCT：从应用程序启动到采样时年轻代中gc所用时间(s)
  - **FGC：从应用程序启动到采样时old代(全gc)gc次数**
  - **FGCT：从应用程序启动到采样时old代(全gc)gc所用时间(s)**
  - GCT：从应用程序启动到采样时gc用的总时间(s)

###### 堆内存统计

- jstat -gccapacity 3346
- 用于查看新生代、老生代及持久代的存储容量情况
  - NGCMN：年轻代(young)中初始化(最小)的大小(字节)
  - NGCMX：年轻代(young)的最大容量 (字节)
  - NGC：年轻代(young)中当前的容量 (字节)
  - S0C：年轻代中第一个survivor（幸存区）的容量 (字节)
  - S1C：年轻代中第二个survivor（幸存区）的容量 (字节)
  - EC：年轻代中Eden（伊甸园）的容量 (字节)
  - OGCMN：old代中初始化(最小)的大小 (字节)
  - OGCMX：old代的最大容量(字节)
  - **OGC：old代当前新生成的容量 (字节)**
  - OC：Old代的容量 (字节)
  - MCMN：metaspace(元空间)中初始化(最小)的大小 (字节)
  - MCMX：metaspace(元空间)的最大容量 (字节)
  - **MC：metaspace(元空间)当前新生成的容量 (字节)**
  - CCSMN：最小压缩类空间大小
  - CCSMX：最大压缩类空间大小
  - CCSC：当前压缩类空间大小
  - **YGC：从应用程序启动到采样时年轻代中gc次数**
  - **FGC：从应用程序启动到采样时old代(全gc)gc次数**

###### 元数据空间统计

- jstat -gcmetacapacity 3346

###### 新生代垃圾回收统计

- jstat -gcnew 3346

###### 老年代垃圾回收统计

- -gcold

###### 老年代内存统计

- -gcoldcapacity

###### 查看GC原因

- jstat -gccause 3346
- 显示垃圾回收的相关信息，同时显示最后一次或当前正在发生的垃圾回收的诱因
  - LGCC：最后一次GC原因
  - GCC：当前GC原因（No GC 为当前没有执行GC）









