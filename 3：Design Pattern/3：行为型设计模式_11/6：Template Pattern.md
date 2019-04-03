## 模板模式（Template Pattern）

一个抽象类公开定义了执行它的方法的模板，它的子类可以按自己的需要重写方法实现，但调用将以抽象类中定义的方式进行，属于行为型模式

### 目的：

定义一个操作中的流程的骨架，而将一些具体的实现步骤延迟到子类中，模板方法使得子类可以不改变一个算法的结构即可重定义该算法的某些特定步骤

**优点：** 1：封装不变部分，扩展可变部分

​	    2：提取公共代码，便于维护

​	    3：行为由父类控制，子类实现

**缺点：**每一个不同的实现都需要一个子类来实现，导致类的个数增加，使得系统更加庞大

**注意：**为防止恶意操作，**一般模板方法都加上 final 关键词**

```java
//游戏设计
public abstract class Game {
   abstract void initialize();
   abstract void startPlay();
   abstract void endPlay();
 
   //模板
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

public class TemplatePatternDemo {
   public static void main(String[] args) {
 
      Game game = new Cricket();
      game.play();
      System.out.println();
      game = new Football();
      game.play();      
   }
}
```

