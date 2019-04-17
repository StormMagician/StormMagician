package org.flowable.rocketmq;

import java.util.ArrayList;
import java.util.List;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.ExclusiveGateway;
import org.flowable.bpmn.model.FlowElement;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessModel {
	
	public List<SequenceFlowInfo> getSequenceFlow(RepositoryService repositoryService,String currentPost) {
		
		ProcessDefinition pd = repositoryService.createProcessDefinitionQuery()
				.processDefinitionKey("myProcess").latestVersion().singleResult();
		
    	BpmnModel bpmnModel = repositoryService.getBpmnModel(pd.getId());

		FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(currentPost);
		List<SequenceFlow> sequenceFlows = flowNode.getIncomingFlows();
		List<SequenceFlowInfo> sequenceFlowInfoList = new ArrayList<SequenceFlowInfo> ();
		
		String lastPost = null;
		for (SequenceFlow sequenceFlow : sequenceFlows) {
			lastPost = getLastPost(bpmnModel,sequenceFlow);
			
			SequenceFlowInfo sInfo = new SequenceFlowInfo();
			sInfo.setPostName(lastPost);
			sInfo.setTaskNature(sequenceFlow.getConditionExpression());
			sequenceFlowInfoList.add(sInfo);
		}
		
		return sequenceFlowInfoList;
	}

	private String getLastPost(BpmnModel bpmnModel, SequenceFlow sequenceFlow) {
		String lastPost = sequenceFlow.getSourceRef();
		FlowElement flowElement  = bpmnModel.getFlowElement(lastPost);
		if (flowElement instanceof ExclusiveGateway) {
			FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement(lastPost);
			lastPost = getLastPost(bpmnModel, flowNode.getIncomingFlows().get(0));
		}
		
		return lastPost;
	}
}
