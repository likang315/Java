### Interceptor

------

##### 01：Interceptor 

- 支持对Executor、StatementHandler、**PameterHandler**和ResultSetHandler 接口进行拦截，生成这四种对象的代理；
- Mybatis拦截器用到责任链模式 + 动态代理 + 反射机制；

##### 02：API

```java
public interface Interceptor {
	// 重写该方法进行拦截处理
    Object intercept(Invocation invocation) throws Throwable;

    default Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    default void setProperties(Properties properties) {  }
}
```

##### 03：敏感字段加解密

1. 利用注解标明需要加密解密的类对象的字段；
2. mybatis拦截Executor.class对象中的query，update方法；
3. 在方法执行前对parameter进行加密解密，在拦截器执行后，解密返回的结果；