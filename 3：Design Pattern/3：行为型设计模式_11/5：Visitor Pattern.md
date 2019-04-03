### 访问者模式（Visitor Pattern）：使用了一个访问者类，它改变了被访问者类的执行算法，这种类型的设计模式属于行为型模式

**访问者的本质是预留通路，回调实现**

目的：数据结构与数据操作分离
应用场景：需要对一个对象结构中的对象进行很多不同的并且不相关的操作，而需要避免让这些操作"污染"这些对象的类，使用访问者模式将这些封装到类中

Visitor：访问者接口，为所有访问者申明访问元素的 visit 方法
ConcreteVisitor：具体的访问者实现对象，实现要真正被添加到对象结构中的功能
Interviewee：被访问者接口，定义被访问者允许访问的方法
ConcreteInterviewee：具体被访问的对象，通常会回调访问者的真实功能，同时开放自身的数据供访问者使用ObjectStructure：对象结构，通常包含多个被访问的对象，让访问者访问他的所有元素

```java
package com.visitor;
//被访问者
public interface Interviewee {
	public void accept(Visitor v);
	public void fun();
}

package com.visitor;
public class Book implements Interviewee {
@Override
public void fun() {
	System.out.println("访问Book的fun....");
}
@Override
public void accept(Visitor v) {
	v.visit(this);
}
}

package com.visitor;
public class Article implements Interviewee {
@Override
public void accept(Visitor v) {
	v.visit(this);
}
@Override
public void fun() {
	System.out.println("访问Article的fun....");
}
}

--------------------------------------------------------------------------------
package com.visitor;
public interface Visitor {
	public void visit(Interviewee i);
}

package com.visitor;
public class Visitor1 implements Visitor {
@Override
public void visit(Interviewee in) {
	in.fun();
}

}

package com.visitor;

import java.util.ArrayList;

  //被访问者集合,一访多
  public class ManagerInterviewee {
  private ArrayList<Interviewee> arraylist=new ArrayList<Interviewee>();

  public void add(Interviewee in) {
  	arraylist.add(in);
  }

  public void request(Visitor v) {
  	for(Interviewee arr:arraylist)
  	{
  		arr.accept(v);
  	}
  }
  }


package com.visitor;

public class Client {
	public static void main(String[] args) {
		Book book=new Book();
		Article article=new Article();
		ManagerInterviewee mie=new ManagerInterviewee();
		Visitor1 visitor1=new Visitor1();
		
		mie.add(book);
		mie.add(article);
		mie.request(visitor1);
	}
}
```


