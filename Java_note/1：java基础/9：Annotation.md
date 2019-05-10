### 注解 (java.lang.annotation)

相当于一种标记，在程序中加入注解就等于为程序打上某种标记，标记可以加在包、类，属性、方法，方法的参数
以及局部变量上，**一旦加上标记，你的程序要实例化此类**



### 1：它是 jdk1.5 增加的特性之一，内部提供了三种注解

- @SuppressWarnings("deprecation")      
  - 压缩警告：给编译器一条指令，告诉它对被批注的代码**元素内部的某些警告保持静默（不起作用）**

- @Deprecated      

  - 过期的

- @Retention(value=RUNTIME)

  - 被它所注解的注解保留多久

  

### 2：在注解类上使用另一个注解类，那么被使用的注解类就称为元注解

​	判断一个类有没有加注解,只能通过反射测试

### 3：注解的生命周期(三个阶段)

java源文件---> class文件--->内存中的字节码（Runtime）

自定义注解时可以使用 **Retention注解 指明自定义注解的生命周期**，API默认是存在到RetentionPolicy.CLASS阶段 

自定义注解的生命周期阶段
	RetentionPolicy.SOURCE (java源文件阶段)
	RetentionPolicy.CLASS  (class文件阶段)
	RetentionPolicy.RUNTIME(内存中运行阶段)
	
例：@Retention(value=RUNTIME)

### 4：@Target：

**限定了注解使用类型范围**，只能标识到那些程序元素类型上，若注解类型声明中不存在Target元注解，默认的是可以 用在任何程序元素类型

ElementType：程序元素类型

ANNOTATION_TYPE 
      	注释类型声明 
CONSTRUCTOR 
      	构造方法声明 
FIELD 
      	字段声明（包括枚举常量） 
LOCAL_VARIABLE 
      	局部变量声明 
METHOD 
      	方法声明 
PACKAGE 
      	包声明 
PARAMETER 
     	 参数声明 
TYPE 
      	类、接口（包括注释类型）或枚举声明 

例：@Target( { ElementType.METHOD, ElementType.TYPE })

### 5：自定义注解

```java
public @interface  注解类名{
  
}
```

   例：

```java
//定义属性值
public @interface MyAnnotation {
	String name();
	int[] ints();
  // 类型 属性名() default 默认值 
	String color() default "blue"；
}

//使用注解
@MyAnnotation(name = "lisi"，ints = {1,3},)  
```








