##### 装饰模式（Decorator)：对已经存在的某些类添加装饰，扩展了一些功能，同时又不改变其结构，属于结构型模式

是动态的，要求装饰对象（抽象成基类--装饰器）和被装饰对象实现同一个接口，装饰器持有被装饰对象的实例

目的：解决继承为类引入静态特征，并且随着扩展功能的增多，子类会很膨胀的问题

优点：装饰类和被装饰类可以独立发展，不会相互耦合，装饰模式是继承的一个替代模式，装饰模式可以动态扩展一个实现类的功能

```java
/**被装饰对象和装饰对象必须实现此接口
  */
  public interface Base {
  public void method();
  }

public class Product implements Base {
    @Override
    public void method()
    {
        System.out.println("实现其功能....");
    }
}

/** 抽象装饰器
  */
  public  abstract class Decorator implements Base {
  protected Base pbase;

  public Decorator(Base t) {
      pbase=t;
  }
  }

public class ConcreteDecorator extends Decorator {

public ConcreteDecorator(Base t)
{
    super(t);
}

@Override
public void method()
{
    pbase.method();
    newMethod();
}

public void newMethod()
{
    System.out.println("装饰的功能...");
}

}

public class Main {
    public static void main(String[] args)
    {
        ConcreteDecorator dec = new ConcreteDecorator(new Product()); //传被装饰对象
        dec.method();
    }
}
```