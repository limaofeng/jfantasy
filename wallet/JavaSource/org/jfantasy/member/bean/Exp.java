package org.jfantasy.member.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

import javax.persistence.*;

@Entity
@Table(name = "MEM_EXP")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Exp extends BaseBusEntity {

    @Id
    @Column(name = "ID", updatable = false)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 经验值
     */
    private Long value;
    /**
     * 记录
     */
    @Column(name = "NOTES")
    private String notes;
    /**
     * 钱包
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "WALLET_ID", updatable = false, foreignKey = @ForeignKey(name = "FK_BILL_WALLET"))
    private Wallet wallet;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

}
