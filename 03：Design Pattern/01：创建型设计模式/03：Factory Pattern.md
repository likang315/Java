### 工厂模式：属于创建型模式 

------

[TOC]

##### 01：概述

- 简单工厂模式（Simple Factory） 
- 工厂方法模式（Factory Method）
- 抽象工厂模式（Abstract Factory）

##### 02：简单工厂模式 ：

​	建立一个工厂类，对实现了同一接口（Product）的一些类进行实例的创建

###### 优点：

1.  一个调用者如果想创建一个对象，只要知道其名称就可以了 

###### 缺点：

1. 扩展性太差，如若添加一产品就需要修改Factory，违背了开闭原则

###### 应用：

​	数据库访问

```java
public class Factory {
	private Product product;
	public Factory(String str) {
		switch (str) {
			case "A":
				product = new ProductA();
				break;
			case "B":
				product = new ProductB();
				break;
		}
	}
	public void send() {
		product.fun();
	}
  public static void main(String[] args) {
		// 告诉工厂你想要创建对象的类型
    Factory factory = new Factory("A"); 
    factory.send();
  }
}
```

##### 03：工厂方法模式 ：(静态还有非静态)

通过方法实现，一个方法返回一个对象，修改太多，违反了开闭原则

```java
public class Factory {
  public static Product createProA() {
    return new ProductA();
  }
  public static Product createProB() {
    return new ProductB();
  }
  public static void main(String[] args) {
    // 创建对象调用相应的方法
    Factory product = Factory.createProA();
    product.fun();
  }
}
```

##### 03：抽象工厂模式：

​	超级工厂接口是负责创建一个相关对象的基类工厂，每个实现接口的工厂类都能按照工厂模式创建对象；

工厂模式，类的创建依赖工厂类，也就是说，如果想要拓展程序，必须对工厂类进行修改，这违背了开闭原则，创建多个工厂类，这样一旦需要增加新的功能，直接增加新的超级工厂的实现类就可以了，不需要修改之前的代码

```java
public interface Factory {
	public abstract Product send();
}

public class FactoryA implements Factory {
	@Override
	public Product create() {
		return new ProductA();
	}
}

public class FactoryB implements Factory {
	@Override
	public Product create() {
		return new ProductB();
	}
  
  public static void main(String[] args) {
    Factory factory = new FactoryA();
    Product product = factory.create();
    // 调用对应的对象的功能
    product.fun();
  }
}
```