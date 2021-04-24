### JVM 调优

------

​	一般调整垃圾回收器，和堆大小以及最大停顿时间

- -XX:+UseG1GC -Xmx32g -XX:MaxGCPauseMillis = 200
  - -XX:+UseG1GC：为开启G1垃圾收集器
  - -Xmx32g：设计堆内存的最大内存为32G
  - -XX:MaxGCPauseMillis=200：设置GC的最大暂停时间为200ms
- 如果我们需要调优，在内存大小一定的情况下，我们只需要修改最大暂停时间即可

##### 01：Jvm 参数格式

1. 标准参数（-），所有的JVM实现都必须实现这些参数的功能，而且向后兼容；
2. 非标准参数（-X），默认jvm实现这些参数的功能，但是并不保证所有jvm实现都满足，且不保证向后兼容；
3. 非Stable参数（-XX），此类参数各个jvm实现会有所不同，将来可能会随时取消，需要慎重使用；

##### 02：tomcat 配置

- /Tomcat/apache-tomcat-8.5.40/bin/catalina.sh

- 添加JAVA_OPTS

  - ```sh
    JAVA_OPTS="-Xms6g -Xmx6g -XX:-OmitStackTraceInFastThrow 
    -XX:SoftRefLRUPolicyMSPerMB=0 -Xss256k -XX:MaxGCPauseMillis=200 
    -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=256m 
    -Djava.security.egd=file:/dev/./urandom -XX:+HeapDumpOnOutOfMemoryError 
    -Xlog:gc*:file=$CATALINA_BASE/logs/gc-$(date +%Y_%m_%d-%H_%M_%S).log:time,level,tags -XX:HeapDumpPath=${CATALINA_BASE}/logs
    -Djava.awt.headless=true"
    ```

  - 堆配置

    - -Xms2048m(or -XX:InitialHeapSize=2048m)
      - 最小堆（初始化时堆的大小）
    - -Xmx2048m(or -XX:MaxHeapSize=2048m)
      - 最大堆
    - -XX:NewSize=n
      - 设置年轻代大小
    - -XX:NewRatio=n
      - 设置年轻代和年老代的比值。
      - 如：3，表示年轻代与年老代比值为1：3，年轻代占整个堆内存的1/4；
    - -XX:SurvivorRatio=n
      - 年轻代中Eden区与两个Survivor区的比值，注意Survivor区有两个。
      - 如：3，表示Eden：Survivor=3：2，一个Survivor区占整个年轻代的1/5；
    - -XX:MaxPermSize=n
      - 设置持久代大小，JDK11 已经取消该参数，用元数据区代替；

  - 栈配置

    - -Xss256k
    - 单线程的栈大小，该值会影响到深度调用；

  - 元数据区

    - -XX:MetaspaceSize=256m 
    - -XX:MaxMetaspaceSize=256m

  - 收集器设置

    - -XX:+UseSerialGC :设置串行收集器
    - -XX:+UseParallelGC :设置并行收集器
    - -XX:+UseParalledlOldGC :设置并行年老代收集器
    - -XX:+UseConcMarkSweepGC :设置并发收集器

  - 垃圾回收统计信息

    - -XX:+PrintGC
    - -XX:+PrintGCDetails
    - -XX:+PrintGCTimeStamps
    - -Xloggc:filename

  - 并行收集器设置

    - -XX:ParallelGCThreads=n :设置并行收集器收集时使用的CPU数。并行收集线程数。
    - -XX:MaxGCPauseMillis=n :设置并行收集最大暂停时间
    - -XX:GCTimeRatio=n :设置垃圾回收时间占程序运行时间的百分比。公式为1/(1+n)

  - 并发收集器设置

    - -XX:+CMSIncrementalMode :设置为增量模式。适用于单CPU情况。
    - -XX:ParallelGCThreads=n :设置并发收集器年轻代收集方式为并行收集时，使用的CPU数。并行收集线程数。

  