### Log

------

##### 1：log4j

- 添加依赖：剔除springboot的logging，添加log4j，因为自带的logging会使其不启效果

- 一般不同Spring的log4j，我们使用apache 的log4j

  ```
  Logger log = Logger.getLogger(xxx.class);
  ```

##### 2：使用AOP统一处理Web请求日志

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

##### 3：打包部署

1. 使用mave， 打成war包或者jar包
2. 使用java -jar test3-0.0.1-SNAPSHOT.jar 运行即可，不管是jar，还是war都是用的是java -jar 包名；

