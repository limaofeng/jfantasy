package org.jfantasy.framework.lucene.annotations;

/**
 * compare有多个枚举值
 *
 * @author 李茂峰
 * @version 1.0
 * @功能描述
 * @since 2013-1-23 下午03:11:40
 */
public enum Compare {
    /**
     * 等于（==）。支持String、boolean、int、long、float、double、char。
     */
    IS_EQUALS,
    /**
     * 不等于（!=）。支持String、boolean、int、long、float、double、char。
     */
    NOT_EQUALS,
    /**
     * 大于（>）。支持int、long、float、double。
     */
    GREATER_THAN,
    /**
     * 大于等于（>=）。支持int、long、float、double。
     */
    GREATER_THAN_EQUALS,
    /**
     * 小于（<）。支持int、long、float、double。
     */
    LESS_THAN,
    /**
     * 小于等于（<=）。支持int、long、float、double。
     */
    LESS_THAN_EQUALS,
    /**
     * 为空（==null）。支持Object类型，包括String。这时不需要value参数。
     */
    IS_NULL,
    /**
     * 不为空（!=null）。支持Object类型，包括String。这时不需要value参数。
     */
    NOT_NULL;
}
