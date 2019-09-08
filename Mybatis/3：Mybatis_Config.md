### mybatis-config-xml ：配置文件

------

##### 1：资源(properties) ：获取资源文件的值

###### properties文件：

​	在项目中配置数据库信息时，通常会将数据库信息单独写入一个jdbc.properties文件中，方便后续的维护

```xml
<properties resource="jdbc.properties">
	<property name="name" value="${name}"/>
</properties>
```

###### 属性进行了多次配置时：

​	方法参数传递的属性具有最高优先级，resource/url 属性中指定的配置文件次之，最低优先级的是properties 属性中指定的属性

##### 2：设置(Setting)

​	一般设置全局配置参数，比如开启二级缓存，开启延迟加载等

```xml
<settings>
  <!--开启二级缓存-->
	<setting name="cacheEnabled" value="true">
</settings>
```

##### 3：类型别名(typeAliases)：两种

- 别名不区分大小写，用来减少类完全限定名的冗余 
- 用于让 mapper.xml 中的参数找到对应类，如：parameterType="test"

1. <typeAlias alias="Author" type="com.xzy.main.Author"/>
2. <package name="com.xupt.entity">
   - 指定一个包名，MyBatis 会在包名下面搜索需要的 Java Bean，每一个在包中的Java Bean，在没有注解的情况下会使用 Bean 的**首字母小写的非限定类名**来作为它的别名,若类上有注解，则别名为其注解值
   - @Alias("author")

```xml
<!--类型命名-->
<typeAliases>
	<typeAlias alias="Tutor" type="com.mybatis3.domain.Tutor"/>
	<package name="com.mybatis3.domain"/>
</typeAliases>
```

##### 4：类型处理器(typeHandler) 

​	将参数从 javaType 转化为 jdbcType 或者从数据库取出结果时把 jdbcType 转化为 javaType

###### 自定义 TypeHandler

- 自定义类型处理器必须实现TypeHandler  或者继承BaseTypeHandler类，并且重写四个方法
- 配置 mybatis-config.xml

1. ```xml
   <!--类型处理器-->
   <typeHandlers>
    	<typeHandler JavaType="com.xupt.sex" jdbcType="int" handler="com.xupt.SexHandler"/>
   </typeHandlers>
   ```

- 使用 TypeHandler

  ```xml
  <resultMap type="student" id="studentMap">
    	<result column="stu_sex" property="Sex" javaType="string" jdbcType="INT"
    	typeHandler="com.xupt.SexHandler"/>
   </resultMap>
  ```

- 自定义Typehandler示例

```java
/**
 * @Author: likang315
 * @Date: 2019-06-16 10:45
 * @Github: https://github.com/likang315
 * @Description: TypeHandler
 */
@MappedJdbcTypes(value={JdbcType.INT})
@MappedTypes(value={Sex.class})
public class StringTypeHandler implements TypeHandler<Sex> {
    /**
     * TypeHandler，PreparedStatement操作数据库的参数传递方式
     *
     * @param preparedStatement
     * @param i
     * @param s
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, String s, 						JdbcType jdbcType) throws SQLException {
      	// s 为JavaType转换为JdbcType存储
        preparedStatement.setInt(i, Integer.parseInt(s));
    }

    /**
     * 根据列名取值
     *
     * @param resultSet
     * @param s
     * @return
     * @throws SQLException
     */
    @Override
    public Sex getResult(ResultSet resultSet, String s) throws SQLException {
        Sex.setValue(resultSet.getInt(s));
        return null;
    }

    /**
     * 根据列的序列号取值
     *
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    @Override
    public Sex getResult(ResultSet resultSet, int i) throws SQLException {
        Sex.setValue(resultSet.getInt(i));
        return null;
    }

    /**
     * 通过列号取值,给存储过程的
     *
     * @param callableStatement
     * @param i
     * @return
     * @throws SQLException
     */
    @Override
    public Sex getResult(CallableStatement callableStatement, int i) throws SQLException
    {
       	callableStatement.getInt(i);
        return null;
    }
}
```

##### 5：枚举类型 TypeHandler

​	MyBatis 内部提供了两个转化枚举类型的 typeHandler

- org.apache.ibatis.type.EnumTypeHandler
  - 使用枚举字符串名称作为参数传递的
- org.apache.ibatis.type.EnumOrdinalTypeHandler
  - 使用整数下标作为参数传递的

##### 6：对象工厂(ObjectFactory)

​	当 MyBatis 构建一个结果返回时，都会**使用（ObjectFactory）去构建 POJO**，需要做的仅仅是实例化目标类，要么通过默认无参的构造方法，要么在参数映射存在的时候通过有参的构造方法来实例化

- 默认的：org.apache.ibatis.reflection.factory.DefaultObjectFactory

##### 7：配置环境（environments）

​	environments 可以注册多个数据源（DataSource），尽管可以配置多个环境（数据源），每个 SqlSessionFactory 实例只能选择其一，选择环境：default="id"

###### dataSource 属性：

​	配置数据源连接信息，type 属性(数据源类型)是提供我们对数据库连接方式的配置

###### dataSource 数据源分为两大部分：

- 数据源的配置
- 数据库事务(transactionManager)的配置

###### Type 属性（三种数据源类型）

- JNDI：JNDI 数据源，org.apache.ibatis.datasource.jndiDataSourceFactory 来获取数据源
- POOLED： 数据库连接池，org.apache.ibatis.datasource.pooled.PooldDataSource 实现
- UNPOOLED：非连接池数据库，每次连接数据库的时候都会创建一个新的连接，使用MyBatis提供的org.apache.ibatis.datasource.unpooled.UnpooledDataSource

###### transactionManager：配置数据库事务

- JDBC：采用 JDBC 方式管理事务，使用 JDBC 的提交和回滚设置，它依赖于从数据源得到的连接来管理事务
- MANAGED：采用容器方式管理事务， 默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将 closeConnection 属性设置为 false 来阻止它默认的关闭行为

```xml
<transactionManager type="MANAGED">
  <property name="closeConnection" value="false"/>
</transactionManager>
```

##### 8：使用注解配置Mapper接口

​	MyBatis 支持使用注解来配置映射语句,当我们使用基于注解的映射器接口时，则不再需要mappe文件

- @Insert
- @Update
- @Delete
- @Select 

```java
@Insert({"INSERT INTO user (id, username, passwd) VALUES (#{id}, #{username},#{passwd})"})
int insertUser(User user);
```

