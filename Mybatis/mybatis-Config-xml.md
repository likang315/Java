```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration  PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<!--配置-->
<configuration>
  
	<!--从propertise文件中取值-->
  <properties resource="application.properties">
    <property name="username" value="db_user"/>
    <property name="password" value="verysecurepwd"/>
  </properties>
  
  <!--全局设置-->
  <settings>
    <setting name="cacheEnabled" value="true"/>
    <setting name="lazyLoadingEnabled" value="true"/>
    <setting name="multipleResultSetsEnabled" value="true"/>
    <setting name="useColumnLabel" value="true"/>
    <setting name="useGeneratedKeys" value="false"/>
    <setting name="autoMappingBehavior" value="PARTIAL"/>
    <setting name="defaultExecutorType" value="SIMPLE"/>
    <setting name="defaultStatementTimeout" value="25000"/>
    <setting name="safeRowBoundsEnabled" value="false"/>
    <setting name="mapUnderscoreToCamelCase" value="false"/>
    <setting name="localCacheScope" value="SESSION"/>
    <setting name="jdbcTypeForNull" value="OTHER"/>
    <setting name="lazyLoadTriggerMethods" value="equals,clone,hashCode,toString"/>
  </settings>
  
  <!--类型别名-->
  <typeAliases>
    <typeAlias alias="Tutor" type="com.mybatis3.domain.Tutor"/>
    <package name="com.mybatis3.domain"/>
  </typeAliases>
  
  <!--类型处理器-->
  <typeHandlers>
    <typeHandler handler="com.mybatis3.typehandlers.PhoneTypeHandler"/>
    <package name="com.mybatis3.typehandlers"/>
  </typeHandlers>
  
  <!--配置环境-->
  <environments default="development">
    <environment id="development"><!--环境变量-->
      <transactionManager type="JDBC"/><!--事务管理-->
      <dataSource type="POOLED"><!--数据源-->
        <property name="driver" value="${jdbc.driverClassName}"/>
        <property name="url" value="${jdbc.url}"/>
        <property name="username" value="${jdbc.username}"/>
        <property name="password" value="${jdbc.password}"/>
      </dataSource>
    </environment>

    <environment id="production">
      <transactionManager type="JDBC"/>
      <dataSource type="JNDI">
        <property name="data_source" value="java:comp/jdbc/MyBatisDemoDS"/>
      </dataSource>
    </environment>
  </environments>

  <!--三种配置映射器方式-->
  <mappers>
    <mapper resource="com/mybatis3/mappers/StudentMapper.xml"/>
    <mapper url="file:///var/mappers/StudentMapper.xml"/>
    <mapper class="com.mybatis3.mappers.TutorMapper"/>
  </mappers>

</configuration>
```

