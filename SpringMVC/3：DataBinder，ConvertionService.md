### SpringMVC 数据绑定

------

![](/Users/likang/Code/Git/Java-and-Middleware/SpringMVC/SpringMVC/DataBinder.png)

##### 1：DataBinder 流程

1. ######     通过WebDataBinderFactory，产生DataBinder 实例

   - Spring MVC 框架将 ServletRequest 对象及目标方法的入参实例传递给 WebDataBinderFactory实例,以创建DataBinder实例

2. ###### 调用 ConversionService 

   - DataBinder 调用装配在 Spring MVC 上下文中的 ConversionService 组件进行数据类型转换、数据格式化工作,将Servlet 中的请求信息填充到入参对象中

3. ###### 调用Validator 进行校验

   - 调用 Validator 组件对已经绑定了请求消息的入参对象进行数据合法性校验，并最终生成数据绑定结果 BindingResult 对象

4. ###### BindingResult 中的入参对象和校验错误对象，将它们赋给处理方法的入参

   - Spring MVC 通过反射机制对目标处理方法进行解析，将请求消息绑定到处理方法的入参中

##### 2：ConvertionService

HttpMessageConvert<T> 和 ConversionService 是不同的两种东西，前者用于转换请求信息和响应信息，后者用于对象间的转换

1. ###### ConversionService ：类型转换的核心接口

   - boolean canConvert(Class sourceType, Class targetType)
     - 判断是否可以将一个 java 类转换为另一个 java 类
   - boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType)
     - 需转换的类将以成员变量的方式出现在宿主类中，TypeDescriptor 不但描述了需转换类的信息，还描述了从宿主类的上下文信息，如成员变量上的注解，成员是否是数组、集合或 Map 的方式呈现等
   - T convert(Object source, ClassT targetType)
     - 将原类型对象转换为目标类型对象
   - Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType)
     - 将对象从原类型对象转换为目标类型对象，此时往往会用到所在宿主类的上下文信息

2. ###### ConversionServiceFactoryBean

​	利用 ConversionServiceFactoryBean 在 Spring 容器中定义一个ConversionService，Spring 将自动识别出容器中的ConversionService，并在 Bean 属性配置及 Spring MVC 处理方法入参绑定等场合使用它进行数据的转换

######    ConversionServiceFactoryBean 的 converters 属性注册自定义的类型转换器

```xml
<!--配置自定义转换器-->
<bean id=conversionService
      class=org.springframework.context.support.ConversionServiceFactoryBean>
		<property name=converters>
			<list>
				<bean class="com.xupt.mvc.model.UserConverterbean" />
			</list>
		</property>
</bean>

<!--
		该标签默认注册 DefaultAnnotationHandlerMapping(映射器) 和
		ReqeustMappingHandlerAdpter（适配器）实现，还会注册一个默认的 ConversionService 即（FormattingConversionServiceFactoryBean）以满足大多数类型转换的需求
当用到自定义类型转换器时，需要 <mvc:annotation-driven conversion-service=”xxx”> 覆盖默认的
-->
<mvc:annotation-driven conversion-service=conversionService /> 
```


​	

### 3：Spring 自定义类型转换器

​	在org.springframework.core.convert.converter 包中定义了 4 种类型转换器接口，实现任意一个转换器接口都可以作为自定义转换器注册到 ConversionServiceFactroyBean 中

###### 	1：Converter<S,T>：将 S 类型对象转为 T 类型对象

​	2：ConverterFactory：将相同系列多个 “同质” Converter 封装在一起
​	3：GenericConverter：会根据源类对象及目标类对象所在的宿主类中的上下文信息进行类型转换

```java
Package org.springframework.core.convert.converter;
Public interface GenericConverter{
    Public SetConvertiblePair getConvertibleType();
    Object convert(Object source,TypeDescriptor sourceType,TypeDescriptor targetType);
}
```

ConvertiblePair 封装了源类型和目标类型，组成一个“对子”，而 TypeDescriptor包含了需转换类型对象所在宿主类的信息
 因些 GenericConverter 的 convert() 接口方法可以利用这些上下文信息进行类型转换工作
	4：带条件的通用转换器
          ConditionalGenericConverter 扩展于 GenericConverter 接口，并添加一个接口方法：
		Boolean matches (TypeDescriptor sourceType,TypeDescriptor targetType)										访接口方法根据源类型及目标类型所在宿主类的上下文信息决定是否要进行类型转换，只有该接口方法返回 true 时，才调用Object convert(Object source,TypeDescriptor sourceType,TypeDescriptor targetType)

**ConverstionServiceFactoryBean 的 converters 属性可以接受 Converter、ConverterFactory、GenericConverter 或** **ConditionalGenericConverter 接口的实现类，并把这些转换器的转换逻辑统一封装到一个 ConverstionService 实现类的对象中**
Spring 在 Bean属性配置及 Spring MVC 请求消息绑定时将利用这个 ConversionService 实例完成类型转换工作

例：将一个格式化的请求字符串直接转为 User对象，该字符串格式如：(namepwdrealname)

```xml
   <!-- 自定义转换器-->
    <bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">

        <property name="converters">
            <list>
                <bean class="com.aladin.web.controller.order.MsgConvert" />
            </list>
        </property>
    </bean>
```



### 4：@InitBinder 装配自定义编辑器，在控制器初始化的时候调用

​	SpringMVC 支持 JavaBeans 的 PropertyEditor,可以在控制器中使用@InitBinder添加自定义的编辑器，
​	也可以通过 WebBindingInitializer 装配全局范围内使用的编辑器：@InitBinder

​	由 @InitBinder 标识的方法，可以对 WebDataBinder 对象进行初始化WebDataBinder 是 DataBinder 的子类，用于完成由表单字段到 JavaBean 属性的绑定
​	规则：	
​		@InitBinder 方法不能有返回值，它必须声明为 void
​		@InitBinder 方法的参数通常是是 WebDataBinder

```java
//注册一个自定义的编辑器，添加在控制器中
public void initBinder(WebDataBinder binder) {
    //UserEditor 是实现了 PropertyEditor 接口的编辑器
    binder.registerCustomEditor(User.class, new UserEditor());
}
//自定义的编辑器
public class UserEditor extends PropertyEditorSupport {
@Override
public void setAsText(String text) throws IllegalArgumentException {
```

注意：Spring 4.0 可以使用 addCustomFormatter指定格式程序实现，这样就不需要实现一个PropertyEditor 的实例
​	Public void initBinder(WebDataBinder binder)
​	{
​		binder.addCustomFormatter(new DataFormatter(“yyyy-MM-dd”));
​	}



### 5：数据格式化  ( org.springframework.format ) 

从格式化的数据中获取真正的数据以完成数据绑定，并将处理完成的数据输出为格式化的数据，是 spring 的格式化框架

######    Formatter <T>

Public interface Formatter <T> extends Printer <T> , Parse<T> {}

###### 	Printer<T>：接口负责对象的格式化输出 

​		String print(T fieldValue , Locale locale)
​			将类型为 T 的成员对象根据本地化不同输出为不同的格式化字符串

###### 	Parse<T>：负责对象格式化输入

​		T  parse(String clientValue,Locale locale) throws ParseException
​			参考本地化信息将一个格式化的字符串转换为 T 对象，即完成格式化对象的输入

Spring 的 org.springframework.format.datetime 包中提供了一个用于时间对象格式化的 DateFormatter 实现类
Spring 的 org.springframework.format.number 包中提供了 3 个用于数字对象格式化的实现类
​	NumberFormatter：用于数字类型对象的格式化
​	CurrencyFormatter：用于货币类型对象的格式化
​	PercentFormatter： 用于百分数数字类型对象的格式化



### 6：注解驱动的属性对象格式化功能

在Bean 属性设置中,Spring MVC处理方法入参,数据绑定,模型数据输出时,自动通过注解应用格式化功能
AnnotationFormatterFactory<A extends Annotation>：接口 (org.springframework.format)
​			Set<Class<?>> getFieldTypes()
​				注解 A 的应用范围，即哪些属性类可以标注 A 注解
​			Parser<?> getParser(A annotation,Class<?> fieldType)
​				根据注解 A 获特定属性类型的Parser
​			Printer<?> getPreinter(A annotation,Class<?> fieldType):										根据注解 A 获取特定属性类型的 Printer

######  FormattingConversionServiceFactroyBean：内部已经注册了分别支持数字和日期类型注解驱动格式化

###### 1：NumberFormatAnnotationFormatterFactory: 支持对数字类型的属性使用

​	  @NumberFormat(pattern="#,###,####")

###### 2：JodaDateTimeFormatAnnotationFormatterFacroty: 支持对日期类型的属性使用

​	  @DateTimeFormat(pattern="yyyy-MM-dd")



### 7：启动数据转换和格式化功能

FormattingConversionService(class)  extends GenericConversionService implments ConversionService:
​		既具有类型转换功能，又具有格式化功能

在Spring 上下文中通过 FormattingConversionServiceFactoryBean 工厂类构造 FormattingConversionService
通过工厂类，既可以注册,删除自定义的转换器，还可注册自定义的注解驱动格式化逻辑功能

由于 FormattingConversionServiceFactoryBean 在内部会自动注册
NumberFormatAnnotationFormatterFactory 和JodaDateTimeFormatAnnotationFormatterFactory,因此装配了
FormattingConversionServiceFactoryBean 后，就可以在 Spring MVC 入参绑定及模型数据输出时使用注解驱动和格式化功能

```xml
在 Spring 上下文中装配 FormattingConversionServiceFactoryBean
<mvc:annotation-driven conversion-service="conversionService"/>
<bean id="conversionService" class="org.springframework.format.support.FormattingConversionServiceFactoryBean">
    <property name="converters">
        <list>
            <bean class="com.smart.domain.StringToUserConverter"/>
        </list>
    </property>
</bean>
```

注意：< mvc:annotation-driven/>  标签内部默认创建的 ConversionService 实例就是一个FormattingConversionServiceFactoryBean，装配好 FormattingConversionServiceFactoryBean后,SpringMVC 对处理方法的入参绑定就支持注解驱动功能了



### 8：< mvc:annotation-driven  /> ：自动注册这两个类

#### HandlerMapping的实现类的作用

实现类RequestMappingHandlerMapping，它会处理 @RequestMapping 注解，**并将其注册到请求映射表中**

#### HandlerAdapter的实现类的作用

实现类RequestMappingHandlerAdapter，则是处理请求的适配器，**确定调用哪个类的哪个方法**，并且构造方法参数，返回值




