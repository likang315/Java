### Log

------

- private static final Logger log = LoggerFactory.getLogger(Main.class);

##### 1：Slf4j：定义了抽象层的日志框架，LogBack：slf4j的原生实现

1. 配置方法：

   1. 尝试在 classpath下查找文件logback-test.xml
   2. 如果文件不存在，则查找文件logback.xml；
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

   2. 配置lomback.xml
   
      ```xml
      <?xml version="1.0" encoding="UTF-8"?>
      <configuration scan="true" scanPeriod="60 seconds" debug="false">
          <property name="encoding" value="UTF-8"/>
          <!--定义日志文件存储地址-->
      		<property name="log.dir" value="${catalina.base}/logs"/>
          <property name="normal-pattern"
                    value="%date %level [%thread] %logger [%file : %line] %msg%n"/>
          <property name="common-pattern"
                    value="%d{HH:mm:ss.SSS} [%thread] %-5level [%C{1}:%line] %msg%n"/>
          <property name="db-pattern" value="%d{yyyy-mm-dd HH:mm:ss.SSS} [%thread] %logger [%file : %line] %msg%n"/>
          <!-- 打印我们请求第三方的日志(dubbo) -->
          <appender name="error"
                    class="ch.qos.logback.core.rolling.RollingFileAppender">
             <append>true</append>
             <encoder class="ch.qos.logback.classic.encoder.PatternLayoutEncoder">
      <!--%d表示日期，%thread表示线程名，%-5level：级别从左显示5个字符宽度%msg：日志消息，%n是换行符-->
              <pattern>
                %d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{50} - %msg%n
              </pattern>
              <charset>${encoding}</charset>
              </encoder>
            <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
                  <!--日志文件输出的文件名-->
                  <FileNamePattern>
                    ${LOG_HOME}/SharingEnvironmentCofig.log.%d{yyyy-MM-dd}.log
              		</FileNamePattern>
                  <append>true</append>
                  <!--日志文件保留天数-->
                  <MaxHistory>30</MaxHistory>
             </rollingPolicy>
          </appender>
          <!-- 日志输出级别 -->
          <root level="INFO">
              <appender-ref ref="error"/>
          </root>
      </configuration>
      ```

##### 2：Log4j和Commons Logging

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

