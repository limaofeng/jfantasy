package org.jfantasy.pay.bean;

/**
 * 扩展属性
 */
public class ExtProperty {

    enum DataType {
        /**
         * 字符串类型
         */
        string
    }

    /**
     * 字段名(必填)
     */
    private String name;
    /**
     * 字段显示名称(必填)
     */
    private String title;
    /**
     * 数据类型(选填,默认为 string)
     */
    private DataType dataType;
    /**
     * 字段默认值(选填)
     */
    private String defaultValue;
    /**
     * 如何为选项,必须提供选项值
     */
    private String[] optionValues;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String[] getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(String[] optionValues) {
        this.optionValues = optionValues;
    }

    public static class Builder {

        private String name;
        private String title;
        private DataType dataType;
        private String defaultValue;
        private String[] optionValues;

        private Builder(String name, String title) {
            this.name = name;
            this.title = title;
        }

        public static Builder property(String name, String title) {
            return new Builder(name,title);
        }

        public Builder dataType(DataType dataType){
            this.dataType = dataType;
            return this;
        }

        public Builder defaultValue(String defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        public Builder optionValues(String... optionValues) {
            this.optionValues = optionValues;
            return this;
        }

        public ExtProperty build() {
            ExtProperty extProperty = new ExtProperty();
            extProperty.name = name;
            extProperty.title = title;
            return extProperty;
        }

    }

}
