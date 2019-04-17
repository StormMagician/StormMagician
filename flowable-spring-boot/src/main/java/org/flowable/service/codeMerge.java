package org.flowable.service;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class codeMerge implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		System.out.println("进入codeMerge");
	}

}
