### SpringBoot

------

​	简化了Spring开发初始搭建以及开发过程，可以非常容易的构建独立的服务组件，是实现分布式架构、微服务架构利器

##### 1：SpringBoot 的特点

- 内嵌Tomcat、jetty等web容器，不需要部署WAR文件。
- 提供一系列的“starter” 来简化的Maven配置，不需要添加很多依赖。

##### 2：简单构建一个Demo

- quickstart：构建一个jar包
- webapp：构建一个war包

##### 3：pom.xml 依赖的管理

- 在pom.xml中引入spring-boot-start-parent,它可以提供dependency management,也就是说依赖管理，引入以后在申明其它dependency的时候就不需要version了，后面可以看到，可以用于解决以来冲突；
- spring-boot-starter-web是springweb 核心组件，构建war包必须使用；

```xml
 <parent>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-parent</artifactId>
      <version>2.1.10.RELEASE</version>
 </parent>

<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

##### 4：SpringBoot的启动方式

以前启动Spring项目的时候，需要打包部署到tomcat的webapp下，由于SpringBoot内置tomcat,所以有一下两种启动方式；

1. 第一种启动方式比较单一，只能启动一个Controller控制器

1. ```java
   @RestController("/xupt/")
   @EnableAutoConfiguration
   public class HelloWord {
     @RequestMapping("hello")
   	@ResponseBody
   	public String hello() {
   		return "Hello World";
   	}
     
     public static void main(String[] args) {
       // 启动项目
       SpringApplication.run(HelloController.class, args);
     }
   }
   ```

2. 创建一个单独的启动类App

   ```java
   /**
    * SpringBoot的启动类
    */
   @EnableAutoConfiguration
   @ComponentScan(basePackages = "com.xupt")
   public class App {
       public static void main( String[] args ) {
           // 启动springboot项目
           SpringApplication.run(App.class,args);
       }
   }
   ```

##### 5： Spring Boot的依赖介绍

- mvn dependency:tree：执行此命令可以查看依赖树

| **spring-boot-starter**          | 核心 POM，包含自动配置支持、日志库和对 YAML 配置文件的支持。 |
| -------------------------------- | ------------------------------------------------------------ |
| spring-boot-starter-amqp         | 通过 spring-rabbit 支持 AMQP，消息队列                       |
| **spring-boot-starter-aop**      | 包含 spring-aop 和 AspectJ 来支持面向切面编程（AOP）         |
| spring-boot-starter-batch        | 支持 Spring Batch，包含 HSQLDB。                             |
| spring-boot-starter-data-jpa     | 包含 spring-data-jpa、spring-orm 和 Hibernate 来支持 JPA。   |
| spring-boot-starter-data-mongodb | 包含 spring-data-mongodb 来支持 MongoDB。                    |
| spring-boot-starter-data-rest    | 通过 spring-data-rest-webmvc 支持以 REST 方式暴露 Spring Data 仓库。 |
| **spring-boot-starter-jdbc**     | 支持使用 JDBC 访问数据库                                     |
| spring-boot-starter-security     | 包含 spring-security。                                       |
| **spring-boot-starter-test**     | 包含常用的测试所需的依赖，如 JUnit、Hamcrest、Mockito 和 spring-test 等。 |
| **spring-boot-starter-velocity** | 支持使用 Velocity 作为模板引擎。                             |
| **spring-boot-starter-web**      | 支持 Web 应用开发，包含 Tomcat 和 spring-mvc。               |
| spring-boot-starter-websocket    | 支持使用 Tomcat 开发 WebSocket 应用。                        |
| spring-boot-starter-ws           | 支持 Spring Web Services                                     |
| spring-boot-starter-actuator     | 添加适用于生产环境的功能，如性能指标和监测等功能。           |
| spring-boot-starter-remote-shell | 添加远程 SSH 支持                                            |
| spring-boot-starter-jetty        | 使用 Jetty 而不是默认的 Tomcat 作为应用服务器。              |
| **spring-boot-starter-log4j**    | 添加 Log4j 的支持                                            |
| **spring-boot-starter-logging**  | 使用 Spring Boot 默认的日志框架 Logback                      |
| **spring-boot-starter-tomcat**   | 使用 Spring Boot 默认的 Tomcat 作为应用服务器。              |
|                                  |                                                              |

##### 6： Web开发

1. Spring-Boot 默认提供静态资源目录位置**需置于classpath下**(resources目录下)，目录名需符合如下规则：

   - /static
   - 默认的classpath:/resources/，classpath:/static/
     - static：存放静态页面，可以直接访问
     - static：http://localhost:8080/hello.html，静态页面的return默认：跳转到/static/hello.html
     - templates：存放动态页面(跳转过去的页面)
       - 动态页面需要先请求服务器，访问后台应用程序，然后再重定向到页面
       - 默认使用Thymeleaf来做动态页面
         - 例：http://localhost:8080/hello.html，return "hello.html"
       - 当在pom.xml中引入了thymeleaf组件，动态跳转会覆盖默认的静态跳转，/templates/index.html
       - 注意看两者return代码也有区别，动态有或没有html后缀都可以
       - 如果在使用动态页面时还想跳转到/static/index.html，可以使用重定向
         - return "redirect:/index.html"

2. @RestController 

   - 相当于把@Controller和@ResponseBody两个注解和起来，直接作为Json对象返回；

3. 全局异常捕获处理（AOP）

   ```java
   /**
   * 定义一个全局异常捕获处理类，只要Controller发生异常就会调到这里
   */
   @ControllerAdvice
   public class GlobalExceptionHandler {
   		// 捕获运行时异常，方法和返回值自定义的
       @ExceptionHandler(RuntimeException.class)
       @ResponseBody
       public Map<String,Object> exceptionHander(){
           Map<String, Object> map = new HashMap<String, Object>();
           map.put("errorCode", "101");
           map.put("errorMsg", "系統错误!");
           return map;
       }
   }
   ```

4. 渲染Web页面

   1.  模板引擎：基于模板来生成文本输出，实现动态的网页，相当于Jsp，SpringBoot是不推荐使用Jsp，因为其不能打包成jar包部署；
   2. 模板引擎
      - Thymeleaf
      - FreeMarker
      - Velocity
      - Groovy

##### 7：模板引擎

1. ###### FreeMarker 的使用

   1. 加入依赖

      ```xml
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-freemarker</artifactId>
      </dependency>
      ```

   2. 编写控制器

   3. 穿件后缀为 ***.ftl** 模板文件

      - 在src/main/resources/创建一个templates文件夹，后缀为*.ftl，先创建HTML文件，修改后缀为ftl；
      - 此文件加是受保护的，必须通过controller层跳转，不能直接访问；
      - 具体写法类似于jsp标签

2. ###### Jsp的使用

   1. 添加依赖

      ```xml
      <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
      </dependency>
      ```

   2. 创建配置文件

      - 在resources文件夹下创建application.properties文件，并配置
        - spring.mvc.view.prefix=/WEB-INF/view/
        - spring.mvc.view.suffix=.jsp
        - server.port=8888   配置tomcat端口号
        - Server.context-path=/xupt  配置项目名称，用于访问页面时作为根路径

3. ###### thymeleaf

   1. 添加依赖

      ```xml
      <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
      </dependency>
      ```

   2. 在src/main/resources/创建一个templates文件夹，后缀为*.yml，先创建HTML文件，修改后缀为yml；

      1. 必须在html文件中引入xmlns:th="http://www.thymeleaf.org"

   3. 在application.propesties中配置跳转的前后缀

      - spring.thymeleaf.prefix=classpath:/templates/ 
      - spring.thymeleaf.suffix=.html  

   4. 或者在模板引擎文件中配置

      ```yml
      spring:
         thymeleaf:
          prefix: "classpath:/templates/"
          suffix: ".html"
      ```

##### 8：yaml：以数据为中心，使用空白、缩进、分行组织数据，用于指定分层配置数据

- 语法：
  1. 大小写敏感
  2. 使用缩进表示层级关系
  3. 禁止使用tab缩进，只能使用空格键 , 建议使用两个空格
  4. 缩进的空格数目不重要，只要相同层级的元素左侧对齐即可
  5. "#"：表示注释，从这个字符一直到行尾，都会被解析器忽略
  6. 字符串可以不用引号，也可以使用单引号或者双引号	
  7. 书写(Key: Value)
     - key：需要顶格写，不能有空格，冒号后面需要有一个空格然后再跟值, 相同的缩进属于同一个map 

##### 5：配置文件(application.properties)

1. **Spring可以通过注解@Value(“${属性名key}”)**：加载对应的配置属性，然后将属性值赋值给注解对应的实体属性
2. @ConfigurationProperties(prefix = “xxx”)：配置属性注解，可以指定一个属性的前缀，将配置文件中的key为prefix属性名的值赋值给对应的属性，这种方式适用于前缀相同的一组值，这样就不用再为每个属性配置
3. @PropertySource：来定义属性文件的位置
   - @PropertySource(value= {"classpath:/config/book.properties"})
5. **多环境配置文件**：application-{profile}.properties 格式，其中{profile}对应你的环境标识，比如：
   1. application-dev.properties：开发环境
   2. application-beta.properties：测试环境
   3. application-prod.properties：生产环境
6. 在application.properties文件中通过spring.profiles.active=dev属性来设置，其值对应{profile}值,配置文件会被加载

##### 6：@SpringBootApplication：复合注解，包括三个

1. @Configuration:指的Java Config(Java 配置)，是一个Ioc容器类，相当于spring的xml的配置文件
2. @EnableAutoConfiguration：借助@Import来收集所有符合自动配置条件的bean定义的类(@Configuration),汇总成一个加载到IoC容器
3. @ComponentScan：若不配置base-package,则默认从当前类开始下扫描，所以一般运行类放在基包下







