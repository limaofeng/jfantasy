package org.jfantasy.message.bean;

/**
 * 任务
 */
public class Task {

    /**
     * 项目
     */
    private Project project;

    /**
     * 标题
     */
    private String title;
    /**
     * 进度
     */
    private Integer percentage;

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
