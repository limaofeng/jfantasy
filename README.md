# `jfantasy`

[![Build Status][circle-img]][circle-url] [![Coverage Status][coveralls-img]][coveralls-url] [![TechDebt Status][tech_debt-img]][tech_debt-url] [![Coverage Status][coverage-img]][coverage-url]

jfantasy 是[昊略软件公司的java开发框架],并整合了一些开源常用的java开发框架.

### 快速开始

```xml
    <repository>
        <id>jfantasy snapshot</id>
        <name>jfantasy snapshot</name>
        <url>http://maven.jfantasy.org/content/repositories/snapshots/</url>
    </repository>

    <dependency>
      <groupId>org.jfantasy</groupId>
      <artifactId>jfantasy-core</artifactId>
      <version>1.0.0</version>
    </dependency>
```

主要功能及使用技术介绍
-------------
>RESTful
>支付接口
>微信接口
>Hibernate
>Mybatis

演示系统 RESTfull API 地址
-------------
>http://api-docs.jfantasy.org

### 3.3.17 升级日志
* Pager 对象 json 格式调整为 ：
```json
 {
     "count": 4,
     "per_page": 15,
     "total": 1,
     "page": 1,
     "order": [
         "asc"
     ],
     "items": [
         ]
 }
```
* 升级 spring-swagger 到 springfox
* RESTful 添加 X-Page-Fields 请求头
  ```条件查询返回多条数据时，默认返回list。如果需要分页支持需要在请求头中添加 X-Page-Fields：true。注：后端java实现全部返回Pager对象。```
* RESTful 添加 X-Result-Fields  请求头
  ```当调用端想控制返回字段时，在请求头中添加:X-Result-Fields:username,sex. 这样就只会返回两个字段```
* RESTful 添加 X-Expend-Fields  请求头
  当子对象、及关联对象被服务端屏蔽时，前端可以通过设置该字段返回其关联对象的信息:如:X-Expend-Fields:member,orders
 
  
----

© 2015 李茂峰 <limaofeng@msn.com>

Licensed under [MIT](http://jfantasy.org/mit.txt)

[coveralls-img]: http://img.shields.io/coveralls/limaofeng/jfantasy/master.svg?style=flat-square
[coveralls-url]: https://coveralls.io/r/limaofeng/jfantasy
[gitter-img]:    http://img.shields.io/badge/asana-join_chat-1dce73.svg?style=flat-square
[gitter-url]:    https://gitter.im/limaofeng/jfantasy
[circle-img]:    https://img.shields.io/circleci/project/limaofeng/jfantasy.svg?style=flat-square
[circle-url]:    https://circleci.com/gh/limaofeng/jfantasy
[tech_debt-img]:https://img.shields.io/sonar/http/sonar.hoolue.com/com.fantasy:fantasy-framework/tech_debt.svg?style=flat-square
[tech_debt-url]:http://sonar.hoolue.com/dashboard/index/4676
[coverage-img]:https://img.shields.io/sonar/http/sonar.hoolue.com/com.fantasy:fantasy-framework/coverage.svg?style=flat-square
[coverage-url]:http://sonar.hoolue.com/dashboard/index/4676