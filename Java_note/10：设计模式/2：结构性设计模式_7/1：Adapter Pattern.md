##### 适配器模式（Adapter Pattern）：将某个类的接口转换成客户端期望的另一个接口表示，消除接口不匹配的类的兼容性问题、属于结构型模式，它结合了两个独立接口的功能

优点：
	 1、可以让任何两个没有关联的类一起运行
	 2、提高了类的复用
	 3、增加了类的透明度
	 4、灵活性好

分为（3）：
   1：类适配器：继承
   2：对象适配器：关联
   3：接口适配器：当我们需要接口的部分方法，而不是全部需要，我么可以用抽象类去继承它，重写部分方法，再去继承抽象类

##### 1：类适配器

```java
public interface Type_c {
    public void transfer_1();
}

public interface Andriod {
    public void transfer_2();

}

public class Type_imp implements Type_c{

@Override
public void transfer_1()
{
    System.out.println("Type_c:传输数据");
}

}

public class Adapter extends Type_imp implements Andriod {

@Override
public void transfer_2()
{
    System.out.println("Andriod:传输数据.......");
}

}

public class Main {
    public static void main(String[] args)
    {
        Adapter adapter=new Adapter();
        adapter.transfer_1();
        adapter.transfer_2();
    }
}
```



##### 2：对象适配器

```java
public class Adaptee {
	public void method() {
		System.out.println("具体实现...");
	}	
}

public class Adapter {
	private Adaptee adaptee;
	

public Adapter() {
	adaptee=new Adaptee();
}

public void method() {
	adaptee.method();
}	

public static void main(String[] args) {
	Adapter adapter=new Adapter();
	adapter.method();
}

}
```



## 

### 3：接口适配器：需要使用接口的几个方法时，如果全部实现，代码太臃肿，如果使用抽象类就可以选择性实现你需要的方法

```java
public interface Usb {
    public void transfer_01();
    public void transfer_02();
}

/**

- 使用抽象类，空方法体，可以让实现类有选择的实现
  */
  public abstract class Usb_abs implements Usb{
  @Override
  public  void transfer_01(){}

  @Override
  public  void transfer_02(){}
  }

public class Usb_absImp extends Usb_abs {

@Override
public void transfer_01()
{
    System.out.println("选择实现transfer_01...........");
}

}

public class Main {
    public static void main(String[] args)
    {
        new Usb_absImp().transfer_01();
    }
}

```


