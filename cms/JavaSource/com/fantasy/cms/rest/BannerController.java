package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.service.BannerService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @apiDefine paramBanner
 * @apiParam {String} key  编码
 * @apiParam {String} name  名称
 * @apiParam {String} size  图片尺寸
 * @apiParam {String} description  描述
 * @apiParam {List}  bannerItems  图片明细
 * @apiParam {String}  BannerItem.title  标题
 * @apiParam {String}  BannerItem.summary  摘要
 * @apiParam {String}  BannerItem.url  跳转地址
 * @apiParam {String}  BannerItem.sort  排序字段
 * @apiParam {String}  BannerItem.bannerImage 图片对象
 * @apiVersion 3.3.7
 */

/**
 * @apiDefine returnBanner
 * @apiSuccess {String} key  编码
 * @apiSuccess {String} name  名称
 * @apiSuccess {String} size  图片尺寸
 * @apiSuccess {String} description  描述
 * @apiSuccess {List}  bannerItems  图片明细
 * @apiSuccess {String}  BannerItem.id  明细ID
 * @apiSuccess {String}  BannerItem.title  标题
 * @apiSuccess {String}  BannerItem.summary  摘要
 * @apiSuccess {String}  BannerItem.url  跳转地址
 * @apiSuccess {String}  BannerItem.sort  排序字段
 * @apiSuccess {String}  BannerItem.bannerImage 图片对象
 * @apiSuccess {Banner}  BannerItem.banner Banner对象
 * @apiVersion 3.3.7
 */
@RestController
@RequestMapping("/cms/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * @api {post} /cms/banners  添加轮播图
     * @apiVersion 3.3.7
     * @apiName createBanner
     * @apiGroup 内容管理
     * @apiDescription 添加轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST http://localhost/cms/banners
     * @apiUse paramBanner
     * @apiUse returnBanner
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.POST)
    public Banner create(Banner banner) {
        return bannerService.save(banner);
    }

    /**
     * @api {put} /cms/banners/:key  更新轮播图
     * @apiVersion 3.3.7
     * @apiName updateBanner
     * @apiGroup 内容管理
     * @apiDescription 更新轮播图信息
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/banners/root
     * @apiUse paramBanner
     * @apiUse returnBanner
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    public Banner update(@PathVariable("key") String key, Banner banner) {
        banner.setKey(key);
        return bannerService.save(banner);
    }

    /**
     * @api {get} /cms/banners/:key  获取轮播图
     * @apiVersion 3.3.7
     * @apiName getBanner
     * @apiGroup 内容管理
     * @apiDescription 获取轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/banners/root
     * @apiUse returnBanner
     * @apiUse GeneralError
     */
    @RequestMapping("/{key}")
    public Banner view(@PathVariable("key") String key) {
        return bannerService.get(key);
    }

    /**
     * @api {delete} /cms/banners/:key 删除轮播图
     * @apiVersion 3.3.7
     * @apiName deleteBanner
     * @apiGroup 内容管理
     * @apiDescription 删除轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/banners/root
     * @apiUse GeneralError
     */
    @RequestMapping(value="/{key}",method = {RequestMethod.DELETE})
    public void delete(@PathVariable("key") String key) {
        bannerService.delete(key);
    }

    /**
     * @param key bannerId-1
     * @param key bannerId-2
     * @api {batchDelete} /cms/banners/:key 批量删除轮播图
     * @apiVersion 3.3.7
     * @apiName deleteBanner
     * @apiGroup 内容管理
     * @apiDescription 批量删除轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X DELETE http://localhost/cms/banners/root
     * @apiUse GeneralError
     */
    @RequestMapping(method = {RequestMethod.DELETE})
    public void batchDelete(String... key) {
        bannerService.delete(key);
    }

    /**
     * @api {get} /cms/banners   查询轮播图
     * @apiVersion 3.3.7
     * @apiName getBanners
     * @apiGroup 内容管理
     * @apiDescription 查询轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/banners?currentPage=1&EQS_key=test
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse returnPager
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.GET)
    public Pager<Banner> search(Pager<Banner> pager, List<PropertyFilter> filters) {
        return this.bannerService.findPager(pager, filters);
    }
}

