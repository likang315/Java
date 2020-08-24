### Maven

------


​	一个项目管理工具，利用一个中央信息片断，能管理一个项目的构建，报告，文档，依赖等步骤，Apache下的一个纯 Java 开发的开源项目。

##### 01：特点：

1. 基于模型的构建
   - Maven能够将任意数量的项目构建到预定义的输出类型中，JAR 或WAR	
2. 项目信息的一致性站点 
   - 使用与构建过程相同的元数据，Maven 能够生成一个网站或PDF,包括您要添加的任何文档，并添加到关于项目开发状态的标准报告中
3. 发布管理和发布单独的输出
   - Maven 将不需要额外的配置，就可以与源代码管理系统（Git）集成，并可以基于某个标签管理项目的发布

##### 02：环境配置

1. MAVEN_HOME：Maven的路径
2. %MAVEN_HOME%\bin-------添加到path
3. DOS下测试： mvn -v 

##### 03：Maven 项目结构

![Maven项目结构](/Users/likang/Code/Git/Middleware/Tool/photos/Maven项目结构.png)

##### 04：POM(Project Object Model)项目对象模型

- Maven 工程的基本工作单元，是一个XML文件，包含了项目的基本配置信息，用于描述项目如何构建，声明项目依赖，等等。

- 父 POM(super POM): Maven默认的POM,所有的 POM 都继承此父 POM，包含一些可以被继承的默认设置,这些配置可以被重写

  - mvn help:effective-pom---------查看super POM

- 主要字段：

  - project：工程的根标签

  - modelVersion：模型版本需要设置为 4.0

  - groupId：项目的唯一标识,且配置时生成的路径也是由此生成，一般给成com.包名

  - artifactId：构件的标识和groupId共同标识一个构件，构件是项目中使用或产生一个东西,例：jar,war,源码 

  - packaging：构键类型

  - version：工程的版本号

  - build：构键项目需要的信息

  - repository：远端仓库

  - **scope**

    ![Maven-Scope](/Users/likang/Code/Git/Middleware/Tool/photos/Maven-Scope.png)

##### 05：Maven 三个标准的生命周期

- clean：项目清理的处理，命令：mvn post-clean
- default(或 build)：项目部署的处理，命令：mvn compile
- site：项目站点文档创建的处理，命令：mvn site

每个生命周期中都包含着一系列的阶段(phase),这些 phase 就相当于 Maven 提供的统一的接口，然后每一阶段(phase)的实现由Maven的插件来完成

##### 06：Maven 仓库：是项目中依赖的第三方库，帮助管理构件

###### Maven 仓库有三种类型：

1. 本地（local）:在安装好Maven后，在第一次执行 maven 命令的时候被创建
   - 默认情况下，在自己的用户目录下都有一个路径名为 .m2/respository/ 的仓库目录，若要修改默认位置，在%M2_HOME%\conf 目录中的 Maven 的 settings.xml 文件中定义另一个路径
   - < localRepository>/Users/likang/Code/Java/Maven/Local_Maven< /localRepository>
2. 中央(central)：由 Maven 社区提供的仓库，简单的 Java 项目依赖的构件都可以在这里下载
3. 远程(remote) ：有国外提供，更换为阿里云(Aliyun)仓库 

Maven 依赖搜索顺序: local->central->remote,若都没有,Maven将停止处理并抛出错误(无法找到依赖的文件)

###### 更换remote 仓库，更换aliyun镜像

1. 修改 maven 根目录下的 conf 文件夹中的 setting.xml 文件，在 mirrors 节点上

2. 在pom.xml文件里的<repositories>中添加	    

   ```xml
   <mirror>
       <id>alimaven</id>
       <name>aliyun maven</name>
       <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
       <mirrorOf>central</mirrorOf>        
   </mirror>
   <repository>  
     <id>alimaven</id>  
     <name>aliyun maven</name>  
     <url>http://maven.aliyun.com/nexus/content/groups/public/</url>  
     <releases>  
     	<enabled>true</enabled>  
     </releases>  
     <snapshots>  
     	<enabled>false</enabled>  
     </snapshots>  
   </repository> 
   ```

##### 07：Maven 项目模板

​	使用原型 archetype 插件创建Java项目，archetype其实就是项目模板，它的任务是根据模板创建一个项目目录结构，maven-archetype-quickstart 插件

##### 08：Maven 引入外部依赖(jar包)

- 在pom.xml的<dependencies>依赖标签中配置

  ```xml
  <dependency>
  <groupId>ldapjdk</groupId>          <!--库名称，也可以自定义 -->
  <artifactId>ldapjdk</artifactId>    <!--库名称，也可以自定义-->
  <version>1.0</version> 		    <!--版本号-->
  <scope>system</scope> 		    <!--作用域-->
  <!--项目根目录下的lib文件夹下-->
  <systemPath>${basedir}\src\lib\ldapjdk.jar</systemPath> 
  </dependency> 
  ```

##### 09：Maven 依赖管理 

​	管理多模块的项目，模块间的依赖关系就变得非常复杂，管理也变得很困难，针对此种情形，Maven 提供了一种高度控制的方法，通过读取项目文件（pom.xml)，找出它们项目之间的依赖关系，只需要做的是在每个项目的 pom 中定义好直接的依赖关系

###### Maven 提供一些功能来控制可传递的依赖的程度	

1. 依赖调节：如果两个依赖版本在依赖树里的深度是一样的时候，第一个被声明的依赖将会被使用。
2. 依赖管理：直接的指定手动创建的某个版本被使用
3. 依赖范围：包含在构建过程每个阶段的依赖
4. 依赖排除：任何可传递的依赖都可以通过 "exclusion" 元素被排除在外
5. 依赖可选：任何可传递的依赖可以被标记为可选的，通过使用 "optional" 元素

###### Idea Maven Helper 插件管理依赖

##### 10：Maven 在通过控制台构键Web应用

	1>:执行 mvn 命令：
		C:\MVN>mvn archetype:generate -DgroupId=com.companyname.automobile -DartifactId=trucks 
		-DarchetypeArtifactId=maven-archetype-webapp  -DinteractiveMode=false
	2>:执行mvn 命令
		C:\MVN\trucks>mvn clean package
	3>:打开 C:\< MVN < trucks < target < 文件夹，找到 trucks.war 文件，并复制到你的 web 服务器的web应用目录	

##### 11：Maven的命令

	clean：清除目标目录中的生成结果(target目录)
- compile：编译生成class文件，下载依赖包
- test：运行项目中的单元测试
- package：项目打包，但没有把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库和远程maven私服仓库
- install：完成了项目编译、单元测试、打包功能，同时把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库
- deploy：完成了项目编译、单元测试、打包功能，同时把打好的可执行jar包（war包或其它形式的包）布署到本地maven仓库和远程maven私服仓库
- generate-sources：开发环境与代码分离，很少使用，执行这个命令可以通过查看.classpath和.project两个文件来查看变化

##### 12：Maven 多环境打包

##### 使用Maven在编译时指定Profile方式【推荐】

1. < profile>：定义了各个环境的变量ID；
   1. maven启动时会配置自动选择使用哪个< profile>
   2. 打包命令：mvn package -Pdev来指定激活id为 dev 的profile节点, 这样, 开发环境配置文件就会被打包.
      - 开发：mvn package -Pdev (因为配置了默认激活dev部分, 所以也可以使用mvn package, 这与 mvn package -Pdev 效果相同)
      - 测试：mvn package -Ptest
      - 生产：mvn package -Pprod
2. < filters>：定义了**变量配置文件的地址**；
   1. < filter> 标签中指定的文件中$ {key}，会获取相应的值替代；
3. < resources>
   1. < directory>：表示编译所需的资源目录；
   2. < filtering>：表示是否开启替换资源文件中的属性, 设置为 true 才能实现动态替换；
   3. < excludes>：表示排除掉资源目录下的某文件或文件夹
   4. < includes>：表示包含资源目录下的文件；
   5. < targetPath>：表示该资源标签下的资源打包编译后的保存路径,"." 表示当前目录；
4. 执行下面命令进行编译：mvn package -P dev

```xml
<profiles>
    <profile>
      <id>dev</id>
      <properties>
        <project.environment>dev</project.environment>
      </properties>
      <activation>
        <!--默认激活dev环境-->
        <activeByDefault>true</activeByDefault>
      </activation>
    </profile>
    <profile>
      <id>beta</id>
      <properties>
        <project.environment>beta</project.environment>
      </properties>
    </profile>
    <profile>
      <id>prod</id>
      <properties>
        <project.environment>prod</project.environment>
      </properties>
    </profile>
</profiles>

 <build>
        <finalName>工程名</finalName>
        <filters>
            <filter>src/main/config/${project.environment}/filter.properties</filter>
        </filters>
        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <excludes>
                    <exclude>config</exclude>
                </excludes>
            </resource>
            <resource>
                <directory>src/main/resources/config/${profile.active}</directory>
              <targetPath>.</targetPath>
            </resource>
        </resources>
</build>
```