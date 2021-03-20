### Dubbo-API

------

[TOC]

- Dubbo 的常规功能，都保持零侵入，但有些功能不得不用 API 侵入才能实现。
- Dubbo 中**除以下声明以外的接口或类，都是内部接口或扩展接口**，普通用户请不要直接依赖，否则升级版本可能出现不兼容。

##### 01：配置API

- org.apache.dubbo.config.ServiceConfig
- org.apache.dubbo.config.ReferenceConfig
- org.apache.dubbo.config.ProtocolConfig
- org.apache.dubbo.config.RegistryConfig
- org.apache.dubbo.config.MonitorConfig
- org.apache.dubbo.config.ApplicationConfig
- org.apache.dubbo.config.ModuleConfig
- org.apache.dubbo.config.ProviderConfig
- org.apache.dubbo.config.ConsumerConfig
- org.apache.dubbo.config.MethodConfig
- org.apache.dubbo.config.ArgumentConfig

##### 02：注解API

- org.apache.dubbo.config.annotation.Service
- org.apache.dubbo.config.annotation.Reference

##### 03：模型API

- org.apache.dubbo.common.URL
- org.apache.dubbo.rpc.RpcException

##### 04：上下文API

- org.apache.dubbo.rpc.RpcContext
  - 上下文信息

##### 05：服务API

- org.apache.dubbo.rpc.service.GenericService
- org.apache.dubbo.rpc.service.GenericException
  - 泛化引用
- org.apache.dubbo.rpc.service.EchoService
  - 回声测试