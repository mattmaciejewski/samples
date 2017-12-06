package com.nm.cig.configuration;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.annotation.AfterStep;
import org.springframework.beans.factory.annotation.Autowired;

//import com.nm.cig.ProducerWrapper;

/**
 * @author Rupinder Singh
 */
public class StepListener implements StepExecutionListener {

	@Autowired
	//public ProducerWrapper producerWrapper;


	@Override
	@AfterStep
	public ExitStatus afterStep(StepExecution stepExecution) {
		System.out.println("Reader Converter Finished!");
		return stepExecution.getExitStatus();
		
	}

	@Override
	
	public void beforeStep(StepExecution stepExecution) {
		// TODO Auto-generated method stub
		
	}


}
