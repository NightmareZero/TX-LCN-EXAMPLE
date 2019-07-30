# TX-LCN 研究笔记

## 介绍

> **实验用的示例代码：** https://github.com/NightmareZero/TX-LCN-EXAMPLE
> **官方网站:**  https://www.txlcn.org
> **Github:**  https://github.com/codingapi/tx-lcn

TX-LCN分布式事务框架 主要分为两部分，`Client`及`Server`。
分为三种模式

- **LCN模式**  通过代理Connection的方式实现对本地事务的操作，然后在由TxManager统一协调控制事务。当本地事务提交回滚或者关闭连接时将会执行假操作，该代理的连接将由LCN连接池管理。
- **TXC模式**  实现原理是在执行SQL之前，先查询SQL的影响数据，然后保存执行的SQL快走信息和创建锁。当需要回滚的时候就采用这些记录数据回滚数据库，目前锁实现依赖redis分布式锁控制。
- **TCC模式**  相对于传统事务机制（X/Open XA Two-Phase-Commit），其特征在于它不依赖资源管理器(RM)对XA的支持，而是通过对（由业务系统提供的）业务逻辑的调度来实现分布式事务。主要由三步操作，Try: 尝试执行业务、 Confirm:确认执行业务、 Cancel: 取消执行业务。

**本示例基于SpringCloud和TCC模式**

## 项目搭建

### Manager配置

服务端需要jdbc和redis

#### 单点

**需要添加的Pom依赖：**

```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>txlcn-tm</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>

<!-- 此处使用Mysql，可以使用任意JDBC库 -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>6.0.6</version>
</dependency>
```

**项目注解：**

```java
@SpringBootApplication
@EnableTransactionManagerServer
public class TmServApplication { ...... }
```

**application.properties主要配置：**

```properties
spring.application.name=TransactionManager
server.port=7970

#tx-lcn.logger.enabled=true
# TxManager Host Ip
#tx-lcn.manager.host=127.0.0.1
# TxClient连接请求端口
#tx-lcn.manager.port=8070
# 心跳检测时间(ms)
#tx-lcn.manager.heart-time=15000
# 分布式事务执行总时间
#tx-lcn.manager.dtx-time=30000
#参数延迟删除时间单位ms
#tx-lcn.message.netty.attr-delay-time=10000
#tx-lcn.manager.concurrent-level=128
# 开启日志
#tx-lcn.logger.enabled=true
#logging.level.com.codingapi=debug

# jdbc 的设置信息, 线上请用Cluster
spring.datasource.driver-class-name=com.mysql.jdbc.Driver
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/tx-manager?characterEncoding=UTF-8
spring.datasource.username=root
spring.datasource.password=root

# redis 的设置信息. 线上请用Redis Cluster
spring.redis.host=127.0.0.1
spring.redis.port=6379
spring.redis.password=
```

#### 集群

> 待续

### Client配置

**项目注解：**

```java
@SpringBootApplication
@EnableDistributedTransaction // 客户端需要添加的注释
public class TestTmServ1Application {}
```

**依赖包：**

```xml
<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>txlcn-tc</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>

<dependency>
    <groupId>com.codingapi.txlcn</groupId>
    <artifactId>txlcn-txmsg-netty</artifactId>
    <version>5.0.2.RELEASE</version>
</dependency>
```

**配置文件：application.properties**

```properties
tx-lcn.client.manager-address=127.0.0.1:8070 
```

### TCC模式使用示例

**事物发起端：**
特殊配置：`@TccTransaction(propagation = DTXPropagation.REQUIRED)`

```java
@Service
public class Test2Service {

    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public Test2Service(RemoteApi remoteApi) {
        this.remoteApi = remoteApi;
    }

    private RemoteApi remoteApi;

    /**
     * try方法，用于尝试执行或者准备工作
     * propagation - 事物的模式
     * confirmMethod -  成功时执行的方法
     * cancelMethod -  回滚时执行的方法
     */
    @TccTransaction(propagation = DTXPropagation.REQUIRED, confirmMethod = "doWorkConfirm", cancelMethod = "doWorkCancel")
    @Transactional
    public boolean doTry(String val) {
        logger.info("doWork");
        String s = remoteApi.postInfo();
        if (true) {
            throw new RuntimeException("");
        }
        logger.info("finishwork");

        return false;

    }

    /**
     * 事物成功时执行的逻辑
     * 入参：需要保持与对应的Try方法一致
     */ 
    public void doWorkConfirm(String val) {
        logger.info("------serv1--confirm--" + val);
    }

    /**
     * 事物失败时执行的逻辑
     * 入参：需要保持与对应的Try方法一致
     */ 
    public void doWorkCancel(String val) {
        logger.info("------serv1--cancel--" + val);
    }
}
```

**事物接收端：**

特殊配置：`@TccTransaction(propagation = DTXPropagation.SUPPORTS)`

```java
@Service
public class Test2Service {

    public Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @TccTransaction(propagation = DTXPropagation.SUPPORTS, confirmMethod = "doWorkConfirm", cancelMethod = "doWorkCancel")
    @Transactional
    public boolean doTry(String val) {
        logger.info("<======serv2--doWork======>");
//        throw new RuntimeException("");
        logger.info("<======serv2--finishwork======>");
        return false;
    }


    public void doWorkConfirm(String val) {
        logger.info("<======serv2--confirm======>" + val);
    }

    public void doWorkCancel(String val) {
        logger.info("<======serv2--cancel======>" + val);
    }
}
```

### TCC模式运作特点

**名词解释：**

**发起端：**  分布式事物的发起的服务
**参与端：**  被发起点在事物中远程调用的服务

**try方法：**  用于准备或尝试执行的方法，也可以放置业务逻辑
**confirm方法：**  用于事物成功收尾的方法，不会与cancel方法同时发生
**cancel方法：**  用于事物回滚的方法，不会与confirm同时发生

1. try方法成功return时，则视为本地事务执行成功,否则视为本地事物执行失败
2. 在参与端try方法异常退出时，本地事物回滚，但不影响总体事物
3. 在发起端try方法异常退出时，所有执行成功的事物执行cancel方法
4. 在发起端try方法正常结束时，所有未执行cancel的事物执行confirm方法

