### Java 8 新特性

------

### Lambda	

​	允许把函数作为一个方法的参数（函数作为参数传递进方法中），传入一个动作，比如：回调；

- （参数）-> 方法体   作为某个方法的参数;
- 一个lambda由用**逗号分隔的参数列表、–>、函数体**三部分表示;
- 解决匿名内部类复杂的操作而出现，因为匿名内部类需要依赖接口，实现回调方法；

```java
// PersonCallBack 为接口，callBack为接口定义的方法；
Person.create(1, "zhangsan", new PersonCallBack() {
  	// 代表一种动作  
  	@Override
    public void callBack(Person person) {
      	System.out.println("callback -- " +person.getName());
    }
}
              
public static void create(Integer id, String name, PersonCallBack personCallBack) {
    Person person = new Person(id, name);
    // 回调
    personCallBack.callBack(person);
}
```

##### 1：语法格式

```java
(parameters) -> expression
(parameters) ->{statements;}
```

- 可选类型声明：不需要声明参数类型，编译器会统一识别参数值
  
  - ```java
    Collections.sort(list, (x,y) -> x - y);
    ```
- 可选的参数圆括号：一个参数无需定义圆括号，但多个参数需要定义圆括号
  
  - ```
    x -> 2 * x  
    ```
- 可选的大括号：如果主体包含了一个语句，就不需要使用大括号
  
  - ```
    (int x, int y) -> x + y
    ```
- 可选的返回关键字：如果主体只有一个表达式返回值则编译器会自动返回值，大括号需要指定明表达式返回了一个数值
  
  - ```
    (String s) -> System.out.print(s)   只打印，不返回任何值
    ```

##### 2：变量作用域

- lambda 表达式中引用的局部变量，尽量被定义为final 的局部变量；

  ```java
  final String separator = ",";
  Arrays.asList("a", "b", "d" ).forEach( ( String e ) -> System.out.print(e + separator));
  ```

- Lambda 表达式不能修改局部变量；

- Lambda 表达式当中不允许声明一个与局部变量同名的参数或者局部变量；

- Lambda 表达式可能会有返回值，如果只有一行，不需要使用return，它会自动根据上下文识别返回值类型；

  ```java
  Arrays.asList("a", "b", "d").sort( (e1, e2) -> e1.compareTo(e2));
  Arrays.asList("a", "b", "d").sort( (e1, e2) -> {
    // 加了大括号，等价的  
    int result = e1.compareTo(e2);
      return result;
  });
  ```

### 函数式接口

##### 1：定义：函数式接口，只能有唯一的一个方法的接口

- 这种特性非常脆弱，只要多加一个方法就会编译失败，因此提供了一个**@FunctionalInterface**注解来显示的申明函数接口

- Java 8 提供了很多函数式接口：Java.util.function包下

  - Supplier：无参数，返回一个结果；
  - Consumer：接收一个参数，无返回结果；
  - Function：接收一个参数，返回一个结果；

  ```java
  @FunctionalInterface
  public interface Function<T, R> {
    	// 默认和static方法除外
      R apply(T t);
  }
  ```

##### 2：接口的默认方法和静态方法

- Java 8增加了两个新的概念在接口声明的时候：默认和静态方法；
- default 、static修饰的方法在接口中可以有方法体；
- static  修饰的接口中定义的方法不能被重写，但可以有方法体
- 默认方法和抽象方法的区别是抽象方法必须要被实现，默认方法不是，他。作为替代方式，接口可以提供一个默认的方法实现，所有这个接口的实现类都会通过继承得倒这个方法（如果有需要也可以重写这个方法）

### 方法的引用

##### 1：定义

​	方法引用是用来直接访问类或者实例中已经存在的方法或者构造方法

##### 2：使用场景

​	当Lambda表达式中只是执行一个方法调用时

##### 3：使用方式

- 使用一对 : :
- 无参构造方法：
  - 语法：Class::new，或者更一般的Class< T >::new
- 静态方法
  - 语法：Class::static_method_name
- 无参实例方法
  - 语法：Class::method
- 实例方法,只接受某个类型的参数
  - 语法：instance::method
- 当 lambda 调用的方法是父类中的方法或者是当前类中的方法时，可以使用 super::实例方法名 或 this::实例方法名

```java
final Car police = Car.create(Car::new);
// follow 只能是car类型
cars.forEach(police::follow);
```

### 重复注解

##### 1：定义：

​	允许相同注解在声明使用的时候重复使用超过一次，重复注解本身需要被@Repeatable注解，它只是编译器层面的改动

##### 2：反射的API提供一个新方法getAnnotationsByType() 来返回重复注释的类型

```java
public class RepeatingAnnotations {
    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    public @interface Filters {
        Filter[] value();
    }

    @Target( ElementType.TYPE )
    @Retention( RetentionPolicy.RUNTIME )
    @Repeatable( Filters.class )
    public @interface Filter {
        String value();
    };

    // 重复使用
    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    public static void main(String[] args) {
        // 返回使用的两个注解
        for(Filter filter: Filterable.class.getAnnotationsByType(Filter.class) ) {
            System.out.println(filter.value() );
        }
    }
}
```

##### 3：注解的扩展

扩展了注解的使用范围，现在注解基本上 可以用在任何地方

- ElementType.TYPE_USE：对类型的注解
- ElementType.TYPE_PARAMETERL：对参数的注解

### 参数名字保留在字节码文件里

- 使用反射API，Parameter.getName() 方法）和字节码里（使用java编译命令javac的**–parameters**参数）

- ```java
  public static void main(String[] args) throws Exception {
      Method method = ParameterNames.class.getMethod("main", String[].class );
      for(final Parameter parameter: method.getParameters()) {
        System.out.println("Parameter: " + parameter.getName());
      }
  }
  ```

### Java 类库的新特性

##### 1：Optional

​	一个容器，可以保存T，或者仅仅用于保存null，用于解决空指针异常，不赞成代码null检查污染

1. ###### 创建Optional 的方法

   - Optional.ofNullable(T value)； 返回一个Optional对象，value可以为空

2. ###### API

   - optional.orElse(T other)；如果Optional为null，则返回other
   -  optional.orElseGet(Supplier<? extends T> other)；如果Optional为null，则执行other并返回，一种回退机制
   -  optional.orElseThrow(Supplier<? extends X> exceptionSupplier)，如果Optional为null，则执行exceptionSupplier，并抛出异常

##### 2：Stream

1. ###### **Stream API (**java.util.stream)引入了在Java里可以工作的函数式编程

   ```java
   // 先Task集合会被转化为Stream表示，然后filter操作会过滤掉所有关闭的Task，接下来使用Task::getPoints 方法取得每个Task实例的点数，mapToInt方法会把Task Stream转换成Integer Stream，最后使用Sum方法将所有的点数加起来得到最终的结果
   final long totalPointsOfOpenTasks = tasks
       .stream()
       .filter(task -> task.getStatus() == Status.OPEN) 
       .mapToInt(Task::getPoints )
       .sum();
   ```

2. Stream操作被分为中间操作和终点操作

   - 中间操作：返回一个新的Stream，这个操作是由延迟的；
   - 终点操作：比如说forEach或者sum会遍历Stream从而产生最终结果或附带结果。终点操作执行完之后，Stream管道就被消费完了，不再可用。在几乎所有的情况下，**终点操作都是即时完成对数据的遍历操作**。

3. 按照某种规则进行分组

   - ```java
     final Map< Status, List< Task > > map = tasks
         .stream()
         .collect(Collectors.groupingBy(Task::getStatus));
     System.out.println(map);
     ```

4. Stream的方法**onClose** 返回一个等价的有额外引用的Stream，当Stream的close（）方法被调用的时候这个句柄会被执行

   ```java
   final Path path = new File( filename ).toPath();
   try(Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
       lines.onClose(() -> System.out.println("Done!")).forEach( System.out::println );
   }
   ```

### 并行数组

- Arrays  数组的工具类，提供了许多并行的方式提供效率；

  - static void`  `parallelSetAll(int[] array, IntUnaryOperator generator) 
    - 使用生成器对指定的数组进行填充；
  - static void  parallelSort(int[] a) 
    - 并行排序，升序

  ```java
  Arrays.parallelSetAll(arrayOfLong,index -> ThreadLocalRandom.current().nextInt( 1000000));
  Arrays.parallelSort(arrayOfLong);
  ```

  













