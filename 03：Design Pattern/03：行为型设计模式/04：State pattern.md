### 状态模式（State Pattern）：行为型模式

------

[TOC]

##### 01：概述

- 状态模式中，我们**创建表示各种状态的对象**和**一个行为随着状态对象改变而改变的 context** ；

###### 目的：

- **对象的行为依赖于它的状态，并且可以根据状态的改变而改变它的相关行为；**

###### 示例：

- 饮水机满桶，空桶时，有水和无水；


###### 优点： 

​	将所有与某个状态有关的行为放到一个类中，并且可以方便地增加新的状态，只需要改变对象状态即可改变对象的行为

##### 02：示例

```java
public interface State {
    void press();
}
// 满桶
public class FullState implements State {

    @Override
    public void press() {
        System.out.println("Water is pouring!");
    }
}
// 空桶
public class NullState implements State {

    @Override
    public void press() {
        System.out.println("There is not water poured!");
    }
}
// 饮水机
public class WaterDispenser {
    private static int capacity = 20;
  	// 状态对象调用对应的行为
    private static State state;

    public WaterDispenser(State state) {
        this.state = state;
    }
		// 设置状态
    private static void setState(State state) {
        this.state = state;
    }

    public void press() {
        capacity--;
        if (capacity <= 0) {
            setState(new NullState());
        }
        state.press();
    }
   
   public static void main(String[] args) {
        WaterDispenser dispenser = new WaterDispenser(new FullState());
        for (int i = 0; i < 100; ++i) {
            dispenser.press();
        }
   }
}
```

