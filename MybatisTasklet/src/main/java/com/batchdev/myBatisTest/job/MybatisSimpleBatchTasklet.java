package com.batchdev.myBatisTest.job;

import com.batchdev.myBatisTest.Util.BeanUtils;
import com.batchdev.myBatisTest.model.Bill;
import com.batchdev.myBatisTest.service.BillService;
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

@Slf4j
@Component
@StepScope
public class MybatisSimpleBatchTasklet implements Tasklet, StepExecutionListener {
	@Value("#{jobParameters[date]}")
	private String date;

	@Value("#{jobParameters[rerun]}")
	private String rerun;

	private BillService billService;

	@Autowired
	public MybatisSimpleBatchTasklet() {
		super();
		log.info(">>>>>>>> MybatisSimpleBatchTasklet() 생성");
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		final JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();
		final ExecutionContext stepExecutionContext = chunkContext.getStepContext().getStepExecution().getExecutionContext();

		log.info(">>>>>>>> jobParameters[date] {}", date);
		log.info(">>>>>>>> jobParameters[rerun] {}", rerun);
		log.info(">>>>>>>>>>>>>>>> Mybatis start ");
		try {
//			billService = (BillService) BeanUtils.getBean("billService");
			if (null == billService) {
				billService = (BillService) BeanUtils.getBean(BillService.class);
			}

			Bill bill = billService.getBill( 10l );
			log.info(">>>>>>>> billService.getBill {} {}", bill.getFirstName(), bill.getLastName() );

			List<Bill> billList = billService.getBillList();
			for (Bill billVo: billList) {
				log.info(">>>>>>>> BillList {} {}", billVo.getFirstName(), billVo.getLastName() );
			}

		}catch (Exception e) {
			log.info(">>>>>>>> err {}", e.getStackTrace());
			e.printStackTrace();
		}
		return RepeatStatus.FINISHED;
	}

	@Override
	public void beforeStep(StepExecution stepExecution) {
//		JobParameters parameters = stepExecution.getJobExecution().getJobParameters();

		log.info(">>>>> before step execute: {}", stepExecution.getStepName());
//		log.info(">>>>> before step getExecutionContext: {}", stepExecution.getExecutionContext());
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		// To make the job execution fail, set the step execution to fail
		// and return failed ExitStatus
		// stepExecution.setStatus(BatchStatus.FAILED);
		// return ExitStatus.FAILED;
		log.info(">>>>>>>> afterStep {}",stepExecution.getStepName());
		return ExitStatus.COMPLETED;
//		return ExitStatus.STOPPED;
	}
}