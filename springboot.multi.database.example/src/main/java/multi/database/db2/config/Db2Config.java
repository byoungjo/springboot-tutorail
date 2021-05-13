package multi.database.db2.config;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Slf4j
@Configuration
@PropertySource("classpath:/application.properties")
@MapperScan(value="multi.database.db2.dao", sqlSessionFactoryRef="db2SqlSessionFactory")
public class Db2Config {
 	@Autowired
	private ApplicationContext applicationContext;
	
	@Value("${spring.db2.datasource.mapper-locations}")
	private String mapperLocations;
	
	@Value("${spring.db2.datasource.mybatis-config}")
	private String configPath;

	@Bean(name = "db2DataSource")
	@ConfigurationProperties(prefix = "spring.db2.datasource")
	public DataSource db2DataSource() {
//		DataSource dataSource = DataSourceBuilder.create().type(HikariDataSource.class).build();
		HikariDataSource dataSource = new HikariDataSource();
		log.info("db2 DataSource : {}", dataSource);
		return dataSource;
	}

	@Bean(name = "db2SqlSessionFactory")
//	public SqlSessionFactory db2SqlSessionFactory(DataSource dataSource) throws Exception {
	public SqlSessionFactory db2SqlSessionFactory() throws Exception {
		log.info("db2 mapperLocations {}",mapperLocations);
		log.info("db2 SqlSessionFactory : {}", db2DataSource());
		log.info("db2 configPath {}",configPath);
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		//sqlSessionFactoryBean.setDataSource(dataSource);
		sqlSessionFactoryBean.setDataSource(db2DataSource());

		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mapperLocations));
		//Mybatis config파일 위치
		Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource(configPath);
		sqlSessionFactoryBean.setConfigLocation(myBatisConfig);
		
		return sqlSessionFactoryBean.getObject();
	}

	@Bean(name = "db2SqlSessionTemplate")
//	public SqlSessionTemplate db2SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
	public SqlSessionTemplate db2SqlSessionTemplate() throws Exception {
		log.info("db2 SqlSessionTemplate : {}", db2SqlSessionFactory());
		//return new SqlSessionTemplate(sqlSessionFactory);
		return new SqlSessionTemplate(db2SqlSessionFactory());
	}

	@Bean(name = "db2transactionManager")
	public DataSourceTransactionManager db1transactionManager() {
		log.info("db2 db1transactionManager {}",db2DataSource());
		return new DataSourceTransactionManager(db2DataSource());
	}
}
