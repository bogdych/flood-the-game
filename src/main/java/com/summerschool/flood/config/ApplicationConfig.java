package com.summerschool.flood.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.PostConstruct;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@NoArgsConstructor
public class ApplicationConfig extends WebMvcConfigurationSupport {

    @Value("${flood.scheduler.poolSize}")
    private int poolSize;
    private TaskScheduler taskScheduler;
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void postConstruct() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(poolSize);
        taskScheduler = new ConcurrentTaskScheduler(service);
    }

    @Bean
    public TaskScheduler getTaskScheduler() {
        return taskScheduler;
    }

    @Bean
    public ObjectMapper mapper() {
        return objectMapper;
    }

}
