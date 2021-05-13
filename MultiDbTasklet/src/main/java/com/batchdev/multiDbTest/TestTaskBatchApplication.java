package com.batchdev.multiDbTest;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@Slf4j
@MapperScan(basePackageClasses = TestTaskBatchApplication.class)
@EnableTask
@SpringBootApplication
public class TestTaskBatchApplication {

//	@Autowired
//	private JobLauncher jobLauncher;
//
//	@Autowired
//	private JobExplorer jobExplorer;

	@Value("${sample.jobParams:null}")
	private String additionalJobParams;

	@Value("${sample.makeParametersUnique:true}")
	private boolean makeParametersUnique;

	public static void main(String[] args) {
		System.setProperty("spring.profiles.default", "local");
//		Properties p = System.getProperties();
//		Enumeration keys = p.keys();
//		while (keys.hasMoreElements()) {
//			String key = (String) keys.nextElement();
//			String value = (String) p.get(key);
//			System.out.println(key + ": " + value);
//		}

		SpringApplication.run(TestTaskBatchApplication.class, args);
	}
//
//	@Bean
//	public MyJobLauncherCommandLineRunner commandLineRunner() {
//		return new MyJobLauncherCommandLineRunner(jobLauncher, jobExplorer, additionalJobParams, makeParametersUnique);
//	}
}
