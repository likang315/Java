### SpringBoot JDBC

------

##### 1：JDBC

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
   ```

2. 配置数据库用户名、密码、驱动，机器URL

   1. 在application.properties中配置，会自动配置数据源，JdbcTemplate对象，相当于connection

      ```properties
      #数据库配置
      spring.datasource.url=jdbc:mysql://localhost:3306/xupt
      spring.datasource.username=root
      spring.datasource.password=123456
      spring.datasource.driver-class-name=com.mysql.jdbc.Driver
      ```

3. 编写controller，services层

##### 2：耦合 MyBatis

- 实际就是添加Mybatis依赖，JDBC驱动，使用其Jar包，操作数据库而已；

1. 添加依赖

   ```xml
   <!-- mybaties -->
   <dependency>
     <groupId>org.mybatis.spring.boot</groupId>
     <artifactId>mybatis-spring-boot-starter</artifactId>
     <version>1.1.1</version>
   </dependency>
   ```

2. 配置数据库用户名，密码等；

3. 编写dao层，mapper，xml（映射器），若xml 文件没有放在resources目录下，需在pom.xml中这样配置

   ```xml
   <build>
     <resources>
       <resource>
         <directory>src/main/java</directory>
         <includes>
           <include>**/*.xml</include>
         </includes>
       </resource>
     </resources>
   </build>
   ```

##### 3：配置多个数据源

1. 在application.properties中编写两套不同的数据库，前缀不同来区分

   ```properties
   spring.datasource.primary.driverClassName=com.mysql.jdbc.Driver
   spring.datasource.primary.url=jdbc:mysql://localhost:3306/test1?useUnicode=true&characterEncoding=utf-8
   spring.datasource.primary.username=root
   spring.datasource.primary.password=123456
   
   spring.datasource.test.driverClassName=com.mysql.jdbc.Driver
   spring.datasource.test.url=jdbc:mysql://localhost:3306/test2?useUnicode=true&characterEncoding=utf-8
   spring.datasource.test.username=root
   spring.datasource.test.password=123456
   ```

2. 编写两套相同的配置的数据源bean

```java
@Configuration
// 每个数据源扫描对应的包
@MapperScan(basePackages="com.xupt.primary.mapper",
            sqlSessionFactoryRef="test1SqlSessionFactory")
public class DataSourceConfig {

  /**
	 * @return 返回primary数据库的数据源
	 */
  @Bean(name="PrimaryDataSource")
  @Primary
  @ConfigurationProperties(prefix="spring.datasource.primary")
  public DataSource dateSource(){
    return DataSourceBuilder.create().build();
  }

  /**
	 * @return 返回Priamry数据库的会话工厂
	 */
  @Bean(name = "SqlSessionFactory")
  public SqlSessionFactory sqlSessionFactory(@Qualifier("PrimaryDataSource") DataSource ds) throws Exception{
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(ds);

    return bean.getObject();
  }

  /**
	 * @return 返回Primary数据库的事务
	 */
  @Bean(name = "PrimaryTransactionManager")
  @Primary
  public DataSourceTransactionManager transactionManager(
    @Qualifier("PrimaryDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  /**
	 * @return 返回Primary数据库的会话模版
	 */
  @Bean(name = "PrimarySqlSessionTemplate")
  public SqlSessionTemplate sqlSessionTemplate(
    @Qualifier("PriamrySqlSessionFactory") SqlSessionFactory sqlSessionFactory)
    throws Exception {
    return new SqlSessionTemplate(sqlSessionFactory);
  }
}
```

##### 4：多数据库的分布式事务

Case：一个方法中使用了事务，但是由于此方法中调用了两个方法，分别对应存储不同的数据库中，在这两个方法中，有个抛出了异常；

answer：是因为此方法上的事务是只针对单独起作用的哪个数据库，不是针对全部，因此要把两个事务耦合在一起；

###### 使用springboot+jta+atomikos 分布式事物管理解决方案

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

