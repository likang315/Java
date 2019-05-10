计算机：硬件+软件

软件：为了管理 ，维护计算机以及完成用户的某种特定任务而编写的各种程序总和
硬件：计算机系统中由电子，机械和光电原件等组成的各种物理装置的总和

发展：机器语言 （0，1代码） ----   汇编语言（编译解释）  -----高级语言
	
高级语言：c,c++,php,.net,java.......

java语言：1995年 由Sun公司提出
	             2010年 被Oracle公司收购

java之父：James Gosling

eclipse（日食）IBM---sun（太阳）

#### java 版本：

​	javaME：微型版-------塞班手机（诺基亚）
​	javaSE：标准版
​	javaEE：企业版
​	
javaSE是java的基础，主要用于桌面应用程序的开发，javaEE主要用于开发企业级分布式的网络程序，javaME主要用于嵌入式系统开发
​	

#### java 语言的特点：

​		面向对象
​		单继承
​		跨屏台：主要实现是依靠jvm，不同平台对应有不同的jvm
​		
jdk:开发工具包 + jre（运行环境）
jre：api（Java系统类库） + jvm（java虚拟机）
​	

#### 配置环境变量：

   step1:安装jdk 
   step2:配置path
   step3:配置classpath

环境变量的配置：把jdk安装目录下的bin目录，复制，在计算机的属性中找到环境变量，在系统变量中找到path，点击编辑，将光标移动到开始为置粘贴，完了之后一级一级点击确定

 		JAVA_HOME :找到jdk开发工具包  ---------用来辅助配置path的
 		CLASSPATH：告诉编译器字节码文件所在的位置 -----//当前目录下
		.;C:\Program Files\Java\jdk1.8.0_171\;C:\Program Files\Java\jdk1.8.0_171\jre\bin
	注意：
		java -version ，java，javac来测是否配置成功。

path是用来搜索所执行的可执行文件路径的，如果执行的可执行文件不在当前目录下，那就会依次搜索path中设置的路径。
而CLASSPATH是用来告诉编辑器在那里寻找Java编译过程中所需的包和类所以其路径中配置的是lib目录下的tools.jar;



java语言的执行过程：
  step1:将class文件加载到内存
  step2:对每个字节码都实例化一个java.lang.Class的对象
  step3:进行连接，初始化
  step4:实始化静态成员变量，静态代码块，放入方法区
  step5:执行入口方法main

java程序的运行机制 ： 源文件（.java） -> 编译器(javac)生成--字节码文件（.class）-> 解释器（java）-> 机器语言

​	java程序的运行机制：源文件--（编译器编译）--.clss字节码文件---（解释器解释并执行）

​	javac a.java   -----编译
​	java a         -----解释执行

编写java程序时需要注意：
	.java源文件名称要和该文件中public修饰的类名名称完全一样，在一个源文件当中可以存在多个类
	但是只能存在一个public类
	


​	