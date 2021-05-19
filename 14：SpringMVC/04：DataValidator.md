### 数据校验（DataValidator）：JSR303

------

​	Spring MVC 开始支持 **JSR-303 验证框架**，JSR 303 **用于对 Java Bean 中的字段的值进行验证**，Hibernate Validator是实现了这一规范的，所以需要引入Jar包

##### 1：Spring MVC 配置

1. ###### 添加jar包
  
   - validation-api-1.0.0.GA.jar ：JSR-303 规范 API 包
   - hibernate-validator-4.3.0.Final.jar Hibernate 参考实现
   
   ```xml
   <dependency>
       <groupId>org.hibernate</groupId>
       <artifactId>hibernate-validator</artifactId>
       <version>6.1.5.Final</version>
   </dependency>
   <!--任选其一-->
   <dependency>
       <groupId>org.springframework.boot</groupId>
       <artifactId>spring-boot-starter-validation</artifactId>
   </dependency>
   ```
   
2. ###### 在 Spring xml 配置

```xml
<!--实例化validator-->
<bean id="validator"
      class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
	<property name="providerClass" value="org.hibernate.validator.HibernateValidator"/>
	<property name="validationMessageSource" ref="messageSource"/>
</bean>
```

此处使用 Hibernate validator 实现：

- validationMessageSource ：指定国际化错误消息从哪里取，此处使用之前定义的
- messageSource： 来获取国际化消息
  - 如果不指定该属性，则默认到classpath 下的 ValidationMessages.properties 取国际化错误消息

```xml
<!--通过 ConfigurableWebBindingInitializer 注册 validator-->
<bean id="webBindingInitializer"
      class="org.springframework.web.bind.support.ConfigurableWebBindingInitializer">
		<property name="conversionService" ref="conversionService"/>
		<property name="validator" ref="validator"/>
</bean>
```

######   3: 如上集成过程看起来比较麻烦

​		只要加入MVC namespace ，启动注解扫描即可，< mvc:annotation-driven /> 或者 @EnableWebMvc

##### 2：使用方式

​	把注解标记在需要验证的实体类的属性上或是其对应的get方法上即可，再在 Controller 处使用@Valid 注解验证其属性

```java
@NotNull(message="{username.not.null}")
private String username;
```

​	@NotNull 指定此 username 字段不允许为空，当验证失败时将从之前指定的messageSource 中，获取“username.not.empty”，对应的错误信息，此处只有通过"{错误消息键值}"格式指定的才能从 messageSource 获取

##### 3：验证Controller属性

```java
@Controller
@RequestMapping("/validate")
public class HelloWorldController {
    
  	@RequestMapping("/hello")
    public String validate(@Valid @ModelAttribute("user") UserModel user, Errors errors){
      if(errors.hasErrors()) {
        // ...
        return "validate/error";
      } else {
        // ...
        return "redirect:/success";
      }
    }
}
// @Valid 告知 Spring MVC 此命令对象在绑定完毕后需要进行JSR-303验证
// 如果验证失败，取错误信息，然后将错误信息添加到 errors 错误对象中
```

##### 4：约束注解

1. ######   验证约束注解 ( hibernate validator reference )

   - @AssertFalse：验证注解的元素值是 false
   - @AssertTrue：验证注解的元素值是 true
   - **@NotNull**：验证注解的元素值不是 null
   - @NotEmpty：检查元素是否为 Null 或 Empty
   - @Null：验证注解的元素值是 null
   - @Min(value=值)：验证注解的元素值大于等于@Min 指定的 value 值
   - @Max（value=值）：验证注解的元素值小于等于@Max 指定的 value 值
   - @Digits(integer=整数位数, fraction=小数位数) ：验证注解的元素值的整数位数和小数位数上
   - @Size(min=下限，max=上限）：验证注解的元素值的在 min 和 max（包含）指定区间之内，如字符长度、集合大小
   - @Range(min=最小值, max=最大值) ：验证数字是否在指定范围
   - @Pattern(regexp=正则表达式flag=标志的模式) ：用于验证字符串对象，验证是否符合指定的正则表达式
   - **@Valid ：**指定**递归验证**关联的对象(验证属性对象)	

2. ###### 错误信息

- 当验证出错时，需要向用户展示错误消息告知出错原因，因此要为验证约束注解指定错误信息
- 错误消息：通过在**验证约束注解的 message 属性指定**，验证约束注解指定错误消息有两种方式：
  1. 硬编码错误消息 @NotNull(message = "用户名不能为空")
  2. 从资源消息文件中根据消息键读取错误消息（ValidationMessages.properties ）



