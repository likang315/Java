##### 策略模式（Strategy Pattern）：定义了一系列算法，并将每个算法封装起来，使他们可以相互替换，且算法的变化不会影响到使用算法的客户，属于行为型模式

需要设计一个接口，为一系列实现类提供统一的方法，多个实现类实现该接口，使具体事物与其特性相分离，降低耦合度
策略模式多用在算法决策系统中，外部用户只需要决定用哪个算法即可

（三种角色）
抽象策略角色：抽象的角色，通常情况下使用接口或者抽象类去实现
具体策略角色：包装了具体的算法和行为
环境角色：内部会持有一个抽象角色的引用，给客户端调用

策略模式的优点：
	 算法可以自由切换；
	 避免使用多重条件判断；
	 扩展性良好。

策略模式的缺点：
	 策略类会增多
	 所有策略类都需要对外暴露

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
	flybehavior=t;
}

public void fly() {
	flybehavior.fly();
}

}

public class Helicopter extends Flane {

public Helicopter(FlyBehavior t) {
	super(t);
}

public static void main(String[] args) {
	Helicopter hp=new Helicopter(new SuperSonic());
	hp.fly();
}

}
```









