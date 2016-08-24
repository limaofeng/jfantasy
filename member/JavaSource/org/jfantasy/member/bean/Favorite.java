package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.spring.validation.RESTful;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 * 收藏表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2016-07-31 下午4:52:07
 */
@ApiModel("用户收藏")
@Entity
@Table(name = "MEM_FAVORITE", uniqueConstraints = {@UniqueConstraint(name = "UK_MEM_FAVORITE", columnNames = {"TYPE", "TARGET_TYPE", "TARGET_ID", "MEMBER_ID"})})
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "forComment"})
public class Favorite extends BaseBusEntity {

    @Null(groups = RESTful.POST.class)
    @Id
    @Column(name = "ID", nullable = false, updatable = false, precision = 22)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    @NotNull(groups = RESTful.POST.class)
    @ApiModelProperty("类型")
    @Column(name = "TYPE")
    private String type;
    @NotNull(groups = RESTful.POST.class)
    @ApiModelProperty("目标类型")
    @Column(name = "TARGET_TYPE", updatable = false, nullable = false)
    private String targetType;
    @NotNull(groups = RESTful.POST.class)
    @ApiModelProperty("目标ID")
    @Column(name = "TARGET_ID", updatable = false, nullable = false)
    private String targetId;
    @ApiModelProperty(hidden = true)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.REFRESH})
    @JoinColumn(name = "MEMBER_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_SHIP_ADDRESS_MEMBER"))
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Favorite)) return false;

        Favorite favorite = (Favorite) o;

        return new EqualsBuilder()
                .append(id, favorite.id)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(id)
                .toHashCode();
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    @Transient
    @NotNull(groups = RESTful.POST.class)
    public Long getMemberId() {
        return member != null ? member.getId() : null;
    }

    public void setMemberId(Long memberId) {
        this.member = new Member(memberId);
    }

}
