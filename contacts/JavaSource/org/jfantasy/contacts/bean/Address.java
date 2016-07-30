package org.jfantasy.contacts.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

/**
 * 地址表
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-3-15 上午11:11:55
 */
@Entity
@Table(name = "CONTACTS_ADDRESS")
@JsonIgnoreProperties({"hibernate_lazy_initializer", "handler"})
public class Address extends BaseBusEntity {

    private static final long serialVersionUID = -1954479745916107049L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;
    /**
     * 1.住宅<br/>
     * 2.工作<br/>
     * 3.其他(可自定义)
     */
    @Column(name = "TYPE", length = 10)
    private String type;
    /**
     * 国家
     */
    @Column(name = "COUNTRY", length = 20)
    private String country;
    /**
     * 省份
     */
    @Column(name = "PROVINCE", length = 20)
    private String province;
    /**
     * 城市
     */
    @Column(name = "CITY", length = 20)
    private String city;
    /**
     * 街道
     */
    @Column(name = "STREET", length = 200)
    private String street;
    /**
     * 邮政编码
     */
    @Column(name = "ZIP_CODE", length = 200)
    private String zipCode;
    /**
     * 联系人
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = {javax.persistence.CascadeType.REFRESH})
    @JoinColumn(name = "LINKMAN_ID")
    private Linkman linkman;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Linkman getLinkman() {
        return linkman;
    }

    public void setLinkman(Linkman linkman) {
        this.linkman = linkman;
    }

}
