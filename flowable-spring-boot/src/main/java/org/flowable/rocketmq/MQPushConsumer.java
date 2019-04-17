package org.flowable.rocketmq;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;

import com.alibaba.fastjson.JSON;


public class MQPushConsumer extends MQClient{
	private Map<String, String> messageMap = new HashMap<>();
	private Logger logger = Logger.getLogger(getClass());
	private DefaultMQPushConsumer consumer;

	@Override
	public void init() throws Exception{
		init(consumerGroupName,nameServerAddr,topicName,null);
	}
	
	public void init(String consumerGroupName) throws Exception{
		init(consumerGroupName,nameServerAddr,topicName,null);
	}
	
	public void init(String consumerGroupName,String nameServerAddr) throws Exception{
		init(consumerGroupName,nameServerAddr,topicName,null);
	}
	
	public void init(String consumerGroupName,String nameServerAddr,String topicName) throws Exception{
		init(consumerGroupName,nameServerAddr,topicName,null);
	}
	
	
	public void init(String consumerGroupName,String nameServerAddr,String topicName,String tags) throws Exception{
		logger.info("开始启动消息消费者 服务。。。");
		consumer = new DefaultMQPushConsumer(consumerGroupName);
		consumer.setNamesrvAddr(nameServerAddr);
		consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
		consumer.subscribe(topicName, tags==null?"*":tags);
		//consumer.registerMessageListener(new MessageListener());
		consumer.registerMessageListener((List<MessageExt> msgs, ConsumeConcurrentlyContext context)->{
			if(msgs!=null){
				for (MessageExt messageExt : msgs) {
					try {
						String key = messageExt.getKeys();
						if(messageMap.containsKey(key))
							return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
						String msg =  new String(messageExt.getBody());
						messageMap.put(key, msg);
						TaskProperties taskProperties = JSON.parseObject(msg, TaskProperties.class);
						logger.info(consumerGroupName+"监听到"+topicName+tags+"的消息："+taskProperties.toString());
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		});
		consumer.start();
		logger.info("消息消费者服务启动成功");
	}
	
	@Override
	public void distroy(){
		logger.info("开始关闭消息消费者 服务。。。");
		consumer.shutdown();
		logger.info("消息消费者服务已关闭");
	}
	public DefaultMQPushConsumer getConsumer() {
		return consumer;
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
