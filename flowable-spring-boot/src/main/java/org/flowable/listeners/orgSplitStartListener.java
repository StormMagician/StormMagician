package org.flowable.listeners;

import java.util.Date;

import org.flowable.common.engine.api.delegate.Expression;
import org.flowable.engine.delegate.TaskListener;
import org.flowable.rocketmq.MQProducter;
import org.flowable.rocketmq.TaskProperties;
import org.flowable.task.service.delegate.DelegateTask;

public class orgSplitStartListener implements TaskListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9017842759311016891L;
	private Expression fileSN;

	public void setFileSN(Expression fileSN) {
		this.fileSN = fileSN;
	}
	
	
	
	@Override
	public synchronized void notify(DelegateTask delegateTask) {
		String fileSNStr = fileSN.getValue(delegateTask).toString();
		TaskProperties taskProperties = new TaskProperties();
		taskProperties.setTaskID(delegateTask.getId());
		taskProperties.setArticleID(fileSNStr);
		MQProducter mqProducter = new MQProducter();
		try {
			mqProducter.init();
			mqProducter.sendMessage(taskProperties);
			mqProducter.distroy();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(delegateTask.getId());
	}

}
