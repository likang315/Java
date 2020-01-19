## Gradle

​	Gradle 核心目的是作为通用构建工具，主要面向 Java 项目

### 1：安装Gradle 

1. 在环境变量中配置 （～/.bash_profile  ：环境变量文件）

   ```
   GRADLE_HOME=/Users/likang/Code/Java/Gradle/gradle-5.4.1; 
   export GRADLE_HOME 
   export PATH=$PATH:$GRADLE_HOME/bin 
   ```

2. 执行 source  ～/.bash_profile 命令 ，更新.bash_profile文件 

3. 执行 gradle -version  验证是否安装成功

### 2： ProJect 和 Task

​	任何一个 Gradle 构建都是由一个或多个 projects 组成（相当于一个模块）， 每个 project 都由多个 tasks 组成。每个 task 都代表了构建执行过程中的一个原子性操作。如编译，打包，生成 javadoc，发布到某个仓库等操作

##### gradle 命令会从当前目录下寻找 build.gradle 文件来执行构建，build.gradle 文件为构建脚本

```groovy
task hello {
    doLast {
        println 'Hello world!'
    }
}
```

### 3：Gradle 命令

- gradle build ：Gralde 会编译并执行单元测试，并且将 `src/main/*` 下面 class 和资源文件打包
- clean：删除 build 目录以及所有构建完成的文件
- assemble ：编译并打包 jar 文件，但不会执行单元测试
- check：编译并测试代码

### 4：依赖管理

```groovy
repositories {
  	//直接从URL 中加载
    maven {
        url "http://maven.petrikainulainen.net/repo"
    }
  	//从远程库中加载
  	mavenCentral()
}
```

在加入Maven仓库时，Gradle提供了三种“别名”供我们使用，它们分别是：

- mavenCentral() ：表示依赖是从 Central Maven 仓库中获取的
- jcenter() 别名，表示依赖是从 Bintary’s JCenter Maven 仓库中获取的
- mavenLocal() 别名，表示依赖是从本地的 Maven 仓库中获取的

#### 外部依赖

```groovy
dependencies {
    compile group: 'org.hibernate', name: 'hibernate-core', version: '3.6.7.Final'
    testCompile group: 'junit', name: 'junit', version: '4.+'
}
```

##### 将三个属性拼接在一起即可："group:name:version"

- compile：编译范围依赖在所有的 classpath 中可用，同时它们也会被打包
- runtime：依赖在运行和测试系统的时候需要，但在编译的时候不需要
- testCompile：测试期编译需要的附加依赖
- testRuntime：测试运行期需要

### 5：打包发布

```groovy
uploadArchives {
  repositories {
    maven {
      url 'company 路径'
      credentials {
        username 'name'
        password 'word'
      }
    }
  }
}
```

执行 **gradle uploadArchives**，Gradle 便会构建并上传你的 jar 包到目标仓库

### 6：构建Gradle 项目

1. new —>选择Gradle 
2. 选择Java 或者 Web ，创建不同的项目
3. 选择
   - Use auto-import
   - Create directories for empty content roots automatically
4. 余下同 Maven

#### 项目结构

- .gradle：gradle的相关支持文件，不用管
- gradle：gradle的包装程序
  - **gradle-wrapper.jar**：gradlew 执行构建时所依赖的jar包
  - **gradle-wrapper.properties**：gradlew在执行项目构建时的属性文件，该属性文件内容描述了分发的位置
- build.gradle，gradle的构建配置，构建脚本
- gradlew：shell脚本与批处理命令，For *nix
- gradlew.bat：shell脚本与批处理命令，，For Windows

### 7：Gradle 的优势

​	Gradle 既有 Ant 的强大和灵活，又有 Maven 的依赖管理，支持插件且易于使用，Gradle 构建脚本不再使用 xml ，而是使用基于 Groovy 的 DSL 进行书写（Groovy 是一种基于 Java 虚拟机的动态语言），从而使得构建脚本更清晰、简洁

