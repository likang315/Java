### Log

------

[TOC]

- private static final Logger log = LoggerFactory.getLogger(Main.class);

##### 01：Slf4j：定义了抽象层的日志框架，LogBack：slf4j的原生实现

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

   2. 配置logback-spring.xml
   
      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <!-- slf4j日志配置文件 -->
      <configuration debug="true" scan="true" scanPeriod="30 seconds">
          <!-- 设置日志输出根目录 -->
          <property name="app.name" value="h_crm_data_merchantman"/>
          <property name="log.dir" value="${catalina.base}/logs"/>
          <property name="encoding" value="UTF-8"/>
          <property name="pattern"
                    value="%d{yyyy-MM-dd HH:mm:ss.SSS}[%level][%X{QTRACER}][%thread]    |    %C#%M:%L    |    %msg%n"/>
      
          <!-- log file default -->
          <appender name="file" class="ch.qos.logback.core.rolling.RollingFileAppender">
              <File>${log.dir}/merchantman.log</File>
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <fileNamePattern>
                    ${log.dir}/merchantman.%d{yyyy-MM-dd_HH}.log
                </fileNamePattern>
                <MaxHistory>30</MaxHistory>
              </rollingPolicy>
          </appender>
          <!-- 时间滚动输出 level为 ERROR 日志 -->
          <appender name="file-error"
                    class="ch.qos.logback.core.rolling.RollingFileAppender">
            <!-- 用过滤器，只接受ERROR级别的日志信息，其余全部过滤掉 -->  
            <filter class="ch.qos.logback.classic.filter.LevelFilter">
                  <level>ERROR</level>
                  <onMatch>ACCEPT</onMatch>
                  <onMismatch>DENY</onMismatch>
              </filter>
              <File>${log.dir}/error.log</File>
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
            	<!--日志滚动策略-->
              <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                <!-- 历史日志文件的存放路径和名称 也可以配置成.gz 的 -->  
                <FileNamePattern>${log.dir}/error.%d{yyyy-MM-dd}.log</FileNamePattern>
                <!--最大保留30天的日志-->  
                <MaxHistory>30</MaxHistory>
              </rollingPolicy>
          </appender>
      
          <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
              <encoder>
                  <pattern>${pattern}</pattern>
                  <charset>${encoding}</charset>
              </encoder>
          </appender>
        
         <root level="INFO">
              <appender-ref ref="console"/>
              <appender-ref ref="file"/>
              <appender-ref ref="file-error"/>
         </root>
      </configuration>
      ```

##### 02：Log4j和Commons Logging

1. 配置根Logger(level)
   - log4j.rootLogger = debug,info,warn,error,fatal--------按此顺序设置优先级
   - level 是日志记录的优先级，分为FATAL、ERROR、WARN、INFO、DEBUG或者自定义的级别,Log4j建议只使用四个级别，优先级从高到低分别是FATAL，ERROR、WARN、INFO、DEBUG
   - 特殊：
     - ALL：打印所有的日志
     - OFF：关闭所有的日志输出
   - 通过在这里定义的级别，您可以控制到应用程序中相应级别的日志信息的开关,appenderName就是指定日志信息输出到哪个地方，可同时指定多个输出目的地
2. 配置日志信息输出目的地 Appender
   - log4j.appender.XXX (console,file,dailyfile)
3. 配置日志信息的格式（布局）Layout
   - log4j.appender.console.layout.ConversionPattern ="XXX"
4. 配置好之后，如果想给某各类加日志
   - private static Logger log=Logger.getLogger(xxx.class);
   - log.error("error...."+e);

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