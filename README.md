# job_spring
job for spring module in lagou



PS:

1:代码

1.1:测试的客户端是在原来的基础上增加了一个，是se版本的

```
testAnnocationIoCDIY
```

1.2：将相关的服务层，dao层，连接管理层，事务管理层，代理工厂层单独从现有转账工程复制出去一份，在

com.lagou.edu.diy下

1.3：针对数据源datasource这块没有完全封装，单例实现，其他基本都按照注解自动或者依赖注入进去了

1.4:注解容器按照要求为了方便只扫描自定义注解service类，其他通过依赖注入，动态代理增强

1.5：测试通过（正常和异常)

