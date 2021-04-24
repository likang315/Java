### BeanUtils

------

##### 01：特性

1. Spring 的 BeanUtils 的 CopyProperties方法需要对应的属性有getter和setter方法，原理反射；
2. **如果存在属性完全相同的内部类**，但不是同一个内部类，即分别属于各自的内部类，则Spring会认为属性不同,**不会Copy;**
3. Spring和Apache得copy属性得方法源和目的参数得位置正好相反，所以导包和调用得时候需要注意以下；

##### 02：内部类copy失败

- 是一种语法糖，其实还是生成一个class文件：Entity$Inner.class;

##### 03：方法

- static void copyProperties(Object source, Object target) throws BeansException
  - 可能会抛出异常；
- static void copyProperties(Object source, Object target, String... ignoreProperties) throws BeansException
  - 可以选择那些属性忽略，不copy，传入属性名