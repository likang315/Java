### 中介者模式（Mediator Pattern)：降低多个对象和类之间的耦合度,属于行为型模式

  作用：用一个中介对象来封装一系列的对象交互，中介者使各对象不需要显式地相互引用，从而降低偶合度，而且可以独立地改变它们之间的交互

  主要解决：																																1：对象与对象之间存在大量的关联关系，这样会导致系统的结构变得很复杂，同时若一个对象发生改变，我们也需要跟踪与之相关联的所有对象，同时做出相应的处理

2：对象之间的连接增加会导致对象可复用性降低

```java
package com.mediator;
public abstract class Base {
	protected Mediator mediator;
	public abstract void set(Mediator m);
	public abstract void send(String msg);
	public abstract void receive(String msg);
}

package com.mediator;
public class Mediator {
	private Base pboy;
	private Base pgirl; 
	public void set(Base pb,Base pg) {
		pboy=pb;
		pgirl=pg;
	}
	public void send(String msg,Base t) {
		if(t!=pboy) 
		{
			pboy.receive(msg);
		}else
		{
			pgirl.receive(msg);
		}
	} 
	
}

package com.mediator;
public class Boy extends Base {

@Override
public void set(Mediator m) {
	mediator=m;
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

package com.mediator;
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

package com.mediator;
public class Client {

public static void main(String[] args) {
	Boy boy=new Boy();
	Girl girl=new Girl();
	Mediator mediator=new Mediator();
	boy.set(mediator);//和中介者建立连接
	girl.set(mediator);
	mediator.set(boy, girl);//建立双向连接
	boy.send("I Love you ！");
}

}
```





