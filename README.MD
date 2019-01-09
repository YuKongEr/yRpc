## YRPC
---
>[中文文档](https://github.com/YuKongEr/yRpc/blob/master/README-CN.MD)

[![](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/YuKongEr/yRpc/tree/master)  [![](https://img.shields.io/badge/license-apache2.0-blue.svg)](https://github.com/YuKongEr/yRpc/tree/master)   [![]( https://img.shields.io/badge/packagist-1.1-brightgreen.svg)]( https://search.maven.org/search?q=a:yrpc-spring-boot-starter )  [![](https://img.shields.io/badge/license-apache2.0-blue.svg)](https://github.com/YuKongEr/yRpc/tree/master)   [![]( https://img.shields.io/badge/release-v1.1-blue.svg 
)]( https://search.maven.org/search?q=a:yrpc-spring-boot-starter )

YRPC is a highly scalable, high performance, Java RPC framework.

## Functional 
- Transparent and High Performance Remote Service Call
- Support multiple service routing and load balancing strategies
- Support for Zookeeper Center Integration
- Supporting multiple protocols
- Support synchronization, one-way and other invocation methods

## Required
Compilation requires JDK 8 and above, Maven 3.2.5 and above.

Operation requirements JDK 8 and above.

## Quick start
#### Dependence
```xml
    <dependency>
        <groupId>cn.yukonga</groupId>
        <artifactId>yrpc-spring-boot-starter</artifactId>
        <version>1.1.RELEASE</version>
    </dependency>
```

### Configure port registry address
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

### Defined Service Interface
```java
    public interface TestService {
       String say();
    }
```

### Provider Service Implement
```java
    @RemoteService(TestService.class)
    public class TestServiceImpl implements TestService {
        @Override
        public String say() {
            return "zzz";
        }
    }
```
Pass through`@RemoteService(TestService.class)` Designated as a service provider, where Annotated `value`is the interface to the implementation，`isProxyTargetClass` select proxy mode for use。

### Consumer service
```java
    @RemoteReference
    TestService testService;

    @RequestMapping("/index")
    public String say() {
        return testService.say();
    }
```

It can be injected through `@RemoteReference`.


 
 



