### 原型模式（Prototype Pattern）：属于创建型模式

------

- 用于创建相同的对象，同时又能保证性能，通过拷贝一个现有对象生成新对象的
- 实现了一个原型接口，该接口用于创建当前对象的克隆，实现克隆操作

```java
//实现Cloneable 接口，重写 clone 方法
public class Prototype implements Cloneable {
    @Override
    public Object clone() throws CloneNotSupportedException {
      return super.clone();
    }
}

public class Demo extends Prototype {
	private int x;
	public Demo() {
		x = 0;
	}
	public Demo(int x) {
		this.x = x;
	}
}
public class Client {
	public static void main(String[] args) {
		Prototype p = new Demo(3);
		try { 
        // 克隆对象，返回Object对象，强转
        Demo p1 = (Demo)p.clone();
        System.out.println(p1.toString());
		} catch (CloneNotSupportedException e) {
      	System.out.println("clone 失败....");
 		}
	}
}
```