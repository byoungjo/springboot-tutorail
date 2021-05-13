package com.batchdev.myBatisOrclTest.job;

import com.batchdev.myBatisOrclTest.Util.BeanUtils;
import com.batchdev.myBatisOrclTest.model.Employ;
import com.batchdev.myBatisOrclTest.service.EmployService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class MybatisSimpleBatchTasklet implements Tasklet, StepExecutionListener {

	@Autowired
	public MybatisSimpleBatchTasklet() {
		super();
	}

	private EmployService employService;

	@Override
	public RepeatStatus execute(StepContribution contribution,
			ChunkContext chunkContext) throws Exception {

		final JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

		log.info(">>>>>>>> Hello Spring Cloud Data Flow! ");
		if (jobParameters != null && !jobParameters.isEmpty()) {

			final Set<Entry<String, JobParameter>> parameterEntries = jobParameters.getParameters().entrySet();

			System.out.println(String.format("The following %s Job Parameter(s) is/are present:", parameterEntries.size()));
			log.info(">>>>>>>> Job Parameter {}", parameterEntries.size() );
			for (Entry<String, JobParameter> jobParameterEntry : parameterEntries) {
				System.out.println(String.format(
						"Parameter name: %s; isIdentifying: %s; type: %s; value: %s",
						jobParameterEntry.getKey(),
						jobParameterEntry.getValue().isIdentifying(),
						jobParameterEntry.getValue().getType().toString(),
						jobParameterEntry.getValue().getValue()));

				if (jobParameterEntry.getKey().startsWith("context")) {
					System.out.println(String.format("Adding parameter '%s' to stepExecutionContext.", jobParameterEntry.getKey()));
					stepExecutionContext.put(jobParameterEntry.getKey(), jobParameterEntry.getValue().getValue());
				}
			}

			if (jobParameters.getString("throwError") != null
					&& Boolean.TRUE.toString().equalsIgnoreCase(jobParameters.getString("throwError"))) {

				final AtomicInteger executionCounter;

				if (stepExecutionContext.containsKey("executionCounter")) {
					executionCounter = new AtomicInteger(stepExecutionContext.getInt("executionCounter"));
					executionCounter.incrementAndGet();
				}
				else {
					executionCounter = new AtomicInteger(1);
				}

				if (executionCounter.compareAndSet(3, 0)) {
					stepExecutionContext.putInt("executionCounter", executionCounter.get());
					System.out.println("Counter reset to " + executionCounter.get() +" . Execution will succeed.");
				}
				else {
					stepExecutionContext.putInt("executionCounter", executionCounter.get());
					throw new IllegalStateException("Exception triggered by user.");
				}

			}
		}

		log.info(">>>>>>>>>>>>>>>> Mybatis start ");
		try {
			if (null == employService) {
				employService = (EmployService) BeanUtils.getBean(EmployService.class);
			}

			Employ employ = employService.getEmploy("S0060996");
			log.info(">>>>>>>> employService {} {}", employ.getEno(), employ.getEmpKornNm() );

		}catch (Exception e) {
			log.info(">>>>>>>> err {}", e.getStackTrace());
			e.printStackTrace();
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {

		log.info(">>>>> before step execute: {}", stepExecution.getStepName());
		//log.info(">>>>> before step getExecutionContext: {}", stepExecution.getExecutionContext().);
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// To make the job execution fail, set the step execution to fail
		// and return failed ExitStatus
		// stepExecution.setStatus(BatchStatus.FAILED);
		// return ExitStatus.FAILED;
		log.info(">>>>>>>> afterStep ");
		return ExitStatus.COMPLETED;
//		return ExitStatus.STOPPED;
	}
}