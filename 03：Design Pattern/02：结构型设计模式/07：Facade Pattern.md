### 门面（Facade Pattern）：属于结构型模式

------

[TOC]

##### 00：概述

​	又称外观模式，**隐藏系统内部的复杂性，并向客户端提供了一个友好接口**；

- 门面角色：正常情况下，本角色会将所有从客户端发来的请求委派到相应的子系统中去；

###### 优点：

​	在客户端和复杂系统之间再加一层，这一层将调用顺序、依赖关系等处理好，封装内部结构

##### 01：示例

```java
public interface Shape {
   void draw();
}
// 画正方形
public class Rectangle implements Shape {

   @Override
   public void draw() {
      System.out.println("Rectangle::draw()");
   }
}
// 画圆
public class Circle implements Shape {
 
   @Override
   public void draw() {
      System.out.println("Circle::draw()");
   }
}

// 向客户端展示的外观类，内部功能已经封装好
public class ShapeMaker {
   private Shape circle;
   private Shape rectangle;
   public ShapeMaker() {
      circle = new Circle();
      rectangle = new Rectangle();
   }
 
   public void drawCircle(){
      circle.draw();
   }
   public void drawRectangle(){
      rectangle.draw();
   }
}
```



