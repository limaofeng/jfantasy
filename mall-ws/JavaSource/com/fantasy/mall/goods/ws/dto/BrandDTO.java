package com.fantasy.mall.goods.ws.dto;

import java.io.Serializable;

/**
 * 品牌
 * 
 * @author 李茂峰
 * @since 2013-9-21 下午5:46:16
 * @version 1.0
 */
public class BrandDTO implements Serializable {

	private Long id;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 商品英文名称
	 */
	private String engname;
	/**
	 * 网址
	 */
	private String url;
	/**
	 * 介绍
	 */
	private String introduction;
	/**
	 * 
	 * 所属国家
	 */
	private String nation;

	/**
	 * 
	 * 拼音
	 */
	
	private String pinyin;



    /**
     * 系列参数
     */
    private BrandSeriesDTO[]  brandseries;


    /**
     * 型号参数
     */
   private SeriesModelDTO[]  seriesmodel;


    public SeriesModelDTO[] getSeriesmodel() {
        return seriesmodel;
    }

    public void setSeriesmodel(SeriesModelDTO[] seriesmodel) {
        this.seriesmodel = seriesmodel;
    }

    public BrandSeriesDTO[] getBrandseries() {
        return brandseries;
    }

    public void setBrandseries(BrandSeriesDTO[] brandseries) {
        this.brandseries = brandseries;
    }

    public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getEngname() {
		return engname;
	}

	public void setEngname(String engname) {
		this.engname = engname;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
}