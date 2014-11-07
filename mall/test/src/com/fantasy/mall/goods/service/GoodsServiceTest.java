package com.fantasy.mall.goods.service;

import com.fantasy.common.bean.enums.TimeUnit;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.framework.util.ognl.OgnlUtil;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import junit.framework.Assert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class GoodsServiceTest {

    private static final Log logger = LogFactory.getLog(GoodsService.class);

    @Resource
    private GoodsService goodsService;

    @Before
    public void setUp() throws Exception {
        this.testSave();
    }

    @After
    public void tearDown() throws Exception {
        this.testDeleteGoods();
        this.testDeleteCategory();
    }

    @Test
    public void testGet() throws Exception {
        List<Goods> goodses = this.goodsService.find(new LinkedList<PropertyFilter>(), "sn", "desc", 0, 1);
        if (!goodses.isEmpty()) {
            goodsService.get(testGetGoods().getSn());
        }
    }

    @Test
    public void testCalculateStockNumber() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_category.sign", category.getSign()));
        Long id = this.goodsService.find(filters, "sn", "desc", 0, 1).get(0).getId();
        this.goodsService.calculateStockNumber(id);
    }

    public void testSave() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");

        if (category != null) {
            this.testDeleteGoods();
            this.testDeleteCategory();
        }

        category = new GoodsCategory();
        category.setSign("JUnit-TEST");
        category.setName("JUnit测试");
        OgnlUtil.getInstance().setValue("parent.sign", category, "ROOT");
        this.goodsService.save(category);

        Assert.assertNotNull(category.getId());

        Goods goods = new Goods();
        goods.setEngname("test goods");
        goods.setName("测试商品");
        goods.setCategory(category);
        goods.setMarketPrice(BigDecimal.valueOf(0.5));
        goods.setPrice(BigDecimal.valueOf(0.5));
        this.goodsService.save(goods);

        logger.debug("goods sn = " + goods.getSn());

    }

    @Test
    public void testGetCategories() throws Exception {
        List<GoodsCategory> categories = this.goodsService.getCategories("ROOT");

        for (GoodsCategory category : categories) {
            logger.debug(category.getName() + "(" + category.getSign() + ")");
        }

    }

    @Test
    public void testRootCategories() throws Exception {
        List<GoodsCategory> categories = this.goodsService.rootCategories();

        for (GoodsCategory category : categories) {
            logger.debug(category.getName() + "(" + category.getSign() + ")");
        }

    }

    @Test
    public void testGetCategory() throws Exception {
        GoodsCategory category = goodsService.getCategory("JUnit-TEST");

        Assert.assertNotNull(category);

        Assert.assertNotNull(this.goodsService.getCategory(category.getId()));
    }

    public void testDeleteGoods() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_category.sign", category.getSign()));
        for (Goods goods : this.goodsService.find(filters, "sn", "asc", 0, 10)) {
            this.goodsService.deleteGoods(goods.getId());
        }
    }

    public void testDeleteCategory() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");

        this.goodsService.deleteCategory(category.getId());
    }

    @Test
    public void testGoodsCategorySignUnique() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");
        this.goodsService.goodsCategorySignUnique("JUnit-TEST", 0l);
        this.goodsService.goodsCategorySignUnique("JUnit-TEST", category.getId());
    }

    @Test
    public void testFindPager() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_category.sign", category.getSign()));
        this.goodsService.findPager(new Pager<Goods>(), filters);
    }

    @Test
    public void testGetCategoryBySign() throws Exception {
        Assert.assertNotNull(this.goodsService.getCategoryBySign("JUnit-TEST"));
    }

    @Test
    public void testUpGoods() throws Exception {
        this.goodsService.upGoods(testGetGoods().getId());
    }

    @Test
    public void testDownGoods() throws Exception {
        this.goodsService.downGoods(testGetGoods().getId());
    }

    public Goods testGetGoods() throws Exception {
        GoodsCategory category = this.goodsService.getCategory("JUnit-TEST");
        List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
        filters.add(new PropertyFilter("EQS_category.sign", category.getSign()));
        List<Goods> goodses = this.goodsService.find(filters, "sn", "desc", 0, 1);
        Assert.assertFalse(goodses.isEmpty());
        return this.goodsService.getGoods(goodses.get(0).getId());
    }


    @Test
    public void testSaleCount() throws Exception {
        this.goodsService.saleCount(testGetGoods().getProducts().get(0), 10, new BigDecimal(0.1));
    }

    @Test
    public void testFreezeStore() throws Exception {
        this.goodsService.freezeStore(testGetGoods().getId(), 1);
    }

    @Test
    public void testReplenish() throws Exception {
        this.goodsService.replenish(testGetGoods().getId(),1);
    }

    @Test
    public void testRanking() throws Exception {

        this.goodsService.ranking("ROOT", TimeUnit.day, "20141115", 5);

        this.goodsService.ranking("ROOT", TimeUnit.day, "20141110", "20141115", 5);

        this.goodsService.ranking(TimeUnit.day, "20141115", 5);

        this.goodsService.ranking("ROOT", TimeUnit.day, "20141110", "20141115", 5);

    }

    @Test
    public void testMoveGoods() throws Exception {
        this.goodsService.moveGoods(new Long[]{testGetGoods().getId()},this.goodsService.getCategory("JUnit-TEST").getId());
    }

    @Test
    public void testListGoodsCategory() throws Exception {
        this.goodsService.listGoodsCategory();
    }

}