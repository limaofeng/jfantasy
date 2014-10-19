package com.fantasy.mall.goods.ws.dto;


import javax.xml.bind.annotation.XmlType;

/**
 * 型号
 *
 * @author 李县宜
 * @since 2014-6-18 下午3:26:16
 * @version 1.0
 */
@XmlType
public class SeriesModelDTO {


    private  String  id;

    private String name;

    private String seriesId;

    private String seriesName;


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

    public String getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(String seriesId) {
        this.seriesId = seriesId;
    }


    public String getSeriesName() {
        return seriesName;
    }

    public void setSeriesName(String seriesName) {
        this.seriesName = seriesName;
    }
}
