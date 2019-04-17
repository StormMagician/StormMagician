package org.flowable.rocketmq;

import java.util.Date;

import org.apache.rocketmq.client.consumer.DefaultMQPullConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.junit.Test;

public class MQTest {
	@Test
	public void product(){
		MQProducter producter = new MQProducter();
		try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "录入");
			//producter.sendMessage(taskProperties);
			producter.sendMessage(taskProperties, "activityTopic", "tag1");
			producter.distroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void product1(){
		MQProducter producter = new MQProducter();
		try {
			producter.init();
			TaskProperties taskProperties = new TaskProperties("123", 
					"12301", new Date().toString(), "期刊", "0001", "录入");
			//producter.sendMessage(taskProperties);
			producter.sendMessage(taskProperties, "activityTopic", "tag2");
			producter.distroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Test
	public void pushConsume(){
		//DefaultMQPullConsumer
		MQPushConsumer consumer = new MQPushConsumer();
		try {
			consumer.init();
			Thread.sleep(200*1000);
			consumer.distroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void pullConsume(){
		MQPullConsumer consumer = new MQPullConsumer();
		try {
			consumer.init();
			consumer.pullMessage();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
