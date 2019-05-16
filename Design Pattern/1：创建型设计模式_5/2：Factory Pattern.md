工厂模式：属于创建型模式 简单工厂模式（Simple Factory） 工厂方法模式（Factory Method） 抽象工厂模式（Abstract Factory）

##### 1：简单工厂模式 ：就是建立一个工厂类，对实现了同一接口（Product）的一些类进行实例的创建

优点： 1、一个调用者如果想创建一个对象，只要知道其名称就可以了 2、扩展性高，如果想增加一个产品，只要扩展一个工厂类就可以

缺点：扩展性太差，如若添加一产品就需要修改Factory,违背了开闭原则

应用：数据库访问

```
public interface Product {
	public abstract void fun();
}

public class ProductA implements Product{

@Override
public void fun() {
System.out.println("ProductA 实现！");
}

}

public class ProductB implements Product {

@Override
public void fun() {
	System.out.println("ProductB 实现！");
}

}

public class Factory {
	private Product product;
	public Factory(String str) {
		switch(str) 
		{
			case "A":
				product=new ProductA();
				break;
			case "B":
				product=new ProductB();	
				break;
		}
	}
	public void send() {
		product.fun();
	}

public static void main(String[] args) {
	Factory factory=new Factory("A"); //创建什么类型，传什么类型
	factory.send();
}

}
```

##### 2：工厂方法模式(静态还有非静态)

通过方法实现，一个方法返回一个对象,泛型

```
public class Factory {
private Product product;

public static  Product createProA() 
{
	return new ProductA();
}

public static Product createProB()
{
	return new ProductB();
} 

public static void main(String[] args) {
	Factory product=Factory.createProA();
	product.fun();
}

}
```

##### 2：抽象工厂模式：超级工厂接口是负责创建一个相关对象的工厂，不需要显式指定它们的类。每个实现接口的工厂类都能按照工厂模式创建对象

工厂模式，类的创建依赖工厂类，也就是说，如果想要拓展程序，必须对工厂类进行修改，这违背了开闭原则，创建多个工厂类，这样一旦需要增加新的功能，直接增加新的超级工厂的实现类就可以了，不需要修改之前的代码

```
public interface Product {
	public abstract void fun();
}

public class ProductA implements Product{

@Override
public void fun() {
System.out.println("ProductA 实现！");
}

}

public class ProductB implements Product {

@Override
public void fun() {
	System.out.println("ProductB 实现！");
}

}

public interface Factory {
	public abstract Product send();
}

public class FactoryA implements Factory {
	@Override
	public Product send() {
		return new ProductA();
	}
}

public class FactoryB implements Factory {
	@Override
	public Product send() {
		return new ProductB();
	}

public static void main(String[] args) {
	Factory factory=new FactoryA();
	Product product=factory.send();
	product.fun();
}

}
```