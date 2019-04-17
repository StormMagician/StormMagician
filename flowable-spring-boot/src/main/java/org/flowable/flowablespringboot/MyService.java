package org.flowable.flowablespringboot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.rocketmq.TaskProperties;
import org.flowable.task.api.Task;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

@Service
@Transactional
public class MyService {

	@Autowired
	private ProcessEngine engine;
	
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private RepositoryService repositoryService;
    
    @Autowired
    private TaskService taskService;

    public void startProcess(String startMessage) {

        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("strSN", "123ABC");
        ProcessInstance pi = runtimeService.startProcessInstanceByMessage(startMessage, variables);
        
        System.out.println("pid:" + pi.getId());
    }
    
    public void tSub(String taskId) {
    	
    	taskService.complete(taskId);
//    	System.out.println(taskService.createTaskQuery().taskId(taskId).singleResult());
    }

    public List<Task> getTasks(String assignee) {
//        return taskService.createTaskQuery().taskAssignee(assignee).list();
        return taskService.createTaskQuery().list();
    }

    public void setAssignee(TaskProperties taskProperties) {
    	taskService.setAssignee(taskProperties.getTaskID(), taskProperties.getArticleID());
    }
    
}
