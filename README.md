关于 fantasy-core
-------------
>fantasy-core 是[昊略软件公司的java开发框架],并整合了一些开源常用的java开发框架。

fantasy-core 的功能
-------------
>待续

意见和建议
-------------
>待续

帮助
-------------
>待续

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
* RESTful 接口功能添加
```{html}
    X-Page-Fields
        条件查询返回多条数据时，默认返回list。如果需要分页支持需要在请求头中添加 X-Page-Fields：true。
    后端java实现全部返回Pager对象。
```
```{html}
    X-Result-Fields
        当调用端想控制返回字段时，在请求头中添加:X-Result-Fields:username,sex. 这样就只会返回两个字段
```

```{html}
    X-Expend-Fields
        子对象、及关联对象被服务端屏蔽时，前端可以通过设置该字段返回其关联对象的信息:如:X-Expend-Fields:member,orders
```