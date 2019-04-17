package org.flowable.flowablespringboot;

import java.util.List;

import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.FlowNode;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.SequenceFlow;
import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngines;
import org.flowable.engine.RepositoryService;
import org.flowable.rocketmq.MQPushConsumer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@SpringBootApplication
@ComponentScans(value= {@ComponentScan(value="org.flowable.flowablespringboot"), 
		@ComponentScan(value="org.flowable.rocketmq")})
public class FlowableSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableSpringBootApplication.class, args);
	}
	
//	@Bean
//	public CommandLineRunner init(final MyService myService, final MQPushConsumer consumer) {
//
//	    return new CommandLineRunner() {
//	    	public void run(String... strings) throws Exception {
//	        	myService.createDemoUsers();
//	    		myService.getBpmnModel();
//	    		consumer.init();
	        	
//	        	MQProducter producter = new MQProducter();
//	        	producter.init();
	        	
//	        	ProcessEngine engine = ProcessEngines.getDefaultProcessEngine();
//	        	
//	        	RepositoryService repositoryService = engine.getRepositoryService();
//	        	
//	        	BpmnModel bpmnModel = repositoryService.getBpmnModel("myProcess:1:0cfb02e0-5fef-11e9-bb67-64006a5d1d9f");
//	    		Process process = bpmnModel.getProcesses().get(0);
//	    		Collection<FlowElement> flowElements = process.getFlowElements();
//	    		for (FlowElement flowElement : flowElements) {
//	    			System.out.println(flowElement.getId());
//	    			System.out.println(flowElement.getName());
//	    			System.out.println(flowElement.getDocumentation());
//	    		}
//	    		FlowNode flowNode = (FlowNode) bpmnModel.getFlowElement("codeMerge");
//	    		List<SequenceFlow> list = flowNode.getOutgoingFlows();
//	    		List<SequenceFlow> list2 = flowNode.getIncomingFlows();
//	    		
//	    		for (SequenceFlow sequenceFlow : list) {
//					System.out.println(sequenceFlow.getId());
//					System.out.println(sequenceFlow.getSourceRef());
//					System.out.println(sequenceFlow.getTargetRef());
//				}
//	    		
//	    		for (SequenceFlow sequenceFlow2 : list2) {
//					System.err.println(sequenceFlow2.getId());
//					System.err.println(sequenceFlow2.getSourceRef());
//					System.err.println(sequenceFlow2.getTargetRef());
//					System.err.println(sequenceFlow2.getConditionExpression());
//				}
//	        }
//	    };
//	}

}
