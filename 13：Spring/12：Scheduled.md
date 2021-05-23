### 定时器

------

[TOC]

##### 01：定时器：@Scheduled(cron="0 0 1 * * ?")

###### 原理

1. spring 在初始化 bean 后，针对 Spring 上下文中所有的Bean，postProcessAfterInitialization 执行后，扫描到所有的用到"@Scheduled"注解的方法；
2. 将对应类型的定时器放入相应的 **延迟任务队列** 中
4. 如果多个定时任务定义的是同一个时间，那么也是顺序执行的，会根据程序加载 Scheduled 方法的先后来执行；

```xml
<!--配置 namespace -->
<beans xmlns:task="http://www.springframework.org/schema/task"
       http://www.springframework.org/schema/task
       http://www.springframework.org/schema/task/spring-task-3.2.xsd />
<task:annotation-driven /> 启动定时器

<!-- 配置线程池，否则多线程下会有延时，因为定时器时单线程的 -->
<!-- 定时任务执行器线程数量 --> 
<task:executor id="executor" pool-size="3" />  
<!-- 任务调度器线程数量 --> 
<task:scheduler id="scheduler" pool-size="3" />  
<!-- 启用annotation方式 -->  
<task:annotation-driven scheduler="scheduler"  
                        executor="executor" proxy-target-class="true" /> 
```

##### 02：Cron Expressions ：七子表达式

​	Cron 的表达式被用来配置 CronTrigger 实例（基于日历的），cron 的表达式是字符串，实际上是由**七子表达式**，描述个别细节的时间表

- 这些子表达式是分开的空白
- "0 0 12 ? * WED"： 在每星期三下午 12:00 执行
  - Seconds (秒)     ：可以用数字 0－59 表示，
  - Minutes(分)        ：可以用数字 0－59 表示，
  - Hours(时)           ：可以用数字 0-23 表示,
  - Day-of-Month(日) ：可以用数字1-31 中的任一一个值
  - Month(月) ：可以用0-11 或用字符串  “JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV and DEC” 表示
  - Day**-**of**-**Week(星期)：可以用数字1-7表示（1 ＝ 星期日）或用字符口串“SUN, MON, TUE, WED, THU, FRI and SAT”表示
  - Year（年份）（1970－2099）
- "/"：特别单位min，
- 表示为“每”如“0/15”表示每隔15分钟执行一次,“0”表示为从“0”分开始, 
- “**3/20”表示表示每隔20分钟执行一次，“3”表示从第3分钟开始执行**
- "?"：表示每月的每一天，或第周的每几天
- "*"：字符代表所有可能的值
- **每分钟执行一次：0 0 0/1 ? * * ***



### 

