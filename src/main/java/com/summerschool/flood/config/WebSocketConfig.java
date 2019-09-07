package com.summerschool.flood.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.summerschool.flood.handler.WebSocketGameHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.config.annotation.*;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${flood.scheduler.poolSize}")
    private int poolSize;
    private TaskScheduler taskScheduler;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final WebSocketGameHandler webSocketGameHandler;

    public WebSocketConfig(WebSocketGameHandler handler) {
        webSocketGameHandler = handler;
    }

    @PostConstruct
    public void postConstruct() {
        ScheduledExecutorService service = Executors.newScheduledThreadPool(poolSize);
        taskScheduler = new ConcurrentTaskScheduler(service);
    }

    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(webSocketGameHandler, "/game").setAllowedOrigins("*");
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
