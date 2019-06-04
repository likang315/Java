### 策略模式（Strategy Pattern）：属于行为型模式

​	定义了一系列算法，并将每个算法封装起来，使他们可以相互替换，且算法的切换使用不会影响到使用者

###### 实现：

​	设计一个接口，为一系列实现类提供统一的方法，多个实现类实现该接口，使具体事物与其特性相分离，降低耦合度

###### 三种角色

- 抽象策略角色：抽象的角色，通常情况下使用接口或者抽象类去实现
- 具体策略角色：包装了具体的算法和行为
- 环境角色：内部会持有一个抽象角色的引用，给客户端调用

###### 策略模式的缺点：

- 策略类会增多
- 所有策略类都需要对外暴露

```java
public interface FlyBehavior {
	public abstract void fly();
}

public class SuperSonic implements FlyBehavior {

  @Override
  public void fly() {
    System.out.println("超音速算法实现....");
  }
}

public class SubSonic implements FlyBehavior {

  @Override
  public void fly() {
    System.out.println("低音速算法实现...");
  }
}

public class Flane {
  private FlyBehavior flybehavior;
  public Flane(FlyBehavior t) {
    flybehavior = t;
  }

  public void fly() {
    flybehavior.fly();
  }
  public static void main(String[] args) {
  	new Flane(new SubSonic()).fly();
  }
}
```









