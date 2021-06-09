### 桥接模式（Bridge）：属于结构型模式

------

[TOC]

##### 00：概述

​	降低抽象与实现之间的耦合度，使得二者可以独立发展，解耦；

###### 优点：

- 抽象和实现的分离
- 优秀的扩展能力
- 桥接模式**将继承关系转化成关联关系**，它降低了类与类之间的耦合度

##### 01：示例

```java
public interface Concrete {
    void method();
}
public class ConcreteA implements Concrete {
    @Override
    public void method() {
        System.out.println("实现...");
    }
}
// 抽象部分，作为桥接类使二者可以独立发展
public abstract class Abstraction {
    private Concrete pimp;
    public Abstraction(Concrete t) {
        pimp = t;
    }
    public void method() {
        pimp.method();
    }
}
// 实现部分
public class AbstractionA extends Abstraction {
    public AbstractionA(Concrete t) {
        super(t);
    }
    public static void main(String[] args) {
        AbstractionA p = new AbstractionA(new ConcreteA());
        p.method();
    }
}
```

