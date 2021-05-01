### 中介者模式（Mediator Pattern)：行为型模式

------

[TOC]

##### 01：概述

​	解藕，用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而降低偶合度

###### 主要解决：	

1. 对象与对象之间**存在大量的关联关系**，这样会导致系统的结构变得很复杂，同时若一个对象发生改变，我们也需要跟踪与之相关联的所有对象，同时做出相应的处理
2. 对象之间的连接增加会导致对象可复用性降低

##### 02：示例

```java
public abstract class Base {
  // 中介者
	protected Mediator mediator;
	public abstract void set(Mediator m);
	public abstract void send(String msg);
	public abstract void receive(String msg);
}
// 中介者
public class Mediator {
	private Base pboy;
	private Base pgirl; 
	public void set(Base pb,Base pg) {
		pboy = pb;
		pgirl = pg;
	}
  // 发送消息
	public void send(String msg,Base t) {
		if (t != pboy) {
			pboy.receive(msg);
		} else {
			pgirl.receive(msg);
		}
	}
}

public class Boy extends Base {
  @Override
  public void set(Mediator m) {
    mediator = m;
  }

  @Override
  public void send(String msg) {
    mediator.send(msg,this);
  }

  @Override
  public void receive(String msg) {
    System.out.println("Boy"+"收到啦");		
  }
}

public class Girl extends Base {
  @Override
  public void set(Mediator m) {
    mediator=m;
  }

  @Override
  public void send(String msg) {
    mediator.send(msg, this);
  }

  @Override
  public void receive(String msg) {
    System.out.println("Girl"+"收到啦");
  }
}


public class Client {
  public static void main(String[] args) {
    Boy boy=new Boy();
    Girl girl=new Girl();
    Mediator mediator=new Mediator();
    // 和中介者建立连接
    boy.set(mediator);
    girl.set(mediator);
    // 建立双向连接
    mediator.set(boy, girl);
    boy.send("I Love you ！");
  }

}
```