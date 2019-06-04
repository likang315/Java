##### 观察者模式（Observer Pattern)：定义对象间的一种一对多的依赖关系，当一个对象的状态发生改变时，所有依赖于它的对象都得到通知并被自动更新，属于行为型模式

注意： 1：观察者知道自己观察那个对象 2：被观察者知道有那几个对象观察我（只一个双向连的问题）

四个角色： 抽象被观察者角色：一个抽象接口，它把所有对观察者对象的引用保存在一个集合中，每个被观察者都可以有任意数量的观察者可以增加和删除观察者角色,一般用一个抽象类和接口来实现

抽象观察者角色：为所有的具体观察者定义一个接口，在得到被观察者数据改变时通知时更新自己具体被观察者角色：在具体被观察者的内部状态改变时，要向集合中的所有观察者发出通知

具体观察者角色：实现抽象观察者角色所需要的更新接口，通知被观察者我观察了你

应用：监听器（Listener）

```java
package com.observer;

public abstract class Subject {
	public abstract void add(Observer obs);
}

package com.observer;

import java.util.ArrayList;

public class ConcreteSubjectA extends Subject {
	private int x;
	private int y;
	private ArrayList<Observer> list=new ArrayList<Observer>();
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

package com.observer;

public abstract class Observer {
	private Subject s;
	public Observer() {
		

}
public Observer(Subject s) {
	this.s=s;
	s.add(this);
}
public abstract void update(int x,int y);

}

package com.observer;

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

package com.observer;

public class client {
	public static void main(String[] args) {
		ConcreteSubjectA cs1=new ConcreteSubjectA();
		ConcreteObserverA cb1=new ConcreteObserverA(cs1);
		

	ConcreteObserverA cb2=new ConcreteObserverA(cs1);//多个观察者
	cs1.dataChange(3, 5);
	cb1.print();
	cb2.print();
}

}
```



