MVC：设计模式，让 M-V 层分离
model:模型层，操作数据库的返回的数据封装成model
view :视图层，Jsp显示数据的
control : 控制层，一般接受用户的请求，并调用service层，处理相应的业务，并调用view层显示数据

pojo：Java的普通类，数据库的实体类，只有get,set方法
dao(data access object)：数据访问层,封装操作数据库的方法，和数据库交互
service ：服务层，业务层，调用dao，处理相应的业务，添加了事务	
controler:控制层
tool：一些Java工具类

![](G:\Java\Java_note\15：JSP, Jsp自定义标签,EL，JSTL，Ajax，JSON\Jsp_模式二.png)