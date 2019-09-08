### 访问者模式（Visitor Pattern）：属于行为型模式

------

​	使用了一个访问者类，它改变了被访问者类的执行算法，访问者的本质是**预留通路，回调实现**

- Visitor：访问者接口，为所有访问者申明访问元素的 visit 方法 
- ConcreteVisitor：具体的访问者实现对象，实现要真正被添加到对象结构中的功能 
- Interviewee：被访问者接口，定义被访问者允许访问的方法 和即将被访问的方法
- ConcreteInterviewee：具体被访问的对象，通常会回调访问者的真实功能，同时开放自身的数据供访问者使用
- ObjectStructure：对象结构，通常包含多个被访问的对象，让访问者访问他的所有元素

###### 访问者想访问被访问者的方法必须经过被访问者的允许后才可以访问 

```java
// 被访问者
public interface Interviewee {
	public void accept(Visitor v);
	public void fun();
}

public class Book implements Interviewee {
  @Override
  public void accept(Visitor v) {
    v.visit(this);
  }
  @Override
  public void fun() {
    System.out.println("访问Book的fun....");
  }
}

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

public interface Visitor {
	public void visit(Interviewee i);
}

public class Visitor1 implements Visitor {
  @Override
  public void visit(Interviewee in) {
    in.fun();
  }
}
// 被访问者集合,一访多
public class ManagerInterviewee {
  private List<Interviewee> arraylist = new ArrayList<>();

  public void add(Interviewee in) {
    arraylist.add(in);
  }
  
  public void request(Visitor v) {
    for(Interviewee arr:arraylist) {
      arr.accept(v);
    }
  }
}

public class Client {
    public static void main(String[] args) {
        Book book = new Book();
        Article article = new Article();
        ManagerInterviewee mie = new ManagerInterviewee();
        Visitor1 visitor1 = new Visitor1();

        mie.add(book);
        mie.add(article);
        mie.request(visitor1);
    }
}
```