### 备忘录模式 (Memento Pattern)：属于行为型模式

------

​	在该对象之外，保存一个对象的某个状态，以便在适当的时候恢复对象

#### 作用:

在不破坏封装的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态，这样可以在以后将对象恢复到原先保存的状态

###### 三个角色：

- Originator(发起人)： 创建备忘录，记录对象的内部状态，恢复状态 
- Memento(备忘录)：存储了发起人对象状态 
- Caretaker（备忘录管理)： 管理备忘录

应用：GIT 版本控制

```java
// 备忘录
public class Memento {
  private String state;
  public Memento(String state){
    this.state = state;
  }
  public String getState(){
    return state;
  }  
}
//发起人
public class Originator {
   private String state;
   public void setState(String state){
      this.state = state;
   }

   public Memento createMemento(){
      return new Memento (this.state);
   }

   public void restoreMemento(Memento m){
      state = m.getState();
   }
   public void show() {
	   System.out.println("Current state:"+state);
   }
}
// 备忘录管理者
public class CareTaker {
  private List<Memento> List = new ArrayList<Memento>();
  public void add(Memento m){
    List.add(m);
  }
  public Memento get(int index){
    return List.get(index);
  }
}	

public class Client {
  public static void main(String[] args) {
    Originator originator = new Originator();
    CareTaker careTaker = new CareTaker();

    originator.setState("State 1");
    careTaker.add(originator.createMemento());
    originator.setState("State 2");
    careTaker.add(originator.createMemento());
    originator.setState("State 3");
    careTaker.add(originator.createMemento());

    originator.restoreMemento(careTaker.get(1));
    originator.show();
  }
}
```