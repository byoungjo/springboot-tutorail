package com.batchdev.batchtest.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
@StepScope
public class JobParameterTasklet  implements Tasklet, StepExecutionListener {
    @Value("#{jobParameters[date]}")
    private String date;

    @Value("#{jobParameters[rerun]}")
    private String rerun;

    @Autowired
    public JobParameterTasklet() {
		super();
        log.info(">>>>>>>> JobParameterTasklet() 생성");
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        final JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
        final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();
        log.info(">>>>>>>> jobParameters[date] {}", date);
        log.info(">>>>>>>> jobParameters[rerun] {}", rerun);

        if (jobParameters != null && !jobParameters.isEmpty()) {

            final Set<Map.Entry<String, JobParameter>> parameterEntries = jobParameters.getParameters().entrySet();

            System.out.println(String.format("The following %s Job Parameter(s) is/are present:", parameterEntries.size()));
            log.info(">>>>>>>> Job Parameter {}", parameterEntries.size() );
            for (Map.Entry<String, JobParameter> jobParameterEntry : parameterEntries) {
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
        return RepeatStatus.FINISHED;
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters parameters = stepExecution.getJobExecution().getJobParameters();
        if (parameters != null && !parameters.isEmpty()) {

            final Set<Map.Entry<String, JobParameter>> parameterEntries = parameters.getParameters().entrySet();
            for (Map.Entry<String, JobParameter> jobParameterEntry : parameterEntries) {
                System.out.println(String.format(
                        "beforeStep Parameter name: %s; isIdentifying: %s; type: %s; value: %s",
                        jobParameterEntry.getKey(),
                        jobParameterEntry.getValue().isIdentifying(),
                        jobParameterEntry.getValue().getType().toString(),
                        jobParameterEntry.getValue().getValue()));
            }

        }
        log.info(">>>>> before step execute: {}", stepExecution.getStepName());
        //log.info(">>>>> before step getExecutionContext: {}", stepExecution.getExecutionContext().);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info(">>>>>>>> afterStep {}",stepExecution.getStepName());
        return ExitStatus.COMPLETED;
//		return ExitStatus.STOPPED;
    }
}
