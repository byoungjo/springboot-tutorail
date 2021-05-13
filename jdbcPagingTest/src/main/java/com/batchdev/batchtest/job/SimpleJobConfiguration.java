package com.batchdev.batchtest.job;

import lombok.RequiredArgsConstructor;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.item.database.*;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.cloud.task.configuration.EnableTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.batchdev.batchtest.model.Bill;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.google.common.collect.Maps;
import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

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
    public Job jdbcPaginItemReaderJob() throws Exception {
        return jobBuilderFactory.get("jdbcPagingItemReaderJob") // job name
                .start(jdbcPagingItemReaderStep())
                .build();
    }

    @Bean
    @JobScope
    public Step jdbcPagingItemReaderStep() throws Exception {
        return stepBuilderFactory.get("jdbcPagingItemReaderStep")   //step name
                .<Bill, Bill>chunk(chunkSize) //Reader의 반환타입 & Writer의 파라미터타입
                .reader(jdbcPagingItemReader())
                //.processor()  생략
                .writer(jdbcPagingItemWriter())
                .build();
    }


    @Bean
    public JdbcPagingItemReader<Bill> jdbcPagingItemReader() throws Exception {
        Map<String, Object> parameterValues = Maps.newHashMap();
        parameterValues.put("id", 10);    //조건절 파라미터
        return new JdbcPagingItemReaderBuilder<Bill>()
                .pageSize(chunkSize)
                .fetchSize(chunkSize)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(Bill.class))
                .queryProvider(createQueryProvider())
                .parameterValues(parameterValues)   //Provider Where에 조건 세팅
                .name("jdbcPagingItemReader")
                .build();

    }

    @Bean
    public ItemWriter<Bill> jdbcPagingItemWriter() {
        ItemWriter<Bill> billItemWriter = new ItemWriter<Bill>() {
           @Override
           public void write(List<? extends Bill> list) throws Exception {
               log.info("jdbcPagingItemWriter bill Override");
               for (Bill bill : list) {
                   if ( "john".equals( bill.getFirstName() )  )   {
                        bill.setFirstName( bill.getFirstName() + "_kim" );
                   }
                   log.info("Current bill2 = {} {}", bill.getId(), bill.getFirstName());
               }
           }
       };

        return billItemWriter;

//        AtomicInteger cnt = new AtomicInteger();
//        return (List<? extends Bill> list) -> {
//            for (Bill bill : list) {
//                log.info("Current bill = {} {} {}", bill.getId(), bill.getFirstName(), Integer.toString(cnt.getAndIncrement()));
//            }
//        };

//        JdbcBatchItemWriter<Bill> writer = new JdbcBatchItemWriterBuilder<Bill>()
//                .beanMapped()
//                .dataSource(dataSource)
//                .sql("INSERT INTO BILL_STATEMENTS (id, first_name, last_name, minutes, data_usage,bill_amount) VALUES (:id, :firstName, :lastName, :minutes, :dataUsage, :billAmount)")
//                .build();
//        return writer;
    }


    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(dataSource); //Datasource를 통한 DB Type 인식해서 Provider 자동 인식
        queryProviderFactoryBean.setSelectClause("id, first_name, last_name, minutes, data_usage, bill_amount,cr_date,cr_time");
        queryProviderFactoryBean.setFromClause("from bill_statements2");
        queryProviderFactoryBean.setWhereClause("where id > :id"); //조건

        Map<String, Order> sortKeys = Maps.newHashMap();

        //Paging은 매번 다른 Connection을 맺기 때문에 Order가 필수
        sortKeys.put("id", Order.ASCENDING);

        queryProviderFactoryBean.setSortKeys(sortKeys);
        return queryProviderFactoryBean.getObject();
    }
}
