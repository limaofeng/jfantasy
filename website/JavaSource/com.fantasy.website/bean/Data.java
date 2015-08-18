package com.fantasy.website.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SWP_DATA")
public class Data extends BaseBusEntity {

    private static final long serialVersionUID = -189976455889892065L;

    /**
     * 数据作用范围
     *
     * @author 李茂峰
     * @version 1.0
     * @since 2013-12-25 上午10:10:53
     */
    public enum Scope {
        global("global"), page("page");

        private Scope(String value) {
            this.value = value;
        }

        private String value;

        public String getValue() {
            return this.value;
        }
    }

    /**
     * 数据源
     */
    public enum DataSource{
        db("数据库"),
        func("方法"),
        stat("静态");

        private String value;

        private DataSource (String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    /**
     * 数据的范围<br/>
     * 暂时只有 global、page <br/>
     * 为global时。所有page都可以使用。为page时。只有引用的page能使用
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "SCOPE")
    private Scope scope;

    /**
     * 数据缓存时间(如果不希望数据被缓存，设置为0)
     */
    @Column(name = "CACHE_INTERVAL")
    private Long cacheInterval;

    /**
     * 关联的page对象
     */
    @ManyToMany(targetEntity = Page.class, fetch = FetchType.LAZY)
    @JoinTable(name = "SWP_PAGE_DATA", joinColumns = @JoinColumn(name = "DATA_ID"), inverseJoinColumns = @JoinColumn(name = "PAGE_ID"),foreignKey =  @ForeignKey(name = "FK_DATA_PAGE"))
    private List<Page> pages;

    /**
     * 数据解析器
     */
    @JoinColumn(name = "DATA_ANALYZER_ID")
    private DataAnalyzer dataAnalyzer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DATAINFERFACE_ID")
    private DataInferface dataInferface;

    @Lob
    @Column(name = "VALUE")
    private String value;

    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 数据源
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "DATA_SOURCE",nullable = false)
    private DataSource dataSource;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public List<Page> getPages() {
        return pages;
    }

    public void setPages(List<Page> pages) {
        this.pages = pages;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }

    public Long getCacheInterval() {
        return cacheInterval;
    }

    public void setCacheInterval(Long cacheInterval) {
        this.cacheInterval = cacheInterval;
    }

    public DataInferface getDataInferface() {
        return dataInferface;
    }

    public void setDataInferface(DataInferface dataInferface) {
        this.dataInferface = dataInferface;
    }

    public DataAnalyzer getDataAnalyzer() {
        return dataAnalyzer;
    }

    public void setDataAnalyzer(DataAnalyzer dataAnalyzer) {
        this.dataAnalyzer = dataAnalyzer;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
