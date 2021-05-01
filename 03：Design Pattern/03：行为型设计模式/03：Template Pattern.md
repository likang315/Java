### 模板模式（Template Pattern）：行为型模式

------

[TOC]

##### 01：概述

​	一个**抽象类公开定义了执行它的方法的模板**，它的子类可以按自己的需要重写方法实现，但调用将以抽象类中定义的方式进行；

###### 目的：

​	**定义功能流程的骨架**，而将一些**具体的实现步骤下沉到子类中**，模板方法使得子类可以不改变一个算法的结构

###### 优点： 

- **封装不变部分，扩展可变部分**
- 提取公共代码，便于维护
- **行为由父类控制，子类实现**

###### 缺点：

​	每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大

**注意：**为防止恶意操作，**一般模板方法都加上 final 关键词**

##### 02：示例

```java
// 游戏设计
public abstract class Game {
   abstract void initialize();
   abstract void startPlay();
   abstract void endPlay();
 
   // 模板
   public final void play(){
      //初始化游戏
      initialize();
      //开始游戏
      startPlay();
      //结束游戏
      endPlay();
   }
}

public class Cricket extends Game {
 
   @Override
   void endPlay() {
      System.out.println("Cricket Game Finished!");
   }
   @Override
   void initialize() {
      System.out.println("Cricket Game Initialized! Start playing.");
   }
   @Override
   void startPlay() {
      System.out.println("Cricket Game Started. Enjoy the game!");
   }
}
```

