### 设计模式： 一般认为有 23 种

##### 设计原则：

- 开闭原则：对扩展开放，对修改关闭
- 高内聚，低耦合：尽量提高模块的聚合度，尽量降低模块之间的联系（尽量减少类之间的依赖性）
- 里氏代换原则：任何基类可以出现的地方，子类一定可以出现

##### 分为三类：

###### 创建型模式（4种）：

​	供了一种在创建对象的同时隐藏创建逻辑的方式，而不是使用 new 运算符直接实例化对象

- 单例模式（Singleton Pattern）
- 工厂模式（Factory Pattern） 和   抽象工厂模式（Abstract Factory Pattern）
- 建造者模式（Builder Pattern）
- 原型模式（Prototype Pattern）

###### 结构性模式（8种）：

​	关注类和对象的组合，被用来组合接口和定义组合对象获得新功能的方式

- 适配器模式（Adapter Pattern）
- 代理模式（Proxy Pattern）
- 装饰模式（Decorator Pattern）
- 桥接模式（Bridge Pattern）
- 过滤器模式（Filter、Criteria Pattern）
- 组合模式（Composite Pattern）
- 外观模式（Facade Pattern）
- 享元模式（Flyweight Pattern）

###### 行为型模式（12种）：

​	关注对象之间的通信

- 策略模式（Strategy Pattern）
- 模板模式（Template Pattern
- 责任链模式（Chain of Responsibility Pattern）
- 备忘录模式（Memento Pattern）
- 观察者模式（Observer Pattern）
- 访问者模式（Visitor Pattern）
- 中介者模式（Mediator Pattern）
- 命令模式（Command Pattern）
- 解释器模式（Interpreter Pattern）
- 迭代器模式（Iterator Pattern）
- 状态模式（State Pattern）
- 空对象模式（Null Object Pattern）