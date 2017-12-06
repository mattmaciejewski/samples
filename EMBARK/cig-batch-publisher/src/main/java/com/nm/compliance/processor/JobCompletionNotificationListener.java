package com.nm.compliance.processor;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

public class JobCompletionNotificationListener implements JobExecutionListener {

	@Override
	public void beforeJob(JobExecution jobExecution) {
		System.out.println("*******************JOB BEGINS***********");

	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		
		System.out.println("*******************JOB ENDS***********");

	}

}
