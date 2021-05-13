package com.batchdev.batchtest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@Slf4j
@EnableTask
@SpringBootApplication
public class BatchtestApplication {

	public static void main(String[] args) {
		SpringApplication.run(BatchtestApplication.class, args);
	}

}
