### 映射器 API

------

[TOC]

##### 01：核心API

- SqlSessionFactoryBuilder.build(InputStream in)
  - 根据 xml 配置信息构建 SqlSessionFactory，有重载的
- Interface **SqlSessionFactory (会话工厂)** ：
  - 两个实现类 DefaultSqlSessionFactory 和 SqlSessionManager
  - **用于加载 mybatis-config.xml 和 xml mapper 文件；**
  - SqlSession openSession() ：有重载的，默认会开启一个事务
    - 会话连接对象从由活动环境配置的**数据源实例**中得到
  - Configuration getConfiguration() ：获取Configuration对象

- Interface **SqlSession：会话连接**，相当于数据库实例

  - 包含增删改查，提交或回滚事务，还有获取映射器实例

  - 通用的方式来执行映射语句是使用映射器类，映射器类就是简单的接口

  - ###### T getMapper(Class<T> type) ：使用映射器

  - void commit()

  - void rollback()	

  - void clearCache() ：清除缓存

  - void close()  ：关闭缓存

  - <T> T selectOne(String statemen,Object parameter)

  - <E> List<E> selectList(String statement, Object parameter)

  - int insert(String statement, Object parameter)

  - int update(String statement, Object parameter)

  - int delete(String statement, Object parameter)

- class **SqlSessionTemplate** 

  - 实现了SqlSession 接口扩展了SqlSession 功能；

- class **DataSourceTransactionManager**

  - 事务管理器；

##### 02：映射器（Mapper）

- 由 **Java 接口 和 Mapper.xml** 文件共同组成，其实就是通过 XML 和反射来动态实现java接口实现类，负责发送 SQL，并返回结果。


###### 注解方式

1. 通过 mapper 中 的 **namespace** 标签绑定接口，可以不用写接口实现类，mybatis会通过该绑定自动帮你找到对应要执行的SQL语句；

   ```xml
   <mapper namespace="com.xupt.StudentMapperInterFace">
   ```

2. 注解

   - **@Configuration**
     - 构造数据库配置；
   - **@MapperScan**("basePackages" = {"",""}，sqlSessionTemplateRef = "writeSqlSessionTemplate") 
     - 把数据库实例赋给操作数据库的接口；

###### XML 配置

- mybatis-config.xml **< mappers > 标签配置**

```xml
<configuration>
    <!--映射器，用来寻找SqlMapper文件.xml 文件-->
    <mappers>
      <!--通过resource加载单个mapper-->
        <mapper resource="com/mybatis3/mappers/StudentMapper.xml"/>
      <!--通过URL加载-->
        <mapper url="file:///var/mappers/StudentMapper.xml"/>
      <!--以下两种模式，需要mapper和接口在同一级目录下-->
      <!--通过类加载。通过注解方式的，不需要xml文件-->
        <mapper class="com.mybatis3.mappers.TutorMapper"/>
      <!--批量加载-->
        <package name="com.xupt.dao.mapper">
    </mappers>
</configuration>
```