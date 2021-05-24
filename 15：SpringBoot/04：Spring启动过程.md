### SpringBoot 启动过程

------

1. ##### ServletInitializer

   - 在SpringBoot中，其初始化器ServletInitializer可以认为大体相当于SpringMvc中的Web.xml角色，都是负责Tomcat启动时触发Spring各组件的初始化；
   - ServletInitializer实现了SpringBootServletInitializer的方法，而SpringBootServletInitializer实现WebApplicationInitializer。最终WebApplicationInitializer和其子类，共同替代了Web.xml的加载工作。
   

