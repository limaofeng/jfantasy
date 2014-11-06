package com.fantasy.mall.goods.service;

import com.fantasy.attr.bean.AttributeVersion;
import com.fantasy.attr.service.AttributeVersionService;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import com.fantasy.mall.goods.bean.Goods;
import com.fantasy.mall.goods.bean.GoodsCategory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/applicationContext.xml"})
public class GoodsServiceTest {

    private static final Log logger = LogFactory.getLog(GoodsService.class);

    @Resource
    private GoodsService goodsService;
    @Resource
    private AttributeVersionService attributeVersionService;

    @Test
    public void testGet() throws Exception {
        List<Goods> goodses = this.goodsService.find(new LinkedList<PropertyFilter>(), "sn", "desc", 0, 1);
        if (!goodses.isEmpty()) {
            goodsService.get(goodses.get(0).getSn());
        }
    }

    @Test
    public void testSave() throws Exception {
        GoodsCategory category = new GoodsCategory();
        GoodsCategory goodsCategory = goodsService.getCategory("DSHD8");

        category.setId(goodsCategory.getId());
        category.setSign(goodsCategory.getSign());

        AttributeVersion attributeVersion = attributeVersionService.get(2L);
        if (attributeVersion != null) {
            category.setGoodsVersion(attributeVersion);
        }

        this.goodsService.save(category);
        logger.debug("version=" + category.getGoodsVersion());
    }

    @Test
    public void testGetCategory() throws Exception {
        GoodsCategory category = goodsService.getCategory("DSHD8");
        logger.debug("version=" + category.getGoodsVersion());
    }

    @Test
    public void testRootCategories() throws Exception {

    }

    @Test
    public void testGetCategories() throws Exception {

    }

    @Test
    public void testGetCategories1() throws Exception {

    }

    @Test
    public void testGoodsCategorySignUnique() throws Exception {

    }

    @Test
    public void testCategories() throws Exception {

    }

    @Test
    public void testGoods() throws Exception {

    }

    @Test
    public void testFindPager() throws Exception {

    }

    @Test
    public void testGetCategory1() throws Exception {

    }

    @Test
    public void testGetCategoryBySign() throws Exception {

    }

    @Test
    public void testDeleteCategory() throws Exception {

    }

    @Test
    public void testDeleteGoods() throws Exception {

    }

    @Test
    public void testUpGoods() throws Exception {

    }

    @Test
    public void testDownGoods() throws Exception {

    }

    @Test
    public void testGetGoods() throws Exception {

    }

    @Test
    public void testSave1() throws Exception {

    }

    @Test
    public void testFind() throws Exception {

    }

    @Test
    public void testCalculateStockNumber() throws Exception {

    }

    @Test
    public void testSaleCount() throws Exception {

    }

    @Test
    public void testFreezeStore() throws Exception {

    }

    @Test
    public void testReplenish() throws Exception {

    }

    @Test
    public void testRanking() throws Exception {

    }

    @Test
    public void testRanking1() throws Exception {

    }

    @Test
    public void testRanking2() throws Exception {

    }

    @Test
    public void testRanking3() throws Exception {

    }

    @Test
    public void testGetTemplatePath() throws Exception {

    }

    @Test
    public void testMoveGoods() throws Exception {

    }

    @Test
    public void testListGoodsCategory() throws Exception {

    }

    @Test
    public void testGoodsCategoryList() throws Exception {

    }
}