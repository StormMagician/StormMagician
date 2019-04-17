package org.flowable.rocketmq;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import com.alibaba.fastjson.JSON;


public class MQProducter extends MQClient{
	private Logger logger = Logger.getLogger(getClass());
	private DefaultMQProducer producter;
	
	@Override
	public void init() throws Exception{
		init(producterGroupName,nameServerAddr);
	}

	public void init(String groupNameOrAddr) throws Exception{
		//一个参数，可能是生产者组名，可能是namesrv地址
		if(groupNameOrAddr != null) {
			String pattern = "(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}";
	        Pattern r = Pattern.compile(pattern);
	        Matcher m = r.matcher(groupNameOrAddr.substring(0, groupNameOrAddr.indexOf(":")));
	        if(m.matches()) this.nameServerAddr = groupNameOrAddr;
	        else this.producterGroupName = groupNameOrAddr;
		}
		init(producterGroupName,nameServerAddr);
	}
	
	public void init(String producterGroupName,String nameServerAddr) throws Exception{
		logger.info("开始启动消息生产者服务。。。");
		producter = new DefaultMQProducer(producterGroupName);
		producter.setNamesrvAddr(nameServerAddr);
		producter.start();
		logger.info("消息生产者服务启动成功。");
	}
	
	@Override
	public void distroy(){
		logger.info("开始关闭消息生产者服务。。。");
		producter.shutdown();
		logger.info("消息生产者服务已关闭。");
	}
	public DefaultMQProducer getProducter() {
		return producter;
	}
	public SendResult sendMessage(TaskProperties taskProperties){
		Message msg 		= null;
		SendResult result 	= null;
		try {
			msg = new Message(topicName,null,
					JSON.toJSONString(taskProperties).getBytes());
			
			result = this.getProducter().send(msg);
			if(result.getSendStatus()!=SendStatus.SEND_OK)
				throw new Exception("消息发送失败！");
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (MQBrokerException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	public SendResult sendMessage(TaskProperties taskProperties,String topicName,String tags){
		Message msg 		= null;
		SendResult result 	= null;
		try {
			msg = new Message(topicName,tags,
					JSON.toJSONString(taskProperties).getBytes());
			//岗位名称+任务序号作为消息的唯一标识
			String keys = taskProperties.getPostname()+taskProperties.getTaskID();
			msg.setKeys(keys);
			result = this.getProducter().send(msg);
			if(result.getSendStatus()!=SendStatus.SEND_OK)
				throw new Exception("消息发送失败！");
			/*this.getProducter().send(msg, new SendCallback() {
				@Override
				public void onSuccess(SendResult sendResult) {
					logger.info("消息发送成功"+sendResult);
				}
				
				@Override
				public void onException(Throwable e) {
					logger.info(e.toString());
				}
			});*/
		} catch (MQClientException e) {
			e.printStackTrace();
		} catch (RemotingException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(result);
		return result;
	}
	
	
	
}
