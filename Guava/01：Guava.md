### Guava

------

​	Guava是一种基于开源的Java库，提供用于集合，缓存，支持原语，并发性，常见注解，字符串处理，I/O和验证的实用方法，Java 8 已经实现其核心功能，以后只当个工具类使用，尽量使用Java 原生 API。

##### 1：导入jar包

```xml
<dependency>
  <groupId>com.google.guava</groupId>
  <artifactId>guava</artifactId>
  <version>18.0</version>
</dependency>
```

##### 2：Optional 类

- Java 8 已实现其功能，使用原生JDK

##### 3：Preconditions

- 提供静态方法来检查方法或构造函数，被调用是否给定适当的参数。它检查的先决条件。其方法失败抛出IllegalArgumentException

###### 方法：

- static void  checkArgument(boolean expression)
  - 检查涉及的一个或者多个参数时调用方法的表达式，若为false，抛出异常，若为true，正常；
- static void checkArgument(boolean expression, @Nullable Object errorMessage)
  - 检查参数表达式是否为true，否则抛出封装好errorMessage的非法参数异常，可通过getMessage() 获取；

##### 4：排序器

- Guava流畅风格比较器[Comparator]的实现，它可以用来为构建复杂的比较器，以完成集合排序的功能;
- Ordering 实例无非就是一个特殊的Comparator 实例，并且提供链式方法调用和加强现有的比较器；

###### 方法：

- **natural()：**使用Comparable类型的自然顺序， 例如：整数从小到大，日期按先后排序;
- **usingToString() ：**使用toString()返回的字符串按字典顺序进行排序；
- **from(Comparator)** ：使用已有的排序器排序

```java
 List<String> list = Lists.newArrayList("wang", "test", "a");
 Collections.sort(list, Ordering.usingToString());
```

###### 链式调用方法：通过链式调用，可以由给定的排序器衍生出其它排序器，当阅读链式调用产生的排序器时，应该从后往前读。

- reverse()

  - 获取语义相反的排序器

- nullsFirst、nullsLast

  - 使用当前排序器，把null值排到最前面
  - 注意：使用原生的JDKCollections.sort会出现异常的，如果排序元素为空

  ```java
  List<Integer> list = Arrays.asList(1, 5, null, 3, 8, 2);
  Collections.sort(list, Ordering.natural().nullsFirst());
  System.out.println(Arrays.toString(list.toArray()));
  ```

- compound(Comparator)

  - 符合排序器，当第一个排序器出现相等值时，通过第二个排序器排序
  -  Ordering.from(new idComparator()).compound(new nameComparator());

- Ordering<F> onResultOf(Function<F, ? extends T> function)

  - 对集合中元素调用Function，再按返回值用当前排序器排序

    ```java
    // Function 
    Ordering.natural().nullsFirst().onResultOf(new Function<Foo, String>() {
      public String apply(Foo foo) {
        return foo.sortedBy;
      }
    }); 
    ```

- isOrdered（）

  - 判断是否已按照指定排序器排序好

- min(Iterable<E> iterable)、max(Iterable<E> iterable)

  - 返回迭代器中最小、最大的元素。如果可迭代对象中没有元素，则抛出NoSuchElementExceptionl;

- leastOf(Iterator<E> elements, int k)

  - 获取可迭代对象中最小的k个元素

- greatestOf(Iterable<E> iterable, int k)

  - 获取可迭代对象中最大的k个元素

- List<E> sortedCopy(Iterable<E> elements)

  - 返回一个新的排序好的可变对象

- ImmutableList<E> immutableSortedCopy(Iterable<E> elements)

  - 返回一个新的排序好的不可变的对象



