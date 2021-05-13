package com.batchdev.multiDbTest.dbConfig;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;
@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
@MapperScan(value="com.batchdev.multiDbTest.repository.postgreSql", sqlSessionFactoryRef="postgreSqlSessionFactory")
public class postgreSqlDbConfig {
        @Autowired
        private ApplicationContext applicationContext;

        @Value("${spring.postgresql.datasource.mapper-locations}")
        private String mapperLocations;

        @Value("${spring.postgresql.datasource.mybatis-config}")
        private String configPath;

        @Bean(name = "postgreSqlDataSource")
        @Primary
        @ConfigurationProperties(prefix = "spring.postgresql.datasource")
        public DataSource postgreSqlDataSource() {
            DataSource dataSource = DataSourceBuilder.create().build();
            log.info("Datasource : {}", dataSource);
            return dataSource;
        }

        @Bean(name = "postgreSqlSessionFactory")
        @Primary
        public SqlSessionFactory postgreSqlSessionFactory(@Qualifier("postgreSqlDataSource") DataSource dataSource) throws Exception {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            sqlSessionFactoryBean.setDataSource(dataSource);
            sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));

            //Mybatis config파일 위치
            Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(configPath);
            sqlSessionFactoryBean.setConfigLocation(myBatisConfig);

            return sqlSessionFactoryBean.getObject();
        }

        @Bean(name = "postgreSqlSqlSessionTemplate")
        @Primary
        public SqlSessionTemplate postgreSqlSessionTemplate(@Qualifier("postgreSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
            return new SqlSessionTemplate(sqlSessionFactory);
        }
    }
