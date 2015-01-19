package com.fantasy.mall.goods.ws.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.io.Serializable;

/**
 * 商品分类
 * 
 * @功能描述
 * @author 李茂峰
 * @since 2013-9-21 下午5:46:48
 * @version 1.0
 */
public class GoodsCategoryDTO implements Serializable {

	private static final long serialVersionUID = 5493195569858322364L;

	/**
	 * 分类ID
	 */
	private Long id; 
	
	/**
	 * 分类名称
	 */
	private String name;
	/**
	 * 分类标识
	 */
	private String sign;
	/**
	 * 页面关键词
	 */
	private String metaKeywords;// 页面关键词
	
	/**
	 * 页面描述
	 */
	private String metaDescription;// 页面描述
	/**
	 * 排序
	 */
	private Integer sort;// 排序
	/**
	 * 树路径
	 */
	private String path;// 树路径
	/**
	 * 层级
	 */
	private Integer layer;
	/**
	 * 上级分类
	 */
	private GoodsCategoryDTO parent;
	/**
	 * 商品品牌
	 */
	private BrandDTO[] brands;
	
	private GoodsCategoryDTO[] children;

    private GoodsParameterDTO[] goodsParameterDTOs;

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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getLayer() {
		return layer;
	}

	public void setLayer(Integer layer) {
		this.layer = layer;
	}

	@JsonProperty("parentId")
	@JsonSerialize(using = CategoryParentSerialize.class)
	public GoodsCategoryDTO getParent() {
		return parent;
	}

	public void setParent(GoodsCategoryDTO parent) {
		this.parent = parent;
	}

	public BrandDTO[] getBrands() {
		return brands;
	}

	public void setBrands(BrandDTO[] brands) {
		this.brands = brands;
	}

	public String getMetaKeywords() {
		return metaKeywords;
	}

	public void setMetaKeywords(String metaKeywords) {
		this.metaKeywords = metaKeywords;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getMetaDescription() {
		return metaDescription;
	}

	public void setMetaDescription(String metaDescription) {
		this.metaDescription = metaDescription;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public static class CategoryParentSerialize extends JsonSerializer<GoodsCategoryDTO> {
		@Override
		public void serialize(GoodsCategoryDTO category, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException {
			jgen.writeString(category.getId() != null ? category.getId().toString() : "");
		}
	}

	public GoodsCategoryDTO[] getChildren() {
		return children;
	}

	public void setChildren(GoodsCategoryDTO[] children) {
		this.children = children;
	}

    public GoodsParameterDTO[] getGoodsParameterDTOs() {
        return goodsParameterDTOs;
    }

    public void setGoodsParameterDTOs(GoodsParameterDTO[] goodsParameterDTOs) {
        this.goodsParameterDTOs = goodsParameterDTOs;
    }
}