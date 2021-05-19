### SSM 框架的搭建：

------

​	由Spring，SpringMVC，Mybatis 的集成，分模块搭建，子父pom继承关系，抽取公共依赖，统一版本号 

- xupt_model：封装对应表的实体类
- xupt_dao：用来继承Myabtis,注意mapper.xml文件的加载，依赖xupt_model
- xupt_service：依赖dao层，被xupt_web依赖
- xupt_web ：依赖xupt_service层	
  - 三个xml文件：
    - springmvc.xml，包含 spring 和 mybatis 的配置文件,视图解析器,自动扫描controller
    - applicationContext.xml 配置文件sqlSessionFactoryBean，dataSource						      		      
    - web.xml：用于配置servlet，和filter(乱码过滤器)和servlet-mapping(映射器),context-param(加载环境)		
    - 此外有 2 个资源文件：jdbc.propertis 和 log4j.properties   

##### 1：依赖问题

- < dependencyManagment>		
  - < dependencies>			
    - < dependency>

1. 父模块只有一个pom.xml,再没有任何文件，因此<packing>pom</packing>，父pom.xml只是申明，没有引用，没有下载，子pom.xml继承时就会引用
2. 父POM：使用dependencyManagement统一项目范围中依赖的版本，当依赖版本在父POM中声明后，子模块在使用依赖的时候就无须声明版本 ，也就不会发生多个子模块使用版本不一致的情况，帮助降低依赖冲突的几率，如果子模块不声明依赖的使用，即使该依赖在父POM中的dependencyManagement中声明了，也不会产生任何效果
3. < properties>：用户可以自定义一个或多个Maven属性，然后在POM的其他地方使用**${属性名}**的方式引用该属性，这种做法的最大意义在于消除重复和统一管理

##### 2：Maven 打包

###### 自动解析mapper.xml文件：

- Mybatis 中接口和对应的 mapper 文件不一定要放在同一个包下，如果放在一起的目的是为了Mybatis进行自动扫描，并且要注意此时Java接口的名称和mapper文件的名称要相同，否则会报异常，由于此时Mybatis会自动解析对应的接口和相应的配置文件，所以就不需要配置 mapper.xml 文件的位置
- 在默认的情况下maven打包的时候，对于**src/main/java目录只打包源代码**，而不会打包其他文件，所以此时如果把对应的mapper文件放到src/main/java目录下时，不会打包到最终的jar文件夹中
-  在resource/mappers文件下的mapper.xml会自动被打包，而如果在同一包下需要重新定义Maven打包路径

##### 3：抽象controller基类成接口，方便写跳转的路径

##### 4：mybatis-plus：

- AutoGenertor 自动生成实体类，接口和mapper.xml文件


##### 5：SSM架构

1. 所有的配置文件都在web模块，耦合度太高，不利于项目的分离
2. web （controller）模块 和 MVC(pojo,dao,service) 分离，分别部署两台服务器，service模块配置所有文件，应用SpringMVC 思想



