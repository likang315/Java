### Lombok

------

- Lombok的目的是减少代码的重复编写，底层采用字节码技术ASM，修改字节码文件，生成get，set方法，每次修改类属性时，不需要重新生成get set方法，从而在开发过程中简化代码的开发。
- lombok在编译的时候修改字节码文件（底层使用字节码技术），因此我们只需要在开发时安装lombok插件，线上环境由于是已经编译的，所以不再需要安装lombok。

##### 1：引入依赖

```xml
<!-- https://mvnrepository.com/artifact/org.projectlombok/lombok -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.8</version>
    <scope>provided</scope>
</dependency>
```

##### 2：注解使用

1. ###### @Getter、@Setter

   - 用于类或属性上
   - 注解在类上，为所有属性添加get，set方法、注解在属性上为该属性提供get，set方法
   - 生成的getter遵循布尔属性的约定
     - 例如：boolean类型的sex,getter方法为 isSex 而不是 getSex

2. ###### @Data

   - 使用在**类**上
   - 它结合了@ToString，@EqualsAndHashCode， @Getter和@Setter。本质上使用 @Data 注解，类默认@ToString和 @EqualsAndHashCode ，以及每个字段都有@Setter 和 @getter
   
3. @NonNull

   - 使用在**属性**上
   - 用于属的非空检查，当放在setter方法的字段上，将生成一个空检查，如果为空，则抛出 NullPointerException 
   - 该注解会默认是生成一个无参构造

4. @toString

   - 使用在**类**上
   - 默认生成任何非简单字段以名称-值的形式输出
   - 如果需要可以通过注释**参数 includeFieldNames** 来控制输出中是否包含的属性名称（key）
   - 可以通过**exclude参数**中包含字段名称，可以从生成的方法中排除特定字段
   - 可以通过callSuper参数控制使用调用父类的ToString（）

5. ###### @EqualsAndHashCode

   - 使用在**类**上
   - 该注解在类级别注释会同时生成 equal 和 hashCode 方法

   - 存在继承关系需要设置 callSuper参数为 true

6. ###### @AllArgsConstructor

   - 使用在**类**上
   - 该注解提供一个全参数的构造方法，默认不提供无参构造

7. ###### @NoArgsConstructor

   - 使用在**类**上，该注解提供一个无参构造

8. ##### @Slf4j

   - private  final Logger logger = LoggerFactory.getLogger(当前类名.class); 
   - 默认生成一个log；

9. @Value

   - 使用在**类**上
   - 生成包含所有参数的构造方法，get 方法，equals、hashCode、toString 方法，**注意：没有setter** 
   
10. @SneakyThrows

   - 处理Exception的常见手段就是外面包一层RuntimeException，接着往上丢;
   - 该注解会将异常抛出，value=“异常”；

   ```java
   try {
     
   } catch(Exception e) { 
     throw new RuntimeException(e);
   }
   ```

11. @UtilityClass

    - 将所有的方法声明为static的；
    - 同时创建了一个私有构造方法，调用该构造会抛出异常；

12. @Accessors(chain = true)

    - 使用链式创建对象；
    - user.setName("").setAge("");

    