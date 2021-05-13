package com.batchdev.batchtest.job;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Random;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
@EnableBatchProcessing
@EnableTask
public class SimpleJobConfiguration {

    @Autowired
    private JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    @Autowired
    private StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음

    @Value("${sample.jobName:}")
    private String jobName;

    private final JobParameterTasklet jobParameterTasklet;

    @Bean
    public Job simpleJob(DataSource dataSource) throws SQLException {
        log.info(">>>>> This is simpleJob");

        final String jobNameToUse;

        if (StringUtils.hasText(this.jobName)) {
            jobNameToUse = this.jobName;
        }
        else {
            Random rand = new Random();
            jobNameToUse = "job" + rand.nextInt();
        }

        Connection connection = dataSource.getConnection();
        log.info("DBCP: " + dataSource.getClass()); // 사용하는 DBCP 타입 확인
        log.info("Url: " + connection.getMetaData().getURL());
        log.info("UserName: " + connection.getMetaData().getUserName());
        log.info("jobNameToUse: " + jobNameToUse);

        return jobBuilderFactory.get(jobNameToUse)
//                .start(simpleStep1(null))
                .start(simpleStep1())
                .build();
    }

//    @Bean
//    @JobScope
//    public Step simpleStep1(@Value("#{jobParameters[date]}") String date)  {
    public Step simpleStep1() {
//        log.info("simpleStep1 jobParameters: " + date );
        log.info("simpleStep1");
        return stepBuilderFactory.get("simpleStep1")
                .tasklet(jobParameterTasklet)
                .build();
    }
}
