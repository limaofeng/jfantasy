package org.jfantasy.pay.product;

/**
 * 发布状态
 */
public enum DeployStatus {

    Develop("开发模式"), Production("生产模式");

    private String name;

    DeployStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
