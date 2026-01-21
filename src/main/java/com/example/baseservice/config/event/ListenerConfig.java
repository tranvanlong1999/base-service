package com.example.baseservice.config.event;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class ListenerConfig {

	@Bean
	public TaskExecutor taskExecutor() {
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
		taskExecutor.setVirtualThreads(true); // Use virtual threads supported by the JVM (Java 21+)
		taskExecutor.setConcurrencyLimit(100); // Limit thread pool size, if not set it will be unlimited
		return taskExecutor;
	}

}
