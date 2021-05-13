package com.batchdev.batchtest.job;

import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import com.batchdev.batchtest.model.Bill;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;

import javax.sql.DataSource;

@Slf4j // log 사용을 위한 lombok 어노테이션
@RequiredArgsConstructor // 생성자 DI를 위한 lombok 어노테이션
@Configuration
@EnableTask
@EnableBatchProcessing
public class SimpleJobConfiguration {
    private final JobBuilderFactory jobBuilderFactory; // 생성자 DI 받음
    private final StepBuilderFactory stepBuilderFactory; // 생성자 DI 받음
    private final DataSource dataSource;

    private static final int chunkSize = 10;    //트랜잭션 범위

    @Bean
    @SneakyThrows
    public Job jdbcCursorItemReaderJob() {
        return jobBuilderFactory.get("jdbcCursorItemReaderJob") // job name
                .start(jdbcCursorItemReaderStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step jdbcCursorItemReaderStep(@Value("#{jobParameters[date]}") String date) {
    //public Step jdbcCursorItemReaderStep() {
        log.info(">>>>> date = {}", date);
        return stepBuilderFactory.get("jdbcCursorItemReaderStep")   //step name
                .<Bill, Bill>chunk(chunkSize) //Reader의 반환타입 & Writer의 파라미터타입
                .reader(jdbcCursorItemReader())
                //.processor()  생략
                .writer(jdbcCursorItemWriter())
                .build();
    }


    @Bean
    public JdbcCursorItemReader<Bill> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<Bill>()   //Cursor는 하나의 Connection으로 사용하기 때문에 Timeout 시간을 길게 부여해야 한다
                .fetchSize(chunkSize)       //Database에서 가져오는 개수 / read()를 통해 1개씩 (Paging과 다름)
                .dataSource(dataSource)
                //.rowMapper(SingleColumnRowMapper.newInstance(Long.class))
                //.sql("SELECT id FROM pay")
                .rowMapper(new BeanPropertyRowMapper<>(Bill.class))
                .sql("SELECT id, first_name, last_name, minutes, data_usage, bill_amount FROM bill_statements")
                .name("jdbcCursorItemReader")   //reader name
                .build();
    }

    @Bean
    public ItemWriter<Bill> jdbcCursorItemWriter() {
        JdbcBatchItemWriter<Bill> writer = new JdbcBatchItemWriterBuilder<Bill>()
                .beanMapped()
                .dataSource(dataSource)
                .sql("INSERT INTO BILL_STATEMENTS (id, first_name, last_name, minutes, data_usage,bill_amount) VALUES (:id, :firstName, :lastName, :minutes, :dataUsage, :billAmount)")
                .build();
//        for (Bill bill : list) {
//                log.info("Current Bill = {} {}", bill.getId(), bill.getFirstName());
//        }
        return writer;
    }
}
