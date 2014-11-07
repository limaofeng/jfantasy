package com.fantasy.remind.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import com.fantasy.remind.bean.Model;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 提醒
 */

@Entity
@Table(name="remind_notice")
public class Notice extends BaseBusEntity {

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = true, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 提醒
     */
    @Column(name="TITLE",length = 500)
    private String title;


    /**
     * 跳转连接
     */
    @Column(name="URL")
    private String url;

    /**
     * 商品所属分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MODEL_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_NOTICE_MODEL"))
    private Model model;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
