///cache/JavaSource/com/fantasy/framework/hibernate/cache/log/SpringCacheMessageLogger.java
/**
     * Log a message (WARN) about inability to find configuration file
     *
     * @param name The name of the configuration file
     */
/**
     * Log a message (WARN) about inability to find named cache configuration
     *
     * @param name The name of the cache configuration
     */
/**
     * Logs a message about not being able to resolve the configuration by resource name.
     *
     * @param configurationResourceName The resource name we attempted to resolve
     */
/**
     * Logs a message (WARN) about attempt to use an incompatible
     */
/**
     * Logs a message (WARN) about attempt to use an incompatible
     *
     * @param cacheName The name of the cache whose config attempted to specify value mode.
     */
/**
     * Log a message (WARN) about an attempt to specify read-only caching for a mutable entity
     *
     * @param entityName The name of the entity
     */
/**
     * Log a message (WARN) about expiry of soft-locked region.
     *
     * @param regionName The region name
     * @param key        The cache key
     * @param lock       The lock
     */
///cache/JavaSource/com/fantasy/framework/hibernate/cache/regions/SpringCacheDataRegion.java
/*
        try {
            final Map<Object, Object> result = new HashMap<Object, Object>();
            for (Object key : getCache().getKeys()) {
                result.put(key, getCache().get(key).getObjectValue());
            }
            return result;
        } catch (Exception e) {
            if (e instanceof NonStopCacheException) {
                HibernateNonstopCacheExceptionHandler.getInstance().handleNonstopCacheException((NonStopCacheException) e);
                return Collections.emptyMap();
            } else {
                throw new CacheException(e);
            }
        }*/
///cache/JavaSource/com/fantasy/framework/hibernate/cache/strategy/AbstractReadWriteSpringCacheAccessStrategy.java
/**
         * Returns true if this Lock has been concurrently locked by more than one transaction.
         */
///cache/JavaSource/com/fantasy/framework/hibernate/cache/util/VicariousThreadLocal.java
/**
     * Check if any strong references need should be removed due to thread exit.
     */
/**
     * Holds strong reference to a thread-local value.
     * The WeakReference is to a thread-local representing the current thread.
     */
/**
         * Construct a new holder for the current thread.
         */
/**
         * Next holder in chain for this thread-local.
         */
/**
         * Current thread-local value.
         * {@link #UNINITIALISED} represents an uninitialised value.
         */
///cms/JavaSource/com/fantasy/cms/bean/Article.java
/**
 * 文章表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:20
 */
/**
     * 文章标题
     */
/**
     * 摘要
     */
/**
     * 关键词
     */
/**
     * 文章正文
     */
/**
     * 作者
     */
/**
     * 发布日期
     */
/**
     * 文章对应的栏目
     */
/**
     * 发布标志
     */
///cms/JavaSource/com/fantasy/cms/bean/ArticleCategory.java
/**
 * 栏目表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:46:57
 */
/**
     * 栏目名称
     */
/**
     * 层级
     */
/**
     * 描述
     */
/**
     * 排序字段
     */
/**
     * 上级栏目
     */
/**
     * 下级栏目
     */
/**
     * 属性版本表
     */
/**
     * 文章
     */
///cms/JavaSource/com/fantasy/cms/bean/Banner.java
/**
 * 横幅图维护
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-3-3 上午11:22:45
 */
/**
     * 唯一编码
     */
/**
     * 名称
     */
/**
     * 上传图片大小
     */
/**
     * 描述
     */
/**
     * 对应的明细项
     */
///cms/JavaSource/com/fantasy/cms/bean/BannerItem.java
/**
 * 横幅图维护项
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-3-4 下午12:56:46
 */
/**
     * 标题
     */
/**
     * 摘要
     */
/**
     * 跳转地址
     */
/**
     * 图片存储位置
     */
/**
     * 排序字段
     */
///cms/JavaSource/com/fantasy/cms/bean/Content.java
/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
/**
     * 正文内容
     */
///cms/JavaSource/com/fantasy/cms/bean/Topic.java
/**
 * 专题表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午06:39:55
 */
/**
     * 专题编码
     */
/**
     * 专题名称
     */
/**
     * 期刊号
     */
/**
     * 专题对应的URL
     */
/**
     * 发布日期
     */
/**
     * 专题对应的栏目
     */
/**
     * 描述
     */
///cms/JavaSource/com/fantasy/cms/rest/ArticleCategoryController.java
/**
 * @apiDefine paramArticleCategory
 * @apiParam {String} code  编码
 * @apiParam {String} name  名称
 * @apiParam {String} layer  层级
 * @apiParam {String} path  路径
 * @apiParam {String} description  描述
 * @apiParam {Date} sort  排序字段
 * @apiParam {String} parent_code  上级栏目编码
 * @apiVersion 3.3.2
 */
/**
 * @apiDefine returnArticleCategory
 * @apiParam {String} code  编码
 * @apiParam {String} name  名称
 * @apiParam {String} layer  层级
 * @apiParam {String} path  路径
 * @apiParam {String} description  描述
 * @apiParam {Date} sort  排序字段
 * @apiParam {String} parent  上级栏目编码
 * @apiVersion 3.3.2
 */
/**
     * @api {post} /cms/categorys   查询分类
     * @apiVersion 3.3.2
     * @apiName searchArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 筛选分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/categorys?currentPage=1&LIKES_title=上海
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
/**
     * @api {post} /cms/categorys/:code   获取分类
     * @apiVersion 3.3.2
     * @apiName getArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 获取单篇分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/categorys/root
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
/**
     * @api {post} /cms/categorys   添加分类
     * @apiVersion 3.3.2
     * @apiName createArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 添加分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "title=测试&summary=测试..." http://localhost/cms/categorys
     * @apiUse paramArticleCategory
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
/**
     * @api {delete} /cms/categorys/:code   删除分类
     * @apiVersion 3.3.2
     * @apiName deleteArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 删除分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/categorys/root
     * @apiUse paramArticleCategory
     * @apiUse GeneralError
     */
/**
     * @api {put} /cms/categorys/:code   更新分类
     * @apiVersion 3.3.2
     * @apiName updateArticleCategory
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 更新分类
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/categorys/root
     * @apiUse paramArticleCategory
     * @apiUse returnArticleCategory
     * @apiUse GeneralError
     */
///cms/JavaSource/com/fantasy/cms/rest/ArticleController.java
/**
 * @apiDefine paramArticle
 * @apiParam {String} title  标题
 * @apiParam {String} summary  摘要
 * @apiParam {String} keywords  关键词
 * @apiParam {String} content  正文
 * @apiParam {String} author  作者
 * @apiParam {Date} releaseDate  发布日期
 * @apiParam {String} category_code  栏目编码
 * @apiVersion 3.3.2
 */
/**
 * @apiDefine returnArticle
 * @apiSuccess {Long} id
 * @apiSuccess {String} title  标题
 * @apiSuccess {String} summary  摘要
 * @apiSuccess {String} keywords  关键词
 * @apiSuccess {String} content  正文
 * @apiSuccess {String} author  作者
 * @apiSuccess {Date} releaseDate  发布日期
 * @apiSuccess {ArticleCategory} category  栏目
 * @apiSuccess {Boolean} issue  发布标示
 * @apiVersion 3.3.2
 */
/**
     * @api {post} /cms/articles   查询文章
     * @apiVersion 3.3.2
     * @apiName searchArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 筛选文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/articles?currentPage=1&LIKES_title=上海
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
/**
     * @api {post} /cms/articles/:id   获取文章
     * @apiVersion 3.3.2
     * @apiName getArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 获取单篇文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/articles/43
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
/**
     * @api {post} /cms/articles   添加文章
     * @apiVersion 3.3.2
     * @apiName createArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 添加文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "title=测试&summary=测试..." http://localhost/cms/articles
     * @apiUse paramArticle
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
/**
     * @api {delete} /cms/articles/43   删除文章
     * @apiVersion 3.3.2
     * @apiName deleteArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 删除文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/articles/43
     * @apiUse paramArticle
     * @apiUse GeneralError
     */
/**
     * @api {put} /cms/articles/43   更新文章
     * @apiVersion 3.3.2
     * @apiName updateArticle
     * @apiGroup 内容管理
     * @apiDescription 通过该接口, 更新文章
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/articles/43
     * @apiUse paramArticle
     * @apiUse returnArticle
     * @apiUse GeneralError
     */
///cms/JavaSource/com/fantasy/cms/service/BannerService.java
/**
 * 轮播图Service
 * 
 * @author li_huang
 * 
 */
///cms/JavaSource/com/fantasy/cms/service/CmsService.java
/**
 * CMS service
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-5-28 下午03:05:30
 */
/**
     * 获取全部栏目
     *
     * @return List<ArticleCategory>
     */
/**
     * 文章查询方法
     *
     * @param pager    翻页对象
     * @param filters 筛选条件
     * @return string
     */
/**
     * 保存栏目
     *
     * @param category 分类对象
     * @return string
     */
/**
     * 得到栏目
     *
     * @param code categoryCode
     * @return ArticleCategory
     */
/**
     * 移除栏目
     *
     * @param codes 栏目 Code
     */
/**
     * 保存文章
     *
     * @param article 文章对象
     * @return Article
     */
/**
     * 获取文章
     *
     * @param id 文章id
     * @return Article
     */
/**
     * 发布文章
     *
     * @param ids 文章 ids
     */
/**
     * 关闭文章
     *
     * @param ids 文章 ids
     */
/**
     * 删除文章
     *
     * @param ids 文章 ids
     */
/**
     * 文章分类编码唯一
     *
     * @param code 分类 code
     * @return boolean
     */
/**
     * 移动文章
     *
     * @param ids          文章 ids
     * @param categoryCode 分类 categoryCode
     */
/**
     * 根据分类code跳转不同界面
     *
     * @param templateUrl 模板 url
     * @param object      object
     * @return String
     */
///cms/JavaSource/com/fantasy/cms/service/TopicService.java
/**
 * 专题服务
 */
///cms-web/JavaSource/com/fantasy/cms/web/BannerAction.java
/**
 * 轮播图Action
 *
 * @author li_huang
 */
/**
     * 搜索
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     *
     * @param banner
     * @return
     */
/**
     * 更新
     *
     * @param banner
     * @return
     */
/**
     * 根据ID查询对象
     *
     * @param id
     * @return
     */
/**
     * 删除
     *
     * @param id
     * @return
     */
///cms-web/JavaSource/com/fantasy/cms/web/CmsAction.java
/**
 * 文章
 */
/**
     * 文章主页面
     *
     * @return string
     */
/**
     * 文章列表页
     *
     * @return string
     */
/**
     * 文章搜索
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return string
     */
/**
     * 文章保存
     *
     * @param article 文章对象
     * @return string
     */
/**
     * 文章修改
     *
     * @param id 文章id
     * @return string
     */
/**
     * 文章显示
     *
     * @param id 文章id
     * @return string
     */
/**
     * 发布文章
     *
     * @param ids 文章id
     * @return string
     */
/**
     * 关闭文章
     *
     * @param ids 文章id
     * @return string
     */
/**
     * 文章批量删除
     *
     * @param ids 文章id
     * @return string
     */
/**
     * 文章栏目保存
     *
     * @param category 文章分类
     * @return string
     */
/**
     * 文章栏目修改
     *
     * @param id 分类code
     * @return string
     */
/**
     * 删除文章栏目
     *
     * @param ids 分类code
     * @return string
     */
/**
     * 文章栏目添加
     *
     * @param categoryCode 分类code
     * @return string
     */
/**
     * 文章添加
     *
     * @param categoryCode 分类code
     * @return string
     */
/**
     * 移动文章
     *
     * @param ids          文章ids
     * @param categoryCode 分类code
     * @return string
     */
///cms-web/JavaSource/com/fantasy/cms/web/CmsBannerAction.java
/**
 * 轮播图Action
 *
 * @author li_huang
 *
 */
/**
     * 首页
     *
     * @return
     */
/**
     * 搜索
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     *
     * @param banner
     * @return
     */
/**
     * 修改
     *
     * @param id
     * @return
     */
/**
     * 删除
     *
     * @param ids
     * @return
     */
///cms-web/JavaSource/com/fantasy/cms/web/TopicAction.java
/**
 * 专题Action
 */
/**
     * 用于显示列表页面
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return struts 返回码
     */
/**
     * 用于异步查询
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return struts 返回码
     */
/**
     * 根据 id 查询对应的专题，一般用于查看或者编辑
     *
     * @param code 主键
     * @return struts 返回码
     */
/**
     * 保存专题
     *
     * @param topic 专题对象
     * @return struts 返回码
     */
/**
     * @param codes 主键数组
     * @return struts 返回码
     */
///core/JavaSource/com/fantasy/album/bean/Photo.java
/**
     * 名称
     */
/**
     * 描述
     */
/**
     * 拍摄时间
     */
/**
     * 拍摄地点
     */
///core/JavaSource/com/fantasy/album/bean/PhotoAlbum.java
/**
     * 相册名称
     */
///core/JavaSource/com/fantasy/attr/framework/converter/CustomBeanTypeConverter.java
/**
 * Created by hebo on 2015/3/20.
 * 动态bean转换
 */
///core/JavaSource/com/fantasy/attr/framework/CustomBeanFactory.java
/**
 * 自定义表单
 */
///core/JavaSource/com/fantasy/attr/framework/DefaultCustomBeanFactory.java
/**
 * 默认的自定义Bean工厂
 */
///core/JavaSource/com/fantasy/attr/framework/DynaBean.java
/**
 * 动态Bean接口
 */
/**
     * 获取接口版本定义
     *
     * @return AttributeVersion
     */
/**
     * 获取动态属性
     *
     * @return List<AttributeValue>
     */
/**
     * 设置动态属性
     *
     * @param attributeValues List<AttributeValue>
     */
///core/JavaSource/com/fantasy/attr/framework/query/DynaBeanQuery.java
/**
 * 动态bean，查询扩展
 */
///core/JavaSource/com/fantasy/attr/framework/query/DynaBeanQueryManager.java
/**
 * 用于动态属性上下文
 */
/**
     * 动态属性
     */
///core/JavaSource/com/fantasy/attr/interceptor/AttributeValueInterceptor.java
/**
     * findPager 时，对动态Bean 添加代理
     *
     * @param pjp     ProceedingJoinPoint
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return pager
     * @throws Throwable
     */
/**
     * find 时，对动态Bean 添加代理
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
/**
     * 保存代理对象时，将代理对象转为原来的类型
     *
     * @param pjp    ProceedingJoinPoint
     * @param entity hibernateEntity
     * @return object
     * @throws Throwable
     */
/**
     * find 时，对动态Bean 添加代理
     *
     * @param pjp ProceedingJoinPoint
     * @return Object
     * @throws Throwable
     */
///core/JavaSource/com/fantasy/attr/storage/BaseDynaBean.java
/**
     * 数据版本
     */
/**
     * 动态属性集合。
     */
/**
     * 字段缓存
     */
///core/JavaSource/com/fantasy/attr/storage/bean/Attribute.java
/**
 * 属性表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:54:48
 */
/**
     * 属性编码(用于自定义界面时，方便操作) 逻辑上，一组配置(如某个栏目扩展字段)中不应该出现两个相同的code
     */
/**
     * 属性名称
     */
/**
     * 非临时属性<br/>
     * 临时属性，会在Version删除后自动删除。
     */
/**
     * 非空
     */
/**
     * 类型
     */
/**
     * 描述
     */
/**
     * 属性对于的值集合
     */
///core/JavaSource/com/fantasy/attr/storage/bean/AttributeType.java
/**
 * 属性类型表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-15 下午11:57:36
 */
/**
     * 属性类型 (java className)
     */
/**
     * 关联的外键字段
     */
/**
     * 属性类型对应的转换器
     */
/**
     * 属性类型名称
     */
/**
     * 属性描述信息
     */
///core/JavaSource/com/fantasy/attr/storage/bean/AttributeValue.java
/**
 * 属性值表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午06:10:12
 */
/**
     * 属性对象
     */
/**
     * 数据版本
     */
/**
     * 关联对象对应的id
     */
/**
     * 属性值
     */
///core/JavaSource/com/fantasy/attr/storage/bean/AttributeVersion.java
/**
 * 动态属性版本表
 */
/**
         * 完全自定义的bean
         */
/**
         * 普遍 javabean 扩展属性
         */
/**
     * 对应的className
     */
/**
     * 版本号
     */
/**
     * 属性
     */
/**
     * 版本对应的 attr values
     */
/**
     * 属性在版本中的排序规则
     */
///core/JavaSource/com/fantasy/attr/storage/bean/Converter.java
/**
 * Attribute 转换器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-2-1 下午03:09:05
 */
/**
     * 转换器名称
     */
/**
     * 转换器本身的 Class
     */
/**
     * 转换器描述
     */
///core/JavaSource/com/fantasy/attr/storage/bean/CustomBean.java
/**
     * 自定义 bean 的名称
     */
/**
     * 对应的版本
     */
/**
     * 属性
     */
///core/JavaSource/com/fantasy/attr/storage/bean/CustomBeanDefinition.java
/**
     * 自定义 bean 的名称
     */
/**
     * 自定义类的
     */
/**
     * 数据版本
     */
/**
     * 属性
     */
/**
     * 属性
     */
///core/JavaSource/com/fantasy/attr/storage/bean/Renderer.java
/**
 * 字段渲染器
 */
/**
     * 名称
     */
/**
     * 新增模板
     */
/**
     * 编辑模板
     */
///core/JavaSource/com/fantasy/attr/storage/bean/Screen.java
/**
 * 字段界面定义
 */
/**
     * 名称
     */
///core/JavaSource/com/fantasy/attr/storage/service/AttributeService.java
/**
 * 商品属性 service
 */
/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
/**
	 * 根据code查找属性
	 * @param code
	 * @return
	 */
/**
	 * 商品属性编码唯一
	 * @param code
	 * @param id
	 * @return
	 */
/**
	 * 获取所有属性
	 * @return
	 */
/**
	 * 分页查询
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 删除
	 * @param ids
	 */
/**
	 * 根据ID获取属性
	 * @param id
	 * @return
	 */
/**
	 * 获取所有属性
	 * @return
	 */
///core/JavaSource/com/fantasy/attr/storage/service/AttributeTypeService.java
/**
 * 商品属性类型service
 *
 * @author mingliang
 */
/**
     * 获取所有商品属性类型
     *
     * @return
     */
///core/JavaSource/com/fantasy/attr/storage/service/AttributeVersionService.java
/**
     * 通过 version id 加载全部版本相关的完整数据
     *
     * @param className 版本对应的 class
     * @param number    版本号
     * @return AttributeVersion
     */
/**
     * 获取class 原有属性及版本属性 名称
     *
     * @param className
     * @return
     */
/**
     * 获取版本列表
     *
     * @return List<AttributeVersion>
     */
/**
     * 静态获取版本列表
     *
     * @return List<AttributeVersion>
     */
///core/JavaSource/com/fantasy/attr/storage/service/ConverterService.java
/**
     * 分页查询转换器
     *
     * @param pager
     * @param filters
     * @return
     */
///core/JavaSource/com/fantasy/attr/storage/service/CustomBeanDefinitionService.java
/**
     * 保存 自定义bean 定义
     *
     * @param className  className
     * @param name       中文描述
     * @param attributes 自定义字段
     */
/**
     * 保存 自定义bean 定义
     *
     * @param className  className
     * @param name       中文描述
     * @param attributes 自定义字段
     */
///core/JavaSource/com/fantasy/common/bean/enums/DataType.java
/**
 * 扩展属性的数据类型
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-11-4 下午06:52:07
 * @version 1.0
 */
///core/JavaSource/com/fantasy/common/bean/enums/TimeUnit.java
/**
 * 时间序列
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-23 上午10:30:04
 * @version 1.0
 */
///core/JavaSource/com/fantasy/common/bean/FtpConfig.java
/**
 * Ftp 连接配置
 * 
 * @author 李茂峰
 * @since 2013-7-12 下午02:32:45
 * @version 1.0
 */
/**
	 * 名称
	 */
/**
	 * 端口
	 */
/**
	 * FTP服务地址
	 */
/**
	 * 登陆名称
	 */
/**
	 * 登陆密码
	 */
/**
	 * 编码格式
	 */
/**
	 * 默认目录
	 */
/**
	 * 描述
	 */
///core/JavaSource/com/fantasy/common/bean/HotKeywords.java
/**
 * 热门关键词
 */
/**
	 * 功能唯一标识
	 */
/**
	 * 关键词
	 */
/**
	 * 搜索次数
	 */
/**
	 * 时间单位
	 */
/**
	 * 可能的值<br/>
	 * 天、周、月、年
	 */
///core/JavaSource/com/fantasy/common/bean/JdbcConfig.java
/**
 * 数据库连接配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:32:06
 */
/**
     * 数据库名称
     */
/**
     * 数据库类型
     */
/**
     * 地址
     */
/**
     * 端口
     */
/**
     * 数据库名称
     */
/**
     * 用户名
     */
/**
     * 密码
     */
/**
     * 引用程序自身的datasource
     */
///core/JavaSource/com/fantasy/common/bean/Keywords.java
/**
 * 自定义关键词表(相当于词库)
 * 
 * @author 李茂峰
 * @since 2013-12-23 下午3:25:46
 * @version 1.0
 */
/**
	 * 类型
	 */
/**
	 * 词组
	 */
///core/JavaSource/com/fantasy/common/interceptor/AreaChangeInterceptor.java
/**
 * 用于生成area.js方便js调用
 * 
 * @author 李茂峰
 * @since 2013-4-25 下午06:24:06
 * @version 1.0
 */
///core/JavaSource/com/fantasy/common/interceptor/KeywordsInterceptor.java
/**
 * 扩展关键字操作
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-24 上午10:14:31
 * @version 1.0
 */
///core/JavaSource/com/fantasy/common/service/AreaService.java
/**
     * 获取全部的地区
     *
     * @return
     */
/**
     * 分页
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 查询所有区域
     *
     * @return
     */
/**
     * 返回地区查询列表
     *
     * @return
     */
///core/JavaSource/com/fantasy/common/service/FtpServiceFactory.java
/**
 * FtpServiceFactory
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-5 下午4:57:11
 * @version 1.0
 */
/**
	 * 更新FtpService连接信息
	 * 
	 * @功能描述
	 * @param config
	 */
/**
	 * 删除一个指定的FtpService
	 * 
	 * @功能描述
	 * @param config
	 */
/**
	 * 通过Id获取FtpService对象
	 * 
	 * @功能描述
	 * @param id
	 * @return
	 */
///core/JavaSource/com/fantasy/common/service/JdbcConfigService.java
/**
     * 获取列表
     *
     * @return
     */
/**
     * 静态获取列表
     *
     * @return
     */
///core/JavaSource/com/fantasy/common/service/KeywordService.java
/**
 * 热门搜索关键词
 */
/**
	 * 获取热门关键字
	 * 
	 * @功能描述
	 * @param timeUnit
	 * @param target
	 * @param size
	 * @return
	 */
/**
	 * 关键词
	 * 
	 * @功能描述 该功能会自动将关键词保存到 HotKeywords
	 * @param key
	 * @param text
	 */
/**
	 * 保存单个关键字
	 * 
	 * @功能描述
	 * @param key
	 * @param keywords
	 */
///core/JavaSource/com/fantasy/contacts/bean/Address.java
/**
 * 地址表
 * 
 * @author 李茂峰
 * @since 2013-3-15 上午11:11:55
 * @version 1.0
 */
/**
	 * 1.住宅<br/>
	 * 2.工作<br/>
	 * 3.其他(可自定义)
	 */
/**
	 * 国家
	 */
/**
	 * 省份
	 */
/**
	 * 城市
	 */
/**
	 * 街道
	 */
/**
	 * 邮政编码
	 */
/**
	 * 联系人
	 */
///core/JavaSource/com/fantasy/contacts/bean/Book.java
/**
 * 地址薄(Address Book)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-15 上午11:53:44
 * @version 1.0
 */
/**
	 * 所有者
	 */
/**
	 * 所有者类型<br/>
	 * 1.个人<br/>
	 * 2.部门<br/>
	 * 3.角色<br/>
	 * 4.联系薄<br/>
	 */
/**
	 * 所有联系人
	 */
/**
	 * 联系人分组列表
	 */
///core/JavaSource/com/fantasy/contacts/bean/Group.java
/**
 * 联系人群组
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-15 上午11:29:20
 * @version 1.0
 */
/**
	 * 所属通讯录
	 */
///core/JavaSource/com/fantasy/contacts/bean/Linkman.java
/**
 * 联系人表
 * 
 * @author 李茂峰
 * @since 2013-3-15 上午11:11:59
 * @version 1.0
 */
/**
	 * 所属通讯录
	 */
/**
	 * 联系人照片
	 */
/**
	 * 姓名
	 */
/**
	 * 性别
	 */
/**
	 * 公司
	 */
/**
	 * 部门
	 */
/**
	 * 职位
	 */
/**
	 * 移动电话
	 */
/**
	 * E-mail
	 */
/**
	 * 网址
	 */
/**
	 * 备注
	 */
/**
	 * 所属分组<br/>
	 * 多个分组以;分割
	 */
///core/JavaSource/com/fantasy/contacts/bean/LinkmanAttribute.java
/**
 * 自定义字段
 * 
 * @author 李茂峰
 * @since 2013-3-15 上午11:18:06
 * @version 1.0
 */
/**
	 * 所有者类型<br/>
	 * 1.全部<br/>
	 * 2.个人<br/>
	 * 3.部门<br/>
	 * 4.角色<br/>
	 * 5.联系薄<br/>
	 */
/**
	 * 所有者
	 */
/**
	 * 字段应用位置<br/>
	 * 1.联系人<br/>
	 * 2.地址<br/>
	 * 3.邮箱<br/>
	 * 4.电话<br/>
	 */
/**
	 * 字段名称
	 */
///core/JavaSource/com/fantasy/file/bean/Directory.java
/**
 * 文件上传时，为其指定的上传目录
 * 
 * 通过Key获取上传的目录及文件管理器
 * @author 李茂峰
 * @since 2013-7-23 上午10:30:06
 * @version 1.0
 */
/**
	 * 对应的文件管理器
	 */
/**
	 * 对应的默认目录
	 */
/**
	 * 目录名称
	 */
///core/JavaSource/com/fantasy/file/bean/FileDetail.java
/**
 * 文件信息表
 *
 * @author 软件
 */
/**
     * 虚拟文件路径
     */
/**
     * 文件名称
     */
/**
     * 文件后缀名
     */
/**
     * 文件类型
     */
/**
     * 描述
     */
/**
     * 文件长度
     */
/**
     * 文件MD5码
     */
/**
     * 文件真实路径
     */
/**
     * 文件夹
     */
/**
     * 设置 文件路径(文件系统中的路径，非虚拟路径)
     *
     * @param absolutePath 文件路径
     */
/**
     * 获取 文件路径(文件系统中的路径，非虚拟路径)
     *
     * @return java.lang.String
     */
/**
     * 设置 文件名称
     *
     * @param fileName 文件名
     */
/**
     * 获取 文件名称
     *
     * @return java.lang.String
     */
/**
     * 设置 文件类型
     *
     * @param contentType 文件类型
     */
/**
     * 获取 文件类型
     *
     * @return java.lang.String
     */
/**
     * 设置 描述
     *
     * @param description 文件描述
     */
/**
     * 获取 描述
     *
     * @return java.lang.String
     */
/**
     * 设置 文件长度
     *
     * @param size 文件大小
     */
/**
     * 获取 文件长度
     *
     * @return java.lang.Long
     */
/**
     * 设置 文件夹ID
     *
     * @param folder 文件夹
     */
/**
     * 获取 文件夹ID
     *
     * @return Folder
     */
///core/JavaSource/com/fantasy/file/bean/FileDetailKey.java
/**
     * 虚拟文件路径
     */
///core/JavaSource/com/fantasy/file/bean/FileManagerConfig.java
/**
 * 文件管理器配置表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午02:30:29
 */
/**
     * 唯一别名，作为文件管理器的ID
     */
/**
     * 文件管理器名称
     */
/**
     * 文件管理器的类型
     */
/**
     * 描述
     */
/**
     * 存放配置参数
     */
/**
     * 参数
     */
/*------------------------------ FTP 配置属性-------------------------------
    @JoinColumn(name = "FTP_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_FILE_MANAGER_FTP_CONFIG"))
    @ManyToOne(fetch = FetchType.LAZY)
    private FtpConfig ftpConfig;
    */
/*------------------------------ JDBC 配置属性-------------------------------
    @JoinColumn(name = "JDBC_CONFIG_ID", foreignKey = @ForeignKey(name = "FK_FILE_MANAGER_JDBC_CONFIG"))
    @ManyToOne(fetch = FetchType.LAZY)
    private JdbcConfig jdbcConfig;
    */
/*------------------------------- Local 配置属性 -----------------------------
    @Column(name = "LOCAL_DEFAULT_DIR", length = 350)
    private String localDefaultDir;
    */
/*------------------------------- 虚拟目录 配置属性 -----------------------------
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "SOURCE_ID")
    private FileManagerConfig source;
    */
/**
     * 文件管理器对应的目录
     */
/**
     * 文件管理器对应的文件
     */
///core/JavaSource/com/fantasy/file/bean/FilePart.java
/**
     * 完整文件的hash值
     */
/**
     * 片段文件的hash值
     */
/**
     * 总的段数
     */
/**
     * 当前段数
     */
///core/JavaSource/com/fantasy/file/bean/Folder.java
/**
 * 文件夹表
 *
 * @author 软件
 */
/**
     * 同一目录下不允许重名
     */
/**
     * 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     */
/**
     * 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     */
/**
     * 允许上传的文件扩展名
     */
/**
     * 可上传文件大小
     */
/**
     * 是否为叶子目录,叶子目录不能创建子目录
     */
/**
     * 父目录ID
     */
/**
     * 获取子目录列表
     */
/**
     * 附件对应的集合
     */
/**
     * 设置 同一目录下不允许重名
     *
     * @param name 名称
     */
/**
     * 获取 同一目录下不允许重名
     *
     * @return java.lang.String
     */
/**
     * 设置 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     *
     * @param type 类型
     */
/**
     * 获取 文件夹类型:0.系统,1.公共,2.组织（分公司）,3.个人
     *
     * @return java.lang.String
     */
/**
     * 设置 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     *
     * @param typeValue 类型值
     */
/**
     * 获取 文件夹类型:当为公司或者个人时，输入公司和个人的ID
     *
     * @return java.lang.String
     */
/**
     * 设置 允许上传的文件扩展名
     *
     * @param exts 后缀名
     */
/**
     * 获取 允许上传的文件扩展名
     *
     * @return java.lang.String
     */
/**
     * 设置 可上传文件大小
     *
     * @param size 长度
     */
/**
     * 获取 可上传文件大小
     *
     * @return java.lang.Long
     */
/**
     * 设置 是否为叶子目录,叶子目录不能创建子目录
     *
     * @param last 是否为叶节点
     */
/**
     * 获取 是否为叶子目录,叶子目录不能创建子目录
     *
     * @return java.lang.Boolean
     */
///core/JavaSource/com/fantasy/file/FileItem.java
/**
 * 文件接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-8 下午4:49:25
 */
/**
     * 获取文件名
     *
     * @return string
     */
/**
     * 是否为文件夹
     *
     * @return boolean
     */
/**
     * 文件大小
     *
     * @return long
     */
/**
     * 文件类型
     *
     * @return contentType
     */
/**
     * 文文件
     *
     * @return FileItem
     */
/**
     * 子文件
     *
     * @return List<FileItem>
     */
/**
     * 绝对路径
     *
     * @return string
     */
/**
     * 最后修改日期
     *
     * @return Date
     */
/**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param filter 过滤器
     * @return List<FileItem>
     */
/**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param selector 选择器
     * @return List<FileItem>
     */
/**
     * 获取 Metadata 信息
     *
     * @return List<FileItem>
     */
/**
     * 获取文件输入流
     *
     * @return InputStream
     * @throws IOException
     */
/**
         * <p>
         * 获取用户自定义的元数据。
         * </p>
         * <p>
         * 同时，元数据字典的键名是不区分大小写的，并且在从服务器端返回时会全部以小写形式返回，
         * 即使在设置时给定了大写字母。比如键名为：MyUserMeta，通过getObjectMetadata接口
         * 返回时键名会变为：myusermeta。
         * </p>
         *
         * @return 用户自定义的元数据。
         */
/**
         * 设置用户自定义的元数据，表示以x-oss-meta-为前缀的请求头。
         *
         * @param userMetadata 用户自定义的元数据。
         */
/**
         * 设置请求头（内部使用）。
         *
         * @param key   请求头的Key。
         * @param value 请求头的Value。
         */
/**
         * 添加一个用户自定义的元数据。
         *
         * @param key   请求头的Key。
         *              这个Key不需要包含OSS要求的前缀，即不需要加入“x-oss-meta-”。
         * @param value 请求头的Value。
         */
/**
         * 获取Last-Modified请求头的值，表示Object最后一次修改的时间。
         *
         * @return Object最后一次修改的时间。
         */
/**
         * 设置Last-Modified请求头的值，表示Object最后一次修改的时间（内部使用）。
         *
         * @param lastModified Object最后一次修改的时间。
         */
/**
         * 获取Expires请求头。
         * 如果Object没有定义过期时间，则返回null。
         *
         * @return Expires请求头。
         */
/**
         * 设置Expires请求头。
         *
         * @param expirationTime 过期时间。
         */
/**
         * 获取Content-Length请求头，表示Object内容的大小。
         *
         * @return Object内容的大小。
         */
/**
         * 设置Content-Length请求头，表示Object内容的大小。
         * 当上传Object到OSS时，请总是指定正确的content length。
         *
         * @param contentLength Object内容的大小。
         * @throws IllegalArgumentException Object内容的长度大小大于最大限定值：5G字节。
         */
/**
         * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
         *
         * @return Object内容的类型，为标准的MIME类型。
         */
/**
         * 获取Content-Type请求头，表示Object内容的类型，为标准的MIME类型。
         *
         * @param contentType Object内容的类型，为标准的MIME类型。
         */
/**
         * 获取Content-Encoding请求头，表示Object内容的编码方式。
         *
         * @return Object内容的编码方式。
         */
/**
         * 设置Content-Encoding请求头，表示Object内容的编码方式。
         *
         * @param encoding 表示Object内容的编码方式。
         */
/**
         * 获取Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
         *
         * @return Cache-Control请求头。
         */
/**
         * 设置Cache-Control请求头，表示用户指定的HTTP请求/回复链的缓存行为。
         *
         * @param cacheControl Cache-Control请求头。
         */
/**
         * 获取Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
         *
         * @return Content-Disposition请求头
         */
/**
         * 设置Content-Disposition请求头，表示MIME用户代理如何显示附加的文件。
         *
         * @param disposition Content-Disposition请求头
         */
/**
         * 获取一个值表示与Object相关的hex编码的128位MD5摘要。
         *
         * @return 与Object相关的hex编码的128位MD5摘要。
         */
/**
         * 返回内部保存的请求头的元数据（内部使用）。
         *
         * @return 内部保存的请求头的元数据（内部使用）。
         */
///core/JavaSource/com/fantasy/file/FileItemFilter.java
/**
 * 文件匹配过滤器
 * 
 * @author 李茂峰
 * @since 2013-9-8 下午4:49:48
 * @version 1.0
 */
///core/JavaSource/com/fantasy/file/FileItemSelector.java
/**
 * 文件选择器
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-8 下午4:50:47
 */
/**
     * 判断是否这个文件或者目录应该被选择
     *
     * @param fileItem 文件对象
     * @return {boolean}
     */
/**
     * 判断这个目录是否应该被遍历
     *
     * @param fileItem 文件对象
     * @return {boolean}
     */
///core/JavaSource/com/fantasy/file/FileManager.java
/**
 * 文件管理接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-9-28 下午12:09:54
 */
/**
     * 将一个 File 对象写入地址对应的文件中
     *
     * @param remotePath 路径
     * @param file       文件对象
     * @throws IOException
     */
/**
     * 将 InputStream 写到地址对应的文件中
     *
     * @param remotePath 路径
     * @param in         输出流
     * @throws IOException
     */
/**
     * 返回一个 out 流，用来接收要写入的内容
     *
     * @param remotePath 路径
     * @return {OutputStream}
     * @throws IOException
     */
/**
     * 通过地址将一个文件写到一个本地地址
     *
     * @param remotePath 路径
     * @param localPath  路径
     * @throws IOException
     */
/**
     * 通过地址将文件写入到 OutputStream 中
     *
     * @param remotePath 路径
     * @param out        输出流
     * @throws IOException
     */
/**
     * 通过一个地址获取文件对应的 InputStream
     *
     * @param remotePath 路径
     * @return 返回 InputStream 对象
     * @throws IOException
     */
/**
     * 获取跟目录的文件列表
     *
     * @return {List<FileItem>}
     */
/**
     * 获取指定路径下的文件列表
     *
     * @param remotePath 路径
     * @return {List<FileItem>}
     */
/**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param selector FileItemSelector
     * @return {List<FileItem>}
     */
/**
     * 通过 FileItemSelector 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param remotePath 路径
     * @param selector   FileItemSelector
     * @return {List<FileItem>}
     */
/**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param filter FileItemFilter
     * @return {List<FileItem>}
     */
/**
     * 通过 FileItemFilter 接口 筛选文件,当前对象必须为文件夹，此方法有效
     *
     * @param remotePath 路径
     * @param filter     FileItemFilter
     * @return {List<FileItem>}
     */
/**
     * 获取目录对应的 FileItem 对象
     *
     * @param remotePath 地址
     * @return {FileItem}
     */
/**
     * 删除地址对应的文件
     *
     * @param remotePath 地址
     */
///core/JavaSource/com/fantasy/file/interceptor/FileContext.java
/**
	 * 从栈中取出元素
	 */
/**
	 * 读取对象，但不取出
	 * 
	 * @return
	 */
/**
	 * 向栈添加元素
	 * 
	 * @param o
	 */
///core/JavaSource/com/fantasy/file/manager/JDBCFileManager.java
/**
 * 使用db存储文件
 */
/**
     * 判断文件是否存在
     *
     * @param absolutePath 文件绝对路径
     * @return {boolean}
     * @throws IOException
     */
///core/JavaSource/com/fantasy/file/manager/UploadFileManager.java
/**
 * 上传文件管理器
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-31 下午5:12:36
 */
/**
     * 对应的真实文件管理器
     */
///core/JavaSource/com/fantasy/file/rest/FileController.java
/**
     * @param file           要上传的文件
     * @param dir            上传的目录标识
     * @param entireFileName 完整文件名
     * @param entireFileDir  完整文件的上传目录标识
     * @param entireFileHash 完整文件Hash值
     * @param partFileHash   分段文件Hash值
     * @param total          总段数
     * @param index          分段序号
     * @return {String} 返回文件信息
     * @throws IOException
     */
///core/JavaSource/com/fantasy/file/service/FileManagerFactory.java
/**
 * FileManager 管理类
 * 1.从数据库。初始化FileManager类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-7-12 下午03:57:31
 */
/**
     * 获取文件管理的单例对象
     *
     * @return FileManagerFactory
     */
/**
     * 获取webroot对应的文件管理器
     *
     * @return FileManager
     */
/**
     * 根据id获取对应的文件管理器
     *
     * @param id Id
     * @return UploadFileManager
     */
/**
     * 根据id获取对应的文件管理器
     *
     * @param id Id
     * @return FileManager
     */
///core/JavaSource/com/fantasy/file/service/FileService.java
/**
     * 获取 Folder 对象
     *
     * @param absolutePath 路径
     * @return {Folder}
     */
/**
     * 获取 Folder 对象
     *
     * @param absolutePath 路径
     * @param parent 上级文件夹
     * @param managerId 文件管理器
     * @return {Folder}
     */
/**
     * 用来转移文件目录<br/> 比如当文章发布时才将图片更新到 http server。一般来说 localization 对应的 FileManager 是不作为上传目录的。只做文件存错。不记录其文件信息
     *
     * @param absolutePath 虚拟目录
     */
/**
     * width、heigth只适用于图片
     *
     * @param absolutePath 虚拟目录
     * @param width        宽
     * @param heigth       高
     */
/**
     * 获取存放文件的绝对路径
     *
     * @param absolutePath  虚拟目录
     * @param fileManagerId 文件管理器Id
     * @return {String}
     */
///core/JavaSource/com/fantasy/file/service/FileUploadService.java
/**
     * 文件上传方法
     * <br/>dir 往下为分段上传参数
     *
     * @param attach         附件信息
     * @param contentType    附件类型
     * @param fileName       附件名称
     * @param dir            附件上传目录Id
     * @param entireFileName 完整文件名称
     * @param entireFileDir  附件完整文件的上传目录信息
     * @param entireFileHash 文件hash值
     * @param partFileHash   分段文件的hash值
     * @param total          分段上传时的总段数
     * @param index          当前片段
     * @return FileDetail
     * @throws IOException
     */
///core/JavaSource/com/fantasy/file/util/ZipUtil.java
/**
	 * zip压缩
	 * 
	 * @功能描述
	 * @param outputStream 要压缩到的流
	 * @param fileItem 文件对象
	 * @param comment 说明信息
	 */
/**
	 * 
	 * @功能描述 
	 * @param outputStream
	 * @param fileItem
	 * @param encoding 压缩时采用的编码
	 * @param comment
	 */
/**
	 * 
	 * @功能描述 
	 * @param outputStream
	 * @param fileItem
	 * @param selector
	 * @param encoding
	 * @param comment
	 */
///core/JavaSource/com/fantasy/framework/comet/CometFilter.java
/**
 * java web 长连接类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-12-3 下午05:44:59
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/annotations/DataSource.java
/**
 * 多数据源情况下，用于标示对应的数据源
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:06:32
 */
///core/JavaSource/com/fantasy/framework/dao/BaseBusEntity.java
/**
     * 创建人
     */
/**
     * 创建时间
     */
/**
     * 最后修改人
     */
/**
     * 最后修改时间
     */
///core/JavaSource/com/fantasy/framework/dao/DaoUtil.java
/**
	 * 多数据分页
	 * 
	 * @功能描述
	 * @param <T>
	 * @param pager
	 * @param param
	 * @param callBacks
	 * @return
	 */
/**
	 * 多数据表分页接口
	 * 
	 * @功能描述
	 * @author 李茂峰
	 * @since 2012-10-31 下午09:01:21
	 * @version 1.0
	 * @param <T>
	 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/AliasToBeanResultTransformer.java
/**
 * 返回结果集转换器
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-12 上午9:52:00
 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/event/PropertyGeneratorSaveOrUpdatEventListener.java
/**
 * 使 GenericGenerator 注解支持非注解的字段生成
 * 
 * @author 李茂峰
 * @since 2013-10-9 下午10:12:33
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/generator/SequenceGenerator.java
/**
 * 自定义序列生成器
 * 
 * @author 李茂峰
 * @since 2013-1-14 下午02:07:25
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/HibernateDao.java
/**
 * hibernate Dao 的默认实现
 *
 * @param <T>
 * @param <PK>
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-11 下午4:17:39
 */
/**
     * 获取当前session
     *
     * @return 返回 session
     */
/**
     * 保存对象 实际调用的是 saveOrUpdate
     *
     * @param entity 保存的对象
     */
/**
     * 更新对象
     *
     * @param entity 要更新的对象
     */
/**
     * 合并对象
     *
     * @param entity 要合并的对象
     */
/**
     * 待测试方法
     *
     * @param m      xxx
     * @param entity 要合并的对象
     */
/**
     * 把一个瞬态的实例持久化
     * <br/>
     * 1，persist把一个瞬态的实例持久化，但是并"不保证"标识符被立刻填入到持久化实例中，标识符的填入可能被推迟 到flush的时间。 <br/>
     * 2，persist"保证"，当它在一个transaction外部被调用的时候并不触发一个Sql Insert，这个功能是很有用的， 当我们通过继承Session/persistence context来封装一个长会话流程的时候，一个persist这样的函数是需要的。 <br/>
     * 3，save"不保证"第2条,它要返回标识符，所以它会立即执行Sql insert，不管是不是在transaction内部还是外部
     *
     * @param entity 需要持久化的对象
     */
/**
     * 自动将游离对象转为持久化对象
     * 需要实现的主要功能 <br/>
     * 1.合并对象<br/>
     * 2.游离对象创建<br/>
     * 3.游离对象自动转持久化对象<br/>
     *
     * @param entity 清理对象
     * @return <T>
     */
/**
     * 删除对象
     *
     * @param entity 删除的实体
     */
/**
     * 通过id删除对象
     *
     * @param id 通过主键删除
     */
/**
     * get 一个对象
     *
     * @param id 根据主键查询
     * @return <T>对象
     */
/**
     * load 一个对象
     *
     * @param id 根据主键查询
     * @return <T>对象
     */
/**
     * 查询全部数据
     *
     * @return List<T>
     */
/**
     * 查询全部数据并排序
     *
     * @param orderBy 排序字段
     * @param isAsc   排序方向
     * @return 返回集合
     */
/**
     * 查询对象返回唯一值,propertyName 值必须是唯一的，value重复将会抛出异常
     *
     * @param propertyName 属性名称
     * @param value        值
     * @return 返回唯一对象
     */
/**
     * 通过主键查询对象,实际采用的是 id in (...) 操作
     *
     * @param ids 主键集合
     * @return 返回对象集合
     */
/**
     * 通过主键查询对象,实际采用的是 id in (...) 操作
     *
     * @param ids id数组
     * @return 返回对象集合
     */
/**
     * 使用hql查询对象,推荐使用 {@link @HibernateDao.find(String,Map<String, ?>)}
     *
     * @param hql    hql语句
     * @param values 参数
     * @return 返回集合
     */
/**
     * 使用hql查询对象
     *
     * @param hql    hql语句
     * @param values 参数
     * @return 返回集合
     */
/**
     * 使用sql查询数据
     *
     * @param sql    sql语句
     * @param values 参数
     * @return 返回集合
     */
/**
     * 查询时自定义返回结果集
     *
     * @param sql         sql语句
     * @param values      参数
     * @param resultClass 返回对象类型
     * @return 返回集合
     */
/**
     * @param pager      翻页对象
     * @param criterions 查询条件
     * @return pager
     */
///core/JavaSource/com/fantasy/framework/dao/hibernate/interceptors/BusEntityInterceptor.java
/**
 * 实体公共属性，自动填充拦截器
 * 
 * @author 李茂峰
 * @since 2012-10-28 下午08:07:30
 * @version 1.0
 */
/**
	 * 默认编辑人
	 */
/**
	 * 默认创建人
	 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/interceptors/MultiEntityInterceptor.java
/**
 * hibernate Interceptor 多实现
 * 
 * @功能描述 默认的hibernate拦截器只能配置一个
 * @author 李茂峰
 * @since 2013-9-12 下午5:01:53
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/hibernate/PropertyFilter.java
/**
 * @apiDefine paramPropertyFilter
 * @apiParam {PropertyFilter} filters  参数格式为:EQS_title=测试
 * @apiVersion 3.3.2
 */
/**
         * 等于
         */
/**
         * 模糊查询
         */
/**
         * 小于
         */
/**
         * 大于
         */
/**
         * 小于等于
         */
/**
         * 大于等于
         */
/**
         * in
         */
/**
         * not in
         */
/**
         * 不等于
         */
/**
         * is null
         */
/**
         * not null
         */
/**
         *
         */
/**
         *
         */
///core/JavaSource/com/fantasy/framework/dao/hibernate/util/ReflectionUtils.java
/**
 * hibernateDao 用到的一些反射方法
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-12 下午5:03:33
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/binding/MyBatisMapperRegistry.java
/**
 * MyBatis Mapper 登记处
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:30:16
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/cache/EhcacheCache.java
/**
 * 扩展mybatis缓存，支持Ehcache
 * 
 * @author 李茂峰
 * @since 2012-10-28 下午08:28:47
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/cache/LoggingEhcache.java
/**
 * 扩展mybatis缓存，支持Ehcache
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:29:52
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/DB2SQLDialect.java
/**
 * DB2 翻页方言
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:11:23
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/Dialect.java
/**
 * MyBatis 方言接口
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:22:02
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/DialectUtil.java
/**
     * 关键字数组
     */
/**
     * sql 排序取反
     *
     * @param over over
     * @return string
     */
/**
     * sql 美化
     * 查询关键字大写
     *
     * @param sql sql
     * @return sql
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/MsSQLDialect.java
/**
 * SqlServer 翻页方言
 * @author 李茂峰
 * @since 2013-1-14 下午02:11:57
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/MultiDialect.java
/**
 * 多数据源时方言
 * 
 * @功能描述 当使用@DataSource区分数据库时，用于匹配不同的数据源
 * @author 李茂峰
 * @since 2012-10-28 下午08:16:54
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/MySQLDialect.java
/**
 * MySql 翻页方言
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:12:16
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/dialect/OraSQLDialect.java
/**
 * oracle 翻页方言
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-14 下午02:12:27
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/interceptors/AutoKeyInterceptor.java
/**
 * 注解序列拦截器(扩展mybatis注解主键生成)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:13:36
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/interceptors/BusEntityInterceptor.java
/**
 * 实体公共属性，自动填充拦截器
 * 
 * @author 李茂峰
 * @since 2012-10-28 下午08:09:13
 * @version 1.0
 */
/**
	 * 默认编辑人
	 */
/**
	 * 默认创建人
	 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/interceptors/LimitInterceptor.java
/**
 * 扩展翻页实现
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:08:34
 */
/**
     * @param invocation invocation
     * @param queryArgs  queryArgs
     * @return Object
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
/**
     * 返回 查询调用总的pager对象
     *
     * @param parameterObject Object
     * @return Pager
     */
/**
     * 查询结果集的总数据条数
     *
     * @param ms        MappedStatement
     * @param parameter parameter
     * @return int
     */
/**
     * 查询显示页对应的结果集
     *
     * @param invocation      invocation
     * @param ms              MappedStatement
     * @param parameterObject parameterObject
     * @return List
     * @throws Throwable
     */
/**
     * 获取翻页查询中查询总条数的SqlSource对象
     *
     * @param mappedStatement MappedStatement
     * @param parameterObject parameterObject
     * @return SqlSource
     */
/**
     * @param mappedStatement MappedStatement
     * @param parameterObject parameterObject
     * @return SqlSource
     * @throws Throwable
     */
/**
     * 获取配置的sql
     *
     * @param mappedStatement MappedStatement
     * @param parameterObject parameterObject
     * @return String
     */
/**
     * 复制MappedStatement对象，并使用SqlSource来构建复制的新对象
     *
     * @param mappedStatement MappedStatement
     * @param sqlSource       SqlSource
     * @return MappedStatement
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/interceptors/MultiDataSourceInterceptor.java
/**
 * 多数据源拦截器
 * 
 * @功能描述 提取Mapper接口上的@DataSource信息
 * @author 李茂峰
 * @since 2012-10-28 下午07:58:04
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/bean/Sequence.java
/**
 * 序列
 * 
 * @author 李茂峰
 * @since 2013-8-24 上午10:35:57
 * @version 1.0
 */
/**
	 * 序列名称
	 */
/**
	 * 序列值
	 */
/**
	 * 原始值
	 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/dao/SequenceDao.java
/**
 * MyBatis SequenceDao 接口
 */
/**
     * 查询序列
     *
     * @param keyName keyName
     * @return Sequence
     */
/**
     * 创建序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
/**
     * 更新序列
     *
     * @param sequence sequence
     * @return int 影响行数
     */
/**
     * 获取表中数据的max(id)
     *
     * @param table 表明
     * @param key   字段
     * @return max id
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/GUIDKeyGenerator.java
/**
 * GUID序列生成器
 * 
 * @author 李茂峰
 * @since 2013-1-14 下午02:09:32
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/MultiKeyGenerator.java
/**
 * 将多个KeyGenerator对象封装为一个
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-14 下午02:09:05
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/SequenceKeyGenerator.java
/**
 * 序列生成器
 * 
 * @author 李茂峰
 * @since 2013-1-14 下午02:08:52
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/service/SequenceService.java
/**
     * 判断序列是否存在
     *
     * @param key 序列名称
     * @return boolean
     */
/**
     * 获取序列的下一个值
     *
     * @param key      序列名称
     * @param poolSize 序列增长值
     * @return long
     */
/**
     * 创建一个新的序列
     *
     * @param key      序列名称
     * @param poolSize 序列增长值
     * @return long
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/keygen/util/SequenceInfo.java
/**
 * 序列信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-24 上午10:40:58
 */
/**
     * 提供直接通过 SequenceInfo 直接查询索引的方法
     *
     * @param keyName KeyName
     * @return long
     */
/**
     * 从数据库检索
     */
/**
     * 获取序列的下个值
     *
     * @param keyName  keyName
     * @param poolSize 缓冲值
     * @return long
     */
/**
     * 创建新的序列
     *
     * @param keyName  keyName
     * @param poolSize 缓冲值
     * @return long
     */
/**
     * 获取序列的下一个值
     *
     * @return long
     */
/**
     * 获取缓存序列的最大值
     *
     * @return long
     */
/**
     * 获取缓存序列的最小值
     *
     * @return long
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/proxy/MyBatisMapperMethod.java
/**
 * MyBatis Mapper 方法
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-10-28 下午08:39:09
 */
/**
     * @param declaringInterface Mapper接口
     * @param method             java.lang.reflect.Method;对象
     * @param sqlSession         SqlSession
     */
/**
     * 将参数转换为Map<String, Object>
     *
     * @param args 查询参数
     * @return Map
     */
/**
     * 检查Pager对象是否为空，为空初始化一个新的Pager对象
     *
     * @param page Pager<Object>
     * @return Pager<Object>
     */
/**
     * 获取方法的名称和参数下标
     */
/**
     * 获取Param注解的名称
     *
     * @param i         位置下标
     * @param paramName 参数名称
     * @return 注解名称
     */
/**
     * 获取方法对应 sql Mapper 中的 command Name
     */
/**
     * 验证 CommandType 是否为Select查询
     */
///core/JavaSource/com/fantasy/framework/dao/mybatis/proxy/MyBatisMapperProxy.java
/**
 * MyBatis Mapper 代理
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-28 下午08:33:14
 * @version 1.0
 */
/**
	 * 不需要代理的方法
	 */
/**
	 * 生成代理方法
	 * 
	 * @param <T> 泛型
	 * @param mapperInterface mapper接口
	 * @param sqlSession sqlSession
	 * @return T
	 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/sqlmapper/SqlMapper.java
/**
 * Mapper 的标示接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-10-29 下午09:04:07
 */
///core/JavaSource/com/fantasy/framework/dao/mybatis/type/CharEnumTypeHandler.java
/**
 * 枚举转字符的Handler
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-10-29 下午09:04:53
 * @version 1.0
 * @param <E>
 */
///core/JavaSource/com/fantasy/framework/dao/Pager.java
/**
 * @apiDefine paramPager
 * @apiParam {int} currentPage  要显示的页码
 * @apiParam {int} pageSize  每页显示数据条数
 * @apiParam {String} orderBy  排序字段
 * @apiParam {String} order  排序方向
 * @apiVersion 3.3.2
 */
/**
 * @apiDefine returnPager
 * @apiSuccess {int} currentPage  当前页码
 * @apiSuccess {int} pageSize  每页显示的数据条数
 * @apiSuccess {int} totalCount  最大数据条数
 * @apiSuccess {int} totalPage  总页数
 * @apiSuccess {String} orderBy  排序字段
 * @apiSuccess {String} order  排序方向
 * @apiSuccess {List} pageItems 当页数据集合
 * @apiVersion 3.3.2
 */
/**
     * 最大数据条数
     */
/**
     * 每页显示的数据条数
     */
/**
     * 总页数
     */
/**
     * 当前页码
     */
/**
     * 开始数据索引
     */
/**
     * 排序字段
     */
/**
     * 获取总页码
     *
     * @return 总页数
     */
/**
     * 设置总页数
     *
     * @param totalPage 总页数
     */
/**
     * 获取每页显示的条数
     *
     * @return 每页显示条数
     */
/**
     * 设置显示的页码 注意是页码
     *
     * @param currentPage 当前页码
     */
/**
     * 设置总数据条数
     *
     * @param totalCount 总数据条数
     */
/**
     * 返回翻页开始位置
     *
     * @param first 数据开始位置
     */
/**
     * 设置每页显示数据的条数
     *
     * @param pageSize 每页显示数据条数
     */
/**
     * 获取当前显示的页码
     *
     * @return currentPage
     */
/**
     * 获取数据总条数
     *
     * @return totalCount
     */
///core/JavaSource/com/fantasy/framework/freemarker/FreeMarkerConfigurationFactoryBean.java
/**
 * 扩展 spring FreeMarkerConfigurationFactory 类
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2012-12-3 下午02:13:53
 */
/**
     * 生成Freemarker 静态方法类
     *
     * @param packageName 为类的全路径
     * @return TemplateModel
     */
///core/JavaSource/com/fantasy/framework/freemarker/FreeMarkerTemplateUtils.java
/**
     * @param data 初始数据
     * @param t    模板对象
     * @param out  输出流
     */
/**
     * @param data 初始数据
     * @param t    模板对象
     * @param out  输出流
     */
///core/JavaSource/com/fantasy/framework/freemarker/loader/DataSourceTemplateLoader.java
/**
 * 用于freemarker从数据库装载template文件
 * 
 * 
 * 属性值配置实例,将生成如下的load sql:
 * 
 * <pre>
 * // select template_content from template where template_name=?
 * DataSourceTemplateLoader loader = new DataSourceTemplateLoader();
 * loader.setDataSource(ds);
 * loader.setTableName(&quot;template&quot;);
 * loader.setTemplateNameColumn(&quot;template_name&quot;);
 * loader.setTemplateContentColumn(&quot;template_content&quot;);
 * loader.setTimestampColumn(&quot;last_modified&quot;);
 * </pre>
 * 
 * mysql的表创建语句:
 * 
 * <pre>
 * CREATE TABLE template (
 *  id bigint(20) PRIMARY KEY,
 *  template_name varchar(255) ,
 *  template_content text ,
 *  last_modified timestamp
 * 
 * </pre>
 * 
 * @author badqiu
 * 
 */
///core/JavaSource/com/fantasy/framework/freemarker/loader/FreemarkerTemplateException.java
/**
 * FreemarkerException等价的异常类，不过继承之RuntimeException
 * 
 * @author badqiu
 * 
 */
///core/JavaSource/com/fantasy/framework/httpclient/DefaultProtocolSocketFactory.java
/**
 * HttpClient连接单向Https
 * 默认接收所有证书验证的类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-13 下午03:11:53
 */
///core/JavaSource/com/fantasy/framework/httpclient/HttpClientUtil.java
/**
 * 用于模拟http请求
 *
 * @author 李茂峰
 * @version 1.0
 *          依赖 commons-httpclient.jar
 * @since 2012-11-30 下午04:38:14
 */
/**
     * 执行get请求
     *
     * @param url webUrl webUrl
     * @return {Response}
     * @throws IOException
     */
/**
     * 执行一个带参数的get请求
     *
     * @param url webUrl         webUrl
     * @param queryString 请求参数字符串
     * @return {Response}
     * @throws IOException
     */
/**
     * 执行一个带参数的get请求
     *
     * @param url webUrl webUrl
     * @param params 请求参数 请求参数
     * @return {Response}
     * @throws IOException
     */
/**
     * 执行一个带请求信息的get请求
     *
     * @param url webUrl webUrl
     * @param request 请求对象 请求对象
     * @return {Response}
     * @throws IOException
     */
/**
     * 执行一个带参数的post请求
     *
     * @param url webUrl
     * @param params 请求参数
     * @return {Response}
     */
/**
     * 执行一个带请求信息的post请求
     *
     * @param url webUrl
     * @param request 请求对象
     * @return {Response}
     * @throws IOException
     */
/**
     * 响应流封装类
     * 请求完成并不会马上解析响应。所以需要缓存InputStream，并在成功读取后自动关闭连接
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2012-11-30 下午04:42:25
     */
///core/JavaSource/com/fantasy/framework/httpclient/Request.java
/**
 * HttpClient 请求对象
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-30 下午04:45:33
 */
///core/JavaSource/com/fantasy/framework/httpclient/Response.java
/**
 * HttpClient 响应对象
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-30 下午04:46:02
 */
/**
     * 状态码
     *
     * @return status
     */
/**
     * 按纯文本格式获取响应信息
     *
     * @return text
     * @throws IOException
     */
/**
     * 按纯文本格式获取响应信息
     *
     * @param pretty 是否显示换行
     * @return text
     * @throws IOException
     */
/**
     * 按纯文本格式获取响应信息
     *
     * @param charset 按指定的编码格式解析
     * @return text
     * @throws IOException
     */
/**
     * 按纯文本格式获取响应信息
     *
     * @param charset 按指定的编码格式解析
     * @param pretty  是否显示换行
     * @return text
     * @throws IOException
     */
/**
     * 按json格式反序列化响应信息
     *
     * @param <T>     泛型类型
     * @param clazz   要转化的类型
     * @param charset 编码格式
     * @return T
     * @throws IOException
     */
/**
     * 缓存响应流
     *
     * @return ByteArrayOutputStream
     * @throws IOException
     */
/**
     * 获取请求头信息
     *
     * @param headerName headerName
     * @return Header
     */
/**
     * 获取响应头信息
     *
     * @param headerName headerName
     * @return Header
     */
/**
     * 设置响应输入流
     *
     * @param responseInputStream 响应流
     */
/**
     * 将相应写入到流中 从缓存区读取，不存在缓存区则建立缓存流
     *
     * @param out   要写入的流对象
     * @param cache 是否缓存
     * @throws IOException
     */
/**
     * 将相应写入到流中 如果缓存区存在，读取缓存区
     *
     * @param out 输出流
     * @throws IOException
     */
/**
     * 将响应信息写入到本地文件
     *
     * @param absolutePath 文件路径
     * @throws IOException
     */
/**
     * 将响应信息写入到本地文件
     *
     * @param file 文件对象
     * @throws IOException
     */
/**
     * 获取响应信息的类型
     *
     * @return contentType
     */
/**
     * 获取响应信息的大小
     *
     * @return length
     */
/**
     * 获取响应信息的编码格式
     *
     * @return {encode}
     */
/**
     * 获取cookie信息
     *
     * @param name cookieName
     * @return value
     */
/**
     * 获取响应流
     * 如果缓存过，获取缓存的流
     *
     * @return InputStream
     * @throws IOException
     */
///core/JavaSource/com/fantasy/framework/io/Connection.java
/**
     * @return {Connection}
     * @throws IOException
     * @功能描述
     */
/**
     * 是否空闲
     *
     * @return {boolean}
     * @功能描述
     */
/**
     * 是否挂起
     *
     * @return {boolean}
     * @功能描述
     */
/**
     * 关闭连接
     *
     * @功能描述
     */
///core/JavaSource/com/fantasy/framework/jcaptcha/backgroundgenerator/JarReaderRandomBackgroundGenerator.java
/**
 * 与FileReaderRandomBackgroundGenerator类似,但是从jar中加载背景图片
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-4-9 上午12:44:51
 */
///core/JavaSource/com/fantasy/framework/jcaptcha/FantasyCaptchaService.java
/**
 * 通过读取配置，判断是否验证验证码。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-8-5 下午03:01:10
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/jcaptcha/JCaptchaFilter.java
/**
 * 验证码生成类
 * 
 * @功能描述<br/>依赖 jcaptcha-1.0.jar jcaptcha-api-1.0 imaging.jar
 * @author 李茂峰
 * @since 2012-11-30 下午05:16:03
 * @version 1.0
 */
/**
	 * 生成验证码
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
///core/JavaSource/com/fantasy/framework/log/annotation/Log.java
/**
	 * 简要描述信息
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 判断是否记录日志
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 日志连接器类型
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 日志拦截器
	 * 
	 * @功能描述
	 * @return
	 */
///core/JavaSource/com/fantasy/framework/log/interceptor/LazyParamAwareEvaluationContext.java
/**
	 * Load the param information only when needed.
	 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/BoostSwitch.java
/**
 * 同一个Entity类的不同的Document，可能需要设置不同的权重<br/>
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-23 下午03:15:53
 */
/**
     * 与@IndexFilter的compare含义相同
     *
     * @return Compare
     */
/**
     * 与@IndexFilter的value含义相同
     *
     * @return String
     */
/**
     * float型，表示满足比较条件时该Document的boost值，缺省值为1.0
     *
     * @return float
     */
/**
     * 表示不满足比较条件时该Document的boost值，缺省值为1.0
     *
     * @return float
     */
///core/JavaSource/com/fantasy/framework/lucene/annotations/Compare.java
/**
 * compare有多个枚举值
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:11:40
 * @version 1.0
 */
/**
	 * 等于（==）。支持String、boolean、int、long、float、double、char。
	 */
/**
	 * 不等于（!=）。支持String、boolean、int、long、float、double、char。
	 */
/**
	 * 大于（>）。支持int、long、float、double。
	 */
/**
	 * 大于等于（>=）。支持int、long、float、double。
	 */
/**
	 * 小于（<）。支持int、long、float、double。
	 */
/**
	 * 小于等于（<=）。支持int、long、float、double。
	 */
/**
	 * 为空（==null）。支持Object类型，包括String。这时不需要value参数。
	 */
/**
	 * 不为空（!=null）。支持Object类型，包括String。这时不需要value参数。
	 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/Entity.java
/**
 * 表示需要映射到MongoDB中的一个实体。<br/>
 * 如果设置了capped=true，则需要设置capSize和capMax两者中的其中一个。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:22:48
 * @version 1.0
 */
/**
	 * String型，表示其在MongoDB中的collection的名称。name属性可以省略，默认使用类名的全小写。
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * boolean型，表示该Entity类对应的是Capped Collection，缺省值为false。
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * long型，设置Capped Collection的空间大小，以字节为单位，默认值为-1，表示未设置。
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * long型，设置Capped Collection的最多能存储多少个document，默认值为-1，表示未设置。
	 * 
	 * @功能描述
	 * @return
	 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexEmbed.java
/**
 * 表示需要嵌入对该Embed对象的索引。结合@IndexEmbedBy使用。索引域的名称形如“embed.x”。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:03:13
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexEmbedBy.java
/**
 * 表示需要嵌入到其它对象的@Embed或@EmbedList域的索引中。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:09:11
 * @version 1.0
 */
/**
	 * value——Class类型，表示被引用的类
	 * 
	 * @return class
	 */
/**
	 * analyze——boolean型，表示是否需要分词
	 * 
	 * @return boolean
	 */
/**
	 * store——boolean型，表示是否需要存储
	 * 
	 * @return boolean
	 */
/**
	 * boost——float型，表示该Field的权重
	 * 
	 * @return boolean
	 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexEmbedList.java
/**
 * 表示需要嵌入对该EmbedList对象的索引。结合@IndexEmbedBy使用。索引域的名称形如“embed.x”。
 * 
 * @author 李茂峰
 * @since 2013-1-23 下午03:03:52
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexFilter.java
/**
 * 表示只有满足该条件的实体才会被索引，否则不创建索引。<br/>
 * IndexFilter有2个参数：compare和value。<br/>
 * compare表示比较操作，是枚举类型Compare。value是比较的值，是字符串，会相应的解析成该属性类型的值。 <br/>
 * 在一个Entity类上可以有多个@IndexFilter注解，表示需要同时满足这些条件。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:11:13
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexProperty.java
/**
 * 标注需要索引的字段<br/>
 * 支持的数据类型包括：String、char、boolean、int、long、float、double、Date等基本数据类型。<br/>
 * 还支持上述基本数据类型组成的数组、List、Set等。这些集合中的元素，不管是什么数据类型，都会连结成一个字符串，然后加以索引。
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-23 下午03:01:02
 */
/**
     * boolean型，表示是否需要分词，缺省值为false
     *
     * @return boolean
     */
/**
     * boolean型，表示是否需要存储，缺省值为 false
     *
     * @return boolean
     */
/**
     * float型，表示该Field的权重，缺省值为1.0
     *
     * @return boolean
     */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexRef.java
/**
 * 表示需要嵌入对该Ref对象的索引。结合@IndexRefBy使用。索引域的名称形如“father.name”。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:06:58
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexRefBy.java
/**
 * 表示需要嵌入到其它对象的@Ref或@RefList域的索引中。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:07:36
 * @version 1.0
 */
/**
	 * value——Class类型，表示被引用的类
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * analyze——boolean型，表示是否需要分词
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * store——boolean型，表示是否需要存储
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * boost——float型，表示该Field的权重
	 * 
	 * @功能描述
	 * @return
	 */
///core/JavaSource/com/fantasy/framework/lucene/annotations/IndexRefList.java
/**
 * 表示需要嵌入对该RefList对象的索引。结合@IndexRefBy使用。索引域的名称形如“father.name”。
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-23 下午03:07:20
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/backend/IndexChecker.java
/**
	 * 判断Class是否标注了 @Indexed 注解
	 * 
	 * @功能描述
	 * @param clazz
	 * @return
	 */
/**
	 * 判断Class是否标注@indexed注解或者属性是否标注了@IndexRefBy注解
	 * 
	 * @功能描述
	 * @param clazz
	 * @return
	 */
///core/JavaSource/com/fantasy/framework/lucene/backend/IndexCreator.java
/**
 * 索引文件 生成器
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-27 上午09:46:11
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/backend/IndexRebuildTask.java
/**
     * 每个IndexRebuildTask拥有单独的锁
     */
///core/JavaSource/com/fantasy/framework/lucene/backend/IndexRemoveTask.java
/**
 * 删除索引
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-1-26 下午09:09:59
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/lucene/BuguIndex.java
/**
     * RAMBufferSizeMB
     */
/**
     * Lucene 版本
     */
/**
     * 分词器
     */
/**
     * 索引文件的存放目录
     */
/**
     * 集群配置
     */
/**
     * 线程池
     */
/**
     * 定时任务
     */
/**
     * reopen 执行周期
     */
/**
     * 初始化方法
     */
/**
     * 关闭方法
     */
///core/JavaSource/com/fantasy/framework/lucene/BuguParser.java
/**
     * 单个字段匹配
     *
     * @param field 字段
     * @param value 匹配值
     * @return Query
     */
/**
     * 单个字段匹配 - 匹配开始
     *
     * @param field 匹配字段
     * @param value 匹配值
     * @return Query
     */
/**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 比较值
     * @return Query
     */
/**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 值
     * @param op    匹配方式 包含 and 与 or
     * @return Query
     */
/**
     * 多个字段模糊检索同一个值
     *
     * @param fields 多个字段
     * @param value  值
     * @return Query
     */
/**
     * 多条件查询
     *
     * @param fields 多个字段
     * @param values 多个条件
     * @return Query
     */
/**
     * 一次性查询多个条件
     *
     * @param fields 多个字段
     * @param value  匹配值
     * @param op     匹配方式 包含 and 与 or
     * @return Query
     */
/**
     * 多字段查询
     *
     * @param fields 多个字段
     * @param occurs 条件连接方式，类似于 and 与 or 、not
     * @param value  匹配字符串
     * @return Query
     */
/**
     * 多字段查询
     *
     * @param fields 多个字段
     * @param occurs 条件连接方式，类似于 and 与 or 、not
     * @param values 匹配多个字符串
     * @return Query
     */
/**
     * 多条件查询
     *
     * @param queries 将多个 Query 对象转为 一个
     * @return Query
     */
/**
     * 多条件查询
     *
     * @param queries 要合并的查询
     * @param occurs  条件连接方式，类似于 and 与 or 、not
     * @return Query
     */
/**
     * 使用 QueryParser 进行查询
     *
     * @param parser parser
     * @param value  匹配的值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     */
/**
     * 单个字段检索 - 数值
     *
     * @param field 匹配字段
     * @param value 数值
     * @return Query
     */
/**
     * 单个字段检索 - 数值区间
     *
     * @param field    匹配字段
     * @param minValue 起始值
     * @param maxValue 结束值
     * @return Query
     */
/**
     * 单个字段检索 - 时间区间
     *
     * @param field 匹配字段
     * @param begin 开始时间
     * @param end   结束时间
     * @return Query
     */
/**
     * 单个字段检索 - 时间区间
     *
     * @param field 匹配字段
     * @param begin 开始时间
     * @param end   结束时间
     * @return Query
     */
/**
     * 单个字段检索
     *
     * @param field 匹配字段
     * @param value boolean值
     * @return Query
     */
/**
     * 单个字段模糊检索
     *
     * @param field 字段
     * @param value 比较值
     * @return Query
     */
///core/JavaSource/com/fantasy/framework/lucene/BuguSearcher.java
/**
 * Lucene 查询接口
 *
 * @param <T>
 * @author 李茂峰
 * @version 1.0
 * @since 2013-5-24 上午10:57:11
 */
/**
     * 打开 IndexReader
     *
     * @return IndexSearcher
     * 每次查询开始前调用
     */
/**
     * 关闭 IndexReader
     * <p/>
     * 查询结束时调用
     *
     * @param searcher IndexSearcher
     */
/**
     * 返回查询的结果
     *
     * @param query 查询条件
     * @param size  返回条数
     * @return List<T>
     */
/**
     * 支持翻页及高亮查询
     *
     * @param pager       翻页对象
     * @param query       查询条件
     * @param highlighter 关键字高亮
     * @return Pager<T>
     */
/**
     * @param pager   翻页对象
     * @param query   查询条件
     * @param fields  需要高亮显示的字段
     * @param keyword 高亮关键字
     * @return Pager<T>
     */
/**
     * @param pager 翻页对象
     * @param query 查询条件
     * @return Pager<T>
     */
/**
     * 将javaType 转换为 SortField
     *
     * @param fieldName 字段名称
     * @return int
     */
///core/JavaSource/com/fantasy/framework/lucene/dao/LuceneDao.java
/**
     * 不完善 待调整
     *
     * @param fieldName    字段
     * @param fieldValue 字段值
     * @return List<T>
     */
///core/JavaSource/com/fantasy/framework/lucene/dao/mybatis/EntityChangedInterceptor.java
/**
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-30 下午12:58:37
 */
///core/JavaSource/com/fantasy/framework/net/util/BufferPool.java
/**
 * <li>文件名：BufferPool.java
 * <li>说明：
 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
 * <li>创建日期：2012-2-2
 * <li>修改人： 
 * <li>修改日期：
 */
/**
 * <li>类型名称：
 * <li>说明：直接缓冲区池，重复利用分配的直接缓冲区
 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
 * <li>创建日期：2012-2-2
 * <li>修改人： 
 * <li>修改日期：
 */
/**
	 * 
	 * <li>方法名：getBuffer
	 * <li>@return
	 * <li>返回类型：ByteBuffer
	 * <li>说明：
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-2-2
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：releaseBuffer
	 * <li>@param bb
	 * <li>返回类型：void
	 * <li>说明：释放缓冲区
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-2-2
	 * <li>修改人： 
	 * <li>修改日期：
	 */
///core/JavaSource/com/fantasy/framework/net/util/CoderUtil.java
/**
 * <li>类型名称：
 * <li>说明：
 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
 * <li>创建日期：2011-9-21
 * <li>修改人： 
 * <li>修改日期：
 */
/**
	 * 
	 * <li>方法名：encode
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：ByteBuffer
	 * <li>说明：
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2011-5-3
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：toByte
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：获取字符串的utf-8编码
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2011-11-12
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：toNormalByte
	 * <li>@param str
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：获取平台相关的字节编码
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：decode
	 * <li>@param bb
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2011-5-3
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：toLong
	 * <li>@param b
	 * <li>@return
	 * <li>返回类型：long
	 * <li>说明：将字节数组转换为Long型数据，高字节在前，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：toInt
	 * <li>@param b
	 * <li>@return
	 * <li>返回类型：int
	 * <li>说明：将字节数组转换为整数，高字节在前，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：toShort
	 * <li>@param b
	 * <li>@return
	 * <li>返回类型：short
	 * <li>说明：将字节转换为短整数，高字节在前，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-18
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：numberToByte
	 * <li>@param l
	 * <li>@param length
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：将数字转换为字节数组；从高位向低位取值，高位在前，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：shortToByte
	 * <li>@param i
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：短整形转换为字节数组，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：intToByte
	 * <li>@param i
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：将整数转换为字节数组，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：longToByte
	 * <li>@param i
	 * <li>@return
	 * <li>返回类型：byte[]
	 * <li>说明：将长整形转换为字节数组，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
/**
	 * 
	 * <li>方法名：formatBytes
	 * <li>@param bytes
	 * <li>@return
	 * <li>返回类型：String
	 * <li>说明：将字节数组转换为字符串，主要用于日志的输出，该方法来自网络
	 * <li>创建人：CshBBrain;技术博客：http://cshbbrain.iteye.com/
	 * <li>创建日期：2012-9-19
	 * <li>修改人： 
	 * <li>修改日期：
	 */
///core/JavaSource/com/fantasy/framework/service/FTPService.java
/**
 * FTP工具类
 * <p/>
 * 暂时只提供读写及删除操作,使其可以直接返回文件列表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-4 下午04:11:21
 */
/**
     * 端口
     */
/**
     * 超时毫秒数
     */
/**
     * FTP服务地址
     */
/**
     * 登陆名称
     */
/**
     * 登陆密码
     */
/**
     * 控制台语言
     */
/**
     * 服务器的语言
     */
/**
     * 系统标志
     */
/**
     * 默认目录 TODO 可以加载FTP内容
     */
/**
     * 登陆Ftp服务器
     *
     * @return FTPClient
     * @throws IOException
     */
/**
     * 关闭连接
     *
     * @param ftpClient FTPClient
     */
/**
     * 判断文件或者文件夹是否存在
     *
     * @param pathname 文件路径
     * @return boolean
     * @throws IOException
     */
/**
     * 上传文件到服务器
     *
     * @param local        本地路径
     * @param remoteFolder 远程路径
     * @throws IOException
     */
/**
     * 上传文件到服务器
     *
     * @param localFile    本地文件
     * @param remoteFolder 远程目录
     * @throws IOException
     */
/**
     * 上传单个文件
     *
     * @param in         输入流
     * @param remoteFile 远程路径
     * @param ftpClient  ftpclien对象
     * @throws IOException
     */
/**
     * 上传单个文件
     *
     * @param in         输入流
     * @param remoteFile 远程文件路径
     * @throws IOException
     */
/**
     * 将文件夹上传到FTP服务器
     *
     * @param localFolder  本地目录
     * @param remoteFolder 远程目录
     * @throws IOException
     */
/**
     * 将文件夹上传到FTP服务器
     *
     * @param localFile    本地文件夹对象
     * @param remoteFolder 远程目录
     * @throws IOException
     */
/**
     * @param localFile    本地目录
     * @param remoteFolder 远程目录
     * @param ftpClient    ftpClient
     * @throws IOException
     */
/**
     * 删除文件
     *
     * @param remoteFile 远程目录
     * @throws IOException
     */
/**
     * 删除文件夹
     *
     * @param remoteFolder 远程目录
     * @throws IOException
     */
/**
     * 删除FTP服务器上的文件
     *
     * @param remoteFile 远程目录
     * @param ftpClient  FTPClient
     * @throws IOException
     */
/**
     * 删除FTP服务器上的文件夹
     *
     * @param remoteFolder 远程目录
     * @param ftpClient    FTPClient
     * @throws IOException
     */
/**
     * 下载整个目录到本地
     *
     * @param remoteFolder 远程目录
     * @param localFolder  本地目录
     * @throws IOException
     */
/**
     * 将远程文件写到本地路径下
     *
     * @param remote 远程目录
     * @param local  本地目录
     * @throws IOException
     */
/**
     * 将远程文件下载到文件中
     *
     * @param remote    远程目录
     * @param localFile 本地文件对象
     * @throws IOException
     */
/**
     * 将远程文件写入到OutputStream中
     *
     * @param remote 远程目录
     * @param out    输入流
     * @throws IOException
     */
/**
     * 获取FTP服务器的输出流
     *
     * @param remoteFile 远程目录
     * @param ftpClient  FtpClient对象
     * @return OutputStream
     * @throws IOException
     */
/**
     * 获取FTP服务器的输出流
     *
     * @param remoteFile 远程目录
     * @return OutputStream
     * @throws IOException
     */
///core/JavaSource/com/fantasy/framework/service/LocalizationService.java
/**
 * 将远程文件写到本地磁盘
 * 
 * @功能描述
 * @状态 开发中...
 * @author 李茂峰
 * @since 2013-6-4 下午04:10:19
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/service/MailSendService.java
/**
 * Email 工具类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-4 下午04:07:27
 */
/**
     * 用于将:李茂峰<limaofeng@msn.com>格式的字符串解开
     */
/**
     * 以普通文本的格式发送Email
     *
     * @param to      发送给谁，对应的email
     * @param title   邮件主题
     * @param message 邮件内容 支持普通文本
     */
/**
     * 发送 邮件 给某人
     *
     * @param title    邮件标题
     * @param template 模板名称
     * @param model    数据
     * @param to       发送到的人
     */
/**
     * 支持HTML脚本的格式发送Email
     *
     * @param to      发送给谁，对应的email
     * @param title   邮件标题
     * @param message 邮件内容 支持HTML脚本
     */
/**
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param to       收件人
     */
/**
     * 带附件的邮件
     *
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param attachs  附件信息
     * @param to       收件人
     */
/**
     * 带附件的邮件
     *
     * @param title    标题
     * @param template 模板路径
     * @param model    数据
     * @param attachs  附件信息
     * @param to       收件人
     */
/**
         * 文件名称
         */
/**
         * 文件类型
         */
/**
         * 文件描述
         */
/**
         * freemarker 模板路径
         */
/**
         * freemarker 数据
         */
///core/JavaSource/com/fantasy/framework/spring/ClassPathScanner.java
/*.class";

    private static ClassPathScanner instance = new ClassPathScanner();

    private ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(this.resourcePatternResolver);

    private String resourcePattern = DEFAULT_RESOURCE_PATTERN;

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
        this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
    }

    public static ClassPathScanner getInstance() {
        return instance;
    }

    private final static String CLASSPATH = "classpath*:";

    public Set<String> findTargetClassNames(String basepackage) {
        Set<String> candidates = new LinkedHashSet<String>();
        try {
            String packageSearchPath = CLASSPATH + ClassUtil.convertClassNameToResourcePath(basepackage) + "/" + this.resourcePattern;
            Resource[] resources = this.resourcePatternResolver.getResources(packageSearchPath);
            for (Resource resource : resources) {
                MetadataReader metadataReader = this.metadataReaderFactory.getMetadataReader(resource);
                String clazzName = metadataReader.getClassMetadata().getClassName();
                candidates.add(clazzName);
                if (logger.isDebugEnabled()) {
                    logger.debug("Find Class : " + clazzName);
                }
            }
        } catch (IOException ex) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", ex);
        }
        return candidates;
    }

    /**
     * 查找Class 根据是否标注指定的注解
     *
     * @param <T>         注解泛型
     * @param basepackage 扫描路径
     * @param anno        注解
     * @return 标注注解的Class
     */
/**
     * @param basepackage    扫描包
     * @param interfaceClass 接口或者父类
     * @return class
     */
///core/JavaSource/com/fantasy/framework/spring/mvc/bind/annotation/FormModel.java
/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * <p>绑定请求参数到模型，并且暴露到模型中供页面使用</p>
 * <p>不同于@ModelAttribute</p>
 *
 * @author Zhang Kaitao
 */
/**
     * 指定请求参数的前缀和暴露到模型对象的名字供视图使用
     * <p/>
     * <p>1、绑定请求参数到模型，绑定规则<br/>
     * 如请求表单：<br>
     * <pre class="code">
     * <input name="student.name" value="Kate" /><br>
     * <input name="student.type" value="自费" /><br>
     * </pre>
     * 则请求处理方法：<br>
     * <pre class="code">
     *
     * @RequestMapping(value = "/test")
     * public String test(@FormModel("student") Student student) //这样将绑定  student.name student.type两个参数
     * </pre>
     * <p/>
     * 而springmvc默认<br>
     * 如请求表单：<br>
     * <pre class="code">
     * <input name="name" value="Kate" /><br>
     * <input name="type" value="自费" /><br>
     * </pre>
     * 则请求处理方法：<br>
     * <pre class="code">
     * public String test(@ModelAttribute("student") Student student) //这样将绑定name type两个参数
     * </pre>
     * <p/>
     * <p>具体可以参考iteye的问题：<a href="http://www.iteye.com/problems/89942">springMVC 数据绑定 多个对象 如何准确绑定</a>
     * <p/>
     * <p>2、根据value中的名字暴露到模型对象中供视图使用
     */
///core/JavaSource/com/fantasy/framework/spring/mvc/bind/annotation/RequestJsonParam.java
/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 
 * 该注解用于绑定请求参数（JSON字符串）
 * 
 * @author Zhang Kaitao
 *
 */
/**
	 * 用于绑定的请求参数名字
	 */
/**
	 * 是否必须，默认是
	 */
///core/JavaSource/com/fantasy/framework/spring/mvc/method/annotation/RequestJsonParamMethodArgumentResolver.java
/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * 解析请求参数json字符串
 *
 * @author Zhang Kaitao
 * @since 3.1
 */
/**
     * spring 3.1之前
     */
///core/JavaSource/com/fantasy/framework/spring/mvc/util/MapWapper.java
/*
 * Copyright 2002-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 *绑定JSON/自定义 数据到 Map
 * <p>默认自定义的MethodArgumentResolver是放在预定义之后，而且如果我们使用Map接收时，会自动绑定到Model上。
 * 请参考<a href="http://jinnianshilongnian.iteye.com/blog/1698916">
 * SpringMVC强大的数据绑定（1）——第六章 注解式控制器详解——跟着开涛学SpringMVC</a>
 * 第六部分：Model、Map、ModelMap
 *   
 * <p>期待springmvc未来版本可以自定义参数解析器顺序
 * </p>
 * 
 * @author Zhang Kaitao
 *
 * @param <K>
 * @param <V>
 */
///core/JavaSource/com/fantasy/framework/spring/SpringContextUtil.java
/**
     * 实现ApplicationContextAware接口的回调方法，设置上下文环境
     *
     * @param applicationContext applicationContext
     * @throws BeansException
     */
/**
     * @return ApplicationContext
     */
/**
     * 获取对象
     *
     * @param name beanId
     * @return Object 一个以所给名字注册的bean的实例
     * @throws BeansException
     */
/**
     * 获取类型为requiredType的对象 如果bean不能被类型转换，相应的异常将会被抛出（BeanNotOfRequiredTypeException）
     *
     * @param name         bean注册名
     * @param requiredType 返回对象类型
     * @return Object 返回requiredType类型对象
     * @throws BeansException
     */
/**
     * 由spring容器初始化该对象
     *
     * @param <T>
     * @param beanClass
     * @param autoType
     * @return
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     */
/**
     * spring 创建该Bean
     *
     * @param <T>
     * @param beanClass
     * @param autoType
     * @return
     * @see #AUTOWIRE_NO
     * @see #AUTOWIRE_BY_NAME
     * @see #AUTOWIRE_BY_TYPE
     * @see #AUTOWIRE_CONSTRUCTOR
     * @see #AUTOWIRE_AUTODETECT
     */
/**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     *
     * @param name
     * @return boolean
     */
/**
     * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
     *
     * @param name
     * @return boolean
     * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
     */
/**
     * @param name
     * @return Class 注册对象的类型
     * @throws NoSuchBeanDefinitionException
     */
/**
     * 如果给定的bean名字在bean定义中有别名，则返回这些别名
     *
     * @param name
     * @return
     * @throws NoSuchBeanDefinitionException
     */
///core/JavaSource/com/fantasy/framework/tags/AbstractUITag.java
/**
	 * 调用ftl模板写标签
	 * 
	 * @param templateName
	 * @param tagModel
	 */
///core/JavaSource/com/fantasy/framework/util/asm/AsmUtil.java
/**
     * 创建一个java动态bean
     *
     * @param className  新生产className
     * @param properties bean 属性
     * @return 新生成的 class
     */
/**
         * 注：第一个参数为版本号
         */
/**
     * 获取类型描述
     * 通过 classname 转换 type descriptor
     *
     * @param classname classname
     * @return type descriptor
     */
/**
     * 获取泛型签名
     *
     * @param clazz        class
     * @param genericTypes 泛型
     * @return 签名
     */
/**
     * <p>
     * 比较参数类型是否一致
     * </p>
     *
     * @param types   asm的类型({@link Type})
     * @param clazzes java 类型({@link Class})
     * @return boolean
     */
/**
     * <p>
     * 获取方法的参数名
     * </p>
     *
     * @param m 方法
     * @return paramNames
     */
///core/JavaSource/com/fantasy/framework/util/asm/Property.java
/**
 * 类属性信息
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述 用于生成动态类时，属性的描述信息
 * @since 2013-5-31 上午09:56:39
 */
/**
     * 属性名称
     */
/**
     * 属性类型
     */
/**
     * 泛型
     */
/**
     * 是否可以写入(set操作)
     */
/**
     * 是否可以读取(get操作)
     */
///core/JavaSource/com/fantasy/framework/util/common/BeanUtil.java
/*
    @Deprecated
	public static <T> T copy(T dest, Object orig, String... excludeProperties) {
		return copy(dest, orig, "", excludeProperties);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Deprecated
	private static <T> T copy(T dest, Object orig, String superName, String[] excludeProperties) {
		try {
			for (Map.Entry<String, Object> entry : OgnlUtil.getInstance().getBeanMap(orig, excludeProperties).entrySet()) {
				if (RegexpUtil.find(superName.concat((String) entry.getKey()), excludeProperties))
					continue;
				if (ObjectUtil.isNull(entry.getValue()))
					continue;
				Property property = ClassUtil.getProperty(dest, (String) entry.getKey());
				if (ObjectUtil.isNull(property))
					continue;
				if (!property.isWrite())
					continue;
//				if (logger.isDebugEnabled())
//					logger.debug(superName + "=>" + entry.getKey());
				if (entry.getValue().getClass().isEnum() || ClassUtil.isPrimitiveOrWrapperOrStringOrDate(entry.getValue().getClass())) {
					OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, entry.getValue());
				} else if (ClassUtil.isList(property.getPropertyType())) {
					List<Object> list = new ArrayList<Object>();
					OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, list);
					int length = length(entry.getValue());
					for (int i = 0; i < length; i++) {
						String _superName = superName + entry.getKey() + "[" + String.valueOf(i) + "]" + ".";
						list.add(copy(ClassUtil.newInstance((Class) ClassUtil.getMethodGenericParameterTypes(property.getWriteMethod().getMethod()).get(0)), get(entry.getValue(), i), _superName, excludeProperties));
					}
				} else if (ClassUtil.isArray(property.getPropertyType())) {
					Object object = OgnlUtil.getInstance().getValue((String) entry.getKey(), dest);
					Object array = Array.newInstance(property.getPropertyType(), Array.getLength(object));
					for (int i = 0; i < Array.getLength(object); i++)
						Array.set(array, i, copy(ClassUtil.newInstance((Class) ClassUtil.getMethodGenericParameterTypes(property.getWriteMethod().getMethod()).get(0)), get(entry.getValue(), i), superName.concat((String) entry.getKey()).concat("[").concat(String.valueOf(i)).concat("]").concat("."), excludeProperties));
				} else {
					String _superName = superName + entry.getKey() + ".";
					Object object = OgnlUtil.getInstance().getValue((String) entry.getKey(), dest);
					if (object == null) {
						OgnlUtil.getInstance().setValue((String) entry.getKey(), dest, copy(ClassUtil.newInstance(property.getPropertyType()), entry.getValue(), _superName, excludeProperties));
					} else
						copy(object, entry.getValue(), _superName, excludeProperties);
				}
			}
		} catch (IntrospectionException e) {
			logger.debug(e.getMessage(), e);
		} catch (OgnlException e) {
			logger.debug(e.getMessage(), e);
		}
		return dest;
	}
	*/
///core/JavaSource/com/fantasy/framework/util/common/ClassUtil.java
/**
     * 创建@{clazz}对象
     * <p/>
     * 通过class创建对象
     *
     * @param <T>   泛型类型
     * @param clazz 类型
     * @return 创建的对象
     */
/**
     * 获取 @{target} 的真实class
     *
     * @param <T>    泛型类型
     * @param target 对象
     * @return class
     */
/**
     * 获取 @{clazz} 的真实class
     *
     * @param <T>   泛型类型
     * @param clazz class
     * @return real class
     */
/**
     * 创建@{clazz}对象
     *
     * @param clazz     class
     * @param parameter parameter
     * @return Object
     * 调用带参数的构造方法
     */
/**
     * 创建@{clazz}对象
     *
     * @param <T>            泛型
     * @param clazz          class
     * @param parameterTypes 构造方法参数表
     * @param parameters     参数
     * @return Object
     * 调用带参数的构造方法
     */
/**
     * 创建@{clazz}对象
     * 根据@{className}
     *
     * @param className class Name
     * @return 对象
     */
/**
     * 获取方法参数的泛型类型
     *
     * @param method 反射方法
     * @param index  参数下标
     * @return 泛型类型集合
     * @功能描述
     */
/**
     * 方法不稳定，请不要使用
     *
     * @param annotClass Annotation class
     * @return Annotation object
     * @throws ClassNotFoundException
     * @throws LinkageError
     */
/**
     * 获取调用堆栈中,类被标注的注解信息
     *
     * @param <T>        泛型
     * @param annotClass 注解 class
     * @return 注解对象
     * @功能描述
     */
///core/JavaSource/com/fantasy/framework/util/common/DateUtil.java
/**
     * 缓存的 SimpleDateFormat
     */
/**
     * 日期驱动接口 针对DateUtil.now()方法获取时间
     */
/**
     * SimpleDateFormat 访问锁
     */
/**
         * 加载时获取日期驱动
         */
/**
     * 获取 SimpleDateFormat 对象
     *
     * @param format 格式
     * @return DateFormat
     */
/**
     * 为ftl模板调用时提供的方法，以防时间对象为NULL时,找不到方法
     *
     * @param date   时间
     * @param format 格式
     * @return string
     */
/**
     * 格式化日期以字符串形式返回
     *
     * @param date   时间
     * @param format 格式
     * @return String
     */
/**
     * 以指定格式格式化日期
     *
     * @param s      时间字符串
     * @param format 格式
     * @return data
     */
/**
     * 将字符串转为日期
     *
     * @param s 时间字符串
     * @return date
     */
/**
     * 相隔的天数
     *
     * @param big   大的时间
     * @param small 小时间
     * @return double
     */
/**
     * 获取当前日期的下一天
     *
     * @param date 时间
     * @return date
     */
/**
     * 添加时间
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
/**
     * 设置时间的某个字段
     *
     * @param date  原始时间
     * @param field 字段项
     * @param value 字段项值
     * @return date
     */
/**
     * 获取时间的最大上限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
/**
     * 获取时间的最小下限
     *
     * @param date   原始日期
     * @param fields 设置的时间格式字段
     * @return date
     */
/**
     * 获取日期间隔
     *
     * @param big   大的日期
     * @param small 小的日期
     * @param field 比较日期字段
     * @return date
     */
/**
     * 最高到天
     * dd天HH小时mm分钟
     *
     * @param field   比较字段
     * @param between 间隔
     * @param format  格式
     * @return {String}
     */
/**
     * 对日期添加天数
     *
     * @param date  原始日期
     * @param value 添加的天数
     * @return {Date}
     */
/**
     * 获取字段项对应的时第一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
/**
     * 获取字段项对应的最后时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return ${Date}
     */
/**
     * 获取字段项对应的下一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
/**
     * 获取字段项对应的上一时间
     *
     * @param date  原始日期
     * @param field 字段项
     * @return {Date}
     */
/**
     * 比较时间返回最小值
     *
     * @param dates 比较的时间数组
     * @return date
     */
/**
     * 比较时间返回最大值
     *
     * @param dates 比较的时间数组
     * @return date
     */
/**
     * 获取当前时间
     *
     * @return date
     */
/**
     * 为DateUtil.now提供的时间启动接口
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-3-27 上午10:07:48
     */
/**
     * DateUtil.now 时间启动接口的默认实现
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-3-27 上午10:08:07
     */
///core/JavaSource/com/fantasy/framework/util/common/I18nUtil.java
/*public static void main(String[] args) throws UnsupportedEncodingException {
		String unicode = unicode("：“成功导入{0}条,新的资源!");
		System.out.println(unicode);
		System.out.print(decodeUnicode(unicode));
	}*/
///core/JavaSource/com/fantasy/framework/util/common/ImageUtil.java
/**
 * 图片处理工具类
 * 待完善，支持jmagick
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-15 上午10:17:58
 */
/**
     * 获取图片文件格式
     *
     * @param imageFile
     *            图片文件
     *
     * @return 图片文件格式
     */
/**
     * 获取图片文件格式
     *
     * @param imageFile 图片文件
     * @return 图片文件格式
     */
/**
     * 转换图片文件为JPEG格式
     *
     * @param srcImageFile  源图片文件
     * @param destImageFile 目标图片文件
     */
/**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
/**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
/**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
/**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     * @throws IOException
     */
/**
     * 添加图片水印(AWT)
     *
     * @param srcBufferedImage       需要处理的BufferedImage
     * @param watermarkBufferedImage 水印图片文件
     * @param watermarkPosition      水印位置
     * @param alpha                  水印图片透明度
     * @return BufferedImage
     */
/**
     * 图片加水印
     *
     * @param press  水印图片
     * @param target 原始图片
     * @param x      坐标 x
     * @param y      坐标 y
     * @param alpha  水印图片透明度
     * @return {BufferedImage}
     */
/*} else if (PNG_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) pngReader(target, os));
        */
/**
     * 缩放图片
     *
     * @param target 图片路径
     * @param width  宽
     * @param heigth 高
     * @return {BufferedImage}
     * @throws IOException
     */
/* else if (PNG_FORMAT_NAME.equalsIgnoreCase(picextendname)) {
            imageOriginal = toBufferedImage((ToolkitImage) pngReader(target, os));
        }*/
/**
     * 缩放图片
     *
     * @param imageOriginal 图片
     * @param realWidth     真实宽度
     * @param realHeight    真实高度
     * @param width         压缩到的宽度
     * @param heigth        压缩到的高度
     *                      java.awt.image.BufferedImage <br/>
     *                      TYPE_CUSTOM 没有识别出图像类型，因此它必定是一个自定义图像。<br/>
     *                      TYPE_INT_RGB 表示一个图像，它具有合成整数像素的8位RGB颜色分量。<br/>
     *                      TYPE_INT_ARGB 表示一个图像，它具有合成整数像素的8位RGBA颜色分量。<br/>
     *                      TYPE_INT_ARGB_PRE 表示一个图像，它具有合成整数像素的8位RGBA颜色分量。<br/>
     *                      TYPE_INT_BGR 表示一个具有8位RGB颜色分量的图像，对应于Windows或Solaris风格的BGR颜色模型，具有打包为整数像素的Blue、Green和Red三种颜色。<br/>
     *                      TYPE_3BYTE_BGR 表示一个具有8位RGB颜色分量的图像，对应于Windows风格的BGR颜色模型，具有用3字节存储的Blue、Green和Red三种颜色。<br/>
     *                      TYPE_4BYTE_ABGR 表示一个具有8位RGBA颜色分量的图像，具有用3字节存储的Blue、Green和Red颜色以及1字节的alpha。<br/>
     *                      TYPE_4BYTE_ABGR_PRE 表示一个具有8位RGBA颜色分量的图像，具有用3字节存储的Blue、Green和Red颜色以及1字节的alpha。<br/>
     *                      TYPE_USHORT_565_RGB 表示一个具有5-6-5RGB颜色分量（5位red、6位green、5位blue）的图像，不带alpha。<br/>
     *                      TYPE_USHORT_555_RGB 表示一个具有5-5-5RGB颜色分量（5位red、5位green、5位blue）的图像，不带alpha。<br/>
     *                      TYPE_BYTE_GRAY 表示无符号byte灰度级图像（无索引）。<br/>
     *                      TYPE_USHORT_GRAY 表示一个无符号short灰度级图像（无索引）。<br/>
     *                      TYPE_BYTE_BINARY 表示一个不透明的以字节打包的1、2或4位图像。<br/>
     *                      TYPE_BYTE_INDEXED 表示带索引的字节图像。<br/>
     *                      <p/>
     *                      java.awt.Image <br/>
     *                      SCALE_DEFAULT 使用默认的图像缩放算法。<br/>
     *                      SCALE_FAST 选择一种图像缩放算法，在这种缩放算法中，缩放速度比缩放平滑度具有更高的优先级。<br/>
     *                      SCALE_SMOOTH 选择图像平滑度比缩放速度具有更高优先级的图像缩放算法。<br/>
     *                      SCALE_REPLICATE 使用 ReplicateScaleFilter 类中包含的图像缩放算法。<br/>
     *                      SCALE_AREA_AVERAGING 使用 Area Averaging 图像缩放算法。<br/>
     * @return {BufferedImage}
     */
/**
     * @param img
     * @param x
     * @param y
     * @param w
     * @param h
     * @return
     */
/**
     * 获取图片的编码字符串<br/><img src="data:image/gif;base64,64位编码字符串"/>
     *
     * @param in 输入流
     * @return {String}
     */
/**
     * 获取base64位编码的字符串 转换为图片对象
     *
     * @param base64
     * @return BufferedImage
     */
///core/JavaSource/com/fantasy/framework/util/common/JavassistUtil.java
/**
	 * 获取方法参数名称
	 * 
	 * @param classname
	 * @param methodname
	 * @param parameterTypes
	 * @return
	 * @throws NotFoundException
	 * @throws MissingLVException
	 */
/**
	 * 获取方法参数名称
	 * 
	 * @param cm
	 * @return
	 * @throws NotFoundException
	 * @throws MissingLVException
	 *             如果最终编译的class文件不包含局部变量表信息
	 */
/**
	 * 在class中未找到局部变量表信息<br>
	 * 使用编译器选项 javac -g:{vars}来编译源文件
	 * 
	 * @author Administrator
	 * 
	 */
///core/JavaSource/com/fantasy/framework/util/common/NumberUtil.java
/**
 * 数字处理集合类 主要功能：四舍五入，随机数，数字类型转换等方法
 * 
 * 
 */
/**
	 * 判断num是否为数字
	 * 
	 * @param num 字符串
	 * @return {boolean}
	 */
/**
	 * 四舍五入方法
	 * 
	 * @param n
	 *            值
	 * @param w
	 *            位数
	 * @return {double}
	 */
/**
	 * int 转 string
	 * 
	 * @param num
	 *            数值
	 * @return {String}
	 */
/**
	 * long 转 string
	 * 
	 * @param num
	 *            数值
	 * @return {String}
	 */
/**
	 * 整数到字节数组转换
	 * 
	 * @param n
	 *            整数
	 * @return {byte[]}
	 */
/**
	 * 字节数组到整数的转换
	 * 
	 * @param b
	 *            字节数组
	 * @return {int}
	 */
/**
	 * 字节转换到字符
	 * 
	 * @param b
	 *            字节
	 * @return {char}
	 */
/**
	 * 解析字符
	 * 
	 * @param c
	 *            字符
	 * @return {int}
	 */
/**
	 * 从字节数组到十六进制字符串转换
	 * 
	 * @param b
	 *            字节数组
	 * @return {String}
	 */
/**
	 * 从十六进制字符串到字节数组转换
	 * 
	 * @param hexstr
	 *            字符串
	 * @return {byte[]}
	 */
/**
	 * 验证整数n是否在整数数组中
	 * 
	 * @param n
	 *            整数
	 * @param ins
	 *            整数数组
	 * @return {boolean}
	 */
/*
	public static void main(String[] args) {

		System.out.println(percent(23d,123d));

		System.out.println(toChinese("11"));
		System.out.println(toRMB("11"));

		System.out.println(toRMB("11010001000.11"));
	}*/
///core/JavaSource/com/fantasy/framework/util/common/ObjectUtil.java
/**
     * 克隆对象,调用org.apache.commons.beanutils.BeanUtils.cloneBean(object);方法实现克隆
     *
     * @param object 将要克隆的对象
     * @return 返回的对象
     */
/**
     * 将集合对象中的 @{fieldName} 对于的值转换为字符串以 @{sign} 连接
     *
     * @param <T>       泛型
     * @param objs      集合
     * @param fieldName 支持ognl表达式
     * @param sign      连接符
     * @return T
     */
/**
     * 返回 集合中 @{fieldName} 值最大的对象
     *
     * @param <T>       泛型
     * @param c         集合
     * @param fieldName 支持ognl表达式
     * @return T
     */
/**
     * 返回 集合中 @{fieldName} 值最小的对象
     *
     * @param <T>       泛型
     * @param c         集合
     * @param fieldName 支持ognl表达式
     * @return T
     */
/**
     * 获取集合中 @{field} 的值为 @{value} 的对象 返回索引下标
     *
     * @param <T>   泛型
     * @param objs  原始集合
     * @param field 支持ognl表达式
     * @param value 比较值
     * @return T
     * 如果有多个只返回第一匹配的对象,比较调用对象的 equals 方法
     */
/**
     * 在集合中查找 @{field} 对于的值为 ${value} 的对象
     *
     * @param <T>   对应的泛型类型
     * @param list  原始集合
     * @param field 支持ognl表达式
     * @param value 比较值
     * @return 返回第一次匹配的对象
     */
/**
     * 对集合进行排序
     *
     * @param <T>        泛型
     * @param collectoin 要排序的集合
     * @param orderField 排序字段 支持ognl表达式
     * @return T
     * 默认排序方向为 asc
     */
/**
     * 对集合进行排序
     *
     * @param <T>        泛型
     * @param collectoin 要排序的集合
     * @param orderBy    排序字段 支持ognl表达式
     * @param order      排序方向 只能是 asc 与 desc
     * @return T
     */
/**
     * 辅助方法 比较两个值的大小
     *
     * @param o1         object1
     * @param o2         object2
     * @param orderField 支持ognl表达式
     * @return int
     * 如果返回1标示 @{o1} 大于 @{o2} <br/>
     * 如果返回0标示 @{o1} 等于 @{o2} <br/>
     * 如果返回-1标示 @{o1} 小于 @{o2}
     */
/**
     * 合并集合,去除重复的项
     *
     * @param <T>  泛型
     * @param dest 源集合
     * @param orig 要合并的集合
     */
/*
    @Deprecated
    public static <T> T copy(T dest, Object orig, String... excludeProperties) {
        return BeanUtil.copy(dest, orig, excludeProperties);
    }*/
/**
     * 判断对象是否存在于集合中
     *
     * @param <T>    泛型
     * @param list   集合
     * @param object 要判断的对象
     * @return boolean
     */
/**
     * 从集合中删除对象返回被移除的对象,通过属性判断删除
     *
     * @param <T>      泛型
     * @param orig     源集合
     * @param property 判断的字段
     * @param value    字段对应的值
     * @return 被移除的对象
     */
/**
     * 从数组中删除对象返回新的数组
     *
     * @param <T>  泛型
     * @param dest 数组
     * @param orig 要删除的对象
     * @return T
     */
/**
     * 获取集合的第一个元素，没有时返回NULL
     *
     * @param <T>  泛型
     * @param list 集合
     * @return T
     */
/**
     * 获取数组的第一个元素，没有时返回NULL
     *
     * @param <T>   泛型
     * @param array 数组
     * @return T
     */
/**
     * 获取集合的末尾元素，没有时返回NULL
     *
     * @param <T>  泛型
     * @param list 集合
     * @return T
     */
/**
     * 获取数组的末尾元素，没有时返回NULL
     *
     * @param <T>   泛型
     * @param array 数组
     * @return T
     */
///core/JavaSource/com/fantasy/framework/util/common/PathUtil.java
/**
     * 获取 Class 相对文件路径
     *
     * @param relatedPath 文件路径
     * @param cls         相对class
     * @return 文件路径
     * @throws IOException
     */
///core/JavaSource/com/fantasy/framework/util/common/PropertiesHelper.java
/**
 * Properties的操作的工具类,为Properties提供一个代理增加相关工具方法如
 * getRequiredString(),getInt(),getBoolean()等方法
 */
/*
            Iterator<URL> urls = ClassLoaderUtil.getResources(propertiesPath, PropertiesHelper.class, true);
            Properties props = new Properties();
            while (urls.hasNext()) {
                URL url = urls.next();
                Properties _props = PropertiesLoaderUtils.loadProperties(new UrlResource(url));
                for (Map.Entry entry : _props.entrySet()) {
                    if (!props.containsKey(entry.getKey())) {
                        props.put(entry.getKey(), entry.getValue());
                    }
                }
            }
            */
///core/JavaSource/com/fantasy/framework/util/common/ScriptHelp.java
/*]*[^\\**\\/]*)*\\*\\/", "$1").replaceAll("\\/\\/[^\\n]*", "");

        if (content.indexOf("/*") == 0){
            content = content.substring(content.indexOf("*/
///core/JavaSource/com/fantasy/framework/util/common/SimilarityUtil.java
/*public static void main(String[] args) {

		String str1 = "中国人";
		String str2 = "中国";
		System.out.println("ld=" + ld(str1, str2));
		System.out.println("sim=" + sim(str1, str2));
	}*/
///core/JavaSource/com/fantasy/framework/util/common/StringUtil.java
/**
     * 字符串超出指定长度时截取到指定长度，并在末尾追加指定的字符串。
     *
     * @param value 源字符串
     * @param len   要求的长度
     * @param word  超出长度，追加末尾的字符串
     * @return {string}
     */
/**
     * 判断字符串长度,非单字节字符串算两个长度
     *
     * @param str 计算长度的字符串
     * @return {int}
     */
/**
     * 判断字符串中是否包含中文
     *
     * @param input 要判断的字符串
     * @return {boolean}
     */
/*
    public static void main(String[] args) throws Exception {
    System.out.println(append("dsdf", 5, "0"));
    }
    */
/**
     * 剔除字符串中的空白字符
     *
     * @param s 字符串
     * @return {string}
     */
/**
     * 以{delim}格式截取字符串返回list,不返回为空的字符串
     *
     * @param s     源字符串
     * @param delim 分割字符
     * @return {List<String>}
     */
/**
     * 判断字符串是否为空或者空字符串
     *
     * @param s 要判断的字符串
     * @return {boolean}
     */
/**
     * 判断字符串是否为非空字符串 {@link #isNull(Object)} 方法的取反
     *
     * @param s 要判断的字符串
     * @return {boolean}
     */
/**
     * 判断字符串是否为空，如果为空返回空字符，如果不为空，trim该字符串后返回
     *
     * @param s 要判断的字符串
     * @return {String}
     */
/**
     * 判断对象是否为空，如果为空返回空字符，如果不为空，返回该字符串的toString字符串
     *
     * @param s 要判断的对象
     * @return {String}
     */
/**
     * 判断字符串是否为空，如果为空返回 {defaultValue}，如果不为空，直接返回该字符串
     *
     * @param s            要转换的对象
     * @param defaultValue 默认字符串
     * @return {String}
     */
/**
     * 判断字符串是否为空，如果为空返回 {defaultValue}，如果不为空，直接返回该字符串
     * {@link #defaultValue(Object, String)} 的重载
     *
     * @param s            要转换的字符串
     * @param defaultValue 默认字符串
     * @return {String}
     */
/**
     * 将Long转为String格式
     * 如果{s}为NULL 或者 小于 0 返回空字符。否则返回{s}的toString字符串
     *
     * @param s Long
     * @return {String}
     */
/**
     * 将Long转为String格式
     * 如果{s}为NULL 或者 小于 0 返回"0"。否则返回{s}的toString字符串
     *
     * @param s Long
     * @return {String}
     */
/**
     * 首字母大写
     *
     * @param s 字符串
     * @return {String}
     */
/**
     * 首字母小写
     *
     * @param s 字符串
     * @return {String}
     */
/**
     * 将字符串{ori}补齐到{length}长度
     * {length} 为正数右边补{fillChar}，为负数左边补{fillChar}<br/>
     * {fillChar}有多个的话取随机
     *
     * @param ori      源字符串
     * @param length   要补齐的长度
     * @param fillChar 追加的字符串
     * @return {String}
     */
/**
     * 左边补零以满足长度要求
     * <p/>
     * resultLength 最终长度
     */
/**
     * 判断字符串是否为数值类型<br/>
     * <p/>
     * 判断规则: 是否为[0-9]之间的字符组成，不能包含其他字符，否则返回false
     *
     * @param curPage 判断字符串是否为数字
     * @return {boolean}
     */
/**
     * 编码字符串 {s} 格式{enc}
     * 调用{@link #'URLEncoder.encode(String,String)'}
     *
     * @param s   要编码的字符串
     * @param enc 格式
     * @return {String}
     */
/**
     * 解码字符串 {s} 格式{enc}
     * 调用{@link #'URLEncoder.decode(String,String)'}
     *
     * @param s   要解码的字符串
     * @param enc 格式
     * @return {String}
     */
/**
     * 转义特殊字符
     *
     * @param condition 字符串
     * @return {String}
     */
/*
    public static boolean equals(String s, char[] buf, int offset, int length) {
        if (s.length() != length)
            return false;
        for (int i = 0; i < length; i++)
            if (buf[(offset + i)] != s.charAt(i))
                return false;
        return true;
    }*/
/**
     * @param s
     * @return
     */
///core/JavaSource/com/fantasy/framework/util/DynamicClassLoader.java
/**
 * 动态类加载器
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2012-11-30 下午05:12:44
 * @version 1.0
 */
///core/JavaSource/com/fantasy/framework/util/FantasyClassLoader.java
/**
 * Fantasy 动态类加载器。
 *
 * @author 李茂峰
 * @version 1.0
 *          用于封装DynamicClassLoader加载器<br/>
 *          如果一个类被多次修改并加载的话。需要不断创建新的加载器
 * @since 2012-11-6 下午10:33:48
 */
/**
     * 已被加载的对象
     */
/**
     * 已被卸载的对象
     */
/**
     * 从文件加载类
     *
     * @param classpath classpath
     * @param classname classname
     * @return Class
     * @throws ClassNotFoundException
     */
/**
     * 通过字节码加载类
     *
     * @param classdata classdata
     * @param classname classname
     * @return Class
     * @throws ClassNotFoundException
     */
/**
     * 自动加载类
     *
     * @param classname 类路径
     * @return Class
     * @throws ClassNotFoundException
     */
///core/JavaSource/com/fantasy/framework/util/highcharts/Test.java
/*System.out.println(JSON.serialize(chart));

		System.out.println(addMonth(-3, null));*/
///core/JavaSource/com/fantasy/framework/util/htmlcleaner/HtmlCleanerUtil.java
/**
 * HtmlCleaner是一个开源的Java语言的Html文档解析器。HtmlCleaner能够重新整理HTML文档的每个元素并生成结构良好(Well-Formed)的 HTML 文档。
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-8-13 下午01:41:42
 */
///core/JavaSource/com/fantasy/framework/util/jackson/HibernateAwareSerializerFactory.java
/*
	private static final String FIELD_HANDLER_PROPERTY_NAME = "fieldHandler";

	@Override
	public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType type) throws JsonMappingException  {
		Class<?> clazz = type.getRawClass();
		if (PersistentCollection.class.isAssignableFrom(clazz)) {
			return new PersistentCollectionSerializer(type);
		}
		if (HibernateProxy.class.isAssignableFrom(clazz)) {
			return new HibernateProxySerializer(type);
		}
		return super.createSerializer(prov,type);
	}*/
/*
	@Override
	protected JsonSerializer<Object> findSerializerFromAnnotation(SerializationConfig config, Annotated annotated, BeanProperty property) throws JsonMappingException  {
		JsonSerializer<Object> serializer = super.findSerializerFromAnnotation(config, annotated, property);
		if ((Object) serializer instanceof DateSerializer && annotated instanceof AnnotatedMethod) {
			DateFormat dateFormat = ((AnnotatedMethod) annotated).getAnnotated().getAnnotation(DateFormat.class);
			((DateSerializer) (Object) serializer).setDateFormat(dateFormat != null ? dateFormat.pattern() : null);
		}
		return serializer;
	}*/
/*
	@Override
	protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
		props = super.filterBeanProperties(config, beanDesc, props);
		filterInstrumentedBeanProperties(beanDesc, props);
//		List<String> transientOnes = new ArrayList<String>();
//		// 去掉瞬态字段
//		for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(beanDesc.getBeanClass())) {
//			Method getter = pd.getReadMethod();
//			if (getter != null && AnnotationUtils.findAnnotation(getter, Transient.class) != null) {
//				transientOnes.add(pd.getName());
//			}
//		}
//		for (Iterator<BeanPropertyWriter> iter = props.iterator(); iter.hasNext();) {
//			if (transientOnes.contains(iter.next().getName())) {
//				iter.remove();
//			}
//		}
		return props;
	}*/
/*
	private void filterInstrumentedBeanProperties(BasicBeanDescription beanDesc, List<BeanPropertyWriter> props) {
		if (!FieldHandled.class.isAssignableFrom(beanDesc.getBeanClass())) {
			return;
		}
		for (Iterator<BeanPropertyWriter> iter = props.iterator(); iter.hasNext();) {
			if (iter.next().getName().equals(FIELD_HANDLER_PROPERTY_NAME)) {
				iter.remove();
			}
		}
	}*/
/*
			BasicBeanDescription beanDesc = config.introspect(type);
//			boolean staticTyping = false;
			JavaType elementType = type.getContentType();
			TypeSerializer elementTypeSerializer = createTypeSerializer(provider.getConfig(), elementType);

			JsonSerializer elementValueSerializer = _findContentSerializer(config, beanDesc.getClassInfo(), beanProperty);

			JsonSerializer<Object> serializer = null;
			if (type.isMapLikeType()) {
				MapLikeType mlt = (MapLikeType) type;
				JsonSerializer keySerializer = _findKeySerializer(config, beanDesc.getClassInfo(), beanProperty);
				if (mlt.isTrueMapType()) {
					serializer = (JsonSerializer<Object>) buildMapSerializer(config, (MapType) mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
				}else {
                    serializer = (JsonSerializer<Object>) _buildMapLikeSerializer(config, mlt, beanDesc, beanProperty, false, keySerializer, elementTypeSerializer, elementValueSerializer);
                }
			} else if (type.isCollectionLikeType()) {
				CollectionLikeType clt = (CollectionLikeType) type;
				if (clt.isTrueCollectionType()) {
					serializer = (JsonSerializer<Object>) buildCollectionSerializer(config, (CollectionType) clt, beanDesc, beanProperty, false, elementTypeSerializer, elementValueSerializer);
				} else {
					serializer = (JsonSerializer<Object>) buildCollectionLikeSerializer(config, clt, beanDesc, beanProperty, false, elementTypeSerializer, elementValueSerializer);
				}
			}
			if (serializer == null) {
				throw new IgnoreException(" JsonSerializer is null !");
			}
//			Class<?> clazz = type.getRawClass();
//			JsonSerializer<Object> serializer;
//			if (PersistentMap.class.isAssignableFrom(clazz)) {
//				serializer = (JsonSerializer<Object>) buildMapSerializer(config, type, beanDesc, beanProperty);
//			} else {
//				serializer = (JsonSerializer<Object>) buildCollectionSerializer(config, type, beanDesc, beanProperty);
//			}
			serializer.serialize(value, jgen, provider);
			*/
///core/JavaSource/com/fantasy/framework/util/jackson/JSON.java
/**
     * 返回 Key 对应的镜像
     *
     * @return Mirror
     */
///core/JavaSource/com/fantasy/framework/util/LinkedBlockingQueue.java
/**
 * 线程安全的队列实现
 *
 * @param <E>
 * @author 李茂峰
 * @version 1.0
 * @since 2012-12-4 下午02:36:30
 */
/**
     *
     */
/**
     * 容量
     */
/**
     * 计数器
     */
/**
     * 链表头元素
     */
/**
     * 链表尾元素
     */
/**
     * 输出锁
     */
/**
     * 输入锁
     */
/**
     * 提供List接口的访问方式
     */
/**
     * 唤醒等待的线程(输出)
     * <p/>
     * 输出线程可以开始取值了.
     */
/**
     * 唤醒等待的线程(输入)
     *
     * @功能描述 <br/>
     * 输入线程可以开始存值了.
     */
/**
     * 向链表的末尾追加元素
     *
     * @param x E
     */
/**
     * 从链表的头取出元素
     *
     * @return E
     */
/**
     * 同时获取输入及输出锁
     *
     * @功能描述
     */
/**
     * 同时释放输入及输出锁
     *
     * @功能描述
     */
/**
     * 获取链表长度
     */
/**
     * 获取链表的可用长度
     */
/**
     * 向链表添加元素
     * <p/>
     * 如果链表容量已满，会持续等待
     *
     * @param o E
     * @throws InterruptedException
     */
/**
     * 添加元素
     *
     * @param o       要添加的元素
     * @param timeout 超时时间
     * @param unit    时间单位
     * @return boolean
     * @throws InterruptedException
     * @功能描述 <br/>
     * 可设置操作的超时时间
     */
/**
     * 添加元素
     *
     * @param o E
     * @return bookean
     * @功能描述 <br/>
     * 如果容量超出，则直接返回false
     */
/**
     * 获取队列的元素
     *
     * @return E
     * @throws InterruptedException
     */
/**
     * 获取队列元素，取出
     *
     * @param timeout 操作超时时间
     * @param unit    时间单位
     * @return 队列为空返回 null
     * @throws InterruptedException
     */
/**
     * 获取队列元素，取出
     *
     * @return 队列为空返回 null
     */
/**
     * 获取队列元素，不取出。
     * <p/>
     * 队列为空返回 null
     *
     * @return E
     */
/**
     * 转换为数组
     *
     * @return Object[]
     */
/**
     * 清空队列
     *
     * @功能描述
     */
/**
     * jdk 中 为什么会有这个方法
     *
     * @param s ObjectInputStream
     * @throws IOException
     * @throws ClassNotFoundException
     */
/**
     * 为List接口提供的方法
     * <p/>
     * {@link #list 的 get(int) 方法}
     *
     * @param index 下标
     * @return E
     */
/**
     * 获取{index}对应的元素
     * <p/>
     * 不移除元素
     *
     * @param index 下标
     * @return Node<E>
     */
/**
     * 添加元素
     *
     * @param index 下标
     * @param o     E
     * @return boolean
     */
/**
     * 删除元素
     *
     * @param index 下标
     * @return E
     */
/**
     * 移除元素
     *
     * @param o 对象
     * @return boolean
     */
/**
     * 链表节点类
     *
     * @param <E>
     * @author 李茂峰
     * @version 1.0
     * @since 2012-12-4 下午02:14:43
     */
/**
         * 当前节点对于的元素
         */
/**
         * 下一个节点
         */
/**
         * 上一个节点
         */
///core/JavaSource/com/fantasy/framework/util/regexp/RegexpCst.java
/**
 * 验证常量定义类
 */
/**
     * 整数
     */
/**
     * 数字
     */
/**
     * 浮点数
     */
/**
     * 电子邮箱
     */
/**
     * URL地址
     */
/**
     * 仅中文
     */
/**
     * 仅ACSII字符
     */
/**
     * 邮编
     */
/**
     * 手机
     */
/**
     * ip4 地址
     */
/**
     * 非空
     */
/**
     * 日期
     */
/**
     * QQ号码
     */
/**
     * 电话号码的函数(包括验证国内区号,国际区号,分机号)
     */
/**
     * 用来用户注册。匹配由数字、26个英文字母或者下划线组成的字符串
     */
/**
     * 字母
     */
/**
     * 身份证
     */
/**
     * 匹配标签内容 如果内容中嵌套标签过多会出现异常
     */
/**
     * @param patternString 验证规则
     * @return Pattern
     */
///core/JavaSource/com/fantasy/framework/util/regexp/RegexpUtil.java
/**
 * 正则工具类
 * 
 * @author 李茂峰
 * @since 2012-11-30 下午05:01:51
 * @version 1.0
 */
/**
	 * 
	 * @param patternString
	 *            验证规则
	 * @return Pattern
	 */
/**
	 * 是否匹配
	 * 
	 * @param input 验证的string
	 * @param regEx 正则表达式
	 * @return boolean
	 */
/**
	 * 是否匹配
	 * 
	 * @param input 验证的string
	 * @param pattern 正则表达式
	 * @return boolean
	 */
/**
	 * 匹配正则表达式(可能有多个) 只要匹配上一个既返回true
	 * 
	 * @param input 验证的string
	 * @param regExs 正则表达式
	 * @return boolean
	 */
/**
	 * 获取第一次匹配到的值
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @return string
	 */
/**
	 * 获取第{0}次匹配到的值
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param group group 下标
	 * @return string
	 */
/**
	 * 替换匹配到的第一个元素
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param replacement 替换成的字符串
	 * @return string
	 */
/**
	 * 替换匹配到的元素
	 * 
	 * @param input 匹配的string
	 * @param regEx 正则表达式
	 * @param replacement  替换成的字符串
	 * @return string
	 */
/**
	 * 将String中的所有regex匹配的字串全部替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param regex
	 *            替换查找的正则表达式
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
/**
	 * 将String中的所有pattern匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param pattern
	 *            替换查找的正则表达式对象
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
/**
	 * 将String中的regex第一次匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param regex
	 *            替换查找的正则表达式
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
/**
	 * 将String中的pattern第一次匹配的字串替换掉
	 * 
	 * @param string
	 *            代替换的字符串
	 * @param pattern
	 *            替换查找的正则表达式对象
	 * @param replacement
	 *            替换函数
	 * @return string
	 */
/**
	 * 抽象的字符串替换接口 主要是添加了$(group)方法来替代matcher.group(group)
	 */
/**
		 * 将text转化为特定的字串返回
		 * 
		 * @param text
		 *            指定的字符串
		 * @param index
		 *            替换的次序
		 * @param matcher
		 *            Matcher对象
		 * @return string
		 */
/**
		 * 获得matcher中的组数据 等同于matcher.group(group)
		 * 
		 * @功能描述 <br/>
		 *       该函数只能在{@link #doReplace(String, int, Matcher)} 中调用
		 * @param group 组下标
		 * @return string
		 */
/* '*', */
///core/JavaSource/com/fantasy/framework/util/RequestUtil.java
/*public static void main(String[] args) throws UnsupportedEncodingException {
		System.out.println(URLDecoder.decode("中文", "UTF-8"));
		System.out.println(new String("中文".getBytes("8859_1"), "UTF-8"));
	}*/
///core/JavaSource/com/fantasy/framework/util/Stack.java
/**
 * 类名: Stack 描述: (先进后出) 作者: 李茂峰 创建时间: 2010-2-2
 */
/**
	 * 从栈中取出元素
	 */
/**
	 * 读取对象，但不取出
	 * 
	 * @return
	 */
/**
	 * 向栈添加元素
	 * 
	 * @param o
	 */
/**
	 * 判断栈 是否为空
	 * 
	 * @return
	 */
/**
	 * 清空栈
	 */
///core/JavaSource/com/fantasy/framework/util/StringMap.java
/**
 * 该类逻辑有待验证
 * @param <V>
 */
///core/JavaSource/com/fantasy/framework/util/userstamp/Decoder.java
/*public static void main(String[] args) {

		UserStamp key = Encoder.encode(1, 50124, "123456", 5);
		System.out.println(key);

		UserResult userResult = decode(key.toString());// "HzRnCQagkhfcQdst"
		System.out.println("cssStyle : " + userResult.getCssStyle());
		System.out.println("passwordHash : " + userResult.getPasswordHash());
		System.out.println("userId : " + userResult.getUserId());
		System.out.println("checkPassword : " + userResult.checkPassword("91919191"));
		System.out.println("userType : " + userResult.getUserType());
		System.out.println(userResult.getMemKey());
	}*/
///core/JavaSource/com/fantasy/framework/util/web/context/ActionContext.java
/**
 * 应用的上下文 提供给Action使用
 */
/**
     * 方法名称: setContext 描述: 设置本次请求的上下文
     *
     * @param context 上下文
     */
/**
     * 获取 Action 上下
     *
     * @return ActionContext
     */
/**
     * 设置程序异常
     *
     * @param conversionErrors 转换异常
     */
/**
     * 获得程序异常 列表
     *
     * @return Map
     */
/**
     * 获取request中的Parameters
     *
     * @return parameters
     */
/**
     * 获取 ActionSession
     *
     * @return session
     */
///core/JavaSource/com/fantasy/framework/util/web/filter/ActionContextFilter.java
/**
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-25 上午12:30:42
 */
///core/JavaSource/com/fantasy/framework/util/web/PagerUtil.java
/**
     *
     * @param curPage
     *            当前页数
     * @param totalPage
     *            总页数
     * @param pageNumber
     *            当前面前面显示几个，后面显示几个
     * @return {List<String>}
     */
///core/JavaSource/com/fantasy/framework/util/web/ServletUtils.java
/**
	 * 设置 页面过期时间
	 * 
	 * @功能描述
	 * @param response
	 * @param expiresSeconds
	 */
/**
	 * 设置 页面不缓存
	 * 
	 * @功能描述
	 * @param response
	 */
/**
	 * 
	 * @功能描述
	 * @param response
	 */
/**
	 * 设置 页面的最后修改时间
	 * 
	 * @功能描述
	 * @param response
	 * @param lastModifiedDate
	 */
/**
	 * 
	 * @功能描述 
	 * @param request
	 * @param response
	 * @param lastModified
	 * @return
	 */
/**
	 * 方法待优化
	 * @功能描述 
	 * @param response
	 * @param fileName
	 */
///core/JavaSource/com/fantasy/framework/util/web/WebUtil.java
/**
 * web 工具类<br/>
 * web开发中经常使用的方法
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-10 上午9:16:01
 */
/**
     * 获取请求URL的后缀名
     *
     * @param request HttpServletRequest
     * @return {String}
     */
/**
     * 获取请求URL的后缀名
     *
     * @param requestUri 请求路径
     * @return {String}
     */
/**
     * 作用不大 不推荐使用
     *
     * @param request HttpServletRequest
     * @return {String}
     */
/**
     * 作用不大 不推荐使用
     *
     * @param url 路径
     * @return {String}
     */
/**
     * 获取协议名称
     *
     * @param url 路径
     * @return {String}
     */
/**
     * 获取请求的端口号
     *
     * @param url 路径
     * @return {String}
     */
/**
     * 获取请求的端口号
     *
     * @param request 路径
     * @return {String}
     */
/**
     * HTTP Header中Accept-Encoding 是浏览器发给服务器,声明浏览器支持的编码类型.常见的有
     * Accept-Encoding: compress, gzip //支持compress 和gzip类型
     * Accept-Encoding:　//默认是identity
     * Accept-Encoding: *　//支持所有类型 Accept-Encoding: compress;q=0.5, gzip;q=1.0//按顺序支持 gzip , compress
     * Accept-Encoding: gzip;q=1.0, identity; q=0.5, *;q=0 // 按顺序支持 gzip , identity
     *
     * @param request HTTP 请求对象
     * @return Accept-Encoding
     */
/**
     * 获取服务器的本地 ip
     *
     * @return {String[]}
     */
/**
     * 将请求参数转换为Map
     *
     * @param query 请求参数字符串
     * @return {Map<String,String[]>}
     */
/**
     * 将请求参数转为指定的对象
     *
     * @param query   请求参数字符串
     * @param classes 要转换成的FormBean
     * @return T 对象
     */
///core/JavaSource/com/fantasy/framework/web/filter/CharacterEncodingFilter.java
/**
 * 终极乱码解决方法
 */
///core/JavaSource/com/fantasy/framework/web/filter/XSSFilter.java
/**
	 * 是否转编码GET请求的参数 8859_1 => request.getCharacterEncoding()
	 */
///core/JavaSource/com/fantasy/member/bean/Member.java
/**
 * 会员
 * 
 * @author 李茂峰
 * @since 2013-9-22 上午11:25:14
 * @version 1.0
 */
/**
	 * 用户登录名称
	 */
/**
	 * 登录密码
	 */
/**
	 * 用户显示昵称
	 */
/**
	 * 是否启用
	 */
/**
	 * 未过期
	 */
/**
	 * 未锁定
	 */
/**
	 * 未失效
	 */
/**
	 * 锁定时间
	 */
/**
	 * 最后登录时间
	 */
/**
	 * 关联用户组
	 */
/**
	 * 关联角色
	 */
/**
	 * 会员其他信息
	 */
/**
	 * 收藏
	@ManyToMany(fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_GOODS_FAVORITE_MEMBER")
	@JoinTable(name = "MALL_FAVORITE_MEMBER", joinColumns = @JoinColumn(name = "MEMBER_ID"), inverseJoinColumns = @JoinColumn(name = "GOODS_ID"))
	private List<Goods> favoriteGoods;

	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = { CascadeType.REMOVE })
	private List<Receiver> receivers;
     */
///core/JavaSource/com/fantasy/member/bean/MemberDetails.java
/**
 * 用户详细信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-25 下午03:43:54
 */
/**
     * 姓名
     */
/**
     * 性别
     */
/**
     * 生日
     */
/**
     * 移动电话
     */
/**
     * 固定电话
     */
/**
     * E-mail
     */
/**
     * 网址
     */
/**
     * 描述信息
     */
/**
     * 是否为vip用户
     */
/**
     * 用户积分
     */
/**
     * 用户头像存储
     */
///core/JavaSource/com/fantasy/member/bean/Point.java
/**
		 * 收入(分)
		 */
/**
		 * 支出(分)
		 */
/**
	 * 备注
	 */
/**
	 * 状态
	 */
/**
	 * 分数
	 */
/**
	 * 会员
	 */
/**
	 * 标题
	 */
/**
	 * 来源/用途 url
	 */
///core/JavaSource/com/fantasy/member/dao/PointDao.java
/**
	 * 统计积分收支明细
	 * @param filters
	 * @return
	 */
/**
	 * 统计即将过期的积分
	 * @param filters
	 * @return
	 */
///core/JavaSource/com/fantasy/member/service/MemberService.java
/**
 * 
 * 会员管理
 * 
 */
/**
	 * 列表查询
	 * 
	 * @param pager
	 *            分页
	 * @param filters
	 *            查询条件
	 * @return
	 */
/**
	 * 前台注册页面保存
	 * 
	 * @param member
	 * @return
	 */
/**
	 * 普通用户注册，要填写邮箱
	 * 
	 * @param member
	 */
/**
	 * 验证邮箱是否已被验证使用
	 * 
	 * @param criterions
	 * @return
	 */
/**
	 * 保存对象
	 * 
	 * @param member
	 * @return
	 */
/**
	 * 获取对象
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 根据id 批量删除
	 * 
	 * @param ids
	 */
/**
	 * customer登录
	 * 
	 * @param user
	 */
/**
	 * customer登出
	 * 
	 * @功能描述
	 * @param user
	 */
///core/JavaSource/com/fantasy/member/service/PointService.java
/**
	 * 统计积分收支明细
	 * @param filters
	 * @return
	 */
/**
	 * 统计即将过期的积分
	 * @param filters
	 * @return
	 */
/**
	 * 保存积分收支明细
	 * @param consume
	 * @return
	 */
/**
	 * 保存积分收支明细
	 * 必须给出的参数
	 * @param title 标题
	 * @param status 状态
	 * @param score  积分
	 * @param memberId 会员ID
	 * 可选参数
	 * @param description 说明描述
	 * @param url 跳转地址
	 */
///core/JavaSource/com/fantasy/member/userdetails/MemberLoginSuccessHandler.java
/*		AuthenticationSuccessHandler handler = SpringContextUtil.getBeanByType(InitializeShopCartLoginSuccessHandler.class);
		if (handler == null) {
			handler = SpringContextUtil.createBean(InitializeShopCartLoginSuccessHandler.class, SpringContextUtil.AUTOWIRE_BY_TYPE);
		}
		this.handlers.add(handler);*/
///core/JavaSource/com/fantasy/payment/bean/Payment.java
/**
 * 支付
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:59
 */
/**
     * 支付编号
     */
/**
     * 交易号（用于记录第三方交易的交易流水号）
     */
/**
     * 支付类型
     */
/**
     * 支付配置名称
     */
/**
     * 收款银行名称
     */
/**
     * 收款银行账号
     */
/**
     * 支付金额
     */
/**
     * 支付手续费
     */
/**
     * 付款人
     */
/**
     * 备注
     */
/**
     * 支付状态
     */
/**
     * 会员
     */
/**
     * 支付方式
     */
/**
     * 订单类型
     */
/**
     * 订单编号
     */
///core/JavaSource/com/fantasy/payment/bean/PaymentConfig.java
/**
 * 支付配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:49
 */
/**
     * 支付配置类型
     */
/**
     * 支付方式名称
     */
/**
     * 支付产品标识
     */
/**
     * 商家ID
     */
/**
     * 商户私钥
     */
/**
     * 担保支付的卖家 email
     */
/**
     * 支付手续费类型
     */
/**
     * 支付费用
     */
/**
     * 介绍
     */
/**
     * 排序
     */
/**
     * 根据总金额计算支付费用
     *
     * @return 支付费用
     */
///core/JavaSource/com/fantasy/payment/bean/Refund.java
/**
 * 退款
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-5 上午9:22:39
 */
/**
     * 订单类型
     */
/**
     * 订单编号
     */
///core/JavaSource/com/fantasy/payment/dao/PaymentConfigDao.java
/**
 *@Author lsz
 *@Date 2013-12-18 下午5:18:39
 *
 */
///core/JavaSource/com/fantasy/payment/order/AbstractOrderDetailsService.java
/**
 * 实现一个简单的  OrderDetailsService 封装实现
 */
/**
     * 异步通知模板
     */
/**
     * 支付信息详情页
     */
/**
     * 同步通知模板
     */
/**
     * 订单查看页面模板
     */
///core/JavaSource/com/fantasy/payment/order/OrderDetails.java
/**
 * 订单支付接口类
 */
/**
     * 订单编号
     *
     * @return String
     */
/**
     * 获取订单类型
     *
     * @return String
     */
/**
     * 订单摘要
     *
     * @return String
     */
/**
     * 订单总金额
     *
     * @return BigDecimal
     */
/**
     * 订单应付金额
     *
     * @return BigDecimal
     */
/**
     * 订单是否可以进行支付
     *
     * @return boolean
     */
///core/JavaSource/com/fantasy/payment/order/OrderDetailsService.java
/**
 * 支付订单接口
 */
/**
     * 查询订单信息
     *
     * @param orderSn 编号
     * @return OrderDetails
     */
/**
     * 获取支付成功后的异步通知接口地址
     *
     * @return url
     */
/**
     * 获取支付成功后的回调处理URL
     *
     * @return url
     */
/**
     * 支付失败
     *
     * @param payment 支付信息
     */
/**
     * 支付成功
     *
     * @param payment 支付信息
     */
/**
     * 订单访问地址
     * 用于支付完成后，或者支付时的查看地址。该方法如果返回 空对象 或者空字符串 ，将显示默认的订单支付信息
     *
     * @param orderSn 订单编码
     * @return {String} 例如 : /order.do?sn=SN_000123
     */
/**
     * 支付完成后，跳转的地址
     * @param paymentSn 支付SN
     * @return {String}
     */
///core/JavaSource/com/fantasy/payment/product/AbstractAlipayPaymentProduct.java
/**
     * 把数组所有元素按照固定参数排序，以“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要参与字符拼接的参数组
     * @return 拼接后字符串
     */
/**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
/**
     * 生成签名结果
     *
     * @param sPara 要签名的数组
     * @return 签名结果字符串
     */
/**
     * 生成要请求给支付宝的参数数组
     *
     * @param sParaTemp 请求前的参数数组
     * @return 要请求的参数数组
     */
/**
     * 建立请求，以模拟远程HTTP的POST请求方式构造并获取支付宝的处理结果
     * 如果接口中没有上传文件参数，那么strParaFileName与strFilePath设置为空值
     * 如：buildRequest("", "",sParaTemp)
     *
     * @param ALIPAY_GATEWAY_NEW 支付宝网关地址
     * @param strParaFileName    文件类型的参数名
     * @param strFilePath        文件路径
     * @param sParaTemp          请求参数数组
     * @return 支付宝处理结果
     */
/**
     * 解析远程模拟提交后返回的信息，获得token
     *
     * @param text 要解析的字符串
     * @return 解析结果
     */
/**
     * MAP类型数组转换成NameValuePair类型
     *
     * @param properties MAP类型数组
     * @return NameValuePair类型数组
     */
/**
     * 解密
     *
     * @param inputPara 要解密数据
     * @return 解密后结果
     */
/**
     * 验证消息是否是支付宝发出的合法消息，验证服务器异步通知
     *
     * @param params 通知返回来的参数数组
     * @return 验证结果
     */
/**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @param isSort 是否排序
     * @return 生成的签名结果
     */
/**
     * 支付宝消息验证地址
     */
/**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param partner   合作身份者ID
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
/**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
///core/JavaSource/com/fantasy/payment/product/AbstractPaymentProduct.java
/**
 * 基类 - 支付产品
 */
/*
    protected static final String RESULT_URL = "/payment/result.do";// 支付结果显示URL
    */
/**
     * 验证签名
     *
     * @param parameters 请求参数
     * @return 是否验证通过
     */
/**
     * 根据参数集合组合参数字符串（忽略空值参数）
     *
     * @param params 请求参数
     * @return 参数字符串
     */
/**
     * 建立请求，以表单HTML形式构造（默认）
     *
     * @param sParaTemp     请求参数数组
     * @param strMethod     提交方式。两个值可选：post、get
     * @param strButtonName 确认按钮显示文字
     * @return 提交表单HTML文本
     */
///core/JavaSource/com/fantasy/payment/product/AlipayDirect.java
/**
 * 支付宝（即时交易）
 */
/*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    public static final String SHOW_URL = "/payment.do";// 支付单回显url
    */
///core/JavaSource/com/fantasy/payment/product/AlipayDirectByWap.java
/**
 * 支付宝 wap 支付接口
 */
/*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    */
///core/JavaSource/com/fantasy/payment/product/AlipayPartner.java
/**
 * 支付宝（担保交易）
 */
/**
     * 支付宝消息验证地址
     */
/*
    public static final String RETURN_URL = "/payment/payreturn.do";// 回调处理URL
    public static final String NOTIFY_URL = "/payment/paynotify.do";// 消息通知URL
    public static final String SHOW_URL = "/payment.do";// 支付单回显url
    */
///core/JavaSource/com/fantasy/payment/product/CurrencyType.java
/**
 * 币种
 */
///core/JavaSource/com/fantasy/payment/product/httpClient/HttpProtocolHandler.java
/* *
 *类名：HttpProtocolHandler
 *功能：HttpClient方式访问
 *详细：获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
/**
     * 连接超时时间，由bean factory设置，缺省为8秒钟
     */
/**
     * 回应超时时间, 由bean factory设置，缺省为30秒钟
     */
/**
     * 闲置连接超时时间, 由bean factory设置，缺省为60秒钟
     */
/**
     * 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒
     */
/**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
/**
     * 工厂方法
     *
     * @return
     */
/**
     * 私有的构造方法
     */
/**
     * 执行Http请求
     *
     * @param request         请求数据
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath     文件路径
     * @return HttpResponse
     * @throws IOException
     */
/**
     * 将NameValuePairs数组转变为字符串
     *
     * @param nameValues
     * @return
     */
///core/JavaSource/com/fantasy/payment/product/httpClient/HttpRequest.java
/* *
 *类名：HttpRequest
 *功能：Http请求对象的封装
 *详细：封装Http请求
 *版本：3.3
 *日期：2011-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
/** HTTP GET method */
/** HTTP POST method */
/**
     * 待请求的url
     */
/**
     * 默认的请求方式
     */
/**
     * Post方式请求时组装好的参数值对
     */
/**
     * Get方式请求时对应的参数
     */
/**
     * 默认的请求编码方式
     */
/**
     * 请求发起方的ip地址
     */
/**
     * 请求返回的方式
     */
/**
     * @return Returns the clientIp.
     */
/**
     * @param clientIp The clientIp to set.
     */
/**
     * @return Returns the charset.
     */
/**
     * @param charset The charset to set.
     */
///core/JavaSource/com/fantasy/payment/product/httpClient/HttpResponse.java
/* *
 *类名：HttpResponse
 *功能：Http返回对象的封装
 *详细：封装Http返回信息
 *版本：3.3
 *日期：2011-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
/**
     * 返回中的Header信息
     */
/**
     * String类型的result
     */
/**
     * btye类型的result
     */
///core/JavaSource/com/fantasy/payment/product/httpClient/HttpResultType.java
/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
/* *
 *类名：HttpResultType
 *功能：表示Http返回的结果字符方式
 *详细：表示Http返回的结果字符方式
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */
/**
     * 字符串方式
     */
/**
     * 字节数组方式
     */
///core/JavaSource/com/fantasy/payment/product/Pay99bill.java
/**
 * 快钱支付
 */
///core/JavaSource/com/fantasy/payment/product/PaymentProduct.java
/**
 * 支付产品接口
 */
/**
     * 支付地址
     *
     * @return String
     */
/**
     * 支付产品名称
     *
     * @return String
     */
/**
     * 支付请求参数
     *
     * @param parameters 请求参数
     * @return Map
     */
/**
     * 支付验证方法
     *
     * @param parameters 请求参数
     * @return boolean
     */
/**
     * 根据支付编号获取支付返回信息
     *
     * @return String
     */
/**
     * 获取支付通知信息
     *
     * @return String
     */
/**
     * 建立请求，以表单HTML形式构造（默认）
     *
     * @param sParaTemp 请求参数数组
     * @return 提交表单HTML文本
     */
/**
     * 解析支付结果
     *
     * @param parameters 支付结果
     * @return PayResult
     */
///core/JavaSource/com/fantasy/payment/product/PayResult.java
/**
         * 失败
         */
/**
         * 成功
         */
/**
     * 交易流水号
     */
/**
     * 系统支付流水
     */
/**
     * 交易状态
     */
/**
     * 交易金额
     */
///core/JavaSource/com/fantasy/payment/product/sign/Base64.java
/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */
/**
     * Encodes hex octects into Base64
     *
     * @param binaryData Array containing binaryData
     * @return Encoded Base64 array
     */
/**
     * Decodes Base64 data into octects
     *
     * @param encoded string containing Base64 data
     * @return Array containind decoded data.
     */
/**
     * remove WhiteSpace from MIME containing encoded Base64 data.
     *
     * @param data  the byte array of base64 data (with WS)
     * @return      the new length
     */
///core/JavaSource/com/fantasy/payment/product/sign/MD5.java
/** 
* 功能：支付宝MD5签名处理核心文件，不需要修改
* 版本：3.3
* 修改日期：2012-08-17
* 说明：
* 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
* 该代码仅供学习和研究支付宝接口使用，只是提供一个
* */
/**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
/**
     * 签名字符串
     * @param text 需要签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param input_charset 编码格式
     * @return 签名结果
     */
/**
     * @param content
     * @param charset
     * @return
     * @throws java.security.SignatureException
     * @throws java.io.UnsupportedEncodingException
     */
///core/JavaSource/com/fantasy/payment/product/sign/RSA.java
/**
     * RSA签名
     *
     * @param content       待签名数据
     * @param privateKey    商户私钥
     * @param input_charset 编码格式
     * @return 签名值
     */
/**
     * RSA验签名检查
     *
     * @param content        待签名数据
     * @param sign           签名值
     * @param ali_public_key 支付宝公钥
     * @param input_charset  编码格式
     * @return 布尔值
     */
/**
     * 解密
     *
     * @param content       密文
     * @param private_key   商户私钥
     * @param input_charset 编码格式
     * @return 解密后的字符串
     */
/**
     * 得到私钥
     *
     * @param key 密钥字符串（经过base64编码）
     * @throws InvalidKeySpecException, NoSuchAlgorithmException
     */
///core/JavaSource/com/fantasy/payment/product/TenpayDirect.java
/**
 * 财付通（即时交易）
 */
/*
        parameters.get("sp_billno")
        new BigDecimal(parameters.get("total_fee")).divide(new BigDecimal(100))
        */
///core/JavaSource/com/fantasy/payment/product/TenpayPartner.java
/**
 * 财付通（担保交易）
 */
///core/JavaSource/com/fantasy/payment/product/Yeepay.java
/**
 * 易宝支付
 */
/*p2_Order == paymentSn */
///core/JavaSource/com/fantasy/payment/rest/PaymentController.java
/**
     * 支付提交
     *
     * @param ordertype       订单类型
     * @param ordersn         订单编号
     * @param paymentConfigId 支付方式
     * @return {string}
     */
///core/JavaSource/com/fantasy/payment/service/PaymentConfigService.java
/**
     * findPager
     *
     * @param pager 分页对象
     * @param filters 筛选条件
     * @return {pager}
     */
/**
     * 保存
     *
     * @param config 支付配置信息
     * @return {paymentConfig}
     */
/**
     * 获取
     *
     * @param id 配置id
     * @return {paymentConfig}
     */
/**
     * 删除
     *
     * @param ids 支付配置ids
     */
///core/JavaSource/com/fantasy/payment/service/PaymentConfiguration.java
/**
 * 支付配置
 */
/**
     * 所有支持的支付产品
     */
///core/JavaSource/com/fantasy/payment/service/PaymentContext.java
/**
 * 支付上下文对象
 */
/**
     * 支付订单对象
     */
/**
     * 支付对象
     */
/**
     * 支付配置
     */
/**
     * 支付产品
     */
/**
     * 支付订单 Service
     */
/**
     * 支付结果
     */
/**
     * 获取支付成功后的异步通知接口地址
     *
     * @return url
     */
/**
     * 获取支付成功后的回调处理URL
     *
     * @return url
     */
/**
     * 订单查看 URL
     *
     * @param orderSn 订单编号
     * @return url
     */
/**
     * 支付详情页
     *
     * @param paymentSn 用于支付成功后的跳转地址
     * @return url
     */
/**
     * 支付失败
     *
     * @param payment 支付对象
     */
/**
     * 支付成功
     *
     * @param payment 支付对象
     */
///core/JavaSource/com/fantasy/payment/service/PaymentService.java
/**
 * 支付service
 */
/**
     * 支付准备
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param membername      会员
     * @param paymentConfigId 支付配置
     * @return Payment
     * @throws PaymentException
     */
/**
     * 过期支付单
     *
     * @param sn 支付编号
     */
/**
     * 支付失败
     *
     * @param sn 支付编号
     */
/**
     * 付款成功
     *
     * @param sn 支付编号
     */
/**
     * 提交支付请求
     *
     * @param orderType       订单类型
     * @param orderSn         订单编码
     * @param paymentConfigId 支付配置id
     * @param payMember       支付人
     * @param parameters      请求参数
     * @return html 表单字符串
     */
///core/JavaSource/com/fantasy/remind/bean/Model.java
/**
 * 公告
 */
/**
     * 模版名称
     */
/**
     * 消息内容模版
     */
/**
     * 跳转连接模版
     */
/**
     * 图片存储
     */
/**
     * 消息
     */
///core/JavaSource/com/fantasy/remind/bean/Notice.java
/**
 * 提醒
 */
/**
     * 提醒内容
     */
/**
     * 跳转连接
     */
/**
     * 是否已读
     */
///core/JavaSource/com/fantasy/remind/dao/ModelDao.java
/**
 * 公告 Dao
 */
///core/JavaSource/com/fantasy/remind/dao/NoticeDao.java
/**
 * 公告 Dao
 */
///core/JavaSource/com/fantasy/remind/service/ModelService.java
/**
 * 消息模版 service
 */
/**
     * 查看
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     * @param notice
     */
/**
     * 查看
     * @param id
     * @return
     */
/**
     * 删除
     * @param ids
     */
///core/JavaSource/com/fantasy/remind/service/NoticeService.java
/**
 * 提醒 service
 */
/**
     * 查看
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     *
     * @param notice
     */
/**
     * 查看
     *
     * @param id
     * @return
     */
/**
     * 删除
     *
     * @param ids
     */
///core/JavaSource/com/fantasy/remind/websocket/NoticeWebSocket.java
/**
 * Created by zzzhong on 2014/11/12.
 */
///core/JavaSource/com/fantasy/schedule/service/ScheduleService.java
/**
     * 返回 各时段的表达式
     *
     * @param cron 表达式
     * @param i    下标
     * @return string
     */
/**
     * 获取全部jobKey
     *
     * @return list<jobkey>
     */
/**
     * 添加任务
     *
     * @param jobKey   key
     * @param jobClass JobClass
     */
/**
     * 添加任务的触发器
     *
     * @param jobKey     jobKey
     * @param triggerKey triggerKey
     * @param cron       任务表达式
     * @param args       参数
     * @return Trigger
     */
/**
     * 添加触发器
     *
     * @param jobKey     jobKey
     * @param triggerKey triggerKey
     * @param interval   触发间隔时间
     * @param repeatCount      触发次数(次数为0触发一次)
     * @param args       每次触发附带的额外数据
     * @return Trigger
     */
/**
     * 是否启动
     *
     * @return boolean
     */
/**
     * 是否关闭
     *
     * @return boolean
     */
/**
     * 停止 job
     *
     * @param jobName   任务名称
     * @param groupName 组名称
     */
/**
     * 恢复 job
     *
     * @param jobKey 任务名称
     */
/**
     * 删除指定的 job
     *
     * @param jobKey 任务名称
     * @return boolean
     */
/**
     * 停止触发器
     *
     * @param triggerKey 触发器名称
     */
/**
     * 重启触发器
     *
     * @param triggerKey 触发器名称
     */
/**
     * 移除触发器
     *
     * @param triggerKey 触发器名称
     * @return boolean
     */
/**
     * 暂停调度中所有的job任务
     */
/**
     * 恢复调度中所有的job的任务
     */
/**
     * 中断TASK执行 job
     *
     * @param jobKey 触发器名称
     * @return boolean
     */
/**
     * 直接执行job
     *
     * @param jobKey jobkey
     */
/**
     * 直接触发job
     *
     * @param jobKey jobkey
     * @param args   执行参数
     */
///core/JavaSource/com/fantasy/security/bean/handler/SexTypeHandler.java
/*
						 * case 'U': return Sex.unknown;
						 */
/* return Sex.unknown; */
///core/JavaSource/com/fantasy/security/bean/Job.java
/**
 * 岗位
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:48
 */
/**
     * 岗位编码
     */
/**
     * 岗位名称
     */
/**
     * 岗位描述信息
     */
/**
     *组织机构
     */
///core/JavaSource/com/fantasy/security/bean/Menu.java
/**
     * 菜单名称
     */
/**
     * 树路径
     */
/**
     * 菜单值
     */
/**
     * 菜单类型
     */
/**
     * 菜单对应的图标
     */
/**
     * 菜单描述
     */
/**
     * 层级
     */
/**
     * 排序字段
     */
/**
     * 下级菜单
     */
/**
     * 上级菜单
     */
/**
     * 菜单是否选中
     */
///core/JavaSource/com/fantasy/security/bean/Organization.java
/**
 * 组织机构
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-1-22 下午04:00:57
 */
/**
     * 组织机构类型
     */
/**
         * 企业
         enterprise,*/
/**
         * 公司
         */
/**
         * 部门
         */
/**
     * 机构简写
     */
/**
     * 机构名称
     */
/**
     * 机构类型
     */
/**
     * 机构描述信息
     */
/**
     * 上级机构
     */
/**
     * 下属机构
     */
/**
     * 维度与上级机构
     */
/**
     * 用户
     */
///core/JavaSource/com/fantasy/security/bean/OrgDimension.java
/**
 * 组织维度
 */
/**
     * 维度Id
     */
/**
     * 维度名称
     */
/**
     * 维度描述
     */
/**
     * 维度对应的站点
     */
///core/JavaSource/com/fantasy/security/bean/OrgHelpBean.java
/**
 * Created by yhx on 2015/1/22.
 *
 * 组织机构 帮助表
 *
 */
/**
     * 组织维度
     */
/**
     * 上级组织机构
     */
///core/JavaSource/com/fantasy/security/bean/OrgRelation.java
/**
 * 组织机构关系
 */
/**
         * 下级(普通下属关系)
         */
/**
         * 分支机构(用于表示子公司,可承担独立法律责任组织)
         */
/**
         * 子公司
         */
/**
     * 标示主键
     */
/**
     * 组织机构
     */
/**
     * 层级
     */
/**
     * 排序字段
     */
/**
     * 用于存储关系path
     */
/**
     * 上级关系
     */
/**
     * 下级关系
     */
/**
     * 机构关系对应的维度
     */
///core/JavaSource/com/fantasy/security/bean/Resource.java
/**
 * 访问规则
 * 
 * @author 李茂峰
 * @since 2014年4月23日 下午5:53:06
 * @version 1.0
 */
/**
	 * 资源名称
	 */
/**
	 * 资源值
	 */
/**
	 * 资源类型
	 */
/**
	 * 是否启用
	 */
/**
	 * 资源描述
	 */
///core/JavaSource/com/fantasy/security/bean/Role.java
/**
     * 角色编码
     */
/**
     * 角色名称
     */
/**
     * 角色类型，用于区分不同类型的角色。比如：后台管理与前台会员之间的角色
     */
/**
     * 是否启用 0禁用 1 启用
     */
/**
     * 描述信息
     */
/**
     * 角色对应的菜单
     */
/**
     * 角色对应的资源
     */
///core/JavaSource/com/fantasy/security/bean/User.java
/**
	 * 用户登录名称
	 */
/**
	 * 登录密码
	 */
/**
	 * 用户显示昵称
	 */
/**
	 * 是否启用
	 */
/**
	 * 未过期
	 */
/**
	 * 未锁定
	 */
/**
	 * 未失效
	 */
/**
	 * 锁定时间
	 */
/**
	 * 最后登录时间
	 */
/**
	 * 用户对应的用户组
	 */
/**
	 * 用户对应的角色
	 */
/**
	 * 用户详细信息
	 */
/**
	 * 用户允许访问的权限
	 */
/**
     * 对应的网站信息
     */
/**
     * 对应的组织机构
     */
///core/JavaSource/com/fantasy/security/bean/UserDetails.java
/**
 * 用户详细信息表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-25 下午03:43:54
 */
/**
     * 姓名
     */
/**
     * 性别
     */
/**
     * 生日
     */
/**
     * 移动电话
     */
/**
     * 固定电话
     */
/**
     * E-mail
     */
/**
     * 网址
     */
/**
     * 描述信息
     */
/**
     * 用户头像存储
     */
///core/JavaSource/com/fantasy/security/bean/UserGroup.java
/**
	 * 用户组编码
	 */
/**
	 * 用户组名称
	 */
/**
	 * 是否启用
	 */
/**
	 * 描述
	 */
/**
	 * 用户组对应的菜单
	 */
/**
	 * 用户组对应的资源
	 */
///core/JavaSource/com/fantasy/security/dao/JobDao.java
/**
 * Created by yhx on 2015/2/4.
 */
///core/JavaSource/com/fantasy/security/dao/OrganizationDao.java
/**
 * Created by yhx on 2015/1/21.
 * 组织维度
 */
///core/JavaSource/com/fantasy/security/dao/OrgDimensionDao.java
/**
 * Created by yhx on 2015/1/21.
 * 组织维度
 */
///core/JavaSource/com/fantasy/security/dao/OrgRelationDao.java
/**
 * Created by yhx on 2015/1/21.
 * 组织维度
 */
///core/JavaSource/com/fantasy/security/service/JobService.java
/**
 * Created by yhx on 2015/2/4.
 */
///core/JavaSource/com/fantasy/security/service/MenuService.java
/**
     * 获取列表
     *
     * @return
     */
/**
     * 静态获取列表
     *
     * @return
     */
///core/JavaSource/com/fantasy/security/service/OrgDimensionService.java
/**
 * Created by yhx on 2015/1/21.
 */
///core/JavaSource/com/fantasy/security/service/OrgRelationService.java
/**
 * Created by hebo on 2015/4/1.
 * 维度与组织机构中间关系
 */
///core/JavaSource/com/fantasy/security/service/ResourceService.java
/**
	 * 搜索
	 * @param pager 翻页对象
	 * @param filters 筛选条件
	 * @return {Pager}
	 */
///core/JavaSource/com/fantasy/security/service/UserService.java
/**
     * 保存用户
     *
     * @param user 用户对象
     */
///core/JavaSource/com/fantasy/security/userdetails/checker/CaptchaChecker.java
/**
 * 
 * @功能描述 
 * @author 李茂峰
 * @since 2013-9-13 上午10:32:02
 * @version 1.0
 */
/**
	 * 验证验证码
	 * 
	 * @param request
	 * @return
	 */
///core/JavaSource/com/fantasy/security/userdetails/checker/FirstLoginUserDetailsChecker.java
/**
 * 是否首次登陆验证
 * 
 * @author Administrator
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/checker/LoginTimesUserDetailsChecker.java
/**
 * 登陆次数验证
 * 
 * @author Administrator
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/checker/MultiLoginUserDetailsChecker.java
/**
 * 重复登陆验证
 * 
 * @author Administrator
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/checker/PasswordExpiredUserDetailsChecker.java
/**
 * 密码过期验证
 * 
 * @author Administrator
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/exception/FirstLoginAuthenticationException.java
/**
 * 自定义异常: 首次登陆异常
 * 
 * @author 李茂峰
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/PostAuthenticationChecks.java
/**
 * spring security 登陆后置验证
 * 
 * @author 李茂峰
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/PreAuthenticationChecks.java
/**
 * spring security 登陆前置验证
 * 
 * @author 李茂峰
 * 
 */
///core/JavaSource/com/fantasy/security/userdetails/SimpleUser.java
/**
	 * 获取用户绑定的具体对象
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 更新绑定的对象,一般在当前用户更新自己的信息之后.
	 * 
	 * @功能描述
	 * @param user
	 */
///core/JavaSource/com/fantasy/security/web/authentication/CaptchaUsernamePasswordAuthenticationFilter.java
/**
	 * 验证验证码
	 * 
	 * @param request
	 * @return
	 */
///core/JavaSource/com/fantasy/security/web/authentication/handler/FantasyLoginFailureHandler.java
/**
 * 登陆失败后的操作
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-6-28 下午04:47:46
 * @version 1.0
 */
///core/JavaSource/com/fantasy/security/web/authentication/handler/FantasyLoginSuccessHandler.java
/**
 * 登陆成功后的操作
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-6-28 下午04:48:09
 * @version 1.0
 */
///core/JavaSource/com/fantasy/security/web/authentication/handler/FantasyLogoutSuccessHandler.java
/**
 * 成功登出后的操作
 * 
 * @author 李茂峰
 * 
 */
///core/JavaSource/com/fantasy/security/web/filter/AutoLoginFilter.java
/**
 * 自动以默认用户名登录的filter, 用于开发时不需要每次进入登录页面.s
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-4-9 上午10:11:14
 * @version 1.0
 */
///core/JavaSource/com/fantasy/security/web/savedrequest/FantasyHttpSessionRequestCache.java
/**
 * 替换默认的HttpSessionRequestCache
 * 
 *  ajax请求的时候不要记录请求地址
 * @author 李茂峰
 * @since 2013-9-10 上午9:07:21
 * @version 1.0
 */
///core/JavaSource/com/fantasy/system/bean/ChartSource.java
/**
 * Created by hebo on 2014/8/11.
 */
/**
     * 值
     */
/**
     * key
     */
/**
     * 描述
     */
///core/JavaSource/com/fantasy/system/bean/DataDictionary.java
/**
 * 数据字段类
 * <br/>
 * 该类为了取代Config.java
 */
/**
     * 代码
     */
/**
     * 配置类别
     */
/**
     * 名称
     */
/**
     * 排序字段
     */
/**
     * 描述
     */
/**
     * 上级数据字典
     */
/**
     * 下级数据字典
     */
///core/JavaSource/com/fantasy/system/bean/DataDictionaryKey.java
/**
     * 代码
     */
/**
     * 配置类别
     */
///core/JavaSource/com/fantasy/system/bean/DataDictionaryType.java
/**
 * 数据字典分类表
 */
/**
     * 代码
     */
/**
     * 名称
     */
/**
     * 层级
     */
/**
     *
     */
/**
     * 排序字段
     */
/**
     * 描述
     */
/**
     * 上级数据字典
     */
/**
     * 下级数据字典
     */
///core/JavaSource/com/fantasy/system/bean/Setting.java
/**
 * 参数设置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-2-17 上午11:16:22
 */
/**
     * 对应的站点
     */
/**
     * 键
     */
/**
     * 值
     */
/**
     * 名称
     */
/**
     * 描述
     */
///core/JavaSource/com/fantasy/system/bean/WebAccessLog.java
/**
	 * 访问的url
	 */
/**
	 * 请求参数
	 */
/**
	 * 
	 */
/**
	 * sessionId
	 */
/**
	 * 访问Ip
	 */
/**
	 * 浏览器
	 */
/**
	 * 浏览器版本
	 */
/**
	 * 操作系统
	 */
///core/JavaSource/com/fantasy/system/bean/Website.java
/**
 * 站点配置表，一个应用维护多套网站时，需要对每个网站的基本信息进行一些配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-2-17 上午11:07:30
 */
/**
     * 网站key（唯一）
     */
/**
     * 网站名称
     */
/**
     * 网址
     */
/**
     * 网站对应的默认文件管理器(发布文章及日志)
     */
/**
     * 网站对应的默认上传文件管理器(发布文章及日志)
     */
/**
     * 菜单根
     */
/**
     * 网站设置
     */
/**
     * 网站用户信息
     */
///core/JavaSource/com/fantasy/system/converter/DataDictionaryTypeConverter.java
/**
 * 用于将 DataDictionary 类作为动态属性时的转换方法。
 */
///core/JavaSource/com/fantasy/system/rest/DataDictionaryController.java
/**
     * @api {post} /dds   添加数据项
     * @apiVersion 3.3.2
     * @apiName createDD
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据项。
     * @apiPermission admin
     * @apiParam {String}   code            编码
     * @apiParam {String}   type            分类编码
     * @apiParam {String}   name            名称
     * @apiParam {Integer}   sort           排序
     * @apiParam {String}   description     描述
     * @apiParam {String}   parent_code     上级编码
     * @apiParam {String}   parent_type     上级分类编码
     * @apiExample Example usage:
     * curl -i -X POST -d "code=sex&type=usersex..." http://localhost/dds
     * @apiUse GeneralError
     */
/**
     * @api {post} /dds/:key   获取数据项
     * @apiVersion 3.3.2
     * @apiName getDD
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds/usersex:female
     * @apiUse GeneralError
     */
/**
     * @api {get} /dds   查询数据项
     * @apiVersion 3.3.2
     * @apiName findDDS
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以筛选需要的数据字典项。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/dds?currentPage=1&EQS_code=usersex
     * @apiUse GeneralError
     */
///core/JavaSource/com/fantasy/system/rest/DataDictionaryTypeController.java
/**
     * @api {post} /ddts   添加分类
     * @apiVersion 3.3.2
     * @apiName createDDT
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以添加新的数据字典分类。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST -d "param1=value1&param2=value2..." http://localhost/ddts
     * @apiUse GeneralError
     */
/**
     * @api {get} /ddts/:code   获取分类
     * @apiVersion 3.3.2
     * @apiName getDDT
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以获取<code>code</code>对应的字典分类信息。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/ddts/usersex
     * @apiUse GeneralError
     */
/**
     * @api {get} /ddts/:code/dds  获取分类下的字典项
     * @apiVersion 3.3.2
     * @apiName getDDSBYDDTS
     * @apiGroup 数据字典
     * @apiDescription 通过该接口, 可以获取<code>code</code>对应的所有字典信息。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/ddts/usersex/dds
     * @apiExample Response (example):
     * HTTP/1.1 200
     * {
     * "message": "删除成功"
     * }
     * @apiUse GeneralError
     */
/**
     * @api {delete} /ddts/:code   删除分类
     * @apiVersion 3.3.2
     * @apiName deleteDdt
     * @apiGroup 数据字典
     * @apiDescription 通过该接口删除数据字典分类。
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/ddts/usersex
     * @apiExample Response (example):
     * HTTP/1.1 200
     * {
     * "message": "删除成功"
     * }
     * @apiUse GeneralError
     */
///core/JavaSource/com/fantasy/system/service/DataDictionaryService.java
/**
     * 查询配置项分类
     *
     * @param name     like查询
     * @param showsize 返回结果条数，默认15条
     * @return {list}
     */
/**
     * 通过配置项分类及配置项CODE返回配置项
     *
     * @param type 类型
     * @param code 配置项CODE，返回的List顺序与codes的顺序一致
     * @return {DataDictionary}
     */
/**
     * 通过分类及上级编码查询配置项
     *
     * @param type       分类
     * @param parentCode 上级编码
     * @return {List}
     */
/**
     * 分页查询方法
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return {List}
     */
/**
     * 添加及更新配置项
     *
     * @param dataDictionary 数据字典项
     */
/**
     * 添加及更新配置项分类方法
     *
     * @param dataDictionaryType 数据字典分类
     */
/**
     * 删除配置项
     *
     * @param keys 数据字段 key
     */
/**
     * 根据分类获取配置项
     *
     * @param dataDictionaryKey 字典值
     * @return {DataDictionary}
     */
/**
     * 获取所有的配置项类型
     *
     * @return {DataDictionary}
     */
/**
     * 根据上级CODE 分类 查询下级分类
     *
     * @param code
     * @return
     */
/**
     * 根据配置项类型和编码获取配置项
     *
     * @param type 类型
     * @param code 编码
     * @return {DataDictionary}
     */
///core/JavaSource/com/fantasy/system/service/SettingService.java
/**
     * 获取设置
     *
     * @param code
     * @param websiteId
     * @return
     */
///core/JavaSource/com/fantasy/system/service/WebAccessLogService.java
/**
     * 删除
     *
     * @param ids
     */
/**
     * 查询各个浏览器的数量
     *
     * @return
     */
/**
     * 查询每天访问数量
     *
     * @return
     */
/**
     * 查询每天独立IP数
     *
     * @return
     */
///core/JavaSource/com/fantasy/system/service/WebsiteService.java
/**
     * 获取列表
     *
     * @return
     */
/**
     * 静态获取列表
     *
     * @return
     */
///core/JavaSource/org/springframework/context/annotation/ConfigurationClassEnhancer.java
/*
 * Copyright 2002-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Enhances {@link Configuration} classes by generating a CGLIB subclass which
 * interacts with the Spring container to respect bean scoping semantics for
 * {@code @Bean} methods. Each such {@code @Bean} method will be overridden in
 * the generated subclass, only delegating to the actual {@code @Bean} method
 * implementation if the container actually requests the construction of a new
 * instance. Otherwise, a call to such an {@code @Bean} method serves as a
 * reference back to the container, obtaining the corresponding bean by name.
 *
 * @author Chris Beams
 * @author Juergen Hoeller
 * @since 3.0
 * @see #enhance
 * @see ConfigurationClassPostProcessor
 */
/**
     * Loads the specified class and generates a CGLIB subclass of it equipped with
     * container-aware callbacks capable of respecting scoping and other bean semantics.
     * @return the enhanced subclass
     */
/**
     * Creates a new CGLIB {@link Enhancer} instance.
     */
/**
     * Uses enhancer to generate a subclass of superclass,
     * ensuring that callbacks are registered for the new subclass.
     */
/**
     * Marker interface to be implemented by all @Configuration CGLIB subclasses.
     * Facilitates idempotent behavior for {@link ConfigurationClassEnhancer#enhance(Class)}
     * through checking to see if candidate classes are already assignable to it, e.g.
     * have already been enhanced.
     * <p>Also extends {@link DisposableBean} and {@link BeanFactoryAware}, as all
     * enhanced {@code @Configuration} classes require access to the {@link BeanFactory}
     * that created them and must de-register static CGLIB callbacks on destruction,
     * which is handled by the (private) {@code DisposableBeanMethodInterceptor}.
     * <p>Note that this interface is intended for framework-internal use only, however
     * must remain public in order to allow access to subclasses generated from other
     * packages (i.e. user code).
     */
/**
     * Conditional {@link Callback}.
     * @see ConditionalCallbackFilter
     */
/**
     * A {@link CallbackFilter} that works by interrogating {@link Callback}s in the order
     * that they are defined via {@link ConditionalCallback}.
     */
/**
     * Custom extension of CGLIB's DefaultGeneratorStrategy, introducing a {@link BeanFactory} field.
     */
/**
     * Intercepts the invocation of any {@link BeanFactoryAware#setBeanFactory(BeanFactory)} on
     * {@code @Configuration} class instances for the purpose of recording the {@link BeanFactory}.
     * @see EnhancedConfiguration
     */
/**
     * Intercepts the invocation of any {@link Bean}-annotated methods in order to ensure proper
     * handling of bean semantics such as scoping and AOP proxying.
     * @see Bean
     * @see ConfigurationClassEnhancer
     */
/**
         * Enhance a {@link Bean @Bean} method to check the supplied BeanFactory for the
         * existence of this bean object.
         * @throws Throwable as a catch-all for any exception that may be thrown when
         * invoking the super implementation of the proxied method i.e., the actual
         * {@code @Bean} method.
         */
/**
         * Check the BeanFactory to see whether the bean named <var>beanName</var> already
         * exists. Accounts for the fact that the requested bean may be "in creation", i.e.:
         * we're in the middle of servicing the initial request for this bean. From an enhanced
         * factory method's perspective, this means that the bean does not actually yet exist,
         * and that it is now our job to create it for the first time by executing the logic
         * in the corresponding factory method.
         * <p>Said another way, this check repurposes
         * {@link ConfigurableBeanFactory#isCurrentlyInCreation(String)} to determine whether
         * the container is calling this method or the user is calling this method.
         * @param beanName name of bean to check for
         * @return whether <var>beanName</var> already exists in the factory
         */
/**
         * Check whether the given method corresponds to the container's currently invoked
         * factory method. Compares method name and parameter types only in order to work
         * around a potential problem with covariant return types (currently only known
         * to happen on Groovy classes).
         */
/**
         * Create a subclass proxy that intercepts calls to getObject(), delegating to the current BeanFactory
         * instead of creating a new instance. These proxies are created only when calling a FactoryBean from
         * within a Bean method, allowing for proper scoping semantics even when working against the FactoryBean
         * instance directly. If a FactoryBean instance is fetched through the container via &-dereferencing,
         * it will not be proxied. This too is aligned with the way XML configuration works.
         */
/**
     * Intercepts the invocation of any {@link DisposableBean#destroy()} on @Configuration
     * class instances for the purpose of de-registering CGLIB callbacks. This helps avoid
     * garbage collection issues. See SPR-7901.
     * @see EnhancedConfiguration
     */
///core-web/JavaSource/com/fantasy/attr/web/AttributeAction.java
/**
	 * 首页
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 查询属性
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
/**
	 * 编辑
	 * @param id
	 * @return
	 */
/**
	 * 删除
	 * @param ids
	 * @return
	 */
///core-web/JavaSource/com/fantasy/attr/web/AttributeTypeAction.java
/**
 * 商品属性类型
 * @author mingliang
 *
 */
/**
	 * 商品属性类型进入页面
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 查询
	 * 
	 * @功能描述
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 保存
	 * 
	 * @param attributeType
	 * @return
	 */
/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
///core-web/JavaSource/com/fantasy/attr/web/AttributeVersionAction.java
/**
	 * 首页
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 查询属性
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 保存属性
	 * @param attribute
	 * @return
	 */
/**
	 * 编辑
	 * @param id
	 * @return
	 */
/**
	 * 删除
	 * @param ids
	 * @return
	 */
/**
     *添加非临时属性
     * @param vid 版本id
     * @param id 属性id
     * @return
     */
/**
     *移除属性
     * @param vid 版本id
     * @param id 属性id
     * @return
     */
///core-web/JavaSource/com/fantasy/attr/web/ConverterAction.java
/**
 * 转换器
 * @author mingliang
 *
 */
/**
     * 转换器首页
     *
     * @功能描述
     * @return
     */
/**
	 * 查询
	 * 
	 * @功能描述
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 保存
	 * 
	 * @return
	 */
/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
/**
     * 详情
     * @param id
     * @return
     */
///core-web/JavaSource/com/fantasy/common/web/AreaAction.java
/**
 *@Author lsz
 *@Date 2013-11-21 下午1:30:15
 *
 */
/**
     * 首页
     * @return
     */
/**
     * 分页
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     * @param area
     * @return
     */
/**
     * 修改
     * @param id
     * @return
     */
/**
     * 删除
     */
///core-web/JavaSource/com/fantasy/common/web/CommonAction.java
/**
	 * 获取guid
	 * 
	 * @功能描述
	 * @return
	 */
///core-web/JavaSource/com/fantasy/contacts/web/ContactsAction.java
/**
     *
     */
/**
     * 添加联系人分组
     *
     * @param group 分组
     * @return json
     */
/**
     * 联系人查询
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return json
     * @功能描述
     */
/**
     * 查询联系人组
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return json
     */
/**
     * editLinkman
     * 编辑联系人
     *
     * @param id id
     * @return succes
     */
/**
     * 删除联系人
     *
     * @param ids ids
     * @return json
     */
/**
     * 删除分组
     *
     * @param ids ids
     * @return json
     */
/**
     * 添加联系人
     *
     * @param linkman 联系人信息
     * @return json
     */
/**
     * 删除联系人
     *
     * @param ids 联系人ids
     * @return json
     */
///core-web/JavaSource/com/fantasy/file/web/FileAction.java
/**
     * @param attach            要上传的文件
     * @param attachContentType contentType
     * @param attachFileName    名称
     * @param dir               上传的目录标识
     * @param entireFileName    完整文件名
     * @param entireFileDir     完整文件的上传目录标识
     * @param entireFileHash    完整文件Hash值
     * @param partFileHash      分段文件Hash值
     * @param total             总段数
     * @param index             分段序号
     * @return {String} 返回文件信息
     * @throws IOException
     */
/**
     * 文件下载
     *
     * @throws IOException
     */
/**
     * 文件查看页
     *
     * @param fileManagerId 文件管理器
     * @param path          查看的目录
     * @return {String}
     */
/**
     * 用于fileManager加载文件
     *
     * @param fileManagerId 文件管理器
     * @param path          查看的目录
     * @return {String}
     */
///core-web/JavaSource/com/fantasy/file/web/FileManagerAction.java
/**
	 * 文件管理器
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 编辑文件管理器配置
	 * 
	 * @功能描述
	 * @param id
	 * @return
	 */
/**
	 * 保存文件管理器
	 * 
	 * @功能描述
	 * @param fm
	 * @return
	 */
/**
	 * 查看
	 * @param id
	 * @return
	 */
/**
	 * 删除文件管理器
	 * 
	 * @功能描述
	 * @param ids
	 * @return
	 */
///core-web/JavaSource/com/fantasy/file/web/validator/FileManagerValidaotr.java
/**
 *@Author lsz
 *@Date 2013-12-19 下午3:16:53
 *
 */
///core-web/JavaSource/com/fantasy/framework/freemarker/loader/FreemarkerTemplateLoader.java
/**
// * Created by wml on 2015/1/26.
// */
///core-web/JavaSource/com/fantasy/framework/freemarker/TemplateModelUtils.java
/**
 * TemplateModel 工具类
 */
///core-web/JavaSource/com/fantasy/framework/freemarker/util/DirectiveUtil.java
/**
	 * 获取String类型的参数值
	 * 
	 * @return 参数值
	 */
/**
	 * 获取Integer类型的参数值
	 * 
	 * @return 参数值
	 */
/**
	 * 获取Boolean类型的参数值
	 * 
	 * @return 参数值
	 */
/**
	 * 获取Date类型的参数值
	 * 
	 * @return 参数值
	 */
/**
	 * 获取Object类型的参数值
	 * 
	 * @return 参数值
	 */
///core-web/JavaSource/com/fantasy/framework/struts2/core/FantasyActionInvocation.java
/**
     * @throws ConfigurationException If no result can be found with the returned code
     */
/**
     * Uses getResult to get the final Result and executes it
     *
     * @throws ConfigurationException If not result can be found with the returned code
     */
/**
     * Save the result to be used later.
     *
     * @param actionConfig actionConfig
     * @param methodResult the result of the action.
     * @return the result code to process.
     */
///core-web/JavaSource/com/fantasy/framework/struts2/core/interceptor/FileUploadInterceptor.java
/**
	 * Sets the allowed extensions
	 * 
	 * @param allowedExtensions
	 *            A comma-delimited list of extensions
	 */
/**
	 * Sets the allowed mimetypes
	 * 
	 * @param allowedTypes
	 *            A comma-delimited list of types
	 */
/**
	 * Sets the maximum size of an uploaded file
	 * 
	 * @param maximumSize
	 *            The maximum size in bytes
	 */
/*
	 * (non-Javadoc)
	 * 
	 * @see com.opensymphony.xwork2.interceptor.Interceptor#intercept(com.opensymphony.xwork2.ActionInvocation)
	 */
/**
	 * Override for added functionality. Checks if the proposed file is acceptable based on contentType and size.
	 * 
	 * @param action
	 *            - uploading action for message retrieval.
	 * @param file
	 *            - proposed upload file.
	 * @param contentType
	 *            - contentType of the file.
	 * @param inputName
	 *            - inputName of the file.
	 * @param validation
	 *            - Non-null ValidationAware if the action implements ValidationAware, allowing for better logging.
	 * @param locale
	 * @return true if the proposed file is acceptable by contentType and size.
	 */
/**
	 * @param extensionCollection
	 *            - Collection of extensions (all lowercase).
	 * @param filename
	 *            - filename to check.
	 * @return true if the filename has an allowed extension, false otherwise.
	 */
/**
	 * @param itemCollection
	 *            - Collection of string items (all lowercase).
	 * @param item
	 *            - Item to search for.
	 * @return true if itemCollection contains the item, false otherwise.
	 */
///core-web/JavaSource/com/fantasy/framework/struts2/core/interceptor/ParametersInterceptor.java
/**
     * 基本数据类型，及其封装类型。Date，File，String 返回true
     *
     * @param parameterType 类型
     * @return boolean
     */
/**
     * 用于缓存动态formBean
     */
/**
     * 从 parameters 中提取 List<PropertyFilter> 对应的参数
     *
     * @param paramName  参数名称
     * @param parameters 提交的参数
     * @return string
     */
/**
     * TODO 以后扩展时使用
     *
     * @param parameterTypes 参数类型
     * @param paramNames     参数名称
     * @param parameters     请求参数
     * @return Class[]
     */
///core-web/JavaSource/com/fantasy/framework/struts2/core/result/JSONResult.java
/**
     * Retrieve the encoding
     * <p/>
     *
     * @return The encoding associated with this template (defaults to the value of 'struts.i18n.encoding' property)
     */
/**
     * @return OGNL expression of root object to be serialized
     */
/**
     * Sets the root object to be serialized, defaults to the Action
     *
     * @param root OGNL expression of root object to be serialized
     */
/**
     * Add headers to response to prevent the browser from caching the response
     *
     * @param noCache
     */
/**
     * Status code to be set in the response
     *
     * @param statusCode
     */
/**
     * Error code to be set in the response
     *
     * @param errorCode
     */
/**
     * Content type to be set in the response
     *
     * @param contentType
     */
///core-web/JavaSource/com/fantasy/framework/struts2/core/validator/FantasyAnnotationActionValidatorManager.java
/**
 * 扩展 AnnotationActionValidatorManager 的验证<br/>
 * 使其支持数组及List的验证<br/>
 *
 * @author lmf
 */
///core-web/JavaSource/com/fantasy/framework/struts2/core/validator/validators/SpringExpressionValidator.java
/**
 * spring expression 验证
 */
///core-web/JavaSource/com/fantasy/framework/struts2/DynaModelDriven.java
/**
 * 用于动态bean不传递版本号时的Model生产
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/ContentTypeHandlerManager.java
/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Manages content type handlers
 */
/**
     * Gets the handler for the request by looking at the request content type and extension
     * @param req The request
     * @return The appropriate handler
     */
/**
     * Gets the handler for the response by looking at the extension of the request
     * @param req The request
     * @return The appropriate handler
     */
/**
     * Handles the result using handlers to generate content type-specific content
     *
     * @param actionConfig The action config for the current request
     * @param methodResult The object returned from the action method
     * @param target The object to return, usually the action object
     * @return The new result code to process
     * @throws IOException If unable to write to the response
     */
/**
     * Finds the extension in the url
     * 
     * @param url The url
     * @return The extension
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/ContentTypeInterceptor.java
/*
 * $Id: ContentTypeInterceptor.java 1204408 2011-11-21 09:13:25Z mcucchiara $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Uses the content handler to apply the request body to the action
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/DefaultContentTypeHandlerManager.java
/*
 * $Id: DefaultContentTypeHandlerManager.java 1397694 2012-10-12 19:12:17Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Manages {@link ContentTypeHandler} instances and uses them to
 * process results
 */
/** ContentTypeHandlers keyed by the extension */
/** ContentTypeHandlers keyed by the content-type */
/**
     * Gets the handler for the request by looking at the request content type and extension
     * @param req The request
     * @return The appropriate handler
     */
/**
     * Gets the handler for the response by looking at the extension of the request
     * @param req The request
     * @return The appropriate handler
     */
/**
     * Handles the result using handlers to generate content type-specific content
     * 
     * @param actionConfig The action config for the current request
     * @param methodResult The object returned from the action method
     * @param target The object to return, usually the action object
     * @return The new result code to process
     * @throws IOException If unable to write to the response
     */
/**
     * Finds the extension in the url
     * 
     * @param url The url
     * @return The extension
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/DefaultHttpHeaders.java
/*
 * $Id: DefaultHttpHeaders.java 1026675 2010-10-23 20:19:47Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Default implementation of rest info that uses fluent-style construction
 */
/* (non-Javadoc)
     * @see org.apache.struts2.rest.HttpHeaders#apply(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/ContentTypeHandler.java
/*
 * $Id: ContentTypeHandler.java 666756 2008-06-11 18:11:00Z hermanns $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles transferring content to and from objects for a specific content type
 */
/**
     * Populates an object using data from the input stream
     * @param in The input stream, usually the body of the request
     * @param target The target, usually the action class
     */
/**
     * Writes content to the stream
     * 
     * @param obj The object to write to the stream, usually the Action class
     * @param resultCode The original result code
     * @param stream The output stream, usually the response
     * @return The new result code
     * @throws IOException If unable to write to the output stream
     */
/**
     * Gets the content type for this handler
     * 
     * @return The mime type
     */
/**
     * Gets the extension this handler supports
     * 
     * @return The extension
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/FormUrlEncodedHandler.java
/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles the default content type for requests that originate from a browser's HTML form
 *
 * content-type: application/x-www-form-urlencoded
 *
 * This handler is intended for requests only, not for responses
 *
 * {@link http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4}
 *
 */
/** No transformation is required as the framework handles this data */
/**
     * The extension is not used by this handler
     * @return
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/HtmlHandler.java
/*
 * $Id: HtmlHandler.java 651946 2008-04-27 13:41:38Z apetrelli $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles HTML content, usually just a simple passthrough to the framework
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/JacksonLibHandler.java
/*
 * $Id: JsonLibHandler.java 1097172 2011-04-27 16:36:54Z jogep $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles JSON content using jackson-lib
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/MultipartFormDataHandler.java
/*
 * $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles the default content type for requests that originate from a browser's HTML form 
 * with a file upload and multipart/from-data encoding
 *
 * content-type: multipart/form-data
 *
 * This handler is intended for requests only, not for responses
 *
 * {@link http://www.w3.org/TR/html401/interact/forms.html#h-17.13.4}
 *
 */
/** No transformation is required as the framework handles this data */
/**
     * The extension is not used by this handler
     * @return
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/handler/XStreamHandler.java
/*
 * $Id: XStreamHandler.java 666756 2008-06-11 18:11:00Z hermanns $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Handles XML content
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/HttpHeaders.java
/*
 * $Id: HttpHeaders.java 1026675 2010-10-23 20:19:47Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Type-safe rest-related informtion to apply to a response
 */
/**
     * Applies the configured information to the response
     * @param request The request
     * @param response The response
     * @param target The target object, usually the action
     * @return The result code to process
     */
/**
     * The HTTP status code
     */
/**
     * The HTTP status code
     */
/**
     * The result code to process
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/RestActionInvocation.java
/*
 * $Id: RestActionInvocation.java 1397705 2012-10-12 19:56:30Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Extends the usual {@link ActionInvocation} to add support for processing the object returned
 * from the action execution.  This allows us to support methods that return {@link HttpHeaders}
 * as well as apply content type-specific operations to the result.
 */
/**
     * If set to true (by default) blocks returning content from any other methods than GET,
     * if set to false, the content can be returned for any kind of method
     * 
     * @param value true or false
     */
/**
     * Save the result to be used later.
     * @param actionConfig current ActionConfig
     * @param methodResult the result of the action.
     * @return the result code to process.
     *
     * @throws ConfigurationException If it is an incorrect result.
     */
/**
     * Execute the current result. If it is an error and no result is selected load
     * the default error result (default-error).
     */
/**
     * Get the status code from HttpHeaderResult and it is saved in the HttpHeaders object.
     */
/**
     * Find the most appropriate result:
     * - Find by result code.
     * - If it is an error, find the default error result.
     *
     * @throws ConfigurationException If not result can be found
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/RestActionMapper.java
/**
     * Parses the name and namespace from the uri.  Uses the configured package
     * namespaces to determine the name and id parameter, to be parsed later.
     *
     * @param uri     The uri
     * @param mapping The action mapping to populate
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/RestActionProxyFactory.java
/*
 * $Id: RestActionProxyFactory.java 1397705 2012-10-12 19:56:30Z lukaszlenart $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * Factory that creates the {@link RestActionInvocation}
 */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/RestActionSupport.java
/**
     * Default execution.
     *
     * @return object because it can return string, result or httpHeader.
     * @throws Exception
     */
/**
     * Inspect the implemented methods to know the allowed http methods.
     *
     * @return Include the header "Allow" with the allowed http methods.
     */
/**
     * By default, return continue.
     * Is possible override the method to return expectation failed.
     *
     * @return continue
     */
/**
     * By default, return continue.
     * Is possible override the method to return expectation failed.
     *
     * @return continue
     */
///core-web/JavaSource/com/fantasy/framework/struts2/rest/RestWorkflowInterceptor.java
/*
 * $Id: RestWorkflowInterceptor.java 676195 2008-07-12 15:55:58Z mrdon $
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/**
 * <!-- START SNIPPET: description -->
 *
 * An interceptor that makes sure there are not validation errors before allowing the interceptor chain to continue.
 * <b>This interceptor does not perform any validation</b>.
 * 
 * <p>Copied from the {@link com.opensymphony.xwork2.interceptor.DefaultWorkflowInterceptor}, this interceptor adds support for error handling of Restful
 * operations.  For example, if an validation error is discovered, a map of errors is created and processed to be
 * returned, using the appropriate content handler for rendering the body.</p>
 *
 * <p/>This interceptor does nothing if the name of the method being invoked is specified in the <b>excludeMethods</b>
 * parameter. <b>excludeMethods</b> accepts a comma-delimited list of method names. For example, requests to
 * <b>foo!input.action</b> and <b>foo!back.action</b> will be skipped by this interceptor if you set the
 * <b>excludeMethods</b> parameter to "input, back".
 *
 * <b>Note:</b> As this method extends off MethodFilterInterceptor, it is capable of
 * deciding if it is applicable only to selective methods in the action class. This is done by adding param tags
 * for the interceptor element, naming either a list of excluded method names and/or a list of included method
 * names, whereby includeMethods overrides excludedMethods. A single * sign is interpreted as wildcard matching
 * all methods for both parameters.
 * See {@link MethodFilterInterceptor} for more info.
 *
 * <!-- END SNIPPET: description -->
 *
 * <p/> <u>Interceptor parameters:</u>
 *
 * <!-- START SNIPPET: parameters -->
 *
 * <ul>
 *
 * <li>inputResultName - Default to "input". Determine the result name to be returned when
 * an action / field error is found.</li>
 *
 * </ul>
 *
 * <!-- END SNIPPET: parameters -->
 *
 * <p/> <u>Extending the interceptor:</u>
 *
 * <p/>
 *
 * <!-- START SNIPPET: extending -->
 *
 * There are no known extension points for this interceptor.
 *
 * <!-- END SNIPPET: extending -->
 *
 * <p/> <u>Example code:</u>
 *
 * <pre>
 * <!-- START SNIPPET: example -->
 * 
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="params"/&gt;
 *     &lt;interceptor-ref name="validation"/&gt;
 *     &lt;interceptor-ref name="workflow"/&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 * 
 * &lt;-- In this case myMethod as well as mySecondMethod of the action class
 *        will not pass through the workflow process --&gt;
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="params"/&gt;
 *     &lt;interceptor-ref name="validation"/&gt;
 *     &lt;interceptor-ref name="workflow"&gt;
 *         &lt;param name="excludeMethods"&gt;myMethod,mySecondMethod&lt;/param&gt;
 *     &lt;/interceptor-ref name="workflow"&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 *
 * &lt;-- In this case, the result named "error" will be used when
 *        an action / field error is found --&gt;
 * &lt;-- The Interceptor will only be applied for myWorkflowMethod method of action
 *        classes, since this is the only included method while any others are excluded --&gt;
 * &lt;action name="someAction" class="com.examples.SomeAction"&gt;
 *     &lt;interceptor-ref name="params"/&gt;
 *     &lt;interceptor-ref name="validation"/&gt;
 *     &lt;interceptor-ref name="workflow"&gt;
 *        &lt;param name="inputResultName"&gt;error&lt;/param&gt;
*         &lt;param name="excludeMethods"&gt;*&lt;/param&gt;
*         &lt;param name="includeMethods"&gt;myWorkflowMethod&lt;/param&gt;
 *     &lt;/interceptor-ref&gt;
 *     &lt;result name="success"&gt;good_result.ftl&lt;/result&gt;
 * &lt;/action&gt;
 *
 * <!-- END SNIPPET: example -->
 * </pre>
 *
 * @author Jason Carreira
 * @author Rainer Hermanns
 * @author <a href='mailto:the_mindstorm[at]evolva[dot]ro'>Alexandru Popescu</a>
 * @author Philip Luppens
 * @author tm_jee
 */
/**
	 * Set the <code>inputResultName</code> (result name to be returned when 
	 * a action / field error is found registered). Default to {@link Action#INPUT}
	 * 
	 * @param inputResultName what result name to use when there was validation error(s).
	 */
/**
	 * Intercept {@link ActionInvocation} and processes the errors using the {@link com.fantasy.framework.struts2.rest.handler.ContentTypeHandler}
	 * appropriate for the request.  
	 * 
	 * @return String result name
	 */
///core-web/JavaSource/com/fantasy/framework/struts2/StrutsJUnit4TestCase.java
/**
     * gets an object from the stack after an action is executed
     */
/**
     * gets an object from the stack after an action is executed
     *
     * @return The executed action
     */
/**
     * Executes an action and returns it's output (not the result returned from
     * execute()), but the actual output that would be written to the response.
     * For this to work the configured result for the action needs to be
     * FreeMarker, or Velocity (JSPs can be used with the Embedded JSP plugin)
     */
/**
     * Store state of StrutsConstants.STRUTS_LOCALE setting.
     */
/**
     * Merge all application and servlet attributes into a single <tt>HashMap</tt> to represent the entire
     * <tt>Action</tt> context.
     *
     * @param requestMap     a Map of all request attributes.
     * @param parameterMap   a Map of all request parameters.
     * @param sessionMap     a Map of all session attributes.
     * @param applicationMap a Map of all servlet context attributes.
     * @param request        the HttpServletRequest object.
     * @param response       the HttpServletResponse object.
     * @param servletContext the ServletContextmapping object.
     * @return a HashMap representing the <tt>Action</tt> context.
     */
/**
     * Create a context map containing all the wrapped request objects
     *
     * @param request The servlet request
     * @param response The servlet response
     * @param mapping The action mapping
     * @param context The servlet context
     * @return A map of context objects
     */
/**
     * Creates an action proxy for a request, and sets parameters of the ActionInvocation to the passed
     * parameters. Make sure to set the request parameters in the protected "request" object before calling this method.
     */
/**
     * Finds an ActionMapping for a given request
     */
/**
     * Finds an ActionMapping for a given url
     */
/**
     * Injects dependencies on an Object using Struts internal IoC container
     */
/**
     * Sets up the configuration settings, XWork configuration, and
     * message resources
     */
/**
     * Override this method to return a comma separated list of paths to a configuration
     * file.
     * <p>The default implementation simply returns <code>null</code>.
     * @return a comma separated list of config locations
     */
///core-web/JavaSource/com/fantasy/framework/struts2/views/components/File.java
/*, allowDynamicAttributes = true*/
///core-web/JavaSource/com/fantasy/framework/struts2/views/components/Img.java
/*
		if (ratio != null) {
			FileItem fileItem = FileFilter.getFileCache(tmpsrc);
			if (fileItem != null) {
				return;
			}
			FileManager fileManager = SettingUtil.getDefaultUploadFileManager();
			fileItem = fileManager.getFileItem(tmpsrc);
			if (fileItem != null) {
				FileFilter.addFileCache(tmpsrc, fileItem);
				return;
			}
			fileItem = FileFilter.getFileCache(src);
			if (fileItem == null) {
				fileItem = fileManager.getFileItem(src);
				if (fileItem == null) {
					return;
				}
				FileFilter.addFileCache(src, fileItem);
			}
			// 只自动缩放 image/jpeg 格式的图片
			if (!"image/jpeg".equals(fileItem.getContentType())) {
				return;
			}
			try {
				String[] tmpRatio = RegexpUtil.split(ratio, "x");
				// 图片缩放
				BufferedImage image = ImageUtil.reduce(fileItem.getInputStream(), Integer.valueOf(tmpRatio[0]), Integer.valueOf(tmpRatio[1]));
				// 创建临时文件
				File tmp = FileUtil.tmp();
				ImageUtil.write(image, tmp);
				fileManager.writeFile(tmpsrc, tmp);
				// 删除临时文件
				FileUtil.delFile(tmp);
				FileFilter.addFileCache(tmpsrc, fileItem = fileManager.getFileItem(tmpsrc));
			} catch (IOException e) {
				logger.error(e.getMessage(), e);
			}
		}*/
///core-web/JavaSource/com/fantasy/framework/web/filter/PageCacheFilter.java
/**
 * 过滤器 - 页面缓存
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-3-20 下午5:17:43
 */
///core-web/JavaSource/com/fantasy/member/web/FindPwdAction.java
/**
 * @Author lsz
 * @Date 2013-12-10 上午9:25:46
 * 
 */
/**
	 * 判断用户是否进行过邮箱验证
	 * 
	 * @param username
	 * @return
	 */
/**
	 * 找回密码发送邮件
	 * 
	 * @param username
	 * @return
	 */
/**
	 * 重新输入密码
	 * 
	 * @param username
	 * @return
	 */
/**
	 * 密码修改成功
	 * 
	 * @param member
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/ForgotPasswdAction.java
/**
 * 密码找回
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-17 上午10:21:35
 * @version 1.0
 */
/**
	 * 发送验证邮件
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 邮件验证
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 重置密码
	 * 
	 * @功能描述
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/LoginAction.java
/**
 * 登陆Action
 */
/**
     *  跳转登陆页面<br/>
     *  将spring security 的session异常转为request异常
     */
///core-web/JavaSource/com/fantasy/member/web/MemberAction.java
/**
 * 会员操作action
 * 
 * @功能描述 该类方法为管理端方法
 * @author 李茂峰
 * @since 2013-12-17 上午10:22:06
 * @version 1.0
 */
/*
	@Autowired
	private CartService cartService;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private OrderService orderService;

	@Autowired
	private ReceiverService receiverService;
	*/
/**
	 * 会员首页
	 * 
	 * @return
	 */
/**
	 * 会员搜索列表
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 会员购物车搜索
	 * 
	 * @param pager
	 * @param filters
	 * @return

	public String cartsearch(Pager<CartItem> pager, List<PropertyFilter> filters) {
		filters.add(new PropertyFilter("EQE_cart.ownerType", OwnerType.Member));
		this.attrs.put("cartItemPager", this.cartService.findPager(pager, filters));
		return JSONDATA;
	}*/
/**
	 * 会员保存
	 * 
	 * @param member 会员对象
	 * @return {String}
	 */
/**
	 * 会员修改
	 * 
	 * @param id 编辑会员信息
	 * @return {String}
	 */
/**
	 * 会员显示
	 * 
	 * @param id 会员Id
	 * @return {String}
	 */
/**
	 * 会员删除
	 * 
	 * @param ids
	 * @return
	 */
/**
	 * 会员收藏
	 * 
	 * @param id
	 * @return

	public String favorite(String id) {
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQL_favoriteMembers.id", id));
		this.attrs.put("pager", goodsService.findPager(new Pager<Goods>(), filters));
		return SUCCESS;
	}*/
///core-web/JavaSource/com/fantasy/member/web/MemberCenterAction.java
/**
	 * 会员登录中心
	 * 
	 * @return string
     */
/**
	 * 前台基本信息提交页面
	 * 
	 * @return {string}
     */
/**
	 * 我的订单列表
	 *
	 * @return
	public String order(Pager<Order> pager, List<PropertyFilter> filters) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		// 强制排序
		pager.setOrderBy("createTime");
		pager.setOrder(Pager.Order.desc);
		pager.setPageSize(5);
		// 只返回当前用户的订单
		filters.add(new PropertyFilter("EQL_member.id", member.getId() + ""));
		// 直接查询
		this.attrs.put("pager", this.orderService.findPager(pager, filters));
		return SUCCESS;
	}

	 * 我的收藏列表
	 * 
	 * @return

	public String favorite() {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
		filters.add(new PropertyFilter("EQL_favoriteMembers.id", member.getId() + ""));
		Pager<Goods> pager = new Pager<Goods>();
		pager.setOrderBy("createTime");
		pager.setOrder(Pager.Order.desc);
		this.attrs.put("pager", this.goodsService.findPager(pager, filters));
		return SUCCESS;
	}

	 * 删除我的收藏操作
	 * 
	 * @param ids
	 * @return

	public String favoriteDel(Long... ids) {
		Member member = memberService.get(SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser().getId());
		for (Long id : ids) {
			ObjectUtil.remove(member.getFavoriteGoods(), "id", id);
		}
		memberService.save(member);
		return JSONDATA;
	}

	 * 我的收获地址
	 * 
	 * @return

	public String receiver() {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		this.attrs.put("member", member);
		this.attrs.put("receivers", this.receiverService.find(new Criterion[] { Restrictions.eq("member.id", member.getId()) }, "isDefault", "desc"));
		return SUCCESS;
	}*/
/**
	 * 添加我的收获地址
	 * 
	 * @return

	public String receiverSave(Receiver receiver) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		receiver.setMember(member);
		this.attrs.put(ROOT, this.receiverService.save(receiver));
		return JSONDATA;
	}*/
/**
	 * 删除我的收获地址
	 * 
	 * @param receiver
	 * @return

	public String receiverDel(Receiver receiver) {
		this.receiverService.deltele(receiver.getId());
		return JSONDATA;
	}*/
/**
	 * 设置为默认收获地址
	 * 
	 * @param receiver
	 * @return

	public String receiverDef(Long id) {
		Member member = SpringSecurityUtils.getCurrentUser(MemberUser.class).getUser();
		List<Receiver> receivers = this.receiverService.find(new Criterion[] { Restrictions.eq("member.id", member.getId()) }, "isDefault", "desc");
		Receiver receiver = null;

		while (null != (receiver = ObjectUtil.find(receivers, "isDefault", true))) {
			receiver.setIsDefault(false);
			this.receiverService.save(receiver);
		}

		receiver = ObjectUtil.find(receivers, "id", id);
		receiver.setIsDefault(true);
		this.receiverService.save(receiver);

		this.attrs.put(ROOT, ObjectUtil.sort(receivers, "isDefault", "desc"));
		return JSONDATA;
	}

	 * 我的评论
	 * 
	 * @return
	public String comment() {
		return SUCCESS;
	}

	/**
	 * 我的问答
	 * 
	 * @return

	public String problem() {
		return SUCCESS;
	}
     */
///core-web/JavaSource/com/fantasy/member/web/PointAction.java
/**
 * 积分收支明细
 * @author mingliang
 *
 */
/**
	 * 保存或修改数据
	 * @param consume
	 * @return
	 */
/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/RegisterAction.java
/**
 * @Author lsz
 * @Date 2013-12-10 上午9:25:46
 * 
 */
/**
	 * 会员注册成功
	 * 
	 * @param member
	 * @return
	 * @throws Exception
	 */
/**
	 * 邮箱注册成功发送邮件验证
	 * @return
	 */
/**
	 * 邮箱验证第二步发送邮件
	 * 
	 * @param member
	 * @return
	 */
/**
	 * 接收邮件反馈信息
	 * @param memberId
	 * @return
	 */
/**
	 * 判断用户是否进行过邮箱验证
	 * @param username
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/ValidateMailAction.java
/**
 * 邮箱认证
 * 
 * @功能描述 用于验证邮箱是否邮箱及用户与邮箱的绑定操作
 * @author 李茂峰
 * @since 2013-12-17 上午10:08:49
 * @version 1.0
 */
/**
	 * 发送验证邮件
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 绑定邮箱
	 * 
	 * @功能描述
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/ValidateMobileAction.java
/**
 * 手机认证
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-17 上午10:08:33
 * @version 1.0
 */
/**
	 * 发送验证邮件
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 绑定邮箱
	 * 
	 * @功能描述
	 * @return
	 */
///core-web/JavaSource/com/fantasy/member/web/validator/MemberEmailUniqueValidator.java
/**
 * 验证用户email唯一
 * 
 * @Author lsz
 * @Date 2013-12-12 下午6:08:25
 * 
 */
///core-web/JavaSource/com/fantasy/member/web/validator/MemberExistValidator.java
/**
 * @Author lsz
 * @Date 2013-12-9 上午11:42:55
 * 
 */
///core-web/JavaSource/com/fantasy/member/web/validator/MemberUserNameUniqueValidator.java
/**
 * 会员用户名唯一验证
 * 
 * @Author lsz
 * @Date 2013-12-9 上午11:42:55
 * 
 */
///core-web/JavaSource/com/fantasy/payment/web/PaymentAction.java
/**
 * 支付处理类
 */
/**
     * 支付提交
     *
     * @param orderType       订单类型
     * @param orderSn         订单编号
     * @param paymentConfigId 支付方式
     * @return {string}
     */
///core-web/JavaSource/com/fantasy/payment/web/PaymentConfigAction.java
/**
     * 支持测试方法
     *
     * @return {String}
     */
/**
     * 首页
     *
     * @return {String}
     */
/**
     * 搜索
     *
     * @param pager   翻页对象
     * @param filters 过滤条件
     * @return {String}
     */
/**
     * 保存
     *
     * @param config 支付配置
     * @return {String}
     */
/**
     * 修改
     *
     * @param id 配置id
     * @return {String}
     */
/**
     * 显示
     *
     * @param id 配置id
     * @return {String}
     */
/**
     * 批量删除
     *
     * @param ids 配置ids
     * @return {String}
     */
///core-web/JavaSource/com/fantasy/remind/web/NoticeAction.java
/**
 * 提醒 action
 */
///core-web/JavaSource/com/fantasy/schedule/web/ScheduleAction.java
/*"功能代码对照<br>[新单照会=>"+CommonConstant.NEWPOLICY;
        fncoddesc += " 变更照会=>"+CommonConstant.NOTECHANGE;
        fncoddesc += "]\n[变更完成=>"+CommonConstant.CHANGECOMPLETE;
        fncoddesc += " 核保完成=>"+CommonConstant.VALIDPOLICY;
        fncoddesc += "]\n[失效中止自动垫付=>"+CommonConstant.INVALIDPOLICY;
        fncoddesc += " 新单自动转账=>"+CommonConstant.AUTOTRANSFER;
        fncoddesc += "]\n[续期保费到期查询=>"+CommonConstant.DUEPOLICY;
        fncoddesc += " 保险款项待支付=>"+CommonConstant.WAITPAY;
        fncoddesc += " 新单退费=>"+CommonConstant.POLICYREFUND;
        fncoddesc += "]\n[等待生存证明=>"+CommonConstant.WAITLIVEPROOF;
        fncoddesc += " 理赔照会=>"+CommonConstant.CLAIMNOTE;
        fncoddesc += "]\n[理赔完成=>"+CommonConstant.CLAIMCOMPLETE;
        fncoddesc += " 客户来电=>"+CommonConstant.CUSTOMERCALL;
        fncoddesc += "]\n[新单回访=>"+CommonConstant.NEWVISIT;
        fncoddesc += " 保单回执=>"+CommonConstant.POLICYRECEIPT;
        fncoddesc += " 自动转账授权结果=>"+CommonConstant.AUTORESULT;
        fncoddesc += " 客户生日提醒=>"+CommonConstant.REMINDBRITH;
        fncoddesc += "]";*/
/**
     * 添加任务的处理，处理完成后返回任务列表
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 执行某个TASK一次
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 中断TASK执行
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 显示修改页面
     *
     * @return {String}
     * @throws org.quartz.SchedulerException
     */
/**
     * 修改处理
     *
     * @return {String}
     * @throws org.quartz.SchedulerException
     */
/**
     * 删除TASK
     *
     * @throws org.quartz.SchedulerException public String deleteJob(String jobKey) throws SchedulerException {
     *                                       String[] keyArray = jobKey.split("\\.");
     *                                       scheduler.deleteJob(JobKey.jobKey(keyArray[1], keyArray[0]));
     *                                       return JSONDATA;
     *                                       }
     */
/**
     * 暂停一个Trigger（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 关闭调度器（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 关闭调度器（没有调用)
     *
     * @throws org.quartz.SchedulerException
     */
/**
     * 重启一个Trigger（没有调用)
     *
     * @param jobKey 任务ID
     * @throws org.quartz.SchedulerException
     */
/**
     * （没有调用)
     *
     * @param jobsInfo
     * @throws org.quartz.SchedulerException
     */
///core-web/JavaSource/com/fantasy/security/web/JobAction.java
/**
 * 岗位Action
 */
///core-web/JavaSource/com/fantasy/security/web/MenuAction.java
/*Restrictions.like("path", SettingUtil.getRootMenuId() + Menu.PATH_SEPARATOR, MatchMode.START)*/
///core-web/JavaSource/com/fantasy/security/web/OldUserAction.java
/**
	 * 用户列表
	 * 
	 * @功能描述
	 * @return
	 */
/**
	 * 用户查询
	 * 
	 * @功能描述
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 删除用户
	 * 
	 * @功能描述
	 * @param ids
	 * @return
	 * @throws Exception
	 */
///core-web/JavaSource/com/fantasy/security/web/OrganizAction.java
/**
 * 组织机构Action
 *
 */
///core-web/JavaSource/com/fantasy/security/web/OrgDimensionAction.java
/**
 * 组织维度Action
 */
///core-web/JavaSource/com/fantasy/security/web/RoleAction.java
/**
     * 角色列表
     *
     * @return {string}
     */
/**
     * 角色查询
     *
     * @param pager   分页对象
     * @param filters 筛选条件
     * @return {string}
     */
/**
     * 删除角色
     *
     * @param ids 角色ids
     * @return {string}
     */
/**
     * 授权编辑
     *
     * @param id 角色id
     * @return {string}
     */
///core-web/JavaSource/com/fantasy/system/web/DataDictionaryAction.java
/**
     * 跳转配置项列表
     *
     * @return {String}
     */
/**
     * 配置项分页条件查询
     *
     * @param pager 分页对象
     * @param filters 过滤条件
     * @return {String}
     */
/**
     * 保存
     *
     * @功能描述 新增和修改都适用
     * @param dd 数据字典
     * @return {String}
     */
/**
     * 根据Id查询配置项信息
     *
     * @param key 数据字典key
     * @return {String}
     */
/**
     * 保存配置项分类
     *
     * @param ddt 数据字典分类
     * @return {String}
     */
/**
     * 删除配置项
     *
     * @param keys 数据字典 key
     * @return {String}
     */
///core-web/JavaSource/com/fantasy/system/web/SettingAction.java
/**
	 * 首页
	 * 
	 * @return
	 */
/**
	 * 搜索
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
     * 查询某个网站下面的配置信息
     * @param websiteId
     * @return
     */
/**
	 * 保存
	 * 
	 * @param article
	 * @return
	 */
/**
	 * 修改
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
///core-web/JavaSource/com/fantasy/system/web/WebAccessLogAction.java
/**
 * Created by hebo on 2014/8/7.
 */
///core-web/JavaSource/com/fantasy/system/web/WebsiteAction.java
/**
     * 首页
     *
     * @return
     */
/**
     * 搜索
     *
     * @return
     */
/**
     * 保存
     *
     * @param website
     * @return
     */
/**
     * 修改
     *
     * @param id
     * @return
     */
/**
     * 批量删除
     *
     * @param ids
     * @return
     */
///mall/JavaSource/com/fantasy/mall/cart/bean/Cart.java
/**
 * 购物车
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 上午09:48:09
 */
/**
         * 会员
         */
/**
         * cookie
         */
/**
     * 购物车对于的用户信息
     */
/**
     * 购物车所有者类型
     */
///mall/JavaSource/com/fantasy/mall/cart/bean/CartItem.java
/**
 * 购物车中的购物项
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 上午10:01:55
 */
/**
     * 数量
     */
/**
     * 价格
     */
/**
     * 商品
     */
/**
     * 所属购物车
     */
/**
     * 获取优惠价格,若无优惠则返回原价格
     *
     * @return {BigDecimal}
     */
/**
     * 获取小计价格
     *
     * @return {BigDecimal}
     */
///mall/JavaSource/com/fantasy/mall/cart/CartManager.java
/**
 * 
 * @功能描述 <br/>
 *       实现购物车配置功能:<br/>
 *       1、cookie 购物车配置<br/>
 *       2、添加购物车的验证配置<br/>
 *       3、购物车上限<br/>
 * @author 李茂峰
 * @since 2013-6-28 下午05:20:54
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/cart/service/handler/InitializeShopCartLoginSuccessHandler.java
/**
 * 会员登录时,初始化购物车信息
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-6-26 下午11:39:04
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/cart/service/ShopCart.java
/**
 * 购物车 业务实体
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-6-26 下午07:32:34
 */
/**
     * 获取当前用户的购物车
     *
     * @return {ShopCart}
     */
/**
     * 是否存在cookie购物车
     *
     * @return {boolean}
     */
/**
     * 获取当前用户的cookie购物车
     *
     * @return {ShopCart}
     */
/**
     * 向购物车添加商品
     *
     * @param sn       货品编号
     * @param quantity 数量
     * @return {CartItem}
     */
/**
     * 获取总数量
     */
/**
     * 获取总价格
     *
     * @return {double}
     */
/**
     * 清空购物车
     */
/**
     * 从购物车中删除商品
     *
     * @param sns 商品货号数组
     * @return {CartItem[]}
     */
/**
     * 更新购物车信息
     *
     * @param sn 商品货号
     * @param quantity 更新数量
     * @return {CartItem}
     */
/**
     * 获取购物车中的信息
     *
     * @param sn 商品货号
     * @return {CartItem} 返回购物项信息
     */
///mall/JavaSource/com/fantasy/mall/cart/service/ShopCartCookie.java
/**
 * 未登陆时，使用cookie购物车
 * 
 * @author 李茂峰
 * @since 2013-6-26 下午07:33:05
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/cart/service/ShopCartDataBase.java
/**
 * 数据库的购物车
 * 
 * @author 李茂峰
 * @since 2013-6-26 下午07:32:51
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/delivery/bean/DeliveryCorp.java
/**
 * 物流公司
 * 
 * @author 李茂峰
 * @since 2013-10-16 上午11:10:19
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/delivery/bean/DeliveryItem.java
/**
 * 物流项
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-22 上午10:53:37
 */
/**
     * 初始化物流项
     *
     * @param orderItem 订单项
     */
///mall/JavaSource/com/fantasy/mall/delivery/bean/DeliveryType.java
/**
 * 配送方式
 * 
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午3:45:17
 */
/**
		 * 先付款后发货
		 */
/**
		 * 货到付款
		 */
///mall/JavaSource/com/fantasy/mall/delivery/bean/Shipping.java
/**
 * 送货信息表
 * 
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-15 下午3:37:40
 */
///mall/JavaSource/com/fantasy/mall/delivery/dao/DeliveryItemDao.java
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-4
 * Time: 上午11:55
 * TO CHANGE THIS TEMPLATE USE FILE | SETTINGS | FILE TEMPLATES.
 */
///mall/JavaSource/com/fantasy/mall/delivery/dao/ReshipDao.java
/**
 * Created by hebo on 2014/8/13.
 */
///mall/JavaSource/com/fantasy/mall/delivery/dao/ShippingDao.java
/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-12-4
 * Time: 上午11:54
 * To change this template use File | Settings | File Templates.
 */
///mall/JavaSource/com/fantasy/mall/delivery/interceptor/ShippingInterceptor.java
/**
 * 发货拦截器
 */
/**
	 * 发货后，同步修改库存信息
	 * 
	 * @param point
	 */
/**
	 * 发货后，更新商品占用数量
	 * 
	 * @param point
	 */
/**
	 * 发货后，判断订单是否发货完成
	 * 
	 * @param point
	 * @功能描述
	 */
///mall/JavaSource/com/fantasy/mall/delivery/service/DeliveryService.java
/**
     * 列表查询
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<DeliveryCorp>
     */
/**
     * 保存
     */
/**
     * 根据主键获取id
     *
     * @param id 物流公司id
     * @return DeliveryCorp
     */
/**
     * 批量删除
     *
     * @param ids 物流公司 ids
     */
/**
     * 获取列表
     *
     * @return List<DeliveryCorp>
     */
/**
     * 配送列表
     *
     * @param pager   分页对象
     * @param filters 过滤条件
     * @return Pager<DeliveryType>
     */
/**
     * 配送保存
     *
     * @param deliveryType 配送对象
     * @return DeliveryType
     */
/**
     * 配送删除
     *
     * @param ids 配送类型
     */
/**
     * 发货信息
     *
     * @param shipping 必输项: order.id,deliveryType.id,deliverySn，deliveryFee，deliveryItems 可选输入项：memo
     */
/**
     * 退货信息
     *
     * @param reship 退货信息
     */
/**
     * 获取发货商品信息列表
     *
     * @param id id
     * @return Shipping
     */
/**
     * 获取配送方式
     *
     * @param deliveryMethod 配送方式
     * @return List<DeliveryType>
     */
/**
     * 静态获取列表
     *
     * @return List<DeliveryCorp>
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/Brand.java
/**
 * 品牌
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午5:46:16
 */
/**
     * 名称
     */
/**
     * 商品英文名称
     */
/**
     * 图片存储位置
     */
/**
     * 网址
     */
/**
     * 介绍
     */
/**
     * 所属国家
     */
/**
     * 品牌对应的商品
     */
/**
     * 商品分类
     */
/**
     * 获取品牌 Logo
     *
     * @return {FileDetail}
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/Goods.java
/**
 * 商品表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:19:17
 */
/**
     * 商品编号
     */
/**
     * 商品名称
     */
/**
     * 商品英文名称
     */
/**
     * 销售价
     */
/**
     * 成本价
     */
/**
     * 市场价(指导价格)
     */
/**
     * 商品重量(单位: 克)
     */
/**
     * 库存
     */
/**
     * 被占用库存数
     */
/**
     * 月销量(近30天的)
     */
/**
     * 销量(总)
     */
/**
     * 积分
     */
/**
     * 是否上架
     */
/**
     * 产品介绍(基本概况)
     */
/**
     * 页面关键词
     */
/**
     * 页面描述
     */
/**
     * 商品图片存储
     */
/**
     * 是否启用商品规格
     */
/**
     * 评论
     */
/**
     * 品牌
     */
/**
     * 收藏
     */
/**
     * 货品
     */
/**
     * 商品所属分类
     */
/**
     * 商品规格
     */
/**
     * 商品参数存储
     */
/**
     * 所属店铺信息
     */
/**
     * 获取商品ID
     *
     * @return {Long}
     */
/**
     * 修改商品ID
     *
     * @param id id
     */
/**
     * 获取商品编码
     *
     * @return {}
     */
/**
     * 修改商品编码
     */
/**
     * 获取商品名称
     */
/**
     * 修改商品名称
     */
/**
     * 获取销售价
     */
/**
     * 修改销售价
     */
/**
     * 获取商品英文名称
     */
/**
     * 修改商品英文名称
     */
/**
     * 获取市场价
     */
/**
     * 修改市场价
     */
/**
     * 获取商品重量
     */
/**
     * 修改商品重量
     */
/**
     * 获取库存
     */
/**
     * 修改库存
     */
/**
     * 获取被占用库存数
     */
/**
     * 修改被占用库存数
     */
/**
     * 获取积分
     */
/**
     * 修改积分
     */
/**
     * 获取评论集合
     */
/**
     * 修改评论集合
     */
/**
     * 获取品牌对象
     */
/**
     * 修改品牌对象
     */
/**
     * 获取收藏集合
     */
/**
     * 修改收藏集合
     */
/**
     * 获取货品集合
     */
/**
     * 修改货品集合
     */
/**
     * 获取商品分类
     */
/**
     * 获取商品参数
     */
/**
     * 获取商品图片集合
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/GoodsCategory.java
/**
 * 商品分类
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午5:46:48
 */
/**
     * 分类ID
     */
/**
     * 分类名称
     */
/**
     * 分类标识
     */
/**
     * 页面关键词
     */
/**
     * 页面描述
     */
/**
     * 排序
     */
/**
     * 树路径
     */
/**
     * 层级
     */
/**
     * 上级分类
     */
/**
     * 下级分类
     */
/**
     * 属性版本表
     */
/**
     * 商品
     */
/**
     * 商品品牌
     */
/**
     * 品牌在分类中的排序规则
     */
/**
     * 商品参数存储
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/GoodsImage.java
/**
 * 商品预览时的图片
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-10-21 下午4:57:33
 */
/**
     * 排序
     */
/**
     * 获得商品图片（原）路径
     *
     * @return
     */
/**
     * 获得商品图片（大）路径
     *
     * @return
     */
/**
     * 获得商品图片（小）路径
     *
     * @return
     */
/**
     * 获得商品图片（缩略）路径
     *
     * @return
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/GoodsNotify.java
/**
 * 到货通知
 * 
 * @author 李茂峰
 * @since 2013-9-21 下午5:47:17
 * @version 1.0
 */
///mall/JavaSource/com/fantasy/mall/goods/bean/GoodsParameter.java
/**
 * 商品参数
 * 
 * @author 李茂峰
 * @since 2014-4-5 上午8:15:50
 * @version 1.0
 */
/**
	 * 索引Id
	 */
/**
	 * 名称
	 */
/**
	 * 格式
	 */
/**
	 * 排序
	 */
/**
	 * 备注
	 */
///mall/JavaSource/com/fantasy/mall/goods/bean/GoodsParameterValue.java
/**
 * 商品参数
 */
///mall/JavaSource/com/fantasy/mall/goods/bean/Product.java
/**
 * 货品表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午5:04:43
 */
/**
     * 商品图片存储
     */
/**
     * 获取商品图片
     *
     * @return GoodsImage
     */
/**
     * 获取默认商品图片（大）
     *
     * @return String
     */
/**
     * 获取默认商品图片（小）
     *
     * @return String
     */
/**
     * 获取默认商品图片（缩略图）
     *
     * @return String
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/Specification.java
/**
 * 商品规格
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2014-4-4 下午6:32:04
 */
/**
     * 名称
     */
/**
     * 商品规格类型
     */
/**
     * 排序
     */
/**
     * 商品规格值存储
     */
/**
     * 备注
     */
/**
     * 使用该规格的商品
     */
///mall/JavaSource/com/fantasy/mall/goods/bean/SpecificationValue.java
/**
 * 商品规格
 */
///mall/JavaSource/com/fantasy/mall/goods/dao/GoodsDao.java
/*
	@Override
	protected Criterion[] buildPropertyFilterCriterions(List<PropertyFilter> filters) {
		PropertyFilter filter = ObjectUtil.remove(filters, "filterName", "EQS_category.sign");
		if (filter != null) {// 将编号转换为id
			filters.add(new PropertyFilter("EQL_category.id", getGoodsCategoryIdUniqueBySign(filter.getPropertyValue(String.class))));
		}
		filter = ObjectUtil.remove(filters, "filterName", "EQL_category.id");
		if (filter == null) {// 如果没有设置分类条件，默认为根
			String[] ids = getRootGoodsCategoryIds();
			if (ids.length > 1) {
				filter = new PropertyFilter("INL_category.id", ids);
			} else {
				filter = new PropertyFilter("EQL_category.id", ids[0]);
			}
		}
		Criterion likePathCriterion;
		// 将 id 转为 path like 查询
		if (filter.getMatchType() == PropertyFilter.MatchType.IN) {
			Disjunction disjunction = Restrictions.disjunction();
			for (Long id : filter.getPropertyValue(Long[].class)) {
				GoodsCategory category = (GoodsCategory) this.getSession().get(GoodsCategory.class, id);
				disjunction.add(Restrictions.like("category.path", category.getPath(), MatchMode.START));
			}
			likePathCriterion = disjunction;
		} else {
			GoodsCategory category = (GoodsCategory) this.getSession().get(GoodsCategory.class, (Serializable) filter.getPropertyValue());
			likePathCriterion = Restrictions.like("category.path", category.getPath(), MatchMode.START);
		}
		filters = filters == null ? new ArrayList<PropertyFilter>() : filters;
		Criterion[] criterions = super.buildPropertyFilterCriterions(filters);
		criterions = ObjectUtil.join(criterions, likePathCriterion);
		return criterions;
	}*/
///mall/JavaSource/com/fantasy/mall/goods/interceptor/ProductFreezeStoreInterceptor.java
/**
 * 修改货品的占用库存数时
 */
/**
     * 货品占用数量发生变化时，更新商品的占用数量
     * @功能描述
     * @param point
     */
///mall/JavaSource/com/fantasy/mall/goods/interceptor/ProductReplenishInterceptor.java
/**
 * 修改货品的库存数时
 */
/**
     * 货品调整库存时,更新商品的库存数量
     *
     * @功能描述
     * @param point
     */
///mall/JavaSource/com/fantasy/mall/goods/service/BrandService.java
/**
 * 品牌 service
 * 
 */
/**
	 * 品牌列表
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 批量删除
	 * 
	 * @param ids
	 */
/**
	 * 获取品牌
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 获取品牌
	 * 
	 * @param brand
	 * @return
	 */
/**
	 * 获取品牌列表
	 * 
	 * @return
	 */
/**
	 * 静态获取品牌列表
	 * 
	 * @return
	 */
/**
	 * 获取品牌
	 * 
	 * @param brandId
	 * @return
	 */
///mall/JavaSource/com/fantasy/mall/goods/service/GoodsService.java
/**
 * 商品 service
 */
/**
     * 根据商品编码获取商品
     *
     * @param sn 商品编码
     * @return 商品对象
     */
/**
     * 获取商品栏目分类列表
     *
     * @return List<GoodsCategory>
     */
/**
     * 获取商品列表
     *
     * @param pager   翻页对象
     * @param filters 过滤器
     * @return Pager<Goods>
     */
/**
     * 获取商品分类
     *
     * @param id 分类id
     * @return GoodsCategory
     */
/**
     * 获取商品分类
     *
     * @param sign 分类编码
     * @return GoodsCategory
     */
/**
     * 保存商品栏目
     *
     * @param category 分类对象
     * @return GoodsCategory
     */
/**
     * 批量删除商品栏目
     *
     * @param ids 分类ids
     */
/**
     * 批量删除商品
     *
     * @param ids 商品ids
     */
/**
     * 商品批量上架
     *
     * @param ids 商品ids
     */
/**
     * 商品批量下架
     *
     * @param ids 商品ids
     */
/**
     * 获得商品
     *
     * @param id 商品id
     * @return Goods
     */
/**
     * 保存商品
     *
     * @param goods 商品对象            备注：如果只需要添加一个product  specificationEnabled=true  如果需要添加多个product specificationEnabled=true
     * @return Goods
     */
/**
     * 查询商品
     *
     * @param filters 查询条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @param start   结果集返回的开始位置
     * @param size    结果集条数
     * @return List<Goods>
     */
/**
     * 计算商品的存货及销售数量
     *
     * @param id 商品id
     */
/**
     * 商品销量统计 统计总销量与月销量
     *
     * @param product  产品对象
     * @param quantity 数量
     * @param amount   金额
     */
/**
     * 调整被占库存
     *
     * @param id       商品id
     * @param quantity 调整数量
     */
/**
     * 调整库存
     *
     * @param id       商品id
     * @param quantity 调整数量
     */
/**
     * 按销售量查询数据
     *
     * @param path     分类
     * @param timeUnit 时间单位
     * @param time     时间 格式说明：<br/>
     *                 TimeUnit.day = 20130103 <br/>
     *                 TimeUnit.week = 201321 <br/>
     *                 TimeUnit.month = 201312 <br/>
     *                 TimeUnit.year = 2013
     * @param size     返回结果条数
     * @return List<Goods>
     */
/**
     * 按销售量查询数据
     *
     * @param timeUnit 时间单位
     * @param time     格式说明：<br/>
     *                 TimeUnit.day = 20130103 <br/>
     *                 TimeUnit.week = 201321 <br/>
     *                 TimeUnit.month = 201312 <br/>
     *                 TimeUnit.year = 2013
     * @param size     返回数据的条数
     * @return List<Goods>
     */
/**
     * 按时间段查询销量排行
     *
     * @param timeUnit  时间单位 格式说明：<br/>
     *                  TimeUnit.day = 20130103 <br/>
     *                  TimeUnit.week = 201321 <br/>
     *                  TimeUnit.month = 201312 <br/>
     *                  TimeUnit.year = 2013
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @param size      返回的数据条数
     * @return List<Goods>
     */
/**
     * @param templateUrl 模板路径
     * @param object      关联对象
     * @return 新的路径
     */
/**
     * 移动商品
     *
     * @param ids        商品ids
     * @param categoryId 移动到的分类id
     */
/**
     * 分类分页查询
     *
     * @param pager
     * @param filters
     * @return
     */
///mall/JavaSource/com/fantasy/mall/goods/service/ProductService.java
/**
 * 货品 service
 */
/**
	 * 批量上架
	 * 
	 */
/**
	 * 批量下架
	 * 
	 * @param ids
	 */
/**
	 * 调整存货数量
	 * 
	 * @功能描述
	 * @param id
	 *            货物id
	 * @param number
	 *            调整数量
	 */
/**
	 * 订单生成时,更新商品的存货数量
	 * 
	 * @功能描述
	 * @param id
	 * @param quantity
	 */
///mall/JavaSource/com/fantasy/mall/member/bean/Comment.java
/**
 * 商品评论表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:36:07
 */
///mall/JavaSource/com/fantasy/mall/member/bean/Receiver.java
/**
 * 收货地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-9-16 下午4:16:02
 */
/**
     * 收货人姓名
     */
/**
     * 地区存储
     */
/**
     * 收货地址
     */
/**
     * 邮政编码
     */
/**
     * 电话
     */
/**
     * 手机
     */
/**
     * 是否为默认地址
     */
/**
     * 地址对应的用户信息
     */
/**
     * 获取地区
     *
     * @return
     * @功能描述
     */
/**
     * 设置地区
     *
     * @param area
     * @功能描述
     */
///mall/JavaSource/com/fantasy/mall/member/bean/ShipAddress.java
/**
 * 用户地址信息
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:16:02
 */
/**
     * 姓名
     */
/**
     * 国家
     */
/**
     * 省份
     */
/**
     * 城市
     */
/**
     * 街道
     */
/**
     * 邮政编码
     */
/**
     * 电话
     */
/**
     * 手机
     */
/**
     * 地址对应的用户信息
     */
///mall/JavaSource/com/fantasy/mall/order/bean/Order.java
/**
 * 订单表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-21 下午4:21:42
 */
/**
         * 未处理
         */
/**
         * 已处理
         */
/**
         * 已完成
         */
/**
         * 已作废
         */
/**
     * 临时字段，用于订单提交时，保存用户收货地址的id
     */
///mall/JavaSource/com/fantasy/mall/order/bean/OrderItem.java
/**
 * 订单明细表
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-9-22 下午1:59:27
 * @version 1.0
 */
/**
	 * 初始化订单项
	 * 
	 * @param product
	 * @return
	 */
/**
	 * 商品价格
	 */
/**
	 * 商品数量
	 */
/**
	 * 重量小计
	 * 
	 * @return
	 */
/**
	 * 价格小计
	 * 
	 * @return
	 */
///mall/JavaSource/com/fantasy/mall/order/bean/OrderLog.java
/**
 * 订单日志
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-9-16 下午4:03:40
 */
///mall/JavaSource/com/fantasy/mall/order/converter/OrderTypeConverter.java
/**
 * 用于将 order 类作为动态属性时的转换方法。
 */
///mall/JavaSource/com/fantasy/mall/order/interceptor/OrderInvalidInterceptor.java
/**
 * 订单失效(关闭)
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 下午3:43:18
 */
/**
     * 订单关闭时，还购买商品的占用数量
     *
     * @param point {}
     */
///mall/JavaSource/com/fantasy/mall/order/interceptor/OrderSubmitInterceptor.java
/**
 * 订单提交后
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-2 下午1:45:51
 */
/**
     * 订单提交后刷新购物车
     *
     * @param point JoinPoint
     */
/**
     * 更新product占用数量
     * 订单提交完成后，更新
     *
     * @param point JoinPoint
     */
///mall/JavaSource/com/fantasy/mall/order/service/OrderService.java
/**
 * 订单管理
 */
/**
     * 前台中心未完成的订单
     *
     * @param filters 过滤条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @return {List<order>}
     */
/**
     * 查询订单
     *
     * @param filters 过滤条件
     * @param orderBy 排序字段
     * @param order   排序方向
     * @param size    条数
     * @return List<Order>
     */
/**
     * 订单保存
     *
     * @param order 订单对象
     * @return {Order}
     */
/**
     * 新订单
     *
     * @param order 订单对象
     */
/**
     * 根据id 批量删除订单
     *
     * @param ids 订单Ids
     */
/**
     * 根据 编号 获取对象
     *
     * @param sn 订单编号
     * @return {Order}
     */
/**
     * 根据 criterions 获取对象
     *
     * @param criterions 筛选条件
     * @return {Order}
     */
/**
     * 返回订单列表
     *
     * @param filters 过滤条件
     * @return {List<Order>}
     */
/**
     * 获取order
     *
     * @param id 订单id
     * @return {Order}
     */
/**
     * 订单已处理
     */
/**
     * 订单作废
     */
/**
     * 订单发货
     *
     * @param shipping 订单发货操作
     */
///mall/JavaSource/com/fantasy/mall/sales/bean/Sales.java
/**
 * 销量数据表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-9 上午11:31:29
 */
/**
     * 类型
     */
/**
     * 可能的值<br/>
     * 时间 格式说明：<br/>
     * TimeUnit.day = 20130103 <br/>
     * TimeUnit.week = 201321 <br/>
     * TimeUnit.month = 201312 <br/>
     * TimeUnit.year = 2013
     */
/**
     * 时间单位
     */
/**
     * 对应货品、商品、订单 的sn
     */
/**
     * 商品或者货品所属分类的path
     */
/**
     * 统计数量
     */
/**
     * 销售金额
     */
/**
     * 商品退货量
     */
/**
     * 退货金额
     */
/**
     * 统计开始时间
     */
/**
     * 统计结束时间
     */
///mall/JavaSource/com/fantasy/mall/sales/service/SalesService.java
/**
	 * 获取商品近30天的销量
	 * 
	 * @param sn
	 * @return
	 */
/**
	 * 获取销售量
	 * 
	 * @param start
	 *            统计开始时间
	 * @param end
	 *            统计结束时间
	 * @param timeUnit
	 *            类型
	 * @param sn
	 *            对应的编码
	 * @return
	 * @功能描述
	 */
/**
	 * 销量排行
	 * @功能描述 
	 * @param criterions
	 * @param satrt
	 * @param size
	 * @return
	 */
/**
	 * 查询商品统计
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
///mall/JavaSource/com/fantasy/mall/shop/bean/Shop.java
/**
 * 店铺表
 */
/**
     * 店铺名称
     */
///mall/JavaSource/com/fantasy/mall/stock/bean/Stock.java
/**
 *@Author lsz
 *@Date 2013-11-28 下午2:48:12
 *
 */
/**
	 * 添加量
	 */
/**
	 * 备注
	 */
/**
	 * 状态 true:加，false:减
	 */
/**
	 * 商品
	 */
///mall/JavaSource/com/fantasy/mall/stock/bean/WarningSettings.java
/**
 * 预警设置
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-26 上午11:39:53
 * @version 1.0
 */
/**
	 * 判断是否超出警戒线
	 */
///mall/JavaSource/com/fantasy/mall/stock/dao/StockDao.java
/**
 *@Author lsz
 *@Date 2013-11-28 下午4:49:22
 *
 */
///mall/JavaSource/com/fantasy/mall/stock/dao/WarningSetDao.java
/**
 *@Author lsz
 *@Date 2013-12-27 下午2:49:57
 *
 */
///mall/JavaSource/com/fantasy/mall/stock/interceptor/StockInterceptor.java
/**
 * 变更库存(类似保存入库单)
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-12-2 下午2:09:46
 * @version 1.0
 */
/**
	 * 更新product占用数量
	 * 
	 * @功能描述 订单提交完成后，更新
	 * @param point
	 */
///mall/JavaSource/com/fantasy/mall/stock/service/StockService.java
/**
 * @Author lsz
 * @Date 2013-11-28 下午4:51:46
 * 
 */
/**
	 * 保存
	 * 
	 * @param stock
	 * @return
	 */
/**
	 * 查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 出库
	 * 
	 * @功能描述
	 * @param shipping
	 */
/**
	 * 库存预警保存
	 * 
	 * @param id
	 * @param count
	 */
///mall-web/JavaSource/com/fantasy/mall/cart/web/CartItemAction.java
/**
     * 添加购物车
     * <p/>
     * 暂时只提供简单的添加功能，类似颜色、款式、服务等选择后续添加
     *
     * @param sn 商品编号
     * @param q  购买数量
     * @return {String}
     */
/**
     * 清空购物车
     *
     * @return {String}
     */
/**
     * 移除商品
     *
     * @param sns 商品货号数组
     * @return {String}
     */
/**
     * 编辑商品数量
     *
     * @param sn 商品货号
     * @param q  调整数量
     * @return {String}
     */
///mall-web/JavaSource/com/fantasy/mall/delivery/web/DeliveryAction.java
/**
 * 配送管理 <br/>
 * User: Administrator <br/>
 * Date: 13-12-4 <br/>
 */
/**
	 * 添加发货信息
	 * 
	 * @param shipping
	 * @return
	 */
/**
	 * 添加退货信息
	 * 
	 * @param shipping
	 * @return
	 */
/**
	 * 获取发货详情
	 * @param id
	 * @return
	 */
///mall-web/JavaSource/com/fantasy/mall/delivery/web/DeliveryCorpAction.java
/**
 * 物流公司管理
 * 
 */
/**
	 * 物流首页
	 */
/**
	 * 物流查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 物流保存
	 * 
	 * @param deliveryCorp
	 * @return
	 */
/**
	 * 物流编辑
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 物流批量删除
	 * 
	 * @param ids
	 * @return
	 */
///mall-web/JavaSource/com/fantasy/mall/delivery/web/DeliveryTypeAction.java
/**
 * 配送获取
 * 
 */
/**
	 * 配送首页
	 * 
	 * @return
	 */
/**
	 * 配送列表查询
	 * 
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 配送保存
	 * 
	 * @param deliveryType
	 * @return
	 */
/**
	 * 配送修改
	 * 
	 * @param id
	 * @return
	 */
/**
	 * 配送删除
	 * 
	 * @param ids
	 * @return
	 */
///mall-web/JavaSource/com/fantasy/mall/delivery/web/ReshipAction.java
/**
 * Created by hebo on 2014/8/13.
 */
/**
     * 退货记录首页
     * @param pager
     * @param filters
     * @return
     */
/**
     *
     * 退货记录查询
     * @param pager
     * @param filters
     * @return
     */
/**
     * 退货记录详情
     *
     * @param id
     * @return
     */
/**
     * 退货记录删除
     * @param ids
     * @return
     */
///mall-web/JavaSource/com/fantasy/mall/delivery/web/ShippingAction.java
/**
 * Created by hebo on 2014/8/13.
 */
/**
     * 发货记录首页
     * @param pager
     * @param filters
     * @return
     */
/**
     *
     * 发货记录查询
     * @param pager
     * @param filters
     * @return
     */
/**
     * 发货记录详情
     *
     * @param id
     * @return
     */
/**
     * 发货记录删除
     * @param ids
     * @return
     */
///mall-web/JavaSource/com/fantasy/mall/goods/web/OldBrandAction.java
/**
 * 品牌管理
 */
/**
     * 品牌功能进入页面
     *
     * @return {string}
     */
/**
     * 品牌查询
     *
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @return {string}
     */
/**
     * 品牌保存
     *
     * @param brand 品牌
     * @return {string}
     */
/**
     * 品牌编辑
     *
     * @param id 品牌id
     * @return {string}
     */
/**
     * 批量删除
     *
     * @param ids 品牌ids
     * @return {string}
     */
/**
     * 品牌查看
     *
     * @param id 品牌id
     * @return {string}
     */
/**
     * 查询分类树
     *
     * @param filters 过滤条件
     * @return {string}
     */
///mall-web/JavaSource/com/fantasy/mall/goods/web/OldGoodsAction.java
/**
     * 商品功能进入页面
     *
     * @return string
     */
/**
     * 商品查询
     *
     * @param pager   分页条件
     * @param filters 筛选条件
     * @return string
     */
/**
     * 分类保存
     *
     * @param goodsCategory 分类
     * @return string
     */
/**
     * 分类编辑
     *
     * @param id 分类id
     * @return string
     */
/**
     * 分类删除
     *
     * @param ids 分类ids
     * @return string
     */
/**
     * 保存商品
     *
     * @param goods 商品对象
     * @return string
     */
/**
     * 修改商品
     *
     * @param id 商品id
     * @return string
     */
/**
     * 商品上架
     *
     * @param ids 商品ids
     * @return string
     */
/**
     * 商品下架
     *
     * @param ids 商品ids
     * @return string
     */
/**
     * 商品删除
     *
     * @param ids 商品ids
     * @return string
     */
/**
     * 商品数据移动
     *
     * @param ids        商品ids
     * @param categoryId 分类id
     * @return string
     */
///mall-web/JavaSource/com/fantasy/mall/goods/web/ProductAction.java
/**
	 * 商品功能进入页面
	 * @功能描述 
	 * @return
	 */
/**
	 * 商品查询
	 * @功能描述
	 * @param filters
	 * @return
	 */
/**
	 * 添加 
	 * @param id
	 * @return
	 */
/**
	 * 保存
	 * @param product
	 * @return
	 */
/**
	 * 修改
	 * @param id
	 * @return
	 */
/**
	 * 批量删除
	 * @param ids
	 * @return
	 */
/**
	 * 批量上架
	 * @param ids
	 * @return
	 */
/**
	 * 批量下架
	 * @param ids
	 * @return
	 */
/**
	 * 补货
	 * @param id
	 * @return
	 */
/**
	 * 补货保存
	 * @param product
	 * @return
	 */
/**
	 * 显示
	 */
///mall-web/JavaSource/com/fantasy/mall/goods/web/validator/GoodsCategorySignUniqueValidator.java
/**
 * @Author lsz
 * @Date 2013-12-21 下午2:52:20
 * 
 */
///mall-web/JavaSource/com/fantasy/mall/order/web/OrderAction.java
/**
 * 订单
 */
/**
     * 列表页面
     *
     * @return
     */
/**
     * 列表查询
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 查看
     *
     * @param id
     * @return
     */
/**
     * 编辑
     *
     * @param id
     * @return
     */
/**
     * 订单地址修改
     *
     * @param id
     * @return
     */
/**
     * 保存订单收货地址
     *
     * @param order
     * @return
     */
/**
     * 在《新订单》 通知发货 改变订单状态为已处理
     *
     * @param ids
     * @return
     */
/**
     * 订单作废
     *
     * @param ids
     * @return
     */
/**
     * 前台订单列表
     *
     * @return
     */
/**
     * 订单确认
     *
     * @return
     */
/**
     * 订单提交
     *
     * @param order
     * @param order
     * @return
     */
/**
     * 订单支付页面
     *
     * @param sn
     * @return
     */
/**
     * 订单提交完成
     *
     * @return
     */
///mall-web/JavaSource/com/fantasy/mall/order/web/validator/OrderMemberValidator.java
/**
 * @Author lsz
 * @Date 2013-12-5 上午10:25:07
 * 
 */
///mall-web/JavaSource/com/fantasy/mall/sales/web/SalesAction.java
/**
 * @Author lsz
 * @Date 2013-12-26 上午11:51:51
 * 
 */
///mall-web/JavaSource/com/fantasy/mall/stock/web/StockAction.java
/**
 *@Author lsz
 *@Date 2013-11-28 下午5:06:38
 *
 */
/**
	 * 商品功能进入页面
	 * @功能描述 
	 * @return
	 */
/**
	 * 商品查询
	 * @功能描述
	 * @param filters
	 * @return
	 */
/**
	 * 首页
	 * @return
	 */
/**
	 * 搜索
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 添加,减少库存
	 * @param productId
	 * @return
	 */
/**
	 * 库存变量增加
	 * @param stock
	 * @return
	 */
/**
	 * 库存预警保存
	 * @param id
	 * @param count
	 * @return
	 */
///question/JavaSource/com/fantasy/question/bean/Answer.java
/**
         * 普通回答
         */
/**
         * 提问者推荐回答
         */
/**
         * 提问者采纳回答
         */
/**
     * 回答内容
     */
/**
     * 回答级别
     */
/**
     * 追问（答）
     */
/**
     * 回答对应的会员
     */
/**
     * 问题
     */
/**
     * 好评，赞数量
     */
/**
     * 差评数量
     */
///question/JavaSource/com/fantasy/question/bean/AnswerAdditional.java
/**
 * 追问（答）
 */
/**
     * 所属回答
     */
/**
     * 回答内容
     */
/**
     * 追答对应的会员
     */
///question/JavaSource/com/fantasy/question/bean/Category.java
/**
     * 分类ID
     */
/**
     * 分类名称
     */
/**
     * 分类标识
     */
/**
     * 页面关键词
     */
/**
     * 页面描述
     */
/**
     * 排序
     */
/**
     * 树路径
     */
/**
     * 层级
     */
/**
     * 上级分类
     */
/**
     * 下级分类
     */
/**
     * 问题
     */
///question/JavaSource/com/fantasy/question/bean/Question.java
/**
 * 问题
 */
/**
     * 标题
     */
/**
     * 内容
     */
/**
     * 问题状态
     */
/**
     * 问题分类
     *
     */
/**
     * 问题回答
     */
/**
     * 问题对应的会员
     */
/**
     * 回答问题的数量
     */
/**
     * 最后回答时间
     */
/**
     * 最后一条回答的答案
     */
/**
     * 悬赏金额
     */
/**
     * 是否为热门问题
     */
///question/JavaSource/com/fantasy/question/service/QuestionService.java
/**
     * 保存问题栏目
     *
     * @param category
     * @return
     */
/**
     * 删除分类
     * @param ids
     */
/**
     * 根据分类编码查询分类
     * @param sign
     * @return
     */
/**
     * 根据分类编码查询分类
     * @param id
     * @return
     */
/**
     * 获取分类集合
     * @return
     */
/**
     * 获取分类集合
     * @param sign
     * @return
     */
/**
     * 问题查询方法
     *
     * @param pager  翻页对象
     * @param filters 筛选条件
     * @return string
     */
/**
     * 保存问题对象
     *
     * @param question
     * @return question
     */
/**
     * 获取问题
     * @param id
     * @return
     */
/**
     * 删除问题
     * @param ids
     */
/**
     * 关闭问题
     * @param ids
     */
/**
     * 打开问题
     * @param ids
     */
/**
     * 移动问题
     * @param ids
     * @param categoryId
     */
/**
     * 根据传入编码跳转不同的界面
     * @param templateUrl
     * @param object
     * @return
     */
///question-web/JavaSource/com/fantasy/question/web/AnswerAction.java
/**
 * Created by hebo on 2014/10/16.
 */
///question-web/JavaSource/com/fantasy/question/web/AnswerAdditionalAction.java
/**
 * Created by hebo on 2014/10/17.
 */
///question-web/JavaSource/com/fantasy/question/web/QuestionAction.java
/**
 * Created by hebo on 2014/9/22.
 */
/**
     * 问题首页
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 问题列表
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 问题查询
     *
     * @param pager   分页条件
     * @param filters 筛选条件
     * @return string
     */
/**
     * 问题保存
     *
     * @param question
     * @return
     */
/**
     * 根据ID查询问题对象
     *
     * @param id
     * @return
     */
/**
     * 删除问题
     *
     * @param ids
     * @return
     */
/**
     * 问题分类保存
     *
     * @param category
     * @return
     */
/**
     * 根据分类ID查询分类对象
     *
     * @param id
     * @return
     */
/**
     * 删除分类
     *
     * @param ids
     * @return
     */
/**
     * 问题移动分类
     *
     * @param ids
     * @param categoryId
     * @return
     */
/**
     * 关闭问题
     *
     * @param ids
     * @return
     */
/**
     * 打开问题
     *
     * @param ids
     * @return
     */
///test/JavaSource/com/fantasy/test/bean/Article.java
/**
     * 文章标题
     */
/**
     * 摘要
     */
/**
     * 关键词
     */
/**
     * 文章正文
     */
/**
     * 作者
     */
/**
     * 发布日期
     */
/**
     * 文章对应的栏目
     */
/**
     * 发布标志
     */
///test/JavaSource/com/fantasy/test/bean/ArticleCategory.java
/**
     * 栏目名称
     */
/**
     * 层级
     */
/**
     * 描述
     */
/**
     * 排序字段
     */
/**
     * 上级栏目
     */
/**
     * 下级栏目
     */
/**
     * 属性版本表
     */
/**
     * 文章
     */
///test/JavaSource/com/fantasy/test/bean/Content.java
/**
 * 内容表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2012-11-4 下午05:47:36
 */
/**
     * 正文内容
     */
///test/JavaSource/com/fantasy/test/dao/MyBatisBeanDao.java
/**
     * 根据Key查询数据
     *
     * @param key key
     * @return testbean对象
     */
/**
     * 查询所有数据
     *
     * @return
     */
/**
     * 多参数查询方法测试
     *
     * @param key   p1
     * @param value p2
     * @return List<TyBatisbean>
     */
/**
     * 插入数据
     *
     * @param testbean
     * @return 影响行数
     */
/**
     * 更新数据
     *
     * @param testbean
     * @return 影响行数
     */
/**
     * 分页查询 用户基本信息表 的所有数据
     *
     * @param pager       翻页对象
     * @param myBatisBean 查询对象
     * @return Pager<TyBatisbean>
     */
///test/JavaSource/com/fantasy/test/service/CmsService.java
/**
     * 获取全部栏目
     *
     * @return List<ArticleCategory>
     */
/**
     * 文章查询方法
     *
     * @param pager    翻页对象
     * @param filters 筛选条件
     * @return string
     */
/**
     * 保存栏目
     *
     * @param category 分类对象
     * @return string
     */
/**
     * 得到栏目
     *
     * @param code categoryCode
     * @return ArticleCategory
     */
/**
     * 移除栏目
     *
     * @param codes 栏目 Code
     */
/**
     * 保存文章
     *
     * @param article 文章对象
     * @return Article
     */
/**
     * 获取文章
     *
     * @param id 文章id
     * @return Article
     */
/**
     * 发布文章
     *
     * @param ids 文章 ids
     */
/**
     * 关闭文章
     *
     * @param ids 文章 ids
     */
/**
     * 删除文章
     *
     * @param ids 文章 ids
     */
/**
     * 文章分类编码唯一
     *
     * @param code 分类 code
     * @return boolean
     */
/**
     * 移动文章
     *
     * @param ids          文章 ids
     * @param categoryCode 分类 categoryCode
     */
///website/JavaSource/com.fantasy.swp/backend/PageRebuildTask.java
/**
 * 重新生成页面
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-3-28 下午11:46:35
 * @version 1.0
 */
///website/JavaSource/com.fantasy.swp/bean/Data.java
/**
     * 数据作用范围
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-12-25 上午10:10:53
     */
/**
     * 数据源
     */
/**
     * 数据的范围<br/>
     * 暂时只有 global、page <br/>
     * 为global时。所有page都可以使用。为page时。只有引用的page能使用
     */
/**
     * 数据缓存时间(如果不希望数据被缓存，设置为0)
     */
/**
     * 关联的page对象
     */
/**
     * 数据解析器
     */
/**
     * 数据源
     */
///website/JavaSource/com.fantasy.swp/bean/DataAnalyzer.java
/**
     * 显示名称
     */
/**
     * class 的 name
     */
/**
     * 参数
     */
///website/JavaSource/com.fantasy.swp/bean/DataBean.java
/**
 * Created by wuzhiyong on 2015/3/3.
 */
///website/JavaSource/com.fantasy.swp/bean/DataInferface.java
/**
 * 数据接口定义
 */
/**
     * 数据类型
     */
/**
         * 普通类型
         */
/**
         * 分页类型，有一页数据则会生成一个html
         */
/**
         * 对象类型，有一条数据则会生成一个html
         */
/**
     * 数据接口对应的模板
     */
/**
     * 数据在模板文件中的key
     */
/**
     * 表述名称
     */
/**
     * 是否为集合
     */
/**
     * 数据类型
     */
///website/JavaSource/com.fantasy.swp/bean/enums/DataType.java
/**
 * 数据类型
 */
///website/JavaSource/com.fantasy.swp/bean/enums/PageType.java
/**
 * 页面类型
 *
 */
/**
     * 单页面
     */
/**
     * 多页面
     */
/**
     * 分页
     */
///website/JavaSource/com.fantasy.swp/bean/Page.java
/**
 * 页面静态化时，对某个页面的配置
 */
/**
     * 名称
     */
/**
     * 对应模板
     */
/**
     * 对应的数据
     */
/**
     * 站点
     */
/**
     * 文件存储路径
     */
/**
     * list类型的页面,pageSize默认15条
     */
/**
     * 对应的触发器规则
     */
///website/JavaSource/com.fantasy.swp/bean/PageAnalyzer.java
/**
     * 显示名称
     */
/**
     * class 的 name
     */
///website/JavaSource/com.fantasy.swp/bean/PageBean.java
/**
 *
 */
///website/JavaSource/com.fantasy.swp/bean/PageItem.java
/**
 * 页面实例
 */
/**
     * 页面路径
     */
/**
     * 多页面数据id或code
     * 分页页面当前页码
     */
///website/JavaSource/com.fantasy.swp/bean/PageItemBean.java
/**
 *
 */
///website/JavaSource/com.fantasy.swp/bean/PageItemData.java
/**
 * pageItem 的子项
 */
///website/JavaSource/com.fantasy.swp/bean/Template.java
/**
 * 模板
 * <p/>
 * 用于生成静态页面时的thml模板配置
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 上午9:21:05
 */
/**
     * 模板名称
     */
/**
     * 描述
     */
/**
     * 站点
     */
/**
     * 模板文件存放的位置
     */
/**
     * 数据接口定义
     */
/**
     * 模版路径
     */
/**
     * 页面类型
     */
/**
     * Key
     */
///website/JavaSource/com.fantasy.swp/bean/Trigger.java
/**
 * 触发器
 */
/**
         * 定时触发器
         * 参考 Spring Quartz
         */
/**
         * 数据修改
         * 参考项：
         * 1.数据类型《classname》
         * 2.变更类型：新增、修改及删除
         * 3.变更范围：某个字段或者几个字段《 可以带条件如 attr > ?  & attr = ? 》
         * 4.监听范围:page/pageItem
         */
/**
     * 触发类型
     */
/**
     * 触发规则存储
     */
/**
     * 优先条件
     */
/**
     * 触发器描述
     */
/**
     * 对应的page
     */
/*
    Schedule editor
    1.Daily
    2.Days per week
    3.Days per month
    4.Cron expression
    */
///website/JavaSource/com.fantasy.swp/bean/Url.java
/**
 * 网页生成的地址
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 上午10:09:04
 */
///website/JavaSource/com.fantasy.swp/dao/DataDao.java
/**
 *@Author lsz
 *@Date 2014-2-18 下午3:25:56
 *
 */
///website/JavaSource/com.fantasy.swp/dao/DataInferfaceDao.java
/**
 *@Author lsz
 *@Date 2014-2-18 上午10:24:35
 *
 */
///website/JavaSource/com.fantasy.swp/dao/PageDao.java
/**
 *@Author lsz
 *@Date 2014-1-2 上午11:26:49
 *
 */
///website/JavaSource/com.fantasy.swp/dao/TemplateDao.java
/**
 *@Author lsz
 *@Date 2014-1-2 下午4:48:25
 *
 */
///website/JavaSource/com.fantasy.swp/data/SimpleData.java
/**
 * 简单变量接口
 * 
 * @author 李茂峰
 * @since 2013-7-15 下午05:22:54
 * @version 1.0
 */
///website/JavaSource/com.fantasy.swp/DataAnalyzer.java
/**
     * 获取数据对象
     *
     * @param value     字符串形式的value
     * @param clazz     java类型
     * @param list      是否为集合
     * @param arguments 调用参数
     * @param <T>       泛型
     * @return object
     */
///website/JavaSource/com.fantasy.swp/exception/SwpException.java
/**
 * 异常
 */
///website/JavaSource/com.fantasy.swp/factory/DataFactory.java
/**
 * 数据工厂
 */
///website/JavaSource/com.fantasy.swp/factory/DefaultDataFactory.java
/**
 * 默认实现
 */
///website/JavaSource/com.fantasy.swp/factory/SwpWebsite.java
/**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @throws SwpException
     */
/**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @throws SwpException
     */
/**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param listKey 多页数据key
     * @throws SwpException
     */
/**
     *
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param pageKey 分页数据key
     * @throws SwpException
     */
/**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @return
     * @throws SwpException
     */
/**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return
     * @throws SwpException
     */
/**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return
     * @throws SwpException
     */
/**
     *
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @param size 分页条数
     * @return
     * @throws SwpException
     */
/**
     *
     * @return
     */
/**
     *
     * @param path 页面path
     * @return
     */
///website/JavaSource/com.fantasy.swp/factory/SwpWebsiteFactory.java
/**
 * 站点工厂
 */
///website/JavaSource/com.fantasy.swp/factory/TemplateFactory.java
/**
 * 模板工厂
 */
///website/JavaSource/com.fantasy.swp/HistoryService.java
/**
 * Created by lmf on 15/1/21.
 */
///website/JavaSource/com.fantasy.swp/IData.java
/**
 * Created by wuzhiyong on 2015/3/3.
 */
///website/JavaSource/com.fantasy.swp/IGenerate.java
/**
 * Created by wuzhiyong on 2015/3/2.
 */
/**
     * 创建新的page
     * @param page
     * @return
     * @throws SwpException
     */
/**
     * 通过某个具体的pageItem ID重新生成此页面
     * @param pageItemId
     * @return
     * @throws SwpException
     */
/**
     * 通过某个具体的pageItem ID和具体的数据重新生成此页面
     * @param pageItemId 页面定义item id
     * @param datas  页面数据集
     * @return Page页面
     * @throws SwpException
     * @throws IOException
     */
///website/JavaSource/com.fantasy.swp/IPage.java
/**
 * 页面接口
 */
/**
     * 创建或刷新页面详情
     * @return
     * @throws SwpException
     */
/**
     * 删除页面详情
     */
/**
     * 获得文件名为file的pageItem
     * @param path 文件路径
     * @return
     */
///website/JavaSource/com.fantasy.swp/IPageItem.java
/**
 *
 */
/**
     * 刷新
     * @throws SwpException
     */
/**
     * 刷新
     * @param datas 数据
     * @throws SwpException
     */
///website/JavaSource/com.fantasy.swp/ISwpWebsite.java
/**
 *
 */
/**
     * 添加单页面模板（无数据定义）
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @throws SwpException
     */
/**
     * 添加单页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @throws SwpException
     */
/**
     * 添加多页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param listKey 多页数据key
     * @throws SwpException
     */
/**
     * 添加分页面模板
     * @param path 模板路径（唯一，重复覆盖）
     * @param html 模板内容
     * @param dataInferface 数据定义
     * @param pageKey 分页数据key
     * @throws SwpException
     */
/**
     * 创建页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @return IPage
     * @throws SwpException
     */
/**
     * 创建单页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return IPage
     * @throws SwpException
     */
/**
     * 创建多页面
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @return IPage
     * @throws SwpException
     */
/**
     * 创建分页
     * @param url 页面地址(唯一，重复覆盖)
     * @param templatePath 模板地址
     * @param name 页面名称
     * @param datas 数据
     * @param size 分页条数
     * @return IPage
     * @throws SwpException
     */
/**
     * 查询站点中配置的page
     * @return
     */
/**
     * 查询站点中配置的模板
     * @return
     */
/**
     * 删除页面
     * @param url 页面地址
     */
/**
     * 删除模板
     * @param path 模板地址
     */
/**
     * 查询页面
     * @param url 页面地址
     */
/**
     * 查询模板
     * @param path 模板地址
     */
/**
     * 预览生成的页面
     * 第一页数据
     * @param path 页面path
     * @return
     */
///website/JavaSource/com.fantasy.swp/ITemplage.java
/**
 * 模板接口
 */
///website/JavaSource/com.fantasy.swp/log/EntityInfo.java
/**
 * 数据引用表
 */
/**
     * 数据类型
     */
/**
     * 目标Id
     */
/**
     * 引用到该条数据的实例
     */
///website/JavaSource/com.fantasy.swp/OutPutUrl.java
/**
 * 生成地址接口
 * 
 * @author 李茂峰
 * @since 2013-3-28 下午02:47:11
 * @version 1.0
 */
///website/JavaSource/com.fantasy.swp/PageInstance.java
/**
 * 页面实例
 */
///website/JavaSource/com.fantasy.swp/PageService.java
/**
 * 页面生成接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-28 下午01:31:54
 */
/**
     * 创建页面实例<br/>
     * 一个页面实例，可以理解为一个具体的html页面
     *
     * @param outPutUrl 输出页面的url地址
     * @param template  模板
     * @param datas     如果有外置数据的话
     * @return PageInstance
     */
///website/JavaSource/com.fantasy.swp/PageTemplate.java
/**
 * 模板页
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-9-13 上午11:08:31
 * @version 1.0
 */
/**
	 * 预览模板
	 * 
	 * @功能描述
	 * @param out
	 */
/**
	 * 生成页面
	 * 
	 * @功能描述
	 */
///website/JavaSource/com.fantasy.swp/runtime/ExecutionEntity.java
/**
 *
 */
/*
                dataMap.put(entry.getKey(), new TemplateMethodModel() {
                    @Override
                    public Object exec(List arguments) throws TemplateModelException {
//                    com.fantasy.swp.DataAnalyzer analyzer = (com.fantasy.swp.DataAnalyzer) ClassUtil.newInstance(_data.getDataAnalyzer().getClassName());

                        return "test";//analyzer.exec(_data.getValue(), ClassUtil.forName(_data.getDataInferface().getJavaType()),_data.getDataInferface().isList(),arguments);
                    }

                });*/
///website/JavaSource/com.fantasy.swp/runtime/GenerateImpl.java
/**
     * 预览
     * 第一页或第一条数据
     * @param page
     * @return
     * @throws IOException
     */
/**
//     *
//     * @param dm
//     * @param fileName
//     * @param page
//     * @param pageItemId
//     */
///website/JavaSource/com.fantasy.swp/runtime/GeneratePage.java
/**
 * 生成静态页面
 * Created by wml on 2015/1/26.
 */
///website/JavaSource/com.fantasy.swp/schedule/CreateHtmlJob.java
/**
 * Created by hebo on 2015/2/11.
 * 生成htmljob
 */
///website/JavaSource/com.fantasy.swp/service/DefaultPageService.java
/**
 * 默认的 page servie
 */
///website/JavaSource/com.fantasy.swp/service/GeneratePageService.java
/**
     *
     * @param dm
     * @param fileName
     * @param page
     * @param pageItemId
     */
/**
     * 获取数据
     * @param data
     * @return
     */
///website/JavaSource/com.fantasy.swp/service/HqlService.java
/**
 * 使用hql语句查询数据
 * Created by wml on 2015/1/30.
 */
/**
     * 查询
     * 返回结果集
     * @param hql
     * @return
     */
/**
     * 查询唯一结果
     * @param hql
     * @return
     */
///website/JavaSource/com.fantasy.swp/service/PageBeanService.java
/**
     * 页面列表
     * @return
     */
/**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @return
     */
/**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @param datas 构成页面数据
     * @return
     */
/**
     * 保存
     * @param website 站点
     * @param url 页面存储相对路径
     * @param templatePath 模板
     * @param name 页面名称
     * @param datas 构成页面数据
     * @param pageSize 分页时pageSize
     * @return
     */
/**
     * 获得某个页面
     * @param url 页面相对路径
     * @param websiteId 站点id
     * @return
     */
/**
     * 删除
     * @param url
     * @param websiteId
     */
/**
     * 创建页面
     * @param page
     * @return
     */
/**
     * 获取页面详情
     * @param path
     * @param pageId
     * @return
     */
/**
     * 删除静态文件
     * @param url 文件相对路径
     * @param website 站点
     */
/**
     * 获得数据
     * @param url 页面相对路径
     * @param websiteId 站点
     */
/**
     * 预览
     * @param url 页面相对路径
     * @param websiteId 站点
     */
///website/JavaSource/com.fantasy.swp/service/SpelService.java
/**
 * 执行方法
 * Created by wml on 2015/1/28.
 */
///website/JavaSource/com.fantasy.swp/service/TemplateBeanService.java
/**
     * 删除模板文件及所有page文件
     * @param path
     * @param website
     */
///website/JavaSource/com.fantasy.swp/SwpContext.java
/**
 * swp 上下文
 */
/**
     * 站点
     */
///website/JavaSource/com.fantasy.swp/template/FreemarkerTemplate.java
/**
 * 简单的模板生成
 */
///website/JavaSource/com.fantasy.swp/Template.java
/**
 * 页面模板
 */
/**
     * 获取模板内容
     *
     * @return T
     */
/**
     * 模板数据
     *
     * @return Map<String,TemplateData>
     */
///website/JavaSource/com.fantasy.swp/TemplateData.java
/**
 * 模板数据接口
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-28 下午02:46:58
 */
///website/JavaSource/com.fantasy.swp/Trigger.java
/**
 * 触发器<br/>
 * 用于定义页面生成的触发
 */
///website/JavaSource/com.fantasy.swp/url/ExpressionUrl.java
/**
 * 可以通过表达式从数据中生成url
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-28 下午02:40:33
 */
///website/JavaSource/com.fantasy.swp/util/GeneratePageUtil.java
/**
 * Created by wuzhiyong on 2015/2/5.
 */
///website/JavaSource/com.fantasy.swp/web/DataAction.java
/**
 *@Author lsz
 *@Date 2014-2-18 下午3:35:50
 *
 */
///website/JavaSource/com.fantasy.swp/web/DataInferfaceAction.java
/**
 *@Author lsz
 *@Date 2014-2-18 上午10:34:52
 *
 */
///website/JavaSource/com.fantasy.swp/web/PageAction.java
/**
 *@Author lsz
 *@Date 2014-1-2 上午11:38:41
 *
 */
///website/JavaSource/com.fantasy.swp/web/TemplateAction.java
/**
 *@Author lsz
 *@Date 2014-2-17 下午2:27:11
 *
 */
/**
	 * 首页
	 * @return
	 */
/**
	 * 搜索
	 * @param pager
	 * @param filters
	 * @return
	 */
/**
	 * 保存
	 * @param template
	 * @return
	 * @throws IOException
	 */
/**
	 * 修改
	 * @param id
	 * @return
	 */
///website/JavaSource/com.fantasy.swp/web/TriggerAction.java
/**
     * 首页
     * @return
     */
/**
     * 搜索
     * @param pager
     * @param filters
     * @return
     */
/**
     * 保存
     * @param trigger
     * @return
     */
/**
     * 修改
     * @param id
     * @return
     */
///website/JavaSource/com.fantasy.swp/WebPageManager.java
/* implements InitializingBean, ApplicationListener<ApplicationEvent> */
/**
	 * 线程池
	 */
/**
	 * 线程池大小
	 */
/**
	 * 定时任务
	 */
/**
	 * 执行周期
	 */
/**
	 * 初始化方法
	 * 
	 * @功能描述
	 */
/**
	 * 关闭方法
	 * 
	 * @功能描述
	 */
/**
	 * 通过名称刷新指定的模板
	 * 
	 * @功能描述
	 * @param name
	 */
///weixin/JavaSource/com/fantasy/wx/bean/Account.java
/**
 * 微信公众号设置
 * Created by zzzhong on 2014/6/18.
 */
/**
     * 原始ID
     */
/**
     * 公众号类型
     */
/**
     * 公众号名称
     */
///weixin/JavaSource/com/fantasy/wx/bean/Group.java
/**
 * 微信用用户组
 * Created by zzzhong on 2014/6/19.
 */
///weixin/JavaSource/com/fantasy/wx/bean/GroupMessage.java
/**
 * Created by zzzhong on 2014/11/20.
 */
/**
     * 消息类型
     */
/**
     * 文本消息内容
     */
/**
     * 媒体消息id
     */
/**
     * 分组的id通过group api 获取
     */
/**
     * openid列表消息集合
     */
///weixin/JavaSource/com/fantasy/wx/bean/GroupNews.java
/**
 * Created by zzzhong on 2014/12/16.
 */
///weixin/JavaSource/com/fantasy/wx/bean/GroupNewsArticle.java
/**
 * Created by zzzhong on 2014/12/16.
 */
/**
     * (必填) 缩略图文件 生成图文消息缩略图 生成thumbMediaId字段
     */
/**
     * 这个字段由业务方法通过thumbFile 生成图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
     */
/**
     * 图文消息的作者
     */
/**
     * (必填) 图文消息的标题
     */
/**
     * 在图文消息页面点击“阅读原文”后的页面链接
     */
/**
     * (必填) 图文消息页面的内容，支持HTML标签
     */
/**
     * 图文消息的描述
     */
/**
     * 是否显示封面，true为显示，false为不显示
     */
///weixin/JavaSource/com/fantasy/wx/bean/Media.java
/**
 * 微信媒体文件
 * Created by zzzhong on 2014/6/18.
 */
///weixin/JavaSource/com/fantasy/wx/bean/MenuWeixin.java
/**
 * 微信通用接口凭证
 * Created by zzzhong on 2014/6/18.
 */
/**
     * 上级分类
     */
/**
     * 下级分类
     */
///weixin/JavaSource/com/fantasy/wx/bean/Message.java
/**
 * 文本消息
 * Created by zzzhong on 2014/6/17.
 */
/**
     * 群发的结果
     */
/**
     * group_id下粉丝数；或者openid_list中的粉丝数
     */
/**
     * 过滤（过滤是指特定地区、性别的过滤、用户设置拒收的过滤，用户接收已超4条的过滤）后，准备发送的粉丝数，原则上，filterCount = sentCount + errorCount
     */
/**
     * 发送成功的粉丝数
     */
/**
     * 发送失败的粉丝数
     */
///weixin/JavaSource/com/fantasy/wx/bean/QRCode.java
/**
 * Created by zzzhong on 2014/11/21.
 */
/**
     * 二维码的有效时间，以秒为单位。最大不超过1800。
     */
/**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
///weixin/JavaSource/com/fantasy/wx/bean/UserInfo.java
/**
 * 微信用户基本信息
 * Created by zzzhong on 2014/6/19.
 */
///weixin/JavaSource/com/fantasy/wx/dao/AccountDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/GroupDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/GroupMessageDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/GroupNewsArticleDao.java
/**
 * Created by zzzhong on 2014/12/16.
 */
///weixin/JavaSource/com/fantasy/wx/dao/GroupNewsDao.java
/**
 * Created by zzzhong on 2014/12/16.
 */
///weixin/JavaSource/com/fantasy/wx/dao/MediaDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/MenuDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/MessageDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/QRCodeDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/dao/UserInfoDao.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/framework/account/AccountDetailsService.java
/**
 * 微信公众号接口
 */
/**
     * 通过 appid 获取微信号配置信息
     *
     * @param appid appid
     * @return AccountDetails
     * @throws AppidNotFoundException
     */
/**
     * 获取全部的微信公众号信息
     *
     * @return List<AccountDetails>
     */
///weixin/JavaSource/com/fantasy/wx/framework/account/SimpleAccountDetailsService.java
/**
 * 简单的微信公众号详情服务类
 */
///weixin/JavaSource/com/fantasy/wx/framework/core/Jsapi.java
/**
 * 微信 Jsapi
 */
///weixin/JavaSource/com/fantasy/wx/framework/core/MpCoreHelper.java
/**
 * 微信服务号与订阅号
 */
///weixin/JavaSource/com/fantasy/wx/framework/core/WeiXinCoreHelper.java
/**
 * 微信签名相关接口
 */
/**
     * 注册公众号服务，如果账号信息有更改需要重新调用该方法
     *
     * @param accountDetails 账号信息
     */
/**
     * 解析接收到的消息
     *
     * @param session 微信号session对象
     * @param request HTTP请求
     * @return WeiXinMessage
     * @throws WeiXinException
     */
/**
     * 构建回复的消息
     *
     * @param session     微信号session对象
     * @param encryptType encryptType
     * @param message     消息
     * @return String
     * @throws WeiXinException
     */
/**
     * 发送图片消息
     *
     * @param session 微信号session对象
     * @param content 图片消息
     * @param toUsers 接收人
     * @throws WeiXinException
     */
/**
     * 发送图片消息
     *
     * @param session 微信号session对象
     * @param content 图片消息
     * @param toGroup 接收组
     * @throws WeiXinException
     */
/**
     * 发送语音消息
     *
     * @param session 微信号session对象
     * @param content 语音消息
     * @param toUsers 接收人
     * @throws WeiXinException
     */
/**
     * 发送语音消息
     *
     * @param session 微信号session对象
     * @param content 语音消息
     * @param toGroup 接收人
     * @throws WeiXinException
     */
/**
     * 发送视频消息
     *
     * @param session 微信号session对象
     * @param content 视频消息
     * @param toUsers 接收人
     * @throws WeiXinException
     */
/**
     * 发送视频消息
     *
     * @param session 微信号session对象
     * @param content 视频消息
     * @param toGroup 接收人
     * @throws WeiXinException
     */
/**
     * 发送音乐消息
     *
     * @param session 微信号session对象
     * @param content 音乐消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
/**
     * 发送音乐消息
     *
     * @param session 微信号session对象
     * @param content 图文消息
     * @param toUser  接收人
     * @throws WeiXinException
     */
/**
     * 发送图文消息
     *
     * @param session  微信号session对象
     * @param articles 图文消息
     * @param toUsers  接收人
     * @throws WeiXinException
     */
/**
     * 发送图文消息
     *
     * @param session  微信号session对象
     * @param articles 图文消息
     * @param toGroup  接收人
     * @throws WeiXinException
     */
/**
     * 发送文本消息
     *
     * @param session 微信号session对象
     * @param content 文本消息
     * @param toUsers 接收人
     * @throws WeiXinException
     */
/**
     * 发送文本消息
     *
     * @param session 微信号session对象
     * @param content 文本消息
     * @param toGroup 接收组
     * @throws WeiXinException
     */
/**
     * 获取分组信息
     *
     * @param session 微信号session对象
     * @return List<Group>
     */
/**
     * 创建分组信息
     *
     * @param session   微信号session对象
     * @param groupName 分组名称
     */
/**
     * 更新分组信息
     *
     * @param session   微信号session对象
     * @param groupId   分组Id
     * @param groupName 分组名称
     */
/**
     * @param session 微信号session对象
     * @param userId  用户id
     * @param groupId 分组id
     */
/**
     * 获取全部用户关注用户 <br/>
     * 该方法仅在微信粉丝数量有限的情况下，推荐使用
     *
     * @param session 微信号session对象
     * @return List<User>
     */
/**
     * 公众号可通过本接口来获取帐号的关注者列表
     *
     * @param session 微信号session对象
     * @return UserList
     */
/**
     * 公众号可通过本接口来获取帐号的关注者列表
     *
     * @param session    微信号session对象
     * @param nextOpenId 第一个拉取的OPENID，不填默认从头开始拉取
     * @return UserList
     */
/**
     * 获取全部用户关注用户
     *
     * @param session 微信号session对象
     * @param userId  用户id
     * @return List<User>
     */
/**
     * 媒体上传接口
     *
     * @param session   微信号session对象
     * @param mediaType 媒体类型
     * @param fileItem  要上传的文件
     * @return 媒体Id
     */
/**
     * 媒体下载接口
     *
     * @param session 微信号session对象
     * @param mediaId 媒体id
     * @return FileItem
     * @throws WeiXinException
     */
/**
     * 获取安全链接
     *
     * @param session     微信号session对象
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * @param state       重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
     * @return url
     * @throws WeiXinException
     */
/**
     * 通过 accessToken 换取用户信息
     *
     * @param session     微信号session对象
     * @param accessToken 安全连接的用户授权token
     * @return User
     * @throws WeiXinException
     */
/**
     * 通过 openId 获取用户的分组
     *
     * @param session 微信号session对象
     * @param userId  用户id
     * @return group
     * @throws WeiXinException
     */
/**
     * 通过code换取网页授权access_token
     *
     * @param session 微信号session对象
     * @param code    安全连接返回的code
     * @return AccessToken
     * @throws WeiXinException
     */
/**
     * 刷新菜单配置
     *
     * @param session 微信号session对象
     * @param menus   菜单数组
     * @throws WeiXinException
     */
/**
     * 获取配置的菜单
     *
     * @param session 微信号session对象
     * @return List<Menu>
     * @throws WeiXinException
     */
/**
     * 清除Menu配置
     *
     * @param session 微信号session对象
     * @throws WeiXinException
     */
/**
     * 获取jsapi
     *
     * @param session 微信号session对象
     * @return Jsapi
     * @throws WeiXinException
     */
///weixin/JavaSource/com/fantasy/wx/framework/event/ClickEventListener.java
/**
 * 点击菜单拉取消息时的事件推送监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/LocationEventListener.java
/**
 * 微信事件消息监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/ScanEventListener.java
/**
 * 用户已关注时的事件推送监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/SubscribeEventListener.java
/**
 * 订阅事件消息监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/UnsubscribeEventListener.java
/**
 * 取消订阅事件消息监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/ViewEventListener.java
/**
 * 点击菜单跳转链接时的事件推送监听接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/event/WeiXinEventListener.java
/**
 * 微信监听器标示接口
 */
///weixin/JavaSource/com/fantasy/wx/framework/exception/AppidNotFoundException.java
/**
 * 微信账号不存在
 */
///weixin/JavaSource/com/fantasy/wx/framework/exception/NoSessionException.java
/**
 * session 不存在
 */
///weixin/JavaSource/com/fantasy/wx/framework/exception/WeiXinException.java
/**
 * 微信异常
 * Created by zzzhong on 2014/12/4.
 */
///weixin/JavaSource/com/fantasy/wx/framework/exception/WxErrorInfo.java
/**
 * 微信错误码说明
 * http://mp.weixin.qq.com/wiki/index.php?title=全局返回码说明
 *
 * @author zzz
 */
///weixin/JavaSource/com/fantasy/wx/framework/factory/WeiXinSessionFactory.java
/**
     * 第三方工具类
     *
     * @return Signature
     */
/**
     * 获取当前的 WeiXinSession
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
/**
     * 返回一个 WeiXinSession 对象，如果当前不存在，则创建一个新的session对象
     *
     * @return WeiXinSession
     * @throws WeiXinException
     */
/**
     * 获取微信账号存储服务
     *
     * @return AccountDetailsService
     */
/**
     * 处理接收到的请求
     *
     * @param message http response
     * @return WeiXinMessage
     */
///weixin/JavaSource/com/fantasy/wx/framework/factory/WeiXinSessionFactoryBean.java
/**
     * 微信session工厂
     */
///weixin/JavaSource/com/fantasy/wx/framework/factory/WeiXinSessionUtils.java
/**
     * 当前 session 对象
     */
///weixin/JavaSource/com/fantasy/wx/framework/handler/AbstracWeiXinHandler.java
/**
 * 微信处理器
 */
///weixin/JavaSource/com/fantasy/wx/framework/handler/AutoReplyTextHandler.java
/**
 * 自动回复处理器
 */
///weixin/JavaSource/com/fantasy/wx/framework/handler/EventWeiXinHandler.java
/**
 * 事件消息处理器
 */
///weixin/JavaSource/com/fantasy/wx/framework/handler/TextWeiXinHandler.java
/**
 * 文本消息 hander
 */
///weixin/JavaSource/com/fantasy/wx/framework/handler/WeiXinHandler.java
/**
     * 消息处理类
     *
     * @param session 微信回话
     * @param message 微信消息
     * @throws WeiXinException
     */
///weixin/JavaSource/com/fantasy/wx/framework/intercept/DefaultInvocation.java
/**
 * 调用者
 */
///weixin/JavaSource/com/fantasy/wx/framework/intercept/LogInterceptor.java
/**
 * 记录接收及发生的消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/intercept/WeiXinMessageInterceptor.java
/**
 * 微信消息拦截器
 */
/**
     * 消息拦截器
     *
     * @param session    微信公众号
     * @param message    消息
     * @param invocation 调用
     * @return WeiXinMessage
     */
///weixin/JavaSource/com/fantasy/wx/framework/intercept/WeiXinSendMessageInterceptor.java
/**
 * 微信主动发送消息拦截器
 */
/**
     * 消息拦截器
     *
     * @param session    微信公众号
     * @param message    消息内容
     * @param to         接收人
     * @param invocation 回调处理
     * @throws WeiXinException
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/AbstractWeiXinMessage.java
/**
     * 消息id
     */
/**
     * 发送方帐号（一个OpenID）
     */
/**
     * 消息创建时间
     */
/**
     * 开发者微信号（原始ID）
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Article.java
/**
 * 微信图文消息
 */
/**
     * (必填) 缩略图文件 生成图文消息缩略图 生成thumbMediaId字段
     */
/**
     * 图文消息的作者
     */
/**
     * (必填) 图文消息的标题
     */
/**
     * 在图文消息页面点击“阅读原文”后的页面链接
     */
/**
     * (必填) 图文消息页面的内容，支持HTML标签
     */
/**
     * 图文消息的描述
     */
/**
     * 是否显示封面，true为显示，false为不显示
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Event.java
/**
     * 事件类型
     */
/**
     * 事件KEY值，与自定义菜单接口中KEY值对应
     */
/**
     * 二维码的ticket，可用来换取二维码图片
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/EventLocation.java
/**
 * 上报地理位置事件
 */
/**
     * 地理位置维度
     */
/**
     * 地理位置经度
     */
/**
     * 地理位置精度
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Image.java
/**
 * 图片消息对象
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Link.java
/**
 * 链接
 */
/**
     * 消息标题
     */
/**
     * 消息描述
     */
/**
     * 消息链接
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Location.java
/**
 * 地理位置
 */
/**
     * 地理位置维度
     */
/**
     * 地理位置经度
     */
/**
     * 地图缩放大小
     */
/**
     * 地理位置信息
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Media.java
/**
 * 微信媒体消息
 */
/**
     * 媒体文件类型
     */
/**
         * 图片
         */
/**
         * 语音
         */
/**
         * 视频
         */
/**
         * 缩略图
         */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Menu.java
/**
 * 微信菜单
 */
/**
         * 点击推事件 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
         */
/**
         * 跳转URL 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
         */
/**
         * 扫码推事件 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
         */
/**
         * 扫码推事件且弹出“消息接收中”提示框 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
         */
/**
         * 弹出系统拍照发图
         * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
         */
/**
         * 弹出拍照或者相册发图  用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
         */
/**
         * 弹出微信相册发图器 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
         */
/**
         * 弹出地理位置选择器 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
         */
/**
         * 未知类型
         */
/**
     * 点击推事件 用户点击click类型按钮后，微信服务器会通过消息接口推送消息类型为event	的结构给开发者（参考消息接口指南），并且带上按钮中开发者填写的key值，开发者可以通过自定义的key值与用户进行交互；
     *
     * @param name 名称
     * @param key  key
     * @return Menu
     */
/**
     * 跳转URL 用户点击view类型按钮后，微信客户端将会打开开发者在按钮中填写的网页URL，可与网页授权获取用户基本信息接口结合，获得用户基本信息。
     *
     * @param name 名称
     * @param url  URL
     * @return Menu
     */
/**
     * 扫码推事件 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后显示扫描结果（如果是URL，将进入URL），且会将扫码的结果传给开发者，开发者可以下发消息。
     *
     * @param name 名称
     * @return Menu
     */
/**
     * 扫码推事件且弹出“消息接收中”提示框 用户点击按钮后，微信客户端将调起扫一扫工具，完成扫码操作后，将扫码的结果传给开发者，同时收起扫一扫工具，然后弹出“消息接收中”提示框，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
/**
     * 弹出系统拍照发图
     * 用户点击按钮后，微信客户端将调起系统相机，完成拍照操作后，会将拍摄的相片发送给开发者，并推送事件给开发者，同时收起系统相机，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
/**
     * 弹出拍照或者相册发图  用户点击按钮后，微信客户端将弹出选择器供用户选择“拍照”或者“从手机相册选择”。用户选择后即走其他两种流程。
     *
     * @param name 名称
     * @return Menu
     */
/**
     * 弹出微信相册发图器 用户点击按钮后，微信客户端将调起微信相册，完成选择操作后，将选择的相片发送给开发者的服务器，并推送事件给开发者，同时收起相册，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
/**
     * 弹出地理位置选择器 用户点击按钮后，微信客户端将调起地理位置选择工具，完成选择操作后，将选择的地理位置发送给开发者的服务器，同时收起位置选择工具，随后可能会收到开发者下发的消息。
     *
     * @param name 名称
     * @return Menu
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Music.java
/**
 * 音乐消息
 */
/**
     * 音乐标题
     */
/**
     * 音乐描述
     */
/**
     * 音乐链接
     */
/**
     * 高品质音乐链接，wifi环境优先使用该链接播放音乐
     */
/**
     * 缩略图
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/News.java
/**
 * 图文消息
 */
/**
     * 链接消息
     */
/**
     * 图片地址
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Video.java
/**
 * 视频消息
 */
/**
     * 视频标题
     */
/**
     * 视频描述
     */
/**
     * 缩略图
     */
/**
     * 视频
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/content/Voice.java
/**
 * 语音消息
 */
/**
     * 语音识别结果，UTF8编码
     */
/**
     * 音频媒体信息
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/EmptyMessage.java
/**
 * 空消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/EventMessage.java
/**
 * 事件消息接口
 */
/**
         * 订阅 / 扫描带参数二维码事件
         */
/**
         * 取消订阅
         */
/**
         * 用户已关注时的事件推送
         */
/**
         * 点击菜单拉取消息时的事件推送
         */
/**
         * 点击菜单跳转链接时的事件推送
         */
///weixin/JavaSource/com/fantasy/wx/framework/message/ImageMessage.java
/**
 * 图片消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/LinkMessage.java
/**
 * 链接消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/LocationMessage.java
/**
 * 地理位置消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/MessageFactory.java
/**
 * 微信消息工厂
 */
/**
     * 文本消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param content      文本消息内容
     */
/**
     * 图片消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      图片消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param url          图片链接
     * @return ImageMessage
     */
/**
     * 语音消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param format       语音格式，如amr，speex等
     * @return VoiceMessage
     */
/**
     * 视频消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param mediaId      视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
     * @param thumbMediaId 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。
     * @return VideoMessage
     */
/**
     * 地理位置消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param x            地理位置维度
     * @param y            地理位置经度
     * @param scale        地图缩放大小
     * @param label        地理位置信息
     * @return LocationMessage
     */
/**
     * 链接消息
     *
     * @param msgId        消息id
     * @param fromUserName 发送方帐号（一个OpenID）
     * @param createTime   消息创建时间
     * @param title        消息标题
     * @param description  消息描述
     * @param url          消息链接
     * @return LinkMessage
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/MusicMessage.java
/**
 * 音乐消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/NewsMessage.java
/**
 * 图文消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/TextMessage.java
/**
 * 微信文本消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/user/Group.java
/**
 * 用户分组
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/user/OpenIdList.java
/**
 * 用户列表对象
 */
/**
     * 关注该公众账号的总用户数
     */
/**
     * 拉取的OPENID个数，最大值为10000
     */
/**
     * 列表数据，OPENID的列表
     */
/**
     * 拉取列表的后一个用户的OPENID
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/user/User.java
/**
 * 微信用户对象
 */
/**
     * 用户是否订阅该公众号标识，值为0时，代表此用户没有关注该公众号，拉取不到其余信息。
     */
/**
     * 用户的标识，对当前公众号唯一
     */
/**
     * 用户的昵称
     */
/**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
/**
     * 用户所在国家
     */
/**
     * 用户所在省份
     */
/**
     * 用户所在城市
     */
/**
     * 用户的语言，简体中文为zh_CN
     */
/**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），用户没有头像时该项为空
     */
/**
     * 用户关注时间，为时间戳。如果用户曾多次关注，则取最后关注时间
     */
/**
     * 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段。详见：获取用户个人信息（UnionID机制）
     */
///weixin/JavaSource/com/fantasy/wx/framework/message/VideoMessage.java
/**
 * 微信视频消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/VoiceMessage.java
/**
 * 微信语言消息
 */
///weixin/JavaSource/com/fantasy/wx/framework/message/WeiXinMessage.java
/**
 * 微信消息接口
 */
/**
     * MsgId	消息id，64位整型
     *
     * @return id
     */
/**
     * 发送方帐号（一个OpenID）
     *
     * @return String
     */
/**
     * 消息创建时间 （整型）
     *
     * @return date
     */
/**
     * 获取微信内容
     *
     * @return T
     */
/**
     * 开发者微信号 (微信原始ID)
     *
     * @return String
     */
///weixin/JavaSource/com/fantasy/wx/framework/oauth2/AccessToken.java
/**
 * 访问标识
 */
/**
     * 网页授权接口调用凭证,注意：此access_token与基础支持的access_token不同
     */
/**
     * access_token接口调用凭证超时时间，单位（秒）
     */
/**
     * 用户刷新access_token
     */
/**
     * 用户唯一标识，请注意，在未关注公众号时，用户访问公众号的网页，也会产生一个用户和公众号唯一的OpenID
     */
/**
     * 用户授权的作用域，使用逗号（,）分隔
     */
///weixin/JavaSource/com/fantasy/wx/framework/oauth2/Scope.java
/**
 * 应用授权作用域 <br/>
 * snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），
 * snsapi_userinfo（弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
 */
/**
     * 不弹出授权页面
     */
/**
     * 弹出授权页面
     */
///weixin/JavaSource/com/fantasy/wx/framework/session/AbstractWeiXinSession.java
/**
 * 微信 session 抽象实现
 */
///weixin/JavaSource/com/fantasy/wx/framework/session/AccountDetails.java
/**
 * 微信号，账号详细质料
 */
/**
         * 服务号
         */
/**
         * 订阅号
         */
/**
         * 企业号
         */
/**
     * 微信申请的appid
     *
     * @return appid
     */
/**
     * 公众号类型
     *
     * @return type
     */
/**
     * 名称
     *
     * @return String
     */
/**
     * 密钥
     *
     * @return String
     */
/**
     * 令牌 name
     *
     * @return String
     */
/**
     * 安全验证码
     *
     * @return String
     */
/**
     * 原始ID 用户消息回复时的 formusername
     *
     * @return String
     */
///weixin/JavaSource/com/fantasy/wx/framework/session/DefaultWeiXinSession.java
/**
 * 微信Session默认实现
 */
///weixin/JavaSource/com/fantasy/wx/framework/session/WeiXinSession.java
/**
 * 微信 session 接口
 * 主要包含微信消息相关的公众号及订阅号内容
 */
/**
     * 微信号的 appid
     *
     * @return String
     */
/**
     * 发送图片消息
     *
     * @param content 图片消息
     * @param toUsers 接收人
     */
/**
     * 发送图片消息
     *
     * @param content 图片消息
     * @param toGroup 接收组
     */
/**
     * 发送语音消息
     *
     * @param content 语音消息
     * @param toUsers 接收人
     */
/**
     * 发送语音消息
     *
     * @param content 语音消息
     * @param toGroup 接收人
     */
/**
     * 发送视频消息
     *
     * @param content 视频消息
     * @param toUsers 接收人
     */
/**
     * 发送视频消息
     *
     * @param content 视频消息
     * @param toGroup 接收组
     */
/**
     * 发送音乐消息
     *
     * @param content 音乐消息
     * @param toUser  接收人
     */
/**
     * 发送图文消息
     *
     * @param content 图文消息
     * @param toUser  接收人
     */
/**
     * 发送图文消息
     *
     * @param content 图文消息列表
     * @param toUsers 接收人
     */
/**
     * 发送图文消息
     *
     * @param content 图文消息列表
     * @param toGroup 接收人
     */
/**
     * 发送文本消息
     *
     * @param content 文本消息
     * @param toUsers 接收人
     */
/**
     * 发送文本消息
     *
     * @param content 文本消息
     * @param toGroup 接收组
     */
/**
     * 获取安全链接
     *
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * @return url
     */
/**
     * 获取安全链接
     *
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlencode对链接进行处理
     * @param scope       应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     * @param state       重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值
     * @return url
     */
/**
     * 获取安全连接的授权用户
     *
     * @param code 安全链接code
     * @return User
     */
/**
     * 获取安全连接的授权用户
     *
     * @param userId 关注粉丝的openId
     * @return User
     */
/**
     * 获取关注的粉丝
     *
     * @return List<User>
     */
/**
     * 获取关注的粉丝
     *
     * @return List<Group>
     */
/**
     * 创建分组
     *
     * @param name 用户分组名称
     * @return Group
     */
/**
     * 更新分组名称
     *
     * @param groupId 用户ID
     * @param name    用户分组名称
     * @return Group
     */
/**
     * 移动用户的分组
     *
     * @param userId  用户Id
     * @param groupId 分组Id
     */
/**
     * 获取当前公众号信息
     *
     * @return AccountDetails
     */
/**
     * 刷新菜单配置
     *
     * @param menus 菜单数组
     */
/**
     * 获取配置的菜单
     *
     * @return List<Menu>
     */
/**
     * 清除Menu配置
     */
/**
     * 获取 jsapi
     *
     * @return Jsapi
     */
///weixin/JavaSource/com/fantasy/wx/rest/JsapiController.java
/**
     * @api {get} /weixin/jsapi/ticket  获取 ticket
     * @apiVersion 3.3.2
     * @apiName ticket
     * @apiGroup 微信 JSAPI
     * @apiPermission admin
     * @apiDescription 获取微信的 jsticket
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/jsapi/ticket
     * @apiError NoAccessRight Only authenticated Admins can access the data.
     * @apiError UserNotFound   The <code>id</code> of the User was not found.
     * @apiErrorExample Response (example):
     * HTTP/1.1 401 Not Authenticated
     * {
     * "error": "NoAccessRight"
     * }
     */
/**
     * @api {get} /weixin/jsapi/signature   获取 url 签名
     * @apiVersion 3.3.2
     * @apiName signature
     * @apiGroup 微信 JSAPI
     * @apiPermission admin
     * @apiDescription 获取 url 微信的 JSAPI 签名
     * @apiParam {String} url 生成签名的url
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/jsapi/signature?url=http://www.jfantasy.org
     * @apiSuccess {String}     noncestr            随机字符串
     * @apiSuccess {String}     ticket              ticket
     * @apiSuccess {long}       timestamp           时间戳
     * @apiSuccess {String}     url                 URL
     * @apiSuccess {String}     signature           签名串
     * @apiError NoAccessRight  只有被授权通过,才能获取数据。
     * @apiError WeiXinError    微信端抛出异常
     * @apiErrorExample Response (示例):
     * HTTP/1.1 401 Not Authenticated
     * {
     * "error": "NoAccessRight"
     * }
     */
///weixin/JavaSource/com/fantasy/wx/rest/MessageController.java
/**
 * @apiDefine WeiXinMessage
 * @apiSuccess (Success 200)    {Long}      Id              MsgId	消息id，64位整型
 * @apiSuccess (Success 200)    {String}    fromUserName    发送方帐号（OpenID或者微信公众号的原始ID）
 * @apiSuccess (Success 200)    {Date}      createTime      消息创建时间
 * @apiSuccess (Success 200)    {Content}   content         消息内容(参考：WeiXinMessage 接口与其实现类)
 * @apiSuccess (Success 200)    {String}    toUserName      消息的接收方(OpenID或者微信公众号的原始ID)
 * @apiVersion 3.3.2
 */
/**
     * @api {get} /weixin/message/:appid/push   微信消息接口
     * @apiVersion 3.3.2
     * @apiName push
     * @apiGroup 微信消息
     * @apiDescription 该接口为微信公众平台中的接口配置地址。
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/message/wx0e7cef7ad73417eb/push
     * @apiUse WeiXinMessage
     */
///weixin/JavaSource/com/fantasy/wx/rest/UserController.java
/**
 * @apiDefine UserInfo
 * @apiSuccess {String}     id
 * @apiSuccess {String}     openId              标识
 * @apiSuccess {long}       nickname            昵称
 * @apiSuccess {String}     sex                 性别
 * @apiSuccess {String}     country             所在国家
 * @apiSuccess {String}     province            所在省份
 * @apiSuccess {String}     city                所在城市
 * @apiSuccess {String}     avatar              头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表640*640正方形头像），没有头像时该项为空
 * @apiSuccess {String}     subscribeTime       关注时间，为时间戳。如果曾多次关注，则取最后关注时间
 * @apiSuccess {String}     subscribe           是否订阅该公众号标识，值为0时，代表此没有关注该公众号，拉取不到其余信息。
 * @apiVersion 3.3.2
 */
/**
     * @api {get} /weixin/user/:openid  获取粉丝
     * @apiVersion 3.3.2
     * @apiName getUser
     * @apiGroup 微信粉丝
     * @apiPermission admin
     * @apiDescription 通过粉丝ID获取关注的用户信息
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/user/oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw
     * @apiError WeiXinError    微信端抛出异常,对应 response status <code>403</code>
     * @apiUse UserInfo
     * @apiUse GeneralError
     */
/**
     * @api {get} /weixin/user/authorized/:code  通过安全code获取粉丝
     * @apiVersion 3.3.2
     * @apiName getUserByAuthorizedCode
     * @apiGroup 微信粉丝
     * @apiPermission admin
     * @apiDescription 通过 authorized code 获取关注的用户信息
     * @apiExample Example usage:
     * curl -i http://localhost/weixin/user/oJ27YtwbWvKhQ8g3QSzj_Tgmg4uw
     * @apiUse UserInfo
     * @apiError WeiXinError    微信端抛出异常,对应 response status <code>403</code>
     * @apiUse GeneralError
     */
///weixin/JavaSource/com/fantasy/wx/service/AccountWeiXinService.java
/**
 * 数据库存储微信信息
 */
/*return this.getAll().get(0);*/
/**
     * 查找所有配置信息
     *
     * @return List<AccountDetails>
     */
/*
         List<AccountDetails> accountDetailses = new ArrayList<AccountDetails>();Account account = new Account();
        account.setAppId("wxcbc2c9fb9d585cd3");
        account.setSecret("4b224fb5b08f2380572e45baecda63ba");
        account.setType(AccountDetails.Type.service);
        account.setToken("haolue_token");
        account.setAesKey("tUQwZUkxaiRFF14lLqjjIV53JaVaPtyoe0NEn8otai6");
        account.setPrimitiveId("gh_3d6114f11c71");
        accountDetailses.add(account);*/
/**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
/**
     * 保存微信配置信息对象
     *
     * @param wc appid
     * @return 微信配置信息对象
     */
/**
     * 根据id 批量删除
     *
     * @param ids appid
     */
/**
     * 通过id查找微信配置对象
     *
     * @param id appid
     * @return 微信配置对象
     */
///weixin/JavaSource/com/fantasy/wx/service/GroupMessageWeiXinService.java
/**
 * Created by zzzhong on 2014/8/28.
 */
/**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
/**
     * 删除群发消息，只能删除本地，不能删除微信记录
     *
     * @param ids
     */
/**
     * 获取分组消息对象
     *
     * @param id 唯一id
     * @return
     */
/**
     * 保存消息对象
     *
     * @param om
     * @return
     */
/**
     * 创建分组群发消息
     *
     * @param groupId 分组id
     * @param msgType 消息类型
     * @return
     */
/**
     * 创建用户列表群发消息
     *
     * @param openId  用户id集合
     * @param msgType 消息类型
     * @return
     */
/**
     * 用户列表群发，发送文本消息
     *
     * @param openid  永固id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
/**
     * 用户列表群发，发送图文消息
     *
     * @param openid
     * @param news   群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     * @throws IOException                              上传图片失败的io异常
     * @throws com.fantasy.wx.framework.exception.WeiXinException 微信异常
     */
/**
     * 分组群发，发送文本消息
     *
     * @param groupId 分组id
     * @param content 发送文本
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
/**
     * 分组群发，发送图文消息
     *
     * @param groupId 分组id
     * @param news    群发图文消息
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
/**
     * 上传图文消息
     *
     * @param news 图文消息对象
     * @return 上传图文消息的返回对象
     * @throws IOException                              文件上传io异常
     * @throws WxErrorException                         上传素材的异常
     * @throws com.fantasy.wx.framework.exception.WeiXinException 上传图片的异常
     */
/**
     * 按照分组发送群发消息
     *
     * @param message
     * @return
     */
/**
     * 按照openid发送群发消息
     *
     * @param message
     * @return
     */
///weixin/JavaSource/com/fantasy/wx/service/GroupWeiXinService.java
/**
 * Created by zzzhong on 2014/8/28.
 */
///weixin/JavaSource/com/fantasy/wx/service/MediaWeiXinService.java
/**
 * Created by zzzhong on 2014/12/16.
 */
///weixin/JavaSource/com/fantasy/wx/service/MenuWeiXinService.java
/**
 * 微信菜单
 */
/**
     * 获取所有菜单对象
     *
     * @return 菜单对象集合
     */
/**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
/**
     * 通过条件查找菜单对象集合
     *
     * @param filters
     * @return 菜单集合
     */
/**
     * 保存菜单对象
     *
     * @param wc
     */
/**
     * 保存菜单对象集合
     *
     * @param list
     */
/**
     * 根据id 批量删除
     *
     * @param ids
     */
/**
     * 通过id获取菜单对象
     *
     * @param id
     * @return菜单对象
     */
/**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
/**
     * 刷新菜单
     *
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
/**
     * 自定义菜单删除接口
     *
     * @return
     */
///weixin/JavaSource/com/fantasy/wx/service/MessageWeiXinService.java
/**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
/**
     * 删除消息对象
     *
     * @param ids id数组
     */
/**
     * 获取消息对象
     *
     * @param id
     * @return
     */
/**
     * 保存消息对象
     *
     * @param message
     * @return
     */
/**
     * 根据消息类型保存消息对象 更具msgType保存mediaId的消息的媒体文件
     * @param message
     * @return
     * @throws com.fantasy.wx.framework.exception.WeiXinException
     */
/**
     * 发送文本消息
     *
     * @param touser  发送人openId
     * @param content 发送内容
     * @return 微信返回码0为成功其他为错误码，可参考微信公众平台开发文档
     */
///weixin/JavaSource/com/fantasy/wx/service/QRCodeWeiXinService.java
/**
 * 二维码操作类
 */
/**
     * 列表查询
     *
     * @param pager   分页
     * @param filters 查询条件
     * @return 分页对象
     */
/**
     * 删除二维码对象
     *
     * @param ids
     */
/**
     * 获取二维码对象
     *
     * @param id
     * @return
     */
/**
     * 保存二维码对象
     *
     * @param q
     * @return
     */
/**
     * 换取临时二维码ticket
     *
     * @param linkKey 关联id
     * @param e       有效时间
     * @return 二维码对象
     * @throws
     */
/**
     * 换取永久二维码ticket
     * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=生成带参数的二维码
     *
     * @param linkKey 关联id
     * @return 二维码对象
     * @throws com.fantasy.wx.framework.exception.WeiXinException
     */
///weixin/JavaSource/com/fantasy/wx/service/UserInfoWeiXinService.java
/**
     * 刷新所有的用户信息
     *
     * @throws WeiXinException
     */
/**
     * 通过openId刷新用户信息
     */
/**
     * 通过安全连接的code换取微信用户信息
     *
     * @param code
     * @return
     */
/**
     * 设置用户列表的未读信息数量
     *
     * @param list
     */
/**
     * 设置用户的未读信息数量
     *
     * @param u
     */
/**
     * 刷新最后查看事件
     *
     * @param ui
     */
/**
     * 转换user到userinfo
     *
     * @param u
     * @return
     */
/* TODO 关注微信号时,是否自动创建会员记录
        if (ui != null) {
            String bex64OpenId = StringUtil.hexTo64(MessageDigestUtil.getInstance().get(ui.getOpenId()));
            Member member = new Member();
            member.setUsername(bex64OpenId);
            member.setPassword("123456");
            member.setNickName(ui.getNickname());

            member.setEnabled(true);
            member.setAccountNonExpired(false);
            member.setCredentialsNonExpired(false);
            member.setAccountNonLocked(false);

            MemberDetails details = member.getDetails();
            if (details == null) {
                details = new MemberDetails();
            }
            if (StringUtil.isBlank(details.getScore())) {
                details.setScore(0);
            }
            //会员头像
            File file = null;
            try {
                file = FileUtil.tmp();//临时文件
                HttpClientUtil.doGet(ui.getAvatar()).writeFile(file);
                String mimeType = FileUtil.getMimeType(file);
                String fileName = file.getName() + "." + mimeType.replace("image/", "");
                FileDetail fileDetail = fileUploadService.upload(file, mimeType, fileName, "avatar");
                LOG.debug("头像上传成功:" + fileDetail);
                details.setAvatarStore(JSON.serialize(new FileDetail[]{fileDetail}));
            } catch (FileNotFoundException e) {
                LOG.error(e.getMessage(), e);
            } catch (IOException e) {
                LOG.error(e.getMessage(), e);
            } finally {
                if (file != null) {
                    FileUtil.delFile(file);//删除临时文件
                }
            }
            member.setDetails(details);
            memberService.save(member);
            ui.setMember(member);
            save(ui);
            return ui;
        }
        return ui;
        */
///weixin-web/JavaSource/com/fantasy/wx/web/AccountAction.java
/**
 * 微信app配置
 *
 * @author 钟振振
 * @version 1.0
 * @功能描述 管理配置
 * @since 2014-09-19 上午17:36:52
 */
///weixin-web/JavaSource/com/fantasy/wx/web/MessageAction.java
/**
 * Created by zzzhong on 2014/9/23.
 */
/**
     * 搜索用户列表
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 搜索消息
     *
     * @param pager
     * @param filters
     * @return
     */
/**
     * 发送消息
     *
     * @param m
     * @return
     */
