package com.fantasy.mall.goods.ws.dto;

import javax.xml.bind.annotation.XmlType;

/**
 * 系列
 *
 * @author 李县宜
 * @since 2014-6-18 下午3:26:16
 * @version 1.0
 */
@XmlType
public class BrandSeriesDTO {

    private String id;

    private  String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
