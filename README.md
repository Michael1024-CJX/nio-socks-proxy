# nio-socks-proxy
为了在windows环境下使用[kt-connect](https://github.com/alibaba/kt-connect)连接k8s内部网络，需要为`NIO`实现对`SOCKS5`代理的支持，否则一些客户端组件如果使用的是NIO,就无法连接。

具体实现参考了[mocksocks](https://github.com/code4craft/mocksocks)项目以及JDK8中的java.net.SocksSocketImpl的实现。

## 使用方式

配置项目启动参数，javaagent与nioSocksProxyJar的参数值为lib/nio-socks-proxy-1.0.0.jar的绝对路径。nioSocksProxyHost与nioSocksProxyPort配置socks服务器的地址。

```
-DnioSocksProxyHost=127.0.0.1
-DnioSocksProxyPort=2223
-javaagent:xxx\nio-socks-proxy.jar
-DnioSocksProxyJar=xxx\nio-socks-proxy.jar
```

