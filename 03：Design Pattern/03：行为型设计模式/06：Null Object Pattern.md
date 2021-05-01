### 空对象模式（Null Object Pattern）：行为型模式

------

[TOC]

##### 01：概述

- 空对象取代对 NULL 对象实例的检查，**NULL 对象不是用来检查空值，而是用来执行一个不做任何动作的关系，提供默认的行为**；

##### 02：示例

​	工厂类 CustomerFactory 基于客户传递的名字来返回 *RealCustomer* 或 *NullCustomer* 对象

```java
public abstract class AbstractCustomer {
   protected String name;
   public abstract boolean isNull();
   public abstract String getName();
}

public class RealCustomer extends AbstractCustomer {
   public RealCustomer(String name) {
      this.name = name;    
   }
   @Override
   public String getName() {
      return name;
   }
   @Override
   public boolean isNull() {
      return false;
   }
}
// NULL对象默认的处理类
public class NullCustomer extends AbstractCustomer {
	 @Override
   public boolean isNull() {
      return true;
   }
   
   @Override
   public String getName() {
      return "默认的处理操作...";
   }
}

//  工厂区分出是不是空对象
public class CustomerFactory {
   public static final String[] names = {"Rob", "Joe", "Julie"};
   
   public static AbstractCustomer getCustomer(String name){
      for (int i = 0; i < names.length; i++) {
         if (names[i].equalsIgnoreCase(name)){
            return new RealCustomer(name);
         }
      }
      return new NullCustomer();
   }
}
```

