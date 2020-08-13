### Spring profile：上下文环境

------

​	Spring 容器中所定义的Bean的一组逻辑名称，只有当这些Profile被激活的时候，才会将Profile中所对应的Bean注册到Spring容器中，开发时可以定义不同的配置文件

###### 使用场景：

​	3个配置文件，一个用于开发、一个用户测试、一个用户生产，其分别对应于3个Profile。当在实际运行的时候，只需给定一个参数来激活对应的Profile即可，那么容器就会只加载激活后的配置文件

##### 1：配置方式

- @Profile("数据库名")
  - 在Java config 类上配置
- 基于XML文件的配置，配置不同的配置文件

```xml
<!-- 定义开发的profile，主要是配置数据库的一些配置-->
<beans profile="dev">
  <!-- 只扫描开发环境下使用的类 -->
  <context:component-scan base-package="com.spring.profile.service.dev" />
  <!-- 加载开发使用的配置文件 -->
  <util:properties id="config" location="classpath:dev/config.properties"/>
</beans>

<!-- 定义生产使用的profile -->
<beans profile="prod">
  <!-- 只扫描生产环境下使用的类 -->
  <context:component-scan base-package="com.spring.profile.service.produce" />
  <!-- 加载生产使用的配置文件-->
  <util:properties id="config" location="classpath:produce/config.properties"/>
</beans>
```

##### 2：激活 Profile

Spring 在确定哪个 profile 处理激活状态时，需要依赖AbstractEnvironment中两个独立的属性：

- spring.profiles.active
- spring.profiles.default
- 若设置spring.profiles.active,那么它的值优先起作用，在没有spring.profiles.active属性值时,会查找spring.profiles.default，如果都没有设置，就不会激活 profile

###### 设置以上两个属性的方式：

- 在集成测试类上用@ActiveProfiles（"dev"）注解
-  作为 DispatcherServlet 的初始参数，在web.xml中的 <servlet>标签中配置

```xml
<init-param>
  <param-name>spring.profiles.default</param-name>
  <param-value>dev</param-value>
</init-param>
```

- 作为 web 应用上下文的参数

```xml
<context-param>
		<param-name>spring.profiles.default</param-name>
		<param-value>dev</param-value>
</context-param>
```

