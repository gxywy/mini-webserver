# MiniWebServer

MiniWebServer是使用Java编写的轻量级Web应用服务器，基于Java NIO、IO多路复用、并发、XML解析等技术，实现了HTTP请求与响应的处理，采用单线程轮询请求多线程处理请求的方案，内置线程池管理请求处理线程，支持Cookie与Session管理、Servlet上下文、Servlet过滤器与模板引擎等功能。

## 目录结构

- BootStrap.java，启动类
- MiniServer.java，服务器主逻辑循环
- ServerThreadExecutor.java，请求业务处理线程池

```java
.
├── server
│   ├── BootStrap.java
│   ├── MiniServer.java
│   ├── ServerThreadExecutor.java
│   ├── handler
│   │   ├── FileHandler.java
│   │   ├── HttpRequestResolver.java
│   │   ├── RequestHandler.java
│   │   ├── ServletHandler.java
│   │   └── TemplateHandler.java
│   ├── http
│   │   ├── Cookie.java
│   │   ├── Session.java
│   │   ├── request
│   │   │   ├── HttpRequest.java
│   │   │   └── Request.java
│   │   └── response
│   │       ├── HttpResponse.java
│   │       └── Response.java
│   ├── servlet
│   │   ├── HttpServlet.java
│   │   ├── Servlet.java
│   │   └── ServletContext.java
│   ├── template
│   │   └── TemplateResolver.java
│   └── util
│       ├── HttpContentTypeUtil.java
│       ├── HttpRequestMethodUtil.java
│       ├── HttpStatusUtil.java
│       ├── HttpVersionUtil.java
│       └── ServerConfigUtil.java
└── testservlet
    ├── TestServlet.java
    └── UserServlet.java
```

## 性能测试

使用JMeter进行压力测试，Connection: close，测试总请求数为20000次

- BIO ([commit 88639b0](https://github.com/gxywy/java-webserver/tree/88639b02be9e982b525c71cd0bf732a63890ddb6))

  | 线程数 | 循环次数 | 平均响应时间 ms | 吞吐量 /s | 异常 % | 平均字节数 KB |
  | :----: | :------: | :-------------: | :-------: | :----: | :-----------: |
  |   2    |  10000   |        1        |   617.6   |   0    |    11293.0    |
  |   20   |   1000   |        7        |   587.3   |   0    |    11293.0    |
  |  200   |   100    |       59        |   589.9   |   0    |    11293.0    |
  |  1000  |    20    |       326       |   569.0   |  0.17  |    11293.0    |

- NIO

  | 线程数 | 循环次数 | 平均响应时间 ms | 吞吐量 /s | 异常 % | 平均字节数 KB |
  | :----: | :------: | :-------------: | :-------: | :----: | :-----------: |
  |   2    |  10000   |        3        |   575.6   |   0    |    11317.0    |
  |   20   |   1000   |        8        |   586.6   |   0    |    11317.0    |
  |  200   |   100    |       77        |   575.2   |   0    |    11317.0    |
  |  1000  |    20    |       323       |   584.7   |  0.43  |    11279.8    |
  
## TODO

- Servlet Listener

## 参考

- https://github.com/apache/tomcat
- https://github.com/songxinjianqwe/WebServer/
- https://github.com/94fzb/simplewebserver/
- https://github.com/xuguofeng/http-server/
- https://github.com/itgowo/MiniHttpServer

