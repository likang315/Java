### 观察者模式（Observer Pattern)：行为型模式

------

[TOC]

##### 01：概述

  对象间的一种一对多的依赖关系，当**一个对象的状态发生改变**时，**所有依赖于它的对象都得到通知并被自动更新**

###### 注意：

1. 观察者知道自己观察那个对象 （拉）
2. 被观察者知道有那几个对象观察我（**双向问题**） （推）

###### 两个角色： 

- 观察者：在得到被观察者数据改变时，通知更新自己
- 被观察者角色：在具体被观察者的内部状态改变时，要向集合中的所有观察者发出通知

##### 02：示例

```java
public abstract class Subject {
   protected List<Observer> list = new ArrayList<>();
	 public abstract void add(Observer obs);
   public void notifyObserver() ；
}
// 被观察者
public class ConcreteSubjectA extends Subject {
  private int x;
  private int y;
  public ConcreteSubjectA(){
  }
  public ConcreteSubjectA(int x, int y) {
    super();
    this.x = x;
    this.y = y;
  }
  @Override
  public void add(Observer obs) {
    list.add(obs);
  }
	// 通知观察自己的所有观察者
  @Override
  public void notifyObserver() {
    for(Observer obs:list) {
      obs.update(x, y);
    }
  }

  public void dataChange(int x,int y) {
    this.x=x;
    this.y=y;
    notifyObserver();
  }
}

// 观察者
public abstract class Observer {
  private Subject s;
  public Observer() {
  }

  public Observer(Subject s) {
    this.s = s;
    s.add(this);
  }
  public abstract void update(int x,int y);
}

public class ConcreteObserverA extends Observer {
  private int x;
  private int y;
  public ConcreteObserverA(Subject sub) {
    super(sub);
  }

  @Override
  public void update(int x, int y) {
    this.x=x;
    this.y=y;
  }

  public void print() {
    System.out.println("x:"+x);
    System.out.println("y:"+y);
  }
}
```



