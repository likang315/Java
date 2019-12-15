### Charles抓包工具教程

------

##### mac版：

1. 官网下载Charles，由于是外网可能需要等待一段时间
2. 安装之后，提供一种破解方式
   1. 在Help→Registered 中输入 Name: [https://zhile.io](https://zhile.io/) 、 License Key: 48891cf209c6d32bf4
3. 由于Charles原理是代理模式，所以需要配置代理服务器
   1. Proxy ->点击 maxOS Proxy，即可开始抓浏览器网页请求
4. 解决content字段显示乱码
   1. 原因：由于其信任证书为启用
   2. 解决方式：Help→SSL Proxying-install  Charles root certificate 
   3. 之后弹出下载框，然后启用即可

##### 使用ios手机抓包

1. 首先电脑和手机必须链接同一个局域网，手机发送的请求才可以打到Charles；
2. 修改手机网络的代理
   1. IP：为电脑的IP
   2. 端口：Charles 监控的端口
3. 此时手机发送请求,Charles是可以抓到的，但是由于ios未安装Charles CA 证书，所以抓取的内容显示未知；
   1. Help→SSL Proxying-install  Charles root certificate on a Mobile Device ....
   2. 手机访问[hhtp://chrls.pro/ssl](hhtp://chrls.pro/ssl)，下载证书
   3. 手机端：设置->通用→ 关于手机→证书信任设置→开启Charles Proxy CA ；