package org.flowable.rocketmq;

import java.util.List;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;

public class MessageListener implements MessageListenerConcurrently {
	private Logger logger = Logger.getLogger(getClass());
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		if(msgs!=null){
			for (MessageExt messageExt : msgs) {
				try {
					String msg =  new String(messageExt.getBody());
					TaskProperties taskProperties = JsonToBean(msg);
					logger.info("监听到消息："+taskProperties.toString());
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}
	
	private TaskProperties JsonToBean(String msg){
		String[] properties = msg.replaceAll("\\{", "").replaceAll("\\}", "")
				.replaceAll("\"", "").split(",");
		TaskProperties taskProperties = new TaskProperties();
		for (String string : properties) {
			int index = string.indexOf(":");
			String begin = string.substring(0, index);
			String end = string.substring(index+1, string.length());
			if("articleID".equals(begin)) taskProperties.setArticleID(end);
			if("id".equals(begin)) taskProperties.setId(end);
			if("line".equals(begin)) taskProperties.setLine(end);
			if("postname".equals(begin)) taskProperties.setPostname(end);
			if("reachTime".equals(begin)) taskProperties.setReachTime(end);
			if("taskID".equals(begin)) taskProperties.setTaskID(end);
		}
		return taskProperties;
	}

}
