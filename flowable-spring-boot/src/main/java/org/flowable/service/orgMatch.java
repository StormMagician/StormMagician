package org.flowable.service;

import java.util.Random;

import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;

public class orgMatch implements JavaDelegate {

	public void execute(DelegateExecution execution) {
		Random random = new Random();
		boolean isMatch = random.nextBoolean();
		System.out.println(isMatch);
		execution.setVariableLocal("isMatch", isMatch);
	}

}
