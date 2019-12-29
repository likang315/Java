### Java 8 新特性

------

### Lambda	

​	允许把函数作为一个方法的参数（函数作为参数传递进方法中），传入一个动作

- （参数）-> 方法体   作为某个方法的参数;
- 一个lambda由用**逗号分隔的参数列表、–>、函数体**三部分表示;
- 解决匿名内部类复杂的操作而出现，因为匿名内部类需要依赖接口，实现回调方法；

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
- 可选的返回关键字：如果主体只有一个表达式返回值则编译器会**自动返回值**，大括号需要指定明表达式返回了一个数值
  
  - ```
    (String s) -> System.out.print(s)   只打印，不返回任何值
    ```

##### 2：变量作用域

- lambda 表达式中引用的局部变量，尽量被定义为final 的局部变量；

  ```java
  final String separator = ",";
  Arrays.asList("a", "b", "d").forEach( ( String e ) -> System.out.print(e + separator));
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
- 默认方法和抽象方法的区别是抽象方法必须要被实现，默认方法不是。作为替代方式，接口可以提供一个默认的方法实现，所有这个接口的实现类都会通过继承得倒这个方法（如果有需要也可以重写这个方法）

```java
public interface ABTest {
		// 此方法无用，无法调用
    static void ATest () {
        System.out.println("test...");
    }
  	// 实现类可以重写，调用
    default void BTest() {
        System.out.println("test default...");
    }
}
```

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

- 将要处理的元素集合看作一种流， 流在管道中传输， 并且可以在管道的节点上进行处理， 比如筛选， 排序，聚合等；

- ```java
  public interface Stream<T> extends BaseStream<T,Stream<T>>
  +--------------------+       +------+   +------+   +---+   +-------+
  | stream of elements +-----> |filter+-> |sorted+-> |map+-> |collect|
  +--------------------+       +------+   +------+   +---+   +-------+
  ```

1. 生成流

   - 集合接口提供两个方法生成流
      - **stream()** − 为集合创建串行流；
      - **parallelStream()** − 为集合创建并行流；

2. Stream操作被分为中间操作和终点操作

   - 中间操作：返回一个新的Stream，这个操作是由延迟的；
   -  终点操作：比如说forEach或者sum会遍历Stream从而产生最终结果或附带结果。终点操作执行完之后，Stream管道就被消费完了，不再可用。在几乎所有的情况下，**终点操作都是完成对数据的遍历操作**。

3. ###### void forEach(Consumer<? super T> action)

   - 接收一个 Consumer 接口，它只接收不参数，没有返回值。然后在 Stream 的每一个元素上执行该表达式

   ```java
   Random random = new Random();
   // 产生Integer Stream -> 限制10个元素 -> 遍历输出 -> 当前元素调用
   random.ints().limit(10).forEach(System.out::println);
   ```

4. ###### Stream<T>  filter (Predicate<? super T> predicate) 

   - 用于通过设置的条件过滤出元素
   - filter 中的过滤条件返回`true`代表当前元素会**保留下来**；

   ```java
   List<String> str = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl"); 
   // 获取空字符串的数量 
   long count = str.stream().filter(s -> s.isEmpty()).count();
   ```

5. ###### removeIf  

   - Collection 的方法，移除 过滤条件为 true  的会移除掉

   ```java
   List<String> strings = new ArrayList<>();
   List<String> str = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
   // 当调用Arrays.asList()方法时，返回值并非我们常用的java.util.ArrayList，而是java.util.Arrays.ArrayList，此类虽然也继承了AbstractList抽象类，但是并没有去实现remove
   strings.addAll(str);
   // 移除为空的字符串
   strings.removeIf(s -> "".equals(s));
   System.out.println(strings);
   ```

6. ###### Stream<R> map(Function<? super T, ? extends R> mapper);

   - map 方法接收一个功能型接口，功能型接口接收一个参数，返回一个值，返回的是新的流;
   - 用于将旧数据转换后变为新数据，每个输入元素按照规则转换成另一个元素；
   - 该方法是 Intermediate【中间】 操作；

   ```java
   Stream<String> stream = Stream.of("a", "b", "c", "d");
   stream.map(String::toUpperCase).forEach(System.out::println);
   ```

7. ###### Stream<T> limit(long maxSize)

   - 对流的元素数量进行截取指定数量的；

8. ###### Stream<T> sorted(Comparator<? super T> comparator)

   - 根据传入的外比较器进行比较；

   ```java
   // 传入的必须是JavaBean
   List<Student> sortDesList = list.stream()
       .sorted(Comparator
       .comparing(Student::getAge)
       .reversed())
       .forEach(System.out::println);
   // 内比较器，实现类
   List<Student> sortList1 = list.stream()
       .sorted((a, b) -> a.getAge().compareTo(b.getAge()))
       .collect(Collectors.toList());
   ```

9. ###### <R, A> R collect(Collector<? super T, A, R> collector)

   - 收集器，终端归约操作，产生最终的结果；
   - java.util.stream.Collector 是一个收集函数的接口, 声明了一个收集器的功能；

   ```java
   List<String> filtered = str.stream()
   	.filter(string -> !string.isEmpty())
     .collect(Collectors.toList());
   ```

10. ###### 按照某种规则进行分组

   - ```java
     final Map< Status, List< Task > > map = tasks
         .stream()
         .collect(Collectors.groupingBy(Task::getStatus));
     System.out.println(map);
     ```

11. Stream的方法**onClose** 返回一个等价的有额外引用的Stream，当Stream的close（）方法被调用的时候这个句柄会被执行

    ```java
    final Path path = new File( filename ).toPath();
    try(Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
        lines.onClose(() -> System.out.println("Done!")).forEach( System.out::println );
    }
    ```

##### Collector

- Collector是专门用来作为Stream的collect方法的参数的；

##### Collectors

- 工具类，内部提供了多种Collector，直接拿来使用；

  - **toCollection：**将流中的元素全部放置到一个集合中返回，这里使用Collection，泛指多种集合；

  - **toList：**将流中的元素放置到一个列表集合中去。这个列表默认为ArrayList；

  - **toSet：**将流中的元素放置到一个无序集set中去。默认为HashSet；

  - **toMap：**根据给定的键生成器和值生成器生成的键和值保存到一个map中返回，可以指定出现重复键时的处理方案和保存结果的map；

  - ```java
    // 指定键和值的生成方式keyMapper和valueMapper
    Map<String,String> map = list.stream().limit(3).collect(Collectors
    		.toMap(e -> e.substring(0,1),
               e -> e));
    // 在上面方法的基础上增加了对键发生重复时处理方式的mergeFunction，比如上面的默认的处理方法就是抛出异常
    Map<String,String> map1 = list.stream().collect(Collectors
    		.toMap(e -> e.substring(0,1),
               e -> e,
               (a,b) -> b));
    // 在第二个方法的基础上再添加了结果Map的生成方法
    Map<String,String> map2 = list.stream().collect(Collectors
    		.toMap(e -> e.substring(0,1),
               e -> e,
               (a,b) -> b,
               HashMap::new));
    ```

  - **joining：**将流中的元素全部以字符序列的方式连接到一起，可以指定连接符，甚至是结果的前后缀；

  - ```java
    String sss = list.stream().collect(Collectors.joining("-","S","E"));
    ```

  - **groupingBy ：**分组

  - ```java
    // 只需一个分组参数classifier，内部自动将结果保存到一个map中，每个map的键为?类型（即classifier的结果类型），值为一个list，这个list中保存在属于这个组的元素
    Map<Integer,List<String>> s = list.stream().collect(
      Collectors.groupingBy(String::length));
    // 在上面方法的基础上增加了对流中元素的处理方式的Collector，比如上面的默认的处理方法就是Collectors.toList()
    Map<Integer,List<String>> ss = list.stream().collect(Collectors.groupingBy(String::length, Collectors.toList()));
    // 在第二个方法的基础上再添加了结果Map的生成方法
    Map<Integer,Set<String>> sss = list.stream().collect(
      Collectors.groupingBy(
        String::length,
        HashMap::new,
        Collectors.toSet()));
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

  













