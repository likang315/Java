#### 责任链模式（Chain of Responsibility Pattern）：为请求创建了一个接收者对象的链表，每个接收者都包含对另一个接收者的引用，当一个对象不能处理该请求，那么它会把相同的请求传给下一个接收者，并且沿着这条链传递请求，直到有对象处理它为止，属于行为型模式

角色：

?	1：抽象请求处理者

?	2：具体请求处理者

?	3：请求发送者，客户端

应用：servlet 的过滤器filter

```java
public abstract class Base {
private Base next;	
public Base() {
}
public Base(Base t) {
	this.next=t;
}

public abstract void method();
public void fun() {
	if(next!=null)
		next.method();
}
}


public class A1 extends Base {
	public A1() {
		super();
	}
	public A1(Base t) {
		super(t);
	}
    @Override
    public void method() {
        System.out.println("class A1 实现...");
        fun();
    }	
}

public class A2 extends Base {
public A2(){
}
public A2(Base t) {
	super(t);
}
@Override
public void method() {
	System.out.println("class A2  实现...");
	fun();
}
}

public class Client {
	public static void main(String[] args) {
		Base a2= new A2();
		Base a1= new A1(a2);
		a1.method();
	}
}


```












