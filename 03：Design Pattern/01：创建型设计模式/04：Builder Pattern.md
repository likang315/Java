### 建造者模式(Builder Pattern)：属于创建型模式

------

[TOC]

##### 01：概述

- 将一个复杂对象的**构建与其表示相分离**，使得同样的构建过程可以创建不同的对象 ( 属性不同 )	
  - **指挥者(director)**：构建一个使用 builder 接口的对象 
  - **建造者(builder)** ：创建一个产品对象的各个部件的抽象接口 
    - 具体的建造者（concreteBuilder)：实现 Builder 接口，描写各个部件的装配细节
  - 指挥者指挥建造者建造对象；
- 与工厂模式的区别是：建造者模式更加关注与零件装配的

```java
// 指挥者，一个产品分为三个模块
public class Director {
    public void construct(Builder b) {
        b.buildPartA();
        b.buildPartB();
        b.buildPartC();
    }
}
// 建造者
public interface Builder {
    void buildPartA();
    void buildPartB();
    void buildPartC();
}
// 具体的建造者
public class ConcreteBuilderA implements Builder {
    protected Product p;
    public ConcreteBuilderA() {
        p = new Product();
    }
    public Product getP() {
        return p;
    }

    @Override
    public void buildPartA() {
        System.out.println("PartA 建造中...");
        p.add("partA");
    }

    @Override
    public void buildPartB() {
        System.out.println("PartB 建造中...");
        p.add("partB");
    }

    @Override
    public void buildPartC() {
        System.out.println("PartC 建造中...");
        p.add("partC");
    }
}

public class Product {
    public void add(String s) {
        System.out.println("添加部件"+s+"中....");
    }

    public static void main(String[] args) {
        Director director = new Director();
        // 实例化建造者时就创建了product，每个对象只是属性不同
        ConcreteBuilderA ca = new ConcreteBuilderA();  
        // 把具体的建造者传给指挥者构建对象
        director.construct(ca);
        Product p = ca.getP();
    }
}
```