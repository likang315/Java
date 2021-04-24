Hadoop 命令

------

[TOC]

------

##### 01：概述

- 所有的hadoop命令均由bin/hadoop脚本引发

  ```sh
  hadoop [--config confdir] [COMMAND] [GENERIC_OPTIONS] [COMMAND_OPTIONS]
  ```

  - --config confdir  覆盖缺省配置目录。缺省是${HADOOP_HOME}/conf；
  - GENERIC_OPTIONS：多个命令都支持的通用选项。  
  - COMMAND_OPTIONS： 命令选项；

##### 02：用户命令

1. archive

   - 创建一个hadoop档案文件；

   - Hadoop archive的扩展名是*.har。Hadoop archive包含元数据（形式是_index和_masterindx）和数据（part-*）文件。_index文件包含了档案中的文件的文件名和位置信息；

   - ```
     hadoop archive -archiveName NAME <src>* <dest>
     ```

   - -archiveName NAME： 要创建的档案的名字；

   - src  文件系统的路径名，和通常含正则表达的一样：

   - dest  保存档案文件的目标目录；

2. distcp

   - 递归地拷贝文件或目录；

     ```sh
     hadoop distcp <srcurl> <desturl>
     ```

3. fs

   - 运行一个常规的文件系统客户端;

   - ```
     hadoop fs [GENERIC_OPTIONS] [COMMAND_OPTIONS]
     ```

4. jar

   - 运行jar文件。用户可以把他们的Map Reduce代码捆绑到jar文件中;

     ```
     hadoop jar <jar> [mainClass] args...
     ```
   
5. 从Hadoop 集群中下载文件到跳板机上

   - 复制文件到本地文件系统；

   ```
   hadoop fs -get 文件路径 本地下载路径
   ```

   

