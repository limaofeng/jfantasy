package com.fantasy.website.bean;

import com.fantasy.framework.dao.BaseBusEntity;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * 网页生成的地址
 *
 * @author 李茂峰
 * @version 1.0
 * @since 2013-12-25 上午10:09:04
 */
@Entity
@Table(name = "SWP_URL")
public class Url extends BaseBusEntity {

    private static final long serialVersionUID = -6924983319997780554L;

    @Id
    @Column(name = "ID", nullable = false, insertable = true, updatable = false, precision = 22, scale = 0)
    @GeneratedValue(generator = "fantasy-sequence")
    @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
