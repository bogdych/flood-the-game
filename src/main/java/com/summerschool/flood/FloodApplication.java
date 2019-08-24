package com.summerschool.flood;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FloodApplication {

    public static void main(String[] args) {
        SpringApplication.run(FloodApplication.class, args);
    }

}
