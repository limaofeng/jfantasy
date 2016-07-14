package org.jfantasy.pay.bean;

import java.io.Serializable;

/**
 * 附加服务
 */
public class ExtraService implements Serializable {

    public enum ExtraProject {
        point, growth
    }

    /**
     * 项目名称
     */
    private ExtraProject project;
    /**
     * 内容说明
     */
    private String content;
    /**
     * 约束
     */
    private String constraint;
    /**
     * 具体数值
     */
    private int value;

    public ExtraProject getProject() {
        return project;
    }

    public void setProject(ExtraProject project) {
        this.project = project;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getConstraint() {
        return constraint;
    }

    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

}
