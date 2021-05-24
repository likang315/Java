### Spring 的循环依赖

------

[TOC]

##### 01：什么是循环依赖

- 从字面上来理解就是**A依赖B的同时B也依赖了A**；

- 相互依赖

  ```java
  @Component
  public class A {
      // A中注入了B
      @Autowired
      private B b;
  }
  
  @Component
  public class B {
      // B中也注入了A
      @Autowired
      private A a;
  }
  ```

- 自我依赖

- ```java
  // 自己依赖自己
  @Component
  public class A {
      // A中注入了A
  	@Autowired
  	private A a;
  }
  ```

##### 02：什么情况下循环依赖可以被处理？

- Spring解决循环依赖是**有前置条件的**

  1. 出现循环依赖的**Bean必须要是单例**
  2. 依赖注入的方式**不能全是构造器注入的方式**（两个相互依赖的类都是通过构造器注入）

- 为了测试依赖解决情况和注入方式的关系，我们做以下四种测试

  | 依赖情况               | 依赖注入方式                                                 | 循环依赖是否被解决 |
  | :--------------------- | ------------------------------------------------------------ | ------------------ |
  | AB相互依赖（循环依赖） | 均采用setter方法注入                                         | 是                 |
  | AB相互依赖（循环依赖） | 均采用构造器注入                                             | 否                 |
  | AB相互依赖（循环依赖） | A中注入B的方式为setter方法，B中注入A的方式为构造器           | 是                 |
  | AB相互依赖（循环依赖） | A中注入B的方式为构造器，B中注入A的方式为setter方法，创建Bean时，是按照自然排序进行创建的； | 否                 |


##### 03：Spring 是如何解决循环依赖的？

- 关于循环依赖的解决方式应该要**分两种情况来论述**
  1. 简单的循环依赖（没有AOP）
  2. 结合了AOP的循环依赖
- 首先，我们知道**Spring在创建Bean的时候默认是按照自然排序来进行创建的，**所以第一步Spring会去创建A。
- 其次，Spring在创建Bean的过程中分为三步
  1. 实例化，对应方法：`AbstractAutowireCapableBeanFactory`中的`createBeanInstance`方法
  2. 属性注入，对应方法：`AbstractAutowireCapableBeanFactory`的`populateBean`方法
  3. 初始化，对应方法：`AbstractAutowireCapableBeanFactory`的`initializeBean`

###### 简单的循环依赖

- ![](https://github.com/likang315/Middleware/blob/master/15%EF%BC%9ASpringBoot/photos/Spring%E5%BE%AA%E7%8E%AF%E4%BE%9D%E8%B5%96.png?raw=true)

  1. 调用getBean( ) 方法。

     1. 创建一个新对象；
     2. 从缓存中获取已经被创建的对象；

  2. 首先调用`getSingleton(a)`方法，这个方法又会调用`getSingleton(beanName, true)`

     ```java
     public Object getSingleton(String beanName) {
         return getSingleton(beanName, true);
     }
     ```

     `getSingleton(beanName, true)`这个方法**实际上就是到缓存中尝试去获取Bean，整个缓存分为三级**

     1. `singletonObjects`，一级缓存，存储的是所有创建好了的单例Bean；
     2. `earlySingletonObjects`，二级缓存，存储的是完成实例化但还未进行属性注入及初始化的对象；
     3. `singletonFactories`，三级缓存，提前暴露的一个单例工厂；

     因为A是第一次被创建，所以不管哪个缓存中必然都是没有的，因此会进入`getSingleton`的另外一个重载方法`getSingleton(beanName, singletonFactory)`。

  3. 调用getSingleton(beanName, singletonFactory)

     - 创建完成后将对象添加到一级缓存singletonObjects中；

  4. 调用addSingletonFactory方法

     - 将对象添加到三级缓存中，再开始进行属性的注入，初始化Bean；

  5. 当**A完成了实例化并添加进三级缓存后，就要开始为A进行属性注入了，在注入时发现A依赖了B，那么这个时候Spring又会去`getBean(b)**`，然后反射调用setter方法完成属性注入；

  6. 因为B需要注入A，所以在创建B的时候，又会去调用`getBean(a)`，这个时候就**又回到之前的流程了，但是不同的是，之前的`getBean`是为了创建Bean，而此时再调用`getBean`不是为了创建了，而是要从缓存中获取，因为之前A在实例化后已经将其放入了三级缓存`singletonFactories`中**；

  7. 注入到B中的A是通过`getEarlyBeanReference`方法提前暴露出去的一个对象，还不是一个完整的Bean；

     - 实际上就是调用了后置处理器的`getEarlyBeanReference`，而真正实现了这个方法的后置处理器只有一个，就是通过`@EnableAspectJAutoProxy`注解导入的`AnnotationAwareAspectJAutoProxyCreator`；
     - 不在考虑AOP 的情况下，**直接将实例化阶段创建的对象返回**；

  8. 从图中看出，虽然在**创建B时会提前给B注入了一个还未初始化的A对象，但是在创建A的流程中一直使用的是注入到B中的A对象的引用，之后会根据这个引用对A进行初始化**，所以注入到B的中的对象A是完整的；

###### 结合AOP的循环依赖

- org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor#getEarlyBeanReference

- 实现该方法的后置处理器只有一个，就是通过`@EnableAspectJAutoProxy`注解导入的`AnnotationAwareAspectJAutoProxyCreator`

- ```java
  public Object getEarlyBeanReference(Object bean, String beanName) {
      Object cacheKey = getCacheKey(bean.getClass(), beanName);
      this.earlyProxyReferences.put(cacheKey, bean);
      // 如果需要代理，返回一个代理对象，不需要代理，直接返回当前传入的这个bean对象
      return wrapIfNecessary(bean, beanName, cacheKey);
  }
  ```

###### 目前为止Spring也不能确定这个Bean有没有跟别的Bean出现循环依赖。

1. 在给B注入的时候为什么要注入一个代理对象？

   - 当我们**对A进行了`AOP`代理**时，**说明我们希望从容器中获取到的就是A代理后的对象而不是A本身**，因此把A当作依赖进行注入时也要注入它的代理对象;

2. 明明初始化的时候是A对象，那么**Spring是在哪里将代理对象放入到容器中**的呢？

   - 在B完成初始化后，Spring又调用了一次`getSingleton`方法，这一次传入的参数又不一样了，**false可以理解为禁用三级缓存**，在为B中注入A时已经**将三级缓存中的工厂取出，并从工厂中获取到了一个对象放入到了二级缓存中**，所以这里的这个方法就是**从二级缓存中获取到这个代理后的A对象**。`exposedObject == bean`可以认为是必定成立的。

   - ```java
     protected Object doCreateBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
         throws BeanCreationException {
         // ...
         // A是单例的，mbd.isSingleton()条件满足
         // allowCircularReferences：这个变量代表是否允许循环依赖，默认是开启的，条件也满足
         // isSingletonCurrentlyInCreation：正在在创建A，也满足
         // 所以earlySingletonExposure=true
         boolean earlySingletonExposure = (mbd.isSingleton() && 
                                           this.allowCircularReferences &&
                                           isSingletonCurrentlyInCreation(beanName));
         // 无论如何还是会通过三级缓存提前暴露一个工厂对象
         if (earlySingletonExposure) {
             if (logger.isTraceEnabled()) {
                 logger.trace("Eagerly caching bean '" + beanName +
                              "' to allow for resolving potential circular references");
             }
             addSingletonFactory(beanName, () -> getEarlyBeanReference(
                 beanName, mbd, bean));
         }
     
         // 从二级缓存中获取到代理后的Bean
         if (earlySingletonExposure) {
             Object earlySingletonReference = getSingleton(beanName, false);
             if (earlySingletonReference != null) {
                 if (exposedObject == bean) {
                     // 替换陈代理对象添加到一级缓存中
                     exposedObject = earlySingletonReference;
                 }
             }
         }
     
     }
     ```

     

3. 初始化的时候是对A对象本身进行初始化，而容器中以及注入到B中的都是代理对象，这样不会有问题吗？

   - 不会，这是因为不管是`cglib`代理还是`jdk`动态代理**生成的代理类，内部都持有一个目标类的引用，当调用代理对象的方法时，实际会去调用目标对象的方法**，A完成初始化相当于代理对象自身也完成了初始化。

4. 三级缓存为什么要使用工厂而不是直接使用引用？换而言之，**为什么需要这个三级缓存，直接通过二级缓存暴露一个引用不行吗？**

   - 这个工厂的目的**在于延迟对实例化阶段生成对象的代理，只有真正发生循环依赖的时候，才去提前生成代理对象**，否则只会创建一个工厂并将其放入到三级缓存中，但是不会去通过这个工厂去真正创建对象。
   - 生成代理对象的时机不对，若不使用三级缓存，在实例化时就生成了该对象的代理.

##### 04：总结

- Spring是如何解决的循环依赖？

  答：Spring通过**三级缓存**解决了循环依赖，其中**一级缓存为单例池（`singletonObjects`）,二级缓存为早期曝光对象`earlySingletonObjects`，三级缓存为早期曝光对象工厂（`singletonFactories`）。**当A、B两个类发生循环引用时，在A完成实例化后，就使用实例化后的对象去创建一个对象工厂，并添加到三级缓存中，如果A被AOP代理，那么通过这个工厂获取到的就是A代理后的对象，如果A没有被AOP代理，那么这个工厂获取到的就是A实例化的对象。当A进行属性注入时，会去创建B，同时B又依赖了A，所以**创建B的同时又会去调用getBean(a)来获取需要的依赖，此时的getBean(a)会从缓存中获取，第一步，先获取到三级缓存中的工厂；第二步，调用对象工工厂的getObject方法来获取到对应的对象，得到这个对象后将其注入到B中**。紧接着B会走完它的生命周期流程，包括初始化、后置处理器等。当B创建完后，会将B再注入到A中，此时A再完成它的整个生命周期。





