### 注解：java.lang.annotation

------

​	相当于一种标记，在程序中加入注解就等于为程序打上某种标记，标记可以加在包、类，属性、方法，方法的参数以及局部变量上，**一旦加上标记，程序要实例化此类**

##### 1：注解 JDK1.5 的特性，内部提供了三种注解

- @SuppressWarnings("deprecation")      
  - 压缩警告：给编译器一条指令，告诉它对被标记的代码**元素内部的某些警告保持静默（不起作用）**

- @Deprecated      

  - 过期的

- @Retention(value=RUNTIME)

  - 被它所注解的注解保留多久

##### 2：在注解类上使用另一个注解类，那么被使用的注解类就称为元注解

​	判断一个类有没有加注解，只能通过反射测试

##### 3：注解的生命周期(三个阶段)

1. java源文件
2. class文件
3. 内存中的运行阶段（Runtime）

- 自定义注解时使用 **Retention注解 指明自定义注解的生命周期**，默认是保留到RetentionPolicy.CLASS阶段 
- 自定义注解的生命周期阶段
  - RetentionPolicy.SOURCE (java源文件阶段)
  - RetentionPolicy.CLASS  (class文件阶段)
  - RetentionPolicy.RUNTIME(内存中运行阶段)
    - 例：@Retention(value=RUNTIME)

##### 4：@Target

​	限定了注解使用的类型范围，只能标识到指定程序的元素类型上，若注解类型声明中不存在Target元注解，默认的是用于任何元素类型

- ElementType：程序元素类型
  - ANNOTATION_TYPE 
    - 注释类型声明 
  - CONSTRUCTOR 
    - 构造方法声明 
  - FIELD 
    - 字段声明（包括枚举常量） 
  - LOCAL_VARIABLE 
    - 局部变量声明 
  - METHOD 
    - 方法声明 
  - PACKAGE 
    - 包声明 
  - PARAMETER 
    - 参数声明 
  - TYPE 
    - 类、接口（包括注释类型）或枚举声明 
  - 例：@Target( { ElementType.METHOD, ElementType.TYPE })

##### 5：自定义注解

​	使用自定义注解，其作用一定要利用AOP进行切面编程，可以利用其注解实现调用目标方法之前、之后做一些逻辑业务；

```java
public @interface  注解类名 {
  
}
// 定义属性值
public @interface MyAnnotation {
	String name();
	int[] ints();
  // 类型 属性名() default 默认值 
	String color() default "blue"；
}
// 使用注解
@MyAnnotation(name = "lisi"，ints = {1,3},)  

// 定义其自定义注解作用
@Around(value="@annotation(com.xupt.base.customizeClassName)")
public void processFunction(ProceedingJoinPoint point) {
  // ...
  point.process();
  // ...
}
```


