package org.jfantasy.cms.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.htmlcleaner.TagNode;
import org.jfantasy.cms.bean.Article;
import org.jfantasy.cms.bean.ArticleCategory;
import org.jfantasy.cms.dao.ArticleCategoryDao;
import org.jfantasy.cms.dao.ArticleDao;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.hibernate.PropertyFilter;
import org.jfantasy.framework.lucene.BuguSearcher;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.htmlcleaner.HtmlCleanerUtil;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CMS service
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-5-28 下午03:05:30
 */
@Service
@Transactional
public class CmsService extends BuguSearcher<Article> {

    private static final Log LOG = LogFactory.getLog(CmsService.class);

    @Autowired
    private ArticleCategoryDao articleCategoryDao;
    @Autowired
    private ArticleDao articleDao;

//    @Autowired
//    private AttributeVersionService versionService;

    /**
     * 获取全部栏目
     *
     * @return List<ArticleCategory>
     */
    public List<ArticleCategory> getCategorys() {
        return articleCategoryDao.find(new Criterion[0], "layer,sort", "asc,asc");
    }

    public List<ArticleCategory> getCategorys(String code) {
        ArticleCategory category = this.get(code);
        return articleCategoryDao.find(new Criterion[]{Restrictions.like("path", category.getPath(), MatchMode.START), Restrictions.ne("code", code)}, "layer,sort", "asc,asc");
    }

    /**
     * 文章查询方法
     *
     * @param pager    翻页对象
     * @param filters 筛选条件
     * @return string
     */
    public Pager<Article> findPager(Pager<Article> pager, List<PropertyFilter> filters) {
        /*
        AdminUser adminUser = SpringSecurityUtils.getCurrentUser(AdminUser.class);
        if (adminUser != null) {
            String code = SettingUtil.getValue("cms");
            if (StringUtil.isNotBlank(code)) {
                filters.add(new PropertyFilter("LIKES_category.path", this.articleCategoryDao.get(code).getPath()));
            }
        }*/
        return articleDao.findPager(pager, filters);
    }

    public Pager<ArticleCategory> findCategoryPager(Pager<ArticleCategory> pager, List<PropertyFilter> filters) {
        /*
        AdminUser adminUser = SpringSecurityUtils.getCurrentUser(AdminUser.class);
        if (adminUser != null) {
            String code = SettingUtil.getValue("cms");
            if (StringUtil.isNotBlank(code)) {
                ArticleCategory category = this.articleCategoryDao.get(code);
                if (category != null) {
                    filters.add(new PropertyFilter("LIKES_path", category.getPath()));
                    filters.add(new PropertyFilter("NEI_layer", "0"));
                }
            }
        }*/
        return this.articleCategoryDao.findPager(pager, filters);
    }

    private final static String ARTICLECATEGORY_PARENT_PROPERTYNAME = "parent";

    /**
     * 保存栏目
     *
     * @param category 分类对象
     * @return string
     */
    public ArticleCategory save(ArticleCategory category) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("保存栏目 > " + JSON.serialize(category));
        }
        List<ArticleCategory> categories;
        boolean root = false;
        if (category.getParent() == null || StringUtil.isBlank(category.getParent().getCode())) {
            category.setLayer(0);
            category.setPath(category.getCode() + ArticleCategory.PATH_SEPARATOR);
            root = true;
            categories = ObjectUtil.sort(articleCategoryDao.find(Restrictions.isNull(ARTICLECATEGORY_PARENT_PROPERTYNAME)), "sort", "asc");
        } else {
            ArticleCategory parentCategory = this.articleCategoryDao.get(category.getParent().getCode());
            category.setLayer(parentCategory.getLayer() + 1);
            category.setPath(parentCategory.getPath() + category.getCode() + ArticleCategory.PATH_SEPARATOR);// 设置path
            categories = ObjectUtil.sort(articleCategoryDao.findBy("parent.code", parentCategory.getCode()), "sort", "asc");
        }
        ArticleCategory old = category.getCode() != null ? this.articleCategoryDao.get(category.getCode()) : null;
        // 新增数据
        if (old == null) {
            category.setSort(categories.size() + 1);
            // 更新数据
        } else {
            if (category.getSort() != null) {
                if (ObjectUtil.find(categories, "code", old.getCode()) == null) {// 移动了节点的层级
                    int i = 0;
                    for (ArticleCategory m : ObjectUtil.sort((old.getParent() == null || StringUtil.isBlank(old.getParent().getCode())) ? articleCategoryDao.find(Restrictions.isNull(ARTICLECATEGORY_PARENT_PROPERTYNAME)) : articleCategoryDao.findBy("parent.id", old.getParent().getCode()), "sort", "asc")) {
                        m.setSort(i++);
                        this.articleCategoryDao.save(m);
                    }
                    categories.add(category.getSort() - 1, category);
                } else if (!old.getSort().equals(category.getSort())) {
                    ArticleCategory t = ObjectUtil.remove(categories, "code", old.getCode());
                    if (categories.size() >= category.getSort()) {
                        categories.add(category.getSort() - 1, t);
                    } else {
                        categories.add(t);
                    }
                }
                // 重新排序后更新新的位置
                for (int i = 0; i < categories.size(); i++) {
                    ArticleCategory m = categories.get(i);
                    if (m.getCode().equals(category.getCode())) {
                        continue;
                    }
                    m.setSort(i + 1);
                    this.articleCategoryDao.save(m);
                }
            }
        }
        ArticleCategory categoryTemp = this.articleCategoryDao.save(category);
        if (root) {
            category.setParent(null);
            this.articleCategoryDao.update(categoryTemp);
        }
        return categoryTemp;
    }

    /**
     * 得到栏目
     *
     * @param code categoryCode
     * @return ArticleCategory
     */
    public ArticleCategory get(String code) {
        return this.articleCategoryDao.get(code);
    }

    /**
     * 移除栏目
     *
     * @param codes 栏目 Code
     */
    public void delete(String... codes) {
        for (String code : codes) {
            articleCategoryDao.delete(code);
        }
    }

    /**
     * 保存文章
     *
     * @param article 文章对象
     * @return Article
     */
    public Article save(Article article) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("保存文章 > " + JSON.serialize(article));
        }
        article = this.articleDao.save(article);
        if (StringUtil.isBlank(article.getSummary()) && StringUtil.isNotBlank(article.getContent())) {
            TagNode node = HtmlCleanerUtil.htmlCleaner(article.getContent().getText());
            String content = node.getText().toString().trim().replace("\n", "");
            String summary = content.substring(0, content.length() >= 10 ? 10 : content.length());
            article.setSummary(summary);
        }
        return article;
    }

    /**
     * 获取文章
     *
     * @param id 文章id
     * @return Article
     */
    public Article get(Long id) {
        Article article = this.articleDao.get(id);
        if (article == null) {
            return null;
        }
        Hibernate.initialize(article.getCategory());
        Hibernate.initialize(article.getContent());
        return article;
    }

    /**
     * 发布文章
     *
     * @param ids 文章 ids
     */
    public void issue(Long[] ids) {
        for (Long id : ids) {
            Article art = this.get(id);
            art.setIssue(true);
            art.setReleaseDate(DateUtil.format("yyyy-MM-dd hh:mm"));
            this.save(art);
        }
    }

    /**
     * 关闭文章
     *
     * @param ids 文章 ids
     */
    public void colseissue(Long[] ids) {
        for (Long id : ids) {
            Article art = this.get(id);
            art.setIssue(false);
            art.setReleaseDate(DateUtil.format("yyyy-MM-dd hh:mm"));
            this.save(art);
        }
    }

    /**
     * 删除文章
     *
     * @param ids 文章 ids
     */
    public void delete(Long... ids) {
        for (Long id : ids) {
            this.articleDao.delete(id);
        }
    }

    public List<Article> getArticles(Criterion[] criterions, int size) {
        return this.articleDao.find(criterions, 0, size);
    }

    /**
     * 文章分类编码唯一
     *
     * @param code 分类 code
     * @return boolean
     */
    public boolean articleCategoryUnique(String code) {
        return this.articleCategoryDao.findUniqueBy("code", code) == null;
    }

    public List<Article> find(List<PropertyFilter> filters, String orderBy, String order, int size) {
        if (StringUtil.isBlank(orderBy)) {
            return this.articleDao.find(filters, 0, size);
        } else {
            return this.articleDao.find(filters, orderBy, StringUtil.defaultValue(order, Pager.SORT_ASC), 0, size);
        }
    }

    /**
     * 移动文章
     *
     * @param ids          文章 ids
     * @param categoryCode 分类 categoryCode
     */
    public void moveArticle(Long[] ids, String categoryCode) {
        ArticleCategory articleCategory = this.articleCategoryDao.get(categoryCode);
        for (Long id : ids) {
            Article article = this.articleDao.get(id);
            article.setCategory(articleCategory);
            this.articleDao.save(article);
        }
    }

    public List<ArticleCategory> listArticleCategory() {
        return this.articleCategoryDao.find(Restrictions.isNull(ARTICLECATEGORY_PARENT_PROPERTYNAME));
    }
}
