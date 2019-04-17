package org.flowable.rocketmq;

public abstract class MQClient {
	protected String consumerGroupName = "MQconsumeGroup";
	protected String producterGroupName = "MQproductGroup";
	protected String nameServerAddr = "192.168.78.132:9876";
	protected String topicName = "activityTopic";
	public abstract void init() throws Exception;
	public abstract void distroy() throws Exception;
}
