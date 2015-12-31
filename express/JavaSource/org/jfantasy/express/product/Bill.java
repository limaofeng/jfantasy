package org.jfantasy.express.product;

import java.util.List;

/**
 * 物流单据
 */
public class Bill {

    /**
     * 路线图
     */
    List<Route> routes;

    public List<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(List<Route> routes) {
        this.routes = routes;
    }
}
