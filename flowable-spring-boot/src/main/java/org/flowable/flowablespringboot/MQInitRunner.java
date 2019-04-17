package org.flowable.flowablespringboot;


import java.util.List;

import org.flowable.engine.RepositoryService;
import org.flowable.rocketmq.MQPushConsumer;
import org.flowable.rocketmq.ProcessModel;
import org.flowable.rocketmq.SequenceFlowInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MQInitRunner implements CommandLineRunner {
	@Autowired
	private RepositoryService repositoryService;
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		new Thread(){
            public void run() {
            	String[] postname  = {"orgSplit","orgMatch","orgNormalize","codeMerge","dataEntry"};
	        	ProcessModel processModel = new ProcessModel();
	        	for(int i=1;i<postname.length;i++){
	        		List<SequenceFlowInfo> lists = processModel.getSequenceFlow(repositoryService,postname[i]);
	        		for(int j=0;j<lists.size();j++){
	        			consumerInit(postname[i]+j, "192.168.78.132:9876", 
	        					lists.get(j).getPostName(), lists.get(j).getTaskNature());
	        		}
	        	}
            }
        }.start();
	}
	
	@Async
	private void consumerInit(String consumerGroupName,String nameServerAddr,String topicName,String tags){
		MQPushConsumer consumer = new MQPushConsumer();
    	try {
			consumer.init(consumerGroupName, nameServerAddr, 
					topicName, tags);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
