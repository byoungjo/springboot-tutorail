/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.batchdev.myBatisTest.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by glennrenfro on 3/7/16.
 */
@Slf4j
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
@EnableBatchProcessing
@EnableTask
public class JobConfiguration {
	@Autowired
	DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	@Value("${sample.jobName:}")
	private String jobName;

	private final MybatisSimpleBatchTasklet mybatisSimpleBatchTasklet;

	@Bean
	public Job job() {
		final String jobNameToUse;


		if (StringUtils.hasText(this.jobName)) {
			jobNameToUse = this.jobName;
		}
		else {
			Random rand = new Random();
			jobNameToUse = "job" + rand.nextInt();
		}

		try {
			Connection connection = dataSource.getConnection();
			log.info(">>>>> DBCP: " + dataSource.getClass()); // 사용하는 DBCP 타입 확인
			log.info(">>>>> Url: " + connection.getMetaData().getURL());
			log.info(">>>>> UserName: " + connection.getMetaData().getUserName());
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}

		log.info(">>>>> Setting up new Batch Job named: {}",  jobNameToUse);
//		return jobBuilderFactory.get(jobNameToUse)
//				.start(stepBuilderFactory.get("step1").tasklet(new MybatisSimpleBatchTasklet()).build())
//				.build();

		return jobBuilderFactory.get(jobNameToUse)
				.start(simpleStep1())
				.build();
	}

	public Step simpleStep1() {
		log.info("simpleStep1");
		return stepBuilderFactory.get("simpleStep1")
				.tasklet(mybatisSimpleBatchTasklet)
				.build();
	}
}
