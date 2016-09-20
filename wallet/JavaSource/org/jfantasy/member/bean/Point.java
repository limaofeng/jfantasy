package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

/**
 * 积分流水
 */
@Entity(name = "MemPoint")
@Table(name = "MEM_POINTS")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler", "pointDetails"})
public class Point extends BaseBusEntity {

    private static final long serialVersionUID = -2242726270536854841L;

    public enum Type {
        /**
         * 收入
         */
        plus("收入"),
        /**
         * 支出
         */
        minus("支出");

        private String value;

        Type(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum Status {
        /**
         * 锁定
         */
        locked,
        /**
         * 关闭
         */
        close,
        /**
         * 完成
         */
        finished;
    }

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "test:id")
    @TableGenerator(name = "test:id", table = "sys_sequence",pkColumnName = "gen_name",valueColumnName = "gen_value")
    private Long id;

    /**
     * 备注
     */
    @Column(name = "DESCRIPTION")
    private String description;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "TYPE", length = 20, nullable = false)
    private Type type;
    /**
     * 状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", length = 20, nullable = false)
    private Status status;
    /**
     * 分数
     */
    @Column(name = "POINT", length = 10, scale = 2, nullable = false)
    private Integer point;
    /**
     * 积分详情
     */
    @ManyToOne
    @JoinColumn(name = "WALLET_ID", nullable = false)
    private Wallet wallet;
    /**
     * 标题
     */
    @Column(name = "NOTES", nullable = false)
    private String notes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
