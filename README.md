### 项目说明
本项目为防b站后端的练手项目，主要目的在与熟悉相对较为完整的后端开发流程，熟悉常用的spring-boot框架和常用中间件的使用。
#### 用到的主要技术
`spring-boot`、`MySQL`、`Redis`、`RocketMQ`、`JWT`
#### 项目亮点
除了基本的增删改查之外，个人感觉这个项目的两个亮点是`用户发布动态`和`弹幕接口`的实现，用到了`redis`和`MQ`来进行消峰和限流以提高接口的性能和系统的稳定性。

### 接口文档
所有接口均使用`postman`进行测式，详细的接口相关信息发布在下面的地址：
https://documenter.getpostman.com/view/21471749/UzkQadud

![](https://cdn.jsdelivr.net/gh/MerickBao/picEmbedding/img/20220803000417.png)