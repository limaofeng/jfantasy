package com.fantasy.activiti;


import junit.framework.Assert;
import org.activiti.engine.*;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;

import java.util.HashMap;
import java.util.Map;

public class VerySimpleLeaveProcess {

//    @Test
    public void testStartProcess() throws Exception {
        //创建流程引擎，使用内存数据库
        ProcessEngine processEngine = ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration().buildProcessEngine();
        //部署流程定义
        RepositoryService repositoryService = processEngine.getRepositoryService();
        String bpmnFileName = "com/fantasy/activiti/SayHelloToLeave.bpmn";
        //repositoryService.createDeployment().addClasspathResource(bpmnFileName).deploy();
        repositoryService.createDeployment().addInputStream("SayHelloToLeave.bpmn",this.getClass().getClassLoader().getResourceAsStream(bpmnFileName)).deploy();
        //验证已部署的流程
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        Assert.assertEquals("SayHelloToLeave",processDefinition.getKey());
        //启动流程并返回流程实例
        RuntimeService runtimeService = processEngine.getRuntimeService();
        Map<String,Object> variables = new HashMap<String, Object>();
        variables.put("applyUser","employee1");
        variables.put("days",3);
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("SayHelloToLeave",variables);
        Assert.assertNotNull(processInstance);
        System.out.println("pid=" + processInstance.getId() + ",pdid=" + processInstance.getProcessDefinitionId());

        TaskService taskService = processEngine.getTaskService();
        Task taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        Assert.assertNotNull(taskOfDeptLeader);
        Assert.assertEquals("领导审批",taskOfDeptLeader.getName());

        taskService.claim(taskOfDeptLeader.getId(),"leaderUser");

        variables.clear();
        variables.put("approved",true);
        taskService.complete(taskOfDeptLeader.getId(),variables);

        taskOfDeptLeader = taskService.createTaskQuery().taskCandidateGroup("deptLeader").singleResult();
        Assert.assertNull(taskOfDeptLeader);

        HistoryService historyService = processEngine.getHistoryService();
        long count = historyService.createHistoricProcessInstanceQuery().finished().count();
        Assert.assertEquals(1,count);
    }

}
