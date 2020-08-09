### Unsafe 

------

###### 1：package sun.misc;

##### 	 public final class Unsafe 

​	用于在实质上扩展 Java 语言表达能力、便于在更高层（Java 层）代码里实现原本要在更低层（C 层）实现的核心库功能用的，可以直接操作内存的，包括对内存的申请、释放、访问

- Unsafe 被设计成单例模式，构造方法私有
- 非启动类加载器直接调用 Unsafe.getUnsafe() 方法会抛出 SecurityException 异常
- 它的设计就只被标准库使用，因此不建议在生产环境中使用

