##### 桥接模式（Bridge）降低抽象与实现之间的耦合度，使得二者可以独立发展，属于结构型模式

通过提供抽象化和实现化之间的桥接结构，让抽象部分和实现部分独立开来，分别定义接口，来实现二者的解耦 优点： 1：抽象和实现的分离 2：优秀的扩展能力 3：桥接模式将继承关系转化成关联关系，它降低了类与类之间的耦合度

```java
public interface Concrete{
	public abstract void method();
}
public class ConcreteA implements Concrete {
    @Override
    public void method() {
        System.out.println("实现一...");
    }
}

//抽象部分
public abstract class Abstraction {  //作为桥接类
	private Concrete pimp;
	public Abstraction(Concrete t) {
		pimp = t;
	}
	public  void method() {
		pimp.method();
	}
}

// 实现部分
public class AbstractionA extends Abstraction {

    public AbstractionA(Concrete t) {
        super(t);
    }	
    public static void main(String[] args) {
        AbstractionA p=new AbstractionA(new ConcreteA());
        p.method();
    }
}
```

