### 线上机器Debug

------



1. 准备线上debug 之前一定要，**进行摘机器、进行摘机器、进行摘机器**

2. - sudo mv healthcheck.html healthcheck_bak.html
   - 传说中的摘机器如此简单，其实就是将healthcheck.html 改名；

3. 在startenv.sh 最后一行加入：

4. ```
   export JAVA_OPTS=$JAVA_OPTS" -agentlib:jdwp=transport=dt_socket,
   server=y,suspend=n,address=127.0.0.1:60001"
   ```

   - debug参数；

5. sudo /home/q/tools/bin/restart_tomcat.sh app_code && tail -f /home/q/www/应用

   /logs/catalina.out

   - 重启应用，查看日志；

6. socat TCP4-LISTEN:监听端口,fork,range=本地ip/32 TCP4:127.0.0.1:60001

   - 启动转发端口
   - TCP4-LISTEN:15672 在本地建立一个 TCP IPv4 协议的监听端口，也就是转发端口。这里是 15672，就是我们线上机器需要监听的端口。
   - 并将请求转发至 172.0.0.1 的 60001 端口；

7. idea debug 配置 线上这台机器的hostname:转发端口

8. sudo mv healthcheck_bak.html healthcheck.html

   - 同时去掉startenv.sh中加入的debug内容，再重启服务；