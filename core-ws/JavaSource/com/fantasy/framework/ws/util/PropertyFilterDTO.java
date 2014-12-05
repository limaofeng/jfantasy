package com.fantasy.framework.ws.util;

public class PropertyFilterDTO {
    /**
     * 对应过滤条件的值
     */
    private String propertyValue;

    /**
     * 过滤条件名称(比较符+该字段类型+字段名称)  例：EQS_name EQ是等于 S是String类型 加上下划线  加上字段名name
     * 支持的比较符如下：EQ:等于     LIKE:模糊查询        LT:小于      GT:大于      LE:小于等于     GE:大于等于   IN:in  NOTIN:not in  NE:不等于  NULL:is null  NOTNULL:not null
     * NOTEMPTY, BETWEEN, SQL;
     * 支持的数据类型如下：S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class),M(BigDecimal.class), E(Enum.class);
     * 示例: List<PropertyFilter> filters = new ArrayList<PropertyFilter>();
     * filters.add(new PropertyFilter("EQS_name", "xxxx"));
     */
    private String filterName;

    /**
     * 默认构造方法
     */
    public PropertyFilterDTO() {
    }

    /**
     * 带参构造方法
     *
     * @param filterName    筛选条件
     * @param propertyValue 值
     */
    public PropertyFilterDTO(String filterName, String propertyValue) {
        this.filterName = filterName;
        this.propertyValue = propertyValue;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    @Override
    public String toString() {
        return "PropertyFilterDTO{" +
                "filterName='" + filterName + '\'' +
                ", propertyValue='" + propertyValue + '\'' +
                '}';
    }
}
