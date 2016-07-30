package org.jfantasy.common.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 应用场景，当通过一个订单去查看订单详情对应的商品时，如果该商品被编辑过。显示商品该商品购买时的快照信息
 * 匹配规则，下单时间往后，第一个快照
 * 存快照规则。如果有订单关联商品。如果商品被修改。修改前存储快照
 */
@ApiModel(value = "快照信息", description = "用于描述商品及文章的历史信息")
@Entity
@Table(name = "SNAPSHOT")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = {"hibernate_lazy_initializer", "handler", "creator", "createTime", "modifier", "modifyTime", "parent", "children"})
public class Snapshot extends BaseBusEntity {

    @Id
    @Column(name = "ID", insertable = true, updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    @Column(name = "TARGET_ID", nullable = false)
    private Long targetId;

    @Column(name = "TARGET_TYPE", length = 50, nullable = false)
    private String targetType;

    @Lob
    @Column(name = "CONTENT", nullable = false)
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
