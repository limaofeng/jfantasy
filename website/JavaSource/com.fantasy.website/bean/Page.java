package org.jfantasy.website.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.system.bean.Website;

import java.util.List;

/**
 * 页面静态化时，对某个页面的配置
 */
@Entity
@Table(name = "SWP_PAGE", uniqueConstraints = {@UniqueConstraint(name = "UNIQUE_PAGE", columnNames = {"PATH", "WEBSITE_ID"})})
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "datas"})
public class Page extends BaseBusEntity {

    private static final long serialVersionUID = 8032849785819496211L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 名称
     */
    @Column(name = "NAME")
    private String name;
    /**
     * 对应模板
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEMPLATE_ID")
    private Template template;
    /**
     * 对应的数据
     */
    @ManyToMany(targetEntity = Data.class, fetch = FetchType.LAZY)
    @JoinTable(name = "SWP_PAGE_DATA", joinColumns = @JoinColumn(name = "PAGE_ID"), inverseJoinColumns = @JoinColumn(name = "DATA_ID"), foreignKey = @ForeignKey(name = "FK_PAGE_DATA"))
    @OrderBy(value = "id desc")
    private List<Data> datas;
    /**
     * 站点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WEBSITE_ID")
    private Website webSite;
    /**
     * 文件存储路径
     */
    @Column(name = "PATH")
    private String path;
    /**
     * list类型的页面,pageSize默认15条
     */
    @Column(name = "PAGE_SIZE", columnDefinition = "INT default 15")
    private int pageSize;

    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY, cascade = {CascadeType.REMOVE})
    @OrderBy(value = "id desc")
    private List<PageItem> pageItems;

    /**
     * 对应的触发器规则
     */
    @OneToMany(mappedBy = "page", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Trigger> triggers;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Template getTemplate() {
        return template;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    public Website getWebSite() {
        return webSite;
    }

    public void setWebSite(Website webSite) {
        this.webSite = webSite;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<PageItem> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<PageItem> pageItems) {
        this.pageItems = pageItems;
    }


    public List<Trigger> getTriggers() {
        return triggers;
    }

    public void setTriggers(List<Trigger> triggers) {
        this.triggers = triggers;
    }
}
