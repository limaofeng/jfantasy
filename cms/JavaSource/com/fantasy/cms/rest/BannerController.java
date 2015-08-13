package com.fantasy.cms.rest;

import com.fantasy.cms.bean.Banner;
import com.fantasy.cms.service.BannerService;
import com.fantasy.framework.dao.Pager;
import com.fantasy.framework.dao.hibernate.PropertyFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


import java.util.List;

/**
 * 轮播图接口
 *
 * @author limaofeng@msn.com
 * @version 3.3.7, 15/06/27
 */
@RestController
@RequestMapping("/cms/banners")
public class BannerController {

    @Autowired
    private BannerService bannerService;

    /**
     * @param banner banner
     * @return banner
     * @api {post} /cms/banners  添加轮播图
     * @apiVersion 3.3.7
     * @apiName createBanner
     * @apiGroup 内容管理
     * @apiDescription 添加轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X POST http://localhost/cms/banners
     * @apiUse GeneralError
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    public Banner create(Banner banner) {
        return bannerService.save(banner);
    }

    /**
     * @param key    bannerId
     * @param banner banner
     * @return banner
     * @api {put} /cms/banners/:key  更新轮播图
     * @apiVersion 3.3.7
     * @apiName updateBanner
     * @apiGroup 内容管理
     * @apiDescription 更新轮播图信息
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i -X PUT http://localhost/cms/banners/root
     * @apiUse GeneralError
     */
    @RequestMapping(value = "/{key}", method = RequestMethod.PUT)
    public Banner update(@PathVariable("key") String key, Banner banner) {
        banner.setKey(key);
        return bannerService.save(banner);
    }

    /**
     * @param key Banner
     * @return Banner
     * @api {get} /cms/banners/:key  获取轮播图
     * @apiVersion 3.3.7
     * @apiName getBanner
     * @apiGroup 内容管理
     * @apiDescription 获取轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/banners/root
     * @apiUse GeneralError
     */
    public Banner view(String key) {
        return bannerService.get(key);
    }

    /**
     * @param key 编码
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
    public void delete(String key) {
        bannerService.delete(key);
    }

    /**
     * @param pager   翻页对象
     * @param filters 筛选条件
     * @api {get} /cms/banners   查询轮播图
     * @apiVersion 3.3.7
     * @apiName getBanners
     * @apiGroup 内容管理
     * @apiDescription 查询轮播图
     * @apiPermission admin
     * @apiExample Example usage:
     * curl -i http://localhost/cms/banners?currentPage=1&LIKES_name=上海
     * @apiUse paramPager
     * @apiUse paramPropertyFilter
     * @apiUse GeneralError
     */
    public Pager<Banner> search(Pager<Banner> pager, List<PropertyFilter> filters) {
        return this.bannerService.findPager(pager, filters);
    }
}

