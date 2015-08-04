package com.fantasy.framework.dao;

import com.fantasy.framework.util.common.ObjectUtil;
import com.fantasy.framework.util.common.StringUtil;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @apiDefine paramPager
 * @apiParam {int} currentPage  要显示的页码
 * @apiParam {int} pageSize  每页显示数据条数
 * @apiParam {String} orderBy  排序字段
 * @apiParam {String} order  排序方向
 * @apiVersion 3.3.6
 */

/**
 * @apiDefine returnPager
 * @apiSuccess {int} currentPage  当前页码
 * @apiSuccess {int} pageSize  每页显示的数据条数
 * @apiSuccess {int} totalCount  最大数据条数
 * @apiSuccess {int} totalPage  总页数
 * @apiSuccess {String} orderBy  排序字段
 * @apiSuccess {String} order  排序方向
 * @apiSuccess {List} pageItems 当页数据集合
 * @apiVersion 3.3.6
 */
public class Pager<T> implements Serializable {

    private static final long serialVersionUID = -2343309063338998483L;
    /**
     * 最大数据条数
     */
    private int totalCount = 0;
    /**
     * 每页显示的数据条数
     */
    private int pageSize = 0;
    /**
     * 总页数
     */
    private int totalPage = 1;
    /**
     * 当前页码
     */
    private int currentPage = 1;
    /**
     * 开始数据索引
     */
    private int first = 0;
    /**
     * 排序字段
     */
    private String orderBy;
    private Order[] orders = new Order[]{Order.asc};

    private List<T> pageItems;

    public Pager() {
        this.pageSize = 15;
    }

    public Pager(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取总页码
     *
     * @return 总页数
     */
    public int getTotalPage() {
        return totalPage;
    }

    /**
     * 设置总页数
     *
     * @param totalPage 总页数
     */
    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage == 0 ? 1 : totalPage;
    }

    /**
     * 获取每页显示的条数
     *
     * @return 每页显示条数
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置显示的页码 注意是页码
     *
     * @param currentPage 当前页码
     */
    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * 设置总数据条数
     *
     * @param totalCount 总数据条数
     */
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        int totalPages = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
        this.setTotalPage(totalPages);
        if (currentPage >= totalPages) {
            setCurrentPage(totalPages);
            setFirst((totalPages - 1) * pageSize);
        } else if (currentPage <= 0) {
            setCurrentPage(1);
            setFirst(first);
        } else {
            setFirst((currentPage - 1) * pageSize);
        }
    }

    /**
     * 返回翻页开始位置
     *
     * @param first 数据开始位置
     */
    public void setFirst(int first) {
        this.first = first;
    }

    public int getFirst() {
        return first;
    }

    /**
     * 设置每页显示数据的条数
     *
     * @param pageSize 每页显示数据条数
     */
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 获取当前显示的页码
     *
     * @return currentPage
     */
    public int getCurrentPage() {
        return currentPage <= 0 ? 1 : currentPage;
    }

    /**
     * 获取数据总条数
     *
     * @return totalCount
     */
    public int getTotalCount() {
        return totalCount;
    }

    public List<T> getPageItems() {
        return pageItems;
    }

    public void setPageItems(List<T> pageItems) {
        this.pageItems = pageItems;
    }

    public Pager<T> orderBy(String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order... order) {
        this.orders = order;
    }

    public void setOrder(String order) {
        String[] orderBys = StringUtil.tokenizeToStringArray(this.orderBy, ",;");
        if (orderBys.length == 1) {
            if ((!StringUtils.equals("desc", order)) && (!StringUtils.equals("asc", order))) {
                throw new IllegalArgumentException("排序方向" + order + "不是合法值");
            }
            this.orders = new Order[]{Order.valueOf(Order.class, order)};
        } else {
            List<Order> list = new ArrayList<Order>();
            for (String ordersTemp : StringUtil.tokenizeToStringArray(order, ",;")) {
                if ((!StringUtils.equals("desc", ordersTemp)) && (!StringUtils.equals("asc", ordersTemp))) {
                    throw new IllegalArgumentException("排序方向" + ordersTemp + "不是合法值");
                }
                list.add(Order.valueOf(Order.class, ordersTemp));
            }
            this.orders = list.toArray(new Order[list.size()]);
        }
    }

    public void setOrder(Order... order) {
        this.setOrders(order);
    }

    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(this.orderBy)) && (ObjectUtil.isNotNull(orders));
    }

    public enum Order {
        desc, asc
    }

    @Override
    public String toString() {
        return "Pager [totalCount=" + totalCount + ", first=" + first + ", pageSize=" + pageSize + ", totalPage=" + totalPage + ", currentPage=" + currentPage + ", orderBy=" + orderBy + ", order=" + orders + "]";
    }

}