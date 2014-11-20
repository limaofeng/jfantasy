package com.fantasy.mall.goods.service;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.spring.SpringContextUtil;
import com.fantasy.framework.util.common.DateUtil;
import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import com.fantasy.framework.util.regexp.RegexpUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import com.fantasy.mall.goods.bean.Product;
import com.fantasy.mall.goods.dao.GoodsCategoryDao;
import com.fantasy.mall.goods.dao.GoodsDao;
import com.fantasy.mall.sales.bean.Sales;
import com.fantasy.mall.sales.service.SalesService;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.type.BooleanType;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 商品 service
 */
@Service
@Transactional
public class GoodsService implements InitializingBean {

    private static final Log logger = LogFactory.getLog(GoodsService.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        PlatformTransactionManager transactionManager = SpringContextUtil.getBean("transactionManager", PlatformTransactionManager.class);
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            // 初始化商品根目录
            if (goodsCategoryDao.count() == 0) {
                StringBuffer log = new StringBuffer("初始化商品分类根目录");
                GoodsCategory category = new GoodsCategory();
                category.setSort(0);
                category.setSign("root");
                category.setName("商品分类根目录");
                save(category);
                logger.debug(log);
            }
            // 初始化商品图片目录
        } finally {
            transactionManager.commit(status);
        }
    }

    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsCategoryDao goodsCategoryDao;
    @Resource
    private ProductService productService;
    @Resource
    private SalesService salesService;

    /**
     * 根据商品编码获取商品
     *
     * @param sn 商品编码
     * @return 商品对象
     */
    public Goods get(String sn) {
        Goods goods = this.goodsDao.findUnique(Restrictions.eq("sn", sn));
        for (Product product : goods.getProducts()) {
            Hibernate.initialize(product);
        }
        return goods;
    }

    public List<GoodsCategory> rootCategories() {
        return ObjectUtil.sort(this.goodsCategoryDao.find(Restrictions.isNull("parent")), "sort");
    }

    /**
     * 获取商品栏目分类列表
     *
     * @return List<GoodsCategory>
     */
    public List<GoodsCategory> getCategories() {
        return this.goodsCategoryDao.find(new Criterion[0], "layer,sort", "asc,asc");
    }

    public List<GoodsCategory> getCategories(String sign) {
        GoodsCategory category = this.getCategory(sign);
        return this.goodsCategoryDao.find(new Criterion[]{Restrictions.like("path", category.getPath(), MatchMode.START), Restrictions.ne("sign", sign)}, "layer,sort", "asc,asc");
    }

    public GoodsCategory getCategory(String sign) {
        return this.goodsCategoryDao.findUnique(Restrictions.eq("sign", sign));
    }

    public boolean goodsCategorySignUnique(String sign, Long id) {
        GoodsCategory category = this.goodsCategoryDao.findUniqueBy("sign", sign);
        return (category == null) || category.getId().equals(id);
    }

    public static List<GoodsCategory> categories() {
        return SpringContextUtil.getBeanByType(GoodsService.class).getCategories();
    }

    /**
     * 获取商品列表
     *
     * @param pager   翻页对象
     * @param filters 过滤器
     * @return Pager<Goods>
     */
    public Pager<Goods> findPager(Pager<Goods> pager, List<PropertyFilter> filters) {
        return this.goodsDao.findPager(pager, filters);
    }

    /**
     * 获取商品分类
     *
     * @param id 分类id
     * @return GoodsCategory
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public GoodsCategory getCategory(Long id) {
        return this.goodsCategoryDao.get(id);
    }

    /**
     * 获取商品分类
     *
     * @param sign 分类编码
     * @return GoodsCategory
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
    public GoodsCategory getCategoryBySign(String sign) {
        return this.goodsCategoryDao.findUniqueBy("sign", sign);
    }

    /**
     * 保存商品栏目
     *
     * @param category 分类对象
     * @return GoodsCategory
     */
    public GoodsCategory save(GoodsCategory category) {
        List<GoodsCategory> categories;
        boolean root = false;
        GoodsCategory old = category.getId() != null ? this.goodsCategoryDao.get(category.getId()) : null;
        if (category.getParent() == null || StringUtil.isBlank(category.getParent().getId())) {
            category.setLayer(0);
            category.setPath(StringUtil.defaultValue(category.getSign(), old != null ? old.getSign() : "") + GoodsCategory.PATH_SEPARATOR);
            root = true;
            categories = ObjectUtil.sort(goodsCategoryDao.find(Restrictions.isNull("parent")), "sort", "asc");
        } else {
            GoodsCategory parentCategory = this.goodsCategoryDao.get(category.getParent().getId());
            category.setLayer(parentCategory.getLayer() + 1);
            category.setPath(parentCategory.getPath() + StringUtil.defaultValue(category.getSign(), old != null ? old.getSign() : "") + GoodsCategory.PATH_SEPARATOR);// 设置path
            categories = ObjectUtil.sort(goodsCategoryDao.findBy("parent.id", parentCategory.getId()), "sort", "asc");
        }
        if (old != null) {// 更新数据
            if (category.getSort() != null && (ObjectUtil.find(categories, "id", old.getId()) == null || !old.getSort().equals(category.getSort()))) {
                if (ObjectUtil.find(categories, "id", old.getId()) == null) {// 移动了节点的层级
                    int i = 0;
                    for (GoodsCategory m : ObjectUtil.sort((old.getParent() == null || StringUtil.isBlank(old.getParent().getId())) ? goodsCategoryDao.find(Restrictions.isNull("parent")) : goodsCategoryDao.findBy("parent.id", old.getParent().getId()), "sort", "asc")) {
                        m.setSort(i++);
                        this.goodsCategoryDao.save(m);
                    }
                    categories.add(category.getSort() - 1, category);
                } else {
                    GoodsCategory t = ObjectUtil.remove(categories, "id", old.getId());
                    if (categories.size() >= category.getSort()) {
                        categories.add(category.getSort() - 1, t);
                    } else {
                        categories.add(t);
                    }
                }
                // 重新排序后更新新的位置
                for (int i = 0; i < categories.size(); i++) {
                    GoodsCategory m = categories.get(i);
                    if (m.getId().equals(category.getId())) {
                        continue;
                    }
                    m.setSort(i + 1);
                    this.goodsCategoryDao.save(m);
                }
            }
        } else {// 新增数据
            category.setSort(categories.size() + 1);
        }
        this.goodsCategoryDao.save(category);
        if (root) {
            category.setParent(null);
            this.goodsCategoryDao.update(category);
        }
        return category;
    }

    /**
     * 批量删除商品栏目
     *
     * @param ids 分类ids
     */
    public void deleteCategory(Long... ids) {
        for (Long id : ids) {
            this.goodsCategoryDao.delete(id);
        }
    }

    /**
     * 批量删除商品
     *
     * @param ids 商品ids
     */
    @CacheEvict(allEntries = true, value = "fantasy.mall.GoodsService")
    public void deleteGoods(Long... ids) {
        for (Long id : ids) {
            this.goodsDao.delete(id);
        }
    }

    /**
     * 商品批量上架
     *
     * @param ids 商品ids
     */
    @CacheEvict(allEntries = true, value = "fantasy.mall.GoodsService")
    public void upGoods(Long... ids) {
        for (Long id : ids) {
            Goods goods = this.goodsDao.get(id);
            goods.setMarketable(true);
            this.goodsDao.save(goods);
        }
    }

    /**
     * 商品批量下架
     *
     * @param ids 商品ids
     */
    @CacheEvict(allEntries = true, value = "fantasy.mall.GoodsService")
    public void downGoods(Long... ids) {
        for (Long id : ids) {
            Goods goods = this.goodsDao.get(id);
            goods.setMarketable(false);
            this.goodsDao.save(goods);
        }
    }

    /**
     * 获得商品
     *
     * @param id 商品id
     * @return Goods
     */
    public Goods getGoods(Long id) {
        Goods goods = goodsDao.get(id);
        if (goods == null)
            return null;
        for (Product product : goods.getProducts()) {
            Hibernate.initialize(product);
        }
        Hibernate.initialize(goods.getBrand());
        Hibernate.initialize(goods.getCategory());
        return goods;
    }

    /**
     * 保存商品
     *
     * @param goods 商品对象            备注：如果只需要添加一个product  specificationEnabled=true  如果需要添加多个product specificationEnabled=true
     * @return Goods
     */
    public Goods save(Goods goods) {
        if (goods.getId() == null) {// 新增商品时初始化数据
            goods.setStore(0);
            goods.setFreezeStore(0);
            goods.setMonthSaleCount(0);
            goods.setSaleCount(0);
            goods.setMarketable(false);
            //设置是否启用规格默认值
            if (goods.getSpecificationEnabled() == null) {
                goods.setSpecificationEnabled(false);
            }
            //设置 products 默认空集合
            if (goods.getProducts() == null) {
                goods.setProducts(new ArrayList<Product>());
            }
        }
        this.goodsDao.save(goods);
        if (!goods.getSpecificationEnabled()) {// 如果未启用商品规格
            Product product = goods.getProducts().isEmpty() ? new Product() : goods.getProducts().get(0);
            product.initialize(goods);
            productService.save(product);
        } else {//启用了商品规格  TODO 该位置以后需要优化
            for (Product product : goods.getProducts()) {
                product.initialize(goods);
                this.productService.save(product);
            }
        }
        return goods;
    }

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
    public List<Goods> find(List<PropertyFilter> filters, String orderBy, String order, int start, int size) {
        return this.goodsDao.find(filters, orderBy, order, start, size);
    }

    /**
     * 计算商品的存货及销售数量
     *
     * @param id 商品id
     */
    @CacheEvict(key = "'getGoods' + #id ", value = "fantasy.mall.GoodsService")
    public void calculateStockNumber(Long id) {
        Goods goods = this.goodsDao.get(id);
        int store = 0, freezeStore = 0;
        for (Product product : goods.getProducts()) {
            store += product.getStore();
            freezeStore += product.getFreezeStore();
        }
        goods.setStore(store);
        goods.setFreezeStore(freezeStore);
    }

    /**
     * 商品销量统计 统计总销量与月销量
     *
     * @param product  产品对象
     * @param quantity 数量
     * @param amount   金额
     */
    public void saleCount(Product product, Integer quantity, BigDecimal amount) {
        Goods goods = this.goodsDao.get(product.getGoods().getId());
        // 保存货品销量
        Date now = DateUtil.now();
        salesService.addSales(Sales.Type.product, product.getSn(), TimeUnit.day, DateUtil.format(now, "yyyyMMdd"), quantity, amount);
        // 保存商品销量
        salesService.addSales(Sales.Type.goods, goods.getSn(), TimeUnit.day, DateUtil.format(now, "yyyyMMdd"), quantity, amount);
        // 计算商品总销售量
        goods.setSaleCount(goods.getSaleCount() + quantity);
        // 计算商品月销售量
        this.goodsDao.save(goods);
    }

    /**
     * 调整被占库存
     *
     * @param id       商品id
     * @param quantity 调整数量
     */
    @CacheEvict(key = "'getGoods' + #id", value = "fantasy.mall.GoodsService")
    public void freezeStore(Long id, Integer quantity) {
        Goods goods = this.goodsDao.get(id);
        goods.setFreezeStore(goods.getFreezeStore() + quantity);
        this.goodsDao.save(goods);
    }

    /**
     * 调整库存
     *
     * @param id       商品id
     * @param quantity 调整数量
     */
    @CacheEvict(key = "'getGoods' + #id", value = "fantasy.mall.GoodsService")
    public void replenish(Long id, Integer quantity) {
        Goods goods = this.goodsDao.get(id);
        goods.setStore(goods.getStore() + quantity);
        this.goodsDao.save(goods);
    }

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
    @SuppressWarnings("unchecked")
    public List<Goods> ranking(String path, TimeUnit timeUnit, String time, int size) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.sqlRestriction("{alias}.sn in (SELECT SN FROM MALL_GOODS WHERE MARKETABLE = ?)", true, BooleanType.INSTANCE));
        criterions.add(Restrictions.eq("type", Sales.Type.goods));
        criterions.add(Restrictions.like("path", path + "%"));
        criterions.add(Restrictions.eq("timeUnit", timeUnit));
        criterions.add(Restrictions.eq("time", time));
        List<Sales> goodsSales = salesService.sellingRanking(criterions.toArray(new Criterion[criterions.size()]), 0, size);
        if (goodsSales.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Goods> list = this.goodsDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(goodsSales, "sn", String.class)));
        List<Goods> goods = new ArrayList<Goods>();
        for (Sales sales : goodsSales) {
            goods.add(ObjectUtil.find(list, "sn", sales.getSn()));
        }
        return goods;
    }

    @SuppressWarnings("unchecked")
    public List<Goods> ranking(String path, TimeUnit timeUnit, String startTime, String endTime, int size) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.sqlRestriction("{alias}.sn in (SELECT SN FROM MALL_GOODS WHERE MARKETABLE = ?)", true, BooleanType.INSTANCE));
        criterions.add(Restrictions.eq("type", Sales.Type.goods));
        criterions.add(Restrictions.like("path", path + "%"));
        criterions.add(Restrictions.eq("timeUnit", timeUnit));
        criterions.add(Restrictions.ge("time", startTime));
        criterions.add(Restrictions.lt("time", endTime));
        List<Sales> goodsSales = salesService.sellingRanking(criterions.toArray(new Criterion[criterions.size()]), 0, size);
        if (goodsSales.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Goods> list = this.goodsDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(goodsSales, "sn", String.class)));
        List<Goods> goods = new ArrayList<Goods>();
        for (Sales sales : goodsSales) {
            goods.add(ObjectUtil.find(list, "sn", sales.getSn()));
        }
        return goods;
    }

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
    @SuppressWarnings("unchecked")
    public List<Goods> ranking(TimeUnit timeUnit, String time, int size) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("type", Sales.Type.goods));
        criterions.add(Restrictions.sqlRestriction("{alias}.sn in (SELECT SN FROM MALL_GOODS WHERE MARKETABLE = ?)", true, BooleanType.INSTANCE));
        criterions.add(Restrictions.eq("timeUnit", timeUnit));
        criterions.add(Restrictions.eq("time", time));
        List<Sales> goodsSales = salesService.sellingRanking(criterions.toArray(new Criterion[criterions.size()]), 0, size);
        if (goodsSales.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Goods> list = this.goodsDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(goodsSales, "sn", String.class)));
        List<Goods> goods = new ArrayList<Goods>();
        for (Sales sales : goodsSales) {
            goods.add(ObjectUtil.find(list, "sn", sales.getSn()));
        }
        return goods;
    }

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
    @SuppressWarnings("unchecked")
    public List<Goods> ranking(TimeUnit timeUnit, String startTime, String endTime, int size) {
        List<Criterion> criterions = new ArrayList<Criterion>();
        criterions.add(Restrictions.eq("type", Sales.Type.goods));
        criterions.add(Restrictions.sqlRestriction("{alias}.sn in (SELECT SN FROM MALL_GOODS WHERE MARKETABLE = ?)", true, BooleanType.INSTANCE));
        criterions.add(Restrictions.eq("timeUnit", timeUnit));
        criterions.add(Restrictions.ge("time", startTime));
        criterions.add(Restrictions.lt("time", endTime));
        List<Sales> goodsSales = salesService.sellingRanking(criterions.toArray(new Criterion[criterions.size()]), 0, size);
        if (goodsSales.isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        List<Goods> list = this.goodsDao.find(Restrictions.in("sn", ObjectUtil.toFieldArray(goodsSales, "sn", String.class)));
        List<Goods> goods = new ArrayList<Goods>();
        for (Sales sales : goodsSales) {
            goods.add(ObjectUtil.find(list, "sn", sales.getSn()));
        }
        return goods;
    }

    /**
     * @param templateUrl 模板路径
     * @param object      关联对象
     * @return 新的路径
     */
    public static String getTemplatePath(String templateUrl, final Object object) {
        if (object == null || (!(object instanceof GoodsCategory) && !(object instanceof Goods))) {
            return RegexpUtil.replace(templateUrl, "\\{sign\\}", "");
        }
        Configuration configuration = SpringContextUtil.getBean("freemarkerService", Configuration.class);
        GoodsCategory category = object instanceof GoodsCategory ? (GoodsCategory) object : ((Goods) object).getCategory();

        do {
            String newTemplateUrl = RegexpUtil.replace(templateUrl, "\\{sign\\}", "_" + category.getSign());
            try {
                configuration.getTemplate(newTemplateUrl);
                return newTemplateUrl;
            } catch (IOException e) {
                if (category.getParent() == null) {
                    break;
                }
                category = category.getParent();
            }
        } while (true);
        return RegexpUtil.replace(templateUrl, "\\{sign\\}", "");
    }


    /**
     * 移动商品
     *
     * @param ids        商品ids
     * @param categoryId 移动到的分类id
     */
    public void moveGoods(Long[] ids, Long categoryId) {
        GoodsCategory goodsCategory = this.goodsCategoryDao.get(categoryId);
        for (Long id : ids) {
            Goods goods = this.goodsDao.get(id);
            goods.setCategory(goodsCategory);
            this.goodsDao.save(goods);
        }
    }

    public List<GoodsCategory> listGoodsCategory() {
        return this.goodsCategoryDao.find(Restrictions.isNull("parent"));
    }

    public static List<GoodsCategory> goodsCategoryList() {
        GoodsService goodsService = SpringContextUtil.getBeanByType(GoodsService.class);
        return goodsService.listGoodsCategory();
    }


    /**
     * 分类分页查询
     *
     * @param pager
     * @param filters
     * @return
     */
    public Pager<GoodsCategory> findCategoryPager(Pager<GoodsCategory> pager, List<PropertyFilter> filters) {
        return this.goodsCategoryDao.findPager(pager, filters);
    }

}