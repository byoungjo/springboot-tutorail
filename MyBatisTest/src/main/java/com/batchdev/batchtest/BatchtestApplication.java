package com.batchdev.batchtest;

import ch.qos.logback.core.db.ConnectionSource;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@Slf4j
@MapperScan(basePackageClasses = BatchtestApplication.class)
@SpringBootApplication
public class BatchtestApplication {
	public static void main(String[] args) {
		SpringApplication.run(BatchtestApplication.class, args);
	}

}
