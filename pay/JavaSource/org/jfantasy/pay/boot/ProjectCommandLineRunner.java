package org.jfantasy.pay.boot;

import org.jfantasy.pay.bean.Project;
import org.jfantasy.pay.bean.enums.ProjectType;
import org.jfantasy.pay.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 添加默认的支付项目
 */
@Component
public class ProjectCommandLineRunner implements CommandLineRunner {

    @Autowired
    private ProjectService projectService;

    @Override
    public void run(String... args) throws Exception {
        if (projectService.get(Project.ORDER_PAYMENT) == null) {
            Project project = new Project(Project.ORDER_PAYMENT);
            project.setName("订单支付");
            project.setDescription("业务订单的支付");
            project.setType(ProjectType.order);
            projectService.save(project);
        }
        if (projectService.get(Project.ORDER_REFUND) == null) {
            Project project = new Project(Project.ORDER_REFUND);
            project.setName("订单退款");
            project.setDescription("业务订单的退款");
            project.setType(ProjectType.order);
            projectService.save(project);
        }
        if (projectService.get(Project.CARD_INPOUR) == null) {
            Project project = new Project(Project.CARD_INPOUR);
            project.setName("充值卡");
            project.setDescription("充值卡");
            project.setType(ProjectType.card);
            projectService.save(project);
        }
    }

}
