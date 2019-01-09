## YRPC


[![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/YuKongEr/yRpc/tree/master)  [![](https://img.shields.io/badge/license-apache2.0-blue.svg)](https://github.com/YuKongEr/yRpc/tree/master)   [![]( https://img.shields.io/badge/packagist-1.1-brightgreen.svg)]( https://search.maven.org/search?q=a:yrpc-spring-boot-starter )  [![](https://img.shields.io/badge/license-apache2.0-blue.svg)](https://github.com/YuKongEr/yRpc/tree/master)   [![]( https://img.shields.io/badge/release-v1.1-blue.svg 
)]( https://search.maven.org/search?q=a:yrpc-spring-boot-starter )

YRPC 是一个高可扩展性、高性能、 Java RPC 框架。

##功能特性
- 透明化、高性能的远程服务调用
- 支持多种服务路由及负载均衡策略
- 支持Zookeeper的集成
- 支持多种协议
- 支持同步、单向等多种调用方式

## 需要
编译需要 JDK 8 及以上、Maven 3.2.5 及以上。

运行需求 JDK 8 及以上。

## 快速开始
#### 依赖
```xml
    <dependency>
        <groupId>cn.yukonga</groupId>
        <artifactId>yrpc-spring-boot-starter</artifactId>
        <version>1.1.RELEASE</version>
    </dependency>
```

### 配置端口注册中心地址
```yaml
    yrpc:
      zookeeper:
        address: 193.112.100.103:2181
        rootPath: /yRpc
        sessionTimeOut: 10000
      netty:
        port: 8989
        host: 127.0.0.1
```

### 定义服务接口
```java
    public interface TestService {
       String say();
    }
```

### 提供服务实现
```java
    @RemoteService(TestService.class)
    public class TestServiceImpl implements TestService {
        @Override
        public String say() {
            return "zzz";
        }
    }
```
其中通过`@RemoteService(TestService.class)` 指定为服务提供者，其中
注解的`value`值为实现的接口，`isProxyTargetClass`为使用的代理模式。

### 消费服务
```java
    @RemoteReference
    TestService testService;

    @RequestMapping("/index")
    public String say() {
        return testService.say();
    }
```

通过`@RemoteReference`即可注入。


 
 



