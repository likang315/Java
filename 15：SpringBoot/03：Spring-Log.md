### Log

------

[TOC]

- private static final Logger log = LoggerFactory.getLogger(Main.class);

##### 01：Slf4j：定义了抽象层的日志框架，LogBack：slf4j的原生实现

![](https://github.com/likang315/Middleware/blob/master/15%EF%BC%9ASpringBoot/photos/log-level.jpg?raw=true)

1. 配置方法：

   1. 尝试在 classpath下查找文件logback.xml;
   2. 如果文件不存在，则查找文件logback-spring.xml；
   3. 如果两个文件都不存在，logback用BasicConfigurator自动对自己进行配置，这会导致记录输出到控制台。

2. ###### 使用方式

   1. 添加jar包：slf4j-api.jar，logback-core.jar，logback-classic.jar

      ```xml
      <dependencies>
      　　　　<dependency>
                <groupId>org.slf4j</groupId>
                <artifactId>slf4j-api</artifactId>
                <version>1.7.5</version>
            </dependency>
      
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-core</artifactId>
                <version>${logback.version}</version>
                <scope>runtime</scope>
            </dependency>
      
            <dependency>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
                <version>${logback.version}</version>
                <scope>runtime</scope>
            </dependency>
   </dependencies>
      ```

   2. 配置logback.xml
   
      ```xml
      </configuration><?xml version="1.0" encoding="UTF-8"?>
      <!-- slf4j日志配置文件 -->
      <configuration debug="true" scan="true" scanPeriod="30 seconds">
      
          <!-- 设置日志输出根目录 -->
          <property name="app.name" value="h_datacube_wrapper"/>
          <property name="log.dir" value="${catalina.base}/logs"/>
          <property name="encoding" value="UTF-8"/>
          <property name="pattern"
                    value="%d{yyyy-MM-dd HH:mm:ss.SSS}[%level][%X{QTRACER}][%thread]    |    %C#%M:%L    |    %msg%n"/>
          <property name="log.maxHistory" value="14"/>
          <property name="log.level" value="info"/>
          <property name="log.maxSize" value="40GB" />
      
          <!-- 只打印 INFO 级别以上的信息-->
          <appender name="info_appender" class="ch.qos.logback.core.rolling.RollingFileAppender">
              <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
                  <level>INFO</level>
              </filter>
              <file>${log.dir}/info.log</file>
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                  <param name="fileNamePattern" value="${log.dir}/info-%d{yyyy-MM-dd_HH}.log"/>
                  <param name="maxHistory" value="${log.maxHistory}"/>
                  <param name="totalSizeCap" value="${log.maxSize}"/>
              </rollingPolicy>
          </appender>
      
          <!-- 只打印 ERROR 级别以上的信息 -->
          <appender name="error_appender" class="ch.qos.logback.core.rolling.RollingFileAppender">
              <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
                  <level>ERROR</level>
              </filter>
              <File>${log.dir}/error.log</File>
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                  <param name="fileNamePattern" value="${log.dir}/error-%d{yyyy-MM-dd_HH}.log"/>
                  <param name="maxHistory" value="${log.maxHistory}"/>
                  <param name="totalSizeCap" value="${log.maxSize}"/>
              </rollingPolicy>
          </appender>
      
          <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
          </appender>
      
          <appender name="dbAccessAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">>
              <file>${log.dir}/dbsource.log</file>
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                  <param name="fileNamePattern" value="${log.dir}/dbsource-%d{yyyy-MM-dd}.log"/>
                  <param name="maxHistory" value="${log.maxHistory}"/>
              </rollingPolicy>
              <encoder>
                  <pattern>${pattern}</pattern>
              </encoder>
          </appender>
      
          <appender name="redisAccessAppender" class="ch.qos.logback.core.rolling.RollingFileAppender">>
              <file>${log.dir}/redis.log</file>
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                  <param name="fileNamePattern" value="${log.dir}/redis-%d{yyyy-MM-dd}.log"/>
                  <param name="maxHistory" value="${log.maxHistory}"/>
              </rollingPolicy>
              <encoder>
                  <pattern>${log.pattern}</pattern>
              </encoder>
          </appender>
      
          <!--自定义logger-->
          <logger name="com.xupt.db.resource" level="warn" additivity="false">
              <level value="INFO" />
              <appender-ref ref="dbAccessAppender" />
          </logger>
      
          <logger name="com.xupt.redis.storage" level="warn" additivity="false">
              <level value="INFO" />
              <appender-ref ref="redisAccessAppender" />
          </logger>
      
          <logger name="com.xupt" additivity="false">
              <appender-ref ref="info_appender" />
              <appender-ref ref="error_appender" />
          </logger>
      
          <root level="INFO">
              <appender-ref ref="console"/>
          </root>
      </configuration>
      ```

##### 02：Logback 配置详解

1. ###### 根节点 <configuration> 

   - scan: 
     - 当此属性设置为true时，配置文件如果发生改变，将会被重新加载，默认值为true。
   - scanPeriod:
     - 设置监测配置文件是否有修改的时间间隔，如果没有给出时间单位，默认单位是毫秒。当scan为true时，此属性生效。默认的时间间隔为1分钟。
   - debug:
     - 当此属性设置为true时，将打印出logback内部日志信息，实时查看logback运行状态。默认值为false。

2. ###### 设置上下文信息  <contextName>

   - 每个logger都关联到logger上下文，默认上下文名称为“default”。但可以使用 <contextName> 设置成其他名字，用于区分不同应用程序的记录。一旦设置，不能修改。
   - <property> 
     - 用来定义变量值的标签，有两个属性，其中name的值是变量的名称，value的值时变量定义的值。通过<property>定义的值会被插入到logger上下文中。**定义变量后，可以使“${}”来使用变量**。
   - <timestamp>
     - 有两个属性，其中key，标识此<timestamp> 的名字；datePattern：设置将当前时间（解析配置文件的时间）转换为字符串的模式，遵循java.txt.SimpleDateFormat的格式；

3. ###### 设置logger，root

   - logger

     - 用来**设置某一个包或者具体的某一个类的日志打印级别、以及指定<appender>。**
     - name ：用来**指定受此logger约束的某一个包或者具体的某一个类**。
     - level：用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，还有一个特俗值INHERITED或者同义词NULL，代表强制执行上级的级别。**如果未设置此属性，那么当前logger将会继承上级（root）的级别。**
     - addtivity：是否向上级传递 该logger 打印的信息。默认是true。
     - ![log-level](/Users/likang/Code/Git/Middleware/SpringBoot/SpringBoot/log-level.jpg)

   - root

     - 也是 <logger>元素，不过它是根loger。只有一个level属性，因为已经被命名为"root"
     - level：用来设置打印级别，大小写无关：TRACE, DEBUG, INFO, WARN, ERROR, ALL 和 OFF，不能设置为INHERITED或者同义词NULL。默认是DEBUG。
     - <root> 可以包含零个或多个<appender-ref>元素，**标识这个appender将会添加到这个logger。**

   - ###### 示例

   - ```xml
     <configuration>   
       <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">   
         <!-- encoder 默认配置为PatternLayoutEncoder -->   
         <encoder>   
           <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>   
         </encoder>   
       </appender>   
     
       <!-- logback为java中的包 -->   
       <logger name="logback"/>   
       <!--logback.LogbackDemo：类的全路径 -->   
       <logger name="logback.LogbackDemo" level="INFO" additivity="false">  
         <appender-ref ref="STDOUT"/>  
       </logger>   
     
       <root level="INFO">             
         <appender-ref ref="STDOUT" />   
       </root>     
     </configuration>
     ```

     - <logger name="logback" />
       - 将控制logback包下的所有类的日志的打印，但是并没用设置打印级别，所以继承他的上级<root>的日志级别“DEBUG”；没有设置addtivity，默认为true，将此loger的打印信息向上级传递；没有设置appender，所以他本身不打印任何信息，而是传递给上级打印信息；
     - <logger name="logback.LogbackDemo" level="INFO" additivity="false">
       - 控制logback.LogbackDemo类的日志打印，打印级别为“INFO”；additivity属性为false，表示此loger的打印信息不再向上级传递，指定了名字为“STDOUT”的appender。
       - **如果additivity="true"，则日志会打印两次，本身打印一次，上级打印一次；**

##### 03：使用AOP统一处理Web请求日志

1. 添加AOP依赖

2. 编写切面类

   ```java
   @Aspect
   @Component
   public class WebLogAspect {
       private Logger logger = Logger.getLogger(getClass());
       // 申明切点
       @Pointcut("execution(com.xupt.controller..*.*(..))")
       public void webLog() {
       }
   
       @Before("webLog()")
       public void doBefore(JoinPoint joinPoint) throws Throwable {
           // 接收到请求，记录请求内容
           ServletRequestAttributes attributes = 
               (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
           HttpServletRequest request = attributes.getRequest();
           // 记录下请求内容
           logger.info("---------------request----------------");
           logger.info("URL : " + request.getRequestURL().toString());
           logger.info("IP : " + request.getRemoteAddr());
       }
   
       @AfterReturning(returning = "resp", pointcut = "webLog()")
       public void doAfterReturning(Object ret) throws Throwable {
           logger.info("---------------response----------------");
           // 处理完请求，返回内容
           logger.info("RESPONSE : " + resp);
       }
   }
   ```