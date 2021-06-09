###  DataBinder（数据绑定）

------

[TOC]

##### 01：DataBinder 流程【重要】

![](https://github.com/likang315/Middleware/blob/master/14%EF%BC%9ASpringMVC/photos/DataBinder.png?raw=true)

1. **通过 WebDataBinderFactory，产生 DataBinder 实例**
   - Spring MVC 框架将 ServletRequest 对象及目标方法的入参实例传递给 WebDataBinderFactory 实例，以创建DataBinder实例；
   
2. 调用 **ConversionService** 
   - DataBinder 调用装配在 Spring MVC 上下文中的 ConversionService 组件进行数据**类型转换、数据格式化**工作，将 Servlet 中的请求信息填充到入参对象中；
   
3. **调用 Validator 校验**
   - 调用 Validator 组件对已经绑定了请求消息的入参对象进行数据合法性校验，并最终生成数据绑定结果 BindingResult 对象；
   
4. **BindingResult** 中的入参对象和校验错误对象，将它们**赋给处理方法的入参**；
- Spring MVC 通过**反射机制**对目标处理方法进行解析，将请求消息绑定到处理方法的入参中；

##### 02：ConvertionService

- HttpMessageConvert<T> 和 ConversionService 是不同的两种东西，前者用于转换请求信息和响应信息，后者将请求消息中一个对象转换为另一个我们需要的对象；


###### ConversionService ：类型转换的核心接口

- boolean canConvert(Class sourceType, Class targetType)
  - 判断是否可以将一个 java 类转换为另一个 java 类
- boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
  - 需转换的类将以成员变量的方式出现在宿主类中，TypeDescriptor 不但描述了需转换类的信息，还描述了从宿主类的上下文信息，如成员变量上的注解，成员是否是数组、集合或 Map 的方式呈现等
- T convert(Object source, ClassT targetType)
  - 将原类型对象转换为目标类型对象
- Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
  - 将对象从原类型对象转换为目标类型对象，此时往往会用到所在宿主类的上下文信息

###### ConversionServiceFactoryBean

- 利用 ConversionServiceFactoryBean 在 Spring 容器中定义一个ConversionService，Spring 将自动识别出容器中的ConversionService，并在 Bean 属性配置及 Spring MVC 处理方法入参绑定等场合使用它进行数据的转换；


```java
// 将请求信息中（多个对象）的一个对象转换为一个Location对象,自定义类型转换器
public class ConvertLocation implements Converter<String, Location> {
    @Override
    public Location convert(String str) {
        // 转换具体操作
        return null;
    }
}

@RequestMapping(value = "/")
public String action(@RequestParam("location")Location location){
    System.out.println(location.getLongitude());
    return "String";
}
```

##### 03：Convert

- 在 org.springframework.core.convert.converter 包中定义了 4 种类型转换器接口，实现**任意一个转换器接口都可以作为自定义转换器注册到 ConversionServiceFactroyBean 中；**

- Converter<S,T>：将 S 类型对象转为 T 类型对象；

- ConverterFactory：将相同系列多个 “同质” Converter 封装在一起；

- GenericConverter：会根据源类对象及目标类对象所在的宿主类中的上下文信息进行类型转换

  ```Java
  Public interface GenericConverter {
      Public SetConvertiblePair getConvertibleType();
      Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
  }
  ```
  
- ConditionalGenericConverter：带条件的通用转换器
  - Boolean matches (TypeDescriptor sourceType,TypeDescriptor targetType)									
    - 访接口方法根据源类型及目标类型所在宿主类的上下文信息决定是否要进行类型转换，只有该接口方法返回 true 时，才调用Object convert(Object source,TypeDescriptor sourceType,TypeDescriptor targetType)
    - Spring 在 Bean 属性配置及 Spring MVC 请求消息绑定时将利用这个 ConversionService 实例完成类型转换工作

##### 04：Formatter

- 从格式化的数据中获取真正的数据以完成数据绑定，并将处理完成的数据输出为格式化的数据，是 spring 的格式化框架；


######    Formatter <T>： 实现此接口，重写parse（）

- String print(T fieldValue ,  Locale locale)
  - 将类型为 T 的成员对象根据本地化不同输出为不同的格式化字符串
- T  parse(String clientValue, Locale locale) throws ParseException
  - 参考本地化信息将一个格式化的字符串转换为 T 对象，即完成格式化对象的输入

- NumberFormatter：用于数字类型对象的格式化
- CurrencyFormatter：用于货币类型对象的格式化
- PercentFormatter： 用于百分数数字类型对象的格式化

```java
public class MyFormatter implements Formatter<Date> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public String print(Date object, Locale arg1) {
        return dateFormat.format(object);
    }
    public Date parse(String source, Locale arg1) throws ParseException {
        return dateFormat.parse(source); // Formatter只能匹配这种格式的"yyyy-MM-dd"
    }
}
```

##### 05：对象格式化（注解）

- 在 Bean 属性设置中，Spring MVC处理方法入参，数据绑定，模型数据输出时，**自动通过注解应用格式化功能**；


###### AnnotationFormatterFactory

- Set<Class<?>> getFieldTypes( )
  - 注解 A 的应用范围，即哪些属性类可以标注 A 注解，可以像里面添加类型
- Parser< ?> getParser(A annotation,Class<?> fieldType)
  - 根据注解 A 获特定属性类型的Parser
- Printer< ?> getPrinter(A annotation,Class<?> fieldType)
  - 根据注解 A 获取特定属性类型的 Printer

######  FormattingConversionServiceFactroyBean

- NumberFormatAnnotationFormatterFactory: 支持对数字类型的属性使用
  - @NumberFormat(pattern="#,###,####")
- JodaDateTimeFormatAnnotationFormatterFacroty: 支持对日期类型的属性使用
  -  @DateTimeFormat(pattern="yyyy-MM-dd")

##### 06：FormattingConversionService

FormattingConversionService(class)  extends GenericConversionService implments ConversionService

- 既具有类型转换功能，又具有格式化功能；
- 在Spring 上下文中通过 FormattingConversionServiceFactoryBean 工厂类构造 FormattingConversionService
  通过工厂类，既可以注册，删除自定义的转换器，还可注册自定义的注解驱动格式化逻辑功能；

```xml
<mvc:annotation-driven conversion-service="conversionService"/>
<bean id="conversionService"
     class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="com.xupt.StringToUserConverter"/>
        </list>
    </property>
</bean>
```

##### 07：HandlerMapping、HandlerAdapter

###### HandlerMapping

- 实现类RequestMappingHandlerMapping，它会处理 @RequestMapping 注解，并将其注册到请求映射表中；


###### HandlerAdapter

- 实现类RequestMappingHandlerAdapter，则是处理请求的适配器，确定调用哪个类的哪个方法，并且构造，方法参数，返回值；
- HandlerMapping把配置的Controller，注册到请求映射表中，然后HandlerAdapter处理请求，确定调用哪一个Controller方法；