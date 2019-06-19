### 1：JUnit ：白盒测试

​	JUnit是一个Java语言的单元测试框架，Spring 已经集成了JUnit 框架啦，JUnit 5

###### 目的：

​	简化单元测试，写一点测一点，在编写最后的代码时如果发现问题可以较快的定位到问题的原因，减小回归错误的纠错难度

特点：不需要main 方法直接调用配置了@Test 的方法

### 2：JUnit 配置

除了spring的jar包，还需要spring-test.jar 和 JUnit jar包

```
  <dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
  </dependency>
  <dependency>  
    <groupId>org.springframework</groupId>  
    <artifactId>spring-test</artifactId>  
    <version>3.1.1.RELEASE</version>  
    <scope>test</scope>
  </dependency>
```

### 3：单元测试注解

- @RunWith ：Junit 提供的，用来说明此测试类的调用者，这里用了 SpringJUnit4ClassRunner，这个类是一个针对 Junit 运行环境的自定义扩展，用来标准化在 Spring 环境中 Junit4.5 的测试用例
- @ContextConfiguration：是 Spring test context 提供的，用来指定 Spring 配置信息的来源，支持指定 XML 文件位置或者 Spring 配置类名，来初始化Spring 容器
- @Transactional：是表明此测试类的事务启用，这样所有的测试方案都会自动的 rollback，即您不用自己清除自己所做的任何对数据库的变更

###### JUnit 4 常用注解

- @Test：指明测试方法，用于设置测试期望异常和超时时间
  - expected = 异常名称.class：运行过程中，抛出此异常可以不报错
  - timeout = 毫秒：测试时间超过此世间会报异常
- @Before：初始化，每一个测试方法执行前都要执行一次
- @BeforeClass：针对所有测试，只执行一次，且必须为static void ，而且在构造方法执行之前
- @After：释放资源  每一个测试方法执行都要执行一次
- @AfterClass：针对所有测试，只执行一次，且必须为static void 
- @Ignore：忽略的测试方法 

###### 一个JUnit4的单元测试用例执行顺序为： 

@BeforeClass -> @Before -> @Test -> @After -> @AfterClass; 



