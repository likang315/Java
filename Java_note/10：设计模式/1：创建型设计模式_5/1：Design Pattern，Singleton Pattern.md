### 1：设计模式：Java中一般认为有23种

   设计原则：
		1：开闭原则：对扩展开放，对修改关闭
		2：高内聚，低耦合：尽量提高模块的聚合度，尽量降低模块之间的联系（尽量减少类之间的依赖性）
		3：提高软件的重用性

分为三类：

######   创建型模式（5种）：单例模式，工厂模式(2种)，建造者模式，原型模式

?    这些设计模式提供了一种在创建对象的同时隐藏创建逻辑的方式，而不是使用 new 运算符直接实例化对象
?    这使得程序在判断针对某个给定实例需要创建哪些对象时更加灵活
?	

######   结构性模式（7种）：适配器模式，装饰模式，代理模式，外观模式，桥接模式，组合模式，享元模式

?    这些设计模式关注类和对象的组合。继承被用来组合接口和定义组合对象获得新功能的方式

######   行为型模式（11种）：策略模式，模板方法模式，观察者模式，迭代模式，责任链模式，命令模式，备忘录模式，状态模式，访问者模式，中介者模式，解释器模式

设计模式特别关注对象之间的通信



### 2：单例模式：用于加载全局变量时（6中，懒汉，饿汉各3种）

?     目的：保证一个类的只可创建一个对象
?     实现：
?	1：构造函数私有（类外不可创建对象，不可被继承）
?	2：类的static方法
?	
?	

###### 1：//懒汉式，对象是方法被调用时，才初始化，也叫做对象的延时加载

```java
/**
懒汉式，对象的延时加载,线程安全，同步代码块，双重检查Double-Check,效率高（双检锁）
*/
public class Singleton {
private volatile static Singleton instance=null;
public static Singleton getInstance()
{
	if(instance==null)
	{
		//只允许一个类的实例进入
		synchronized(Singleton.class)
		{
			if(instance==null)
			{
				instance=new Singleton();
			}
		}
	}
	return instance;
}   
```

在并发情况下，如果没有volatile关键字，在instance = new TestInstance(会出现出现问题，可以分解为3行伪代码
   1.memory = allocate()   //分配内存

      2. ctorInstanc(memory)   //初始化对象
      3. instance = memory  //设置instance指向刚分配的地址

```java
/**
线程安全，同步方法，效率太低
  */
  public class Singleton {
  private static Singleton singleton;
  public static synchronized Singleton getInstance() {
      if (singleton == null)
      {
          singleton = new Singleton();
      }
      return singleton;
  }
  }
```



 * ```java
    /**
    静态内部类，通过类加载机制，懒加载，调用方法才会装载，效率高
      */
      public class Singleton {
      private static class SingletonInstance {
          private static final Singleton INSTANCE = new Singleton();
      }
    
      public static Singleton getInstance() {
          return SingletonInstance.INSTANCE;
      }
      }
    ```

    



###### 2：//饿汉式，直接创建对象

```java
public class Singleton {
private static Singleton instance=new Singleton();
public static Singleton getInstance() {
	return instance;
}

public static void main(String[] args) {
	Singleton p = Singleton.getInstance();
}

}
```

 * ```java
    /**
    通过静态块，装载类的时候就加载了
      */
      public class Singleton {
      private static Singleton instance;
    
      static {
          instance = new Singleton();
      }
      public static Singleton getInstance() {
          return instance;
      }
      }
    ```

    

    ```java
    /**
    单元素枚举实现单例模式，不仅能避免反射问题，而且还自动支持序列化机制
    */
    public enum  EnumSingleton {
        INSTANCE;
        public EnumSingleton getInstance(){
            return INSTANCE;
        }
    }
    ```


### 枚举实现：

public final class T extends Enum{

}

enum有且仅有private的构造器，防止外部的额外构造，这恰好和单例模式吻合

对于序列化和反序列化，因为每一个枚举类型和枚举变量在JVM中都是唯一的，即Java在序列化和反序列化枚举时做了特殊的规定，枚举的writeObject、readObject、readObjectNoData、writeReplace和readResolve等方法是被编译器禁用的，因此也不存在实现序列化接口后调用readObject会破坏单例的问题
