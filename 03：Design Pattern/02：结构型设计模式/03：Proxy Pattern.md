### 代理模式（Proxy Pattern）：属于结构型模式

------

[TOC]

##### 00：概述

- 某个代理类代表另一个类的功能，并且可以在不改动目标对象的基础上，增加对该功能的控制访问；

###### 目的：

- 为此对象提供一种代理，以**控制对这个对象的访问**；


###### 示例：	

​	Spring AOP

- 和适配器模式的区别：适配器模式主要改变所考虑对象的接口，而代理模式不能改变所代理类的接口
- 和装饰器模式的区别：装饰模式为了增强功能，而代理模式是为了加以控制

###### 分类：

1. 静态代理
2. 动态代理
3. Cglib 代理

##### 01：静态代理

​	在使用时，需要定义接口或者父类，**被代理对象（目标对象）与代理对象（Proxy）一起实现相同的接口**；

###### 缺点：

​	一旦接口增加方法，目标对象与代理对象都要维护，所以使用动态代理方式来解决；

```java
public interface Base {
    public abstract void method ();
}

public class Source implements Base {
    @Override
    public void method() {
        System.out.println("实现 Source 功能...");
    }
}

public class Proxy implements Base {
    private Base pbase;
    public Proxy(Base t) {
        pbase = t;
    }
    @Override
    public void method() {
        System.out.println("添加控制代理功能的逻辑 ");
        pbase.method();
    }
}

public class Main {
    public static void main (String[] args) {
        Proxy p = new Proxy(new Source()); // 传递代理对象，建立代理关系
        p.method();
    }
}
```

##### 02：动态代理（JDK代理）

1. 代理对象不需要实现接口
2. 代理对象的生成，是利用JDK的API，**动态的在内存中创建代理对象**；

代理类所在包：java.lang.reflect.Proxy JDK 实现代理只需要代理工厂使用 newProxyInstance 方法，但是该方法需要接收三个参数,完整的写法是:

- ClassLoader loader ：指定当前目标对象使用类加载器 
- Class<?>[] interfaces ：目标对象实现的接口的类型，使用泛型方式确认类型
-  InvocationHandler h ： 事件处理器

当代理对象调用真实对象的方法时，其会自动的跳转到代理对象关联的 handler 对象的 invoke 方法来进行调用

```java
public interface Email {
    public void receive();
    public void send();
}

public class FlushEmail implements Email{
    @Override
    public void receive() {
        System.out.println("receive Email......");
    }
    @Override
    public void send() {
        System.out.println("Send Email....");
    }
}

// 代理工厂，传入被代理对象返回代理对象
public class ProxyFactory {
    public static Object getProxy(Object obj){
        MyInvocation myHandler = new MyInvocation(obj);
        return 	Proxy.newProxyInstance(obj.getClass().getClassLoader(),
                                       obj.getClass().getInterfaces(),
                                       myHandler);
    }
}

// 动态代理必须实现 invovationHandler，事件处理器
public class MyInvocation implements InvocationHandler {
    // 代理的实际对象
    private Object obj;
    public MyInvocation(Object obj) {
        this.obj = obj;
    }
    // 代理对象被实际调用
    @Override
    public Object invoke(Object proxy, Method method, Object[] objects) throws Throwable {
        Object re = null;
        if("send".equals(method.getName())) {
            Logger.before();
            // 传入代理的实际对象和参数,调用原始方法
            method.invoke(obj, objects);
            Logger.after();
        } else {
            method.invoke(obj,objects);
        }
        return re;
    }
}

//添加业务逻辑
public class Logger {
    public static void before() {
        System.out.println("之前添加业务......");
    }
    public static void after() {
        System.out.println("之后添加业务.....");
    }
}
```

##### 03：Cglib 代理（继承）：

​	对指定的目标类生成一个子类，并覆盖其中方法实现增强，但因为采用的是继承，所以不能对 final 修饰的类进行代理

- Cglib代理，也叫作子类代理，它是**在内存中构建一个子类对象从而实现对目标对象功能的扩展**；

######  Cglib子类代理实现方法: 

1. 需要引入cglib 的 jar 包，但是Spring的核心包中已经包括了Cglib功能，所以直接引入Spring-core.jar 即可
2. 引入功能包后，就可以在内存中动态构建子类
3. 代理的类不能为 final，否则报错
4. 目标对象的方法如果为 final/static，那么就不会被拦截，即不会执行目标对象额外的业务方法；