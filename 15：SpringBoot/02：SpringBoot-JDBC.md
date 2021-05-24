### SpringBoot 数据库配置

------

[TOC]

##### 01：JDBC

1. 添加依赖

   ```xml
   <!-- JDBC -->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-jdbc</artifactId>
   </dependency>
   <!-- 数据库驱动 -->
   <dependency>
       <groupId>mysql</groupId>
       <artifactId>mysql-connector-java</artifactId>
   </dependency>
   <!--数据库连接池-->
   <dependency>
       <groupId>com.alibaba</groupId>
       <artifactId>druid</artifactId>
   </dependency>
   ```

2. 配置数据库用户名、密码、驱动，机器URL

   1. 在application.properties中配置，会**自动配置数据源，JdbcTemplate对象**，相当于connection

      ```properties
      #数据库配置
      spring.datasource.url=jdbc:mysql://localhost:3306/xupt
      spring.datasource.username=root
      spring.datasource.password=123456
      spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
      ```

3. 编写controller，services层

##### 02：耦合 MyBatis

- 实际就是添加Mybatis依赖，使用其Jar包，操作数据库而已；

1. **添加依赖**

   ```xml
   <!-- mybaties -->
   <dependency>
     <groupId>org.mybatis.spring.boot</groupId>
     <artifactId>mybatis-spring-boot-starter</artifactId>
     <version>1.1.1</version>
   </dependency>
   ```

2. 配置数据库用户名，密码等；

3. 定义Mapper，mybatis-config.xml 文件位置

   ```java
   /**
    * 配置数据源
    *
    * @author kangkang.li@qunar.com
    * @date 2020-10-11 19:28
    */
   @MapperScan(basePackages = {"com.atlantis.zeus.index.dao.rw",
                               "com.atlantis.zeus.index.dao.readonly"},
               sqlSessionTemplateRef = "writeSqlSessionTemplate")
   @Configuration
   public class DataSourceWriteConfig {
       /**
        * pxc config
        *
        * @return pxc config
        */
       @Bean(name = "pxcDataSourceConfig")
       @ConfigurationProperties(prefix = "zeus.datasource.db")
       @Primary
       public PxcDataSourceConfig pxcDataSourceConfig() {
           return new PxcDataSourceConfig();
       }
   
       /**
        * 创建写数据源
        *
        * @return datasource
        */
       @Bean(name = "writeDataSource")
       @Primary
       public DataSource merchantDataSource(@Qualifier("pxcDataSourceConfig") PxcDataSourceConfig pxcDataSourceConfig) {
           DruidDataSource dataSource = new DruidDataSource();
           dataSource.setDriverClassName(pxcDataSourceConfig.getDbName());
           dataSource.setUrl(pxcDataSourceConfig.getNamespace());
           dataSource.setUsername(pxcDataSourceConfig.getUsername());
           dataSource.setPassword(pxcDataSourceConfig.getPassword());
           dataSource.setMaxActive(pxcDataSourceConfig.getMaxPoolSize());
           return dataSource;
       }
   
       /**
        * 创建SessionFactory
        *
        * @param dataSource
        * @return sessionFactory
        * @throws Exception
        */
       @Bean(name = "writeSqlSessionFactory")
       @Primary
       public SqlSessionFactory sqlSessionFactory(@Qualifier("writeDataSource") DataSource dataSource) throws Exception {
           SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
           bean.setDataSource(dataSource);
           bean.setConfigLocation(new PathMatchingResourcePatternResolver().getResource("classpath:mybatis/mybatis-config.xml"));
           bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mappers/**/*.xml"));
           return bean.getObject();
       }
   
       /**
        * 创建事务管理器
        *
        * @param dataSource
        * @return
        */
       @Bean(name = "transactionManager")
       @Primary
       public DataSourceTransactionManager transactionManager(@Qualifier("writeDataSource") DataSource dataSource) {
           return new DataSourceTransactionManager(dataSource);
       }
   
       /**
        * 创建SqlSessionTemplate
        *
        * @param sqlSessionFactory
        * @return
        */
       @Bean(name = "writeSqlSessionTemplate")
       @Primary
       public SqlSessionTemplate sqlSessionTemplate(@Qualifier("writeSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
           return new SqlSessionTemplate(sqlSessionFactory);
       }
   
   }
   ```

4. **配置多个数据源【读写分离】**

5. 编写dao 层，操作数据库；

##### 03：分布式事务

- 一个方法中使用了事务，但是由于此方法中调用了两个方法，分别对应存储不同的数据库中，在这两个方法中，有个抛出了异常，是因为此方法上的事务是只针对单独起作用的哪个数据库，不是针对全部，因此要把两个事务耦合在一起；

###### 使用 springboot + jta + atomikos 分布式事物管理解决方案

1. 添加依赖

   ```xml
   <dependency>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-starter-jta-atomikos</artifactId>
   </dependency>
   ```

2. 编写两个数据库配置实体类

3. 使用AtomikosDataSourceBean管理数据源，设置不同的数据库；

   - AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
   - xaDataSource.setUniqueResourceName("test1DataSource");

