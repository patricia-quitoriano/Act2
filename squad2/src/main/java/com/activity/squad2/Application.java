package com.activity.squad2;

import com.activity.squad2.config.CustomHttpApiConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {"com.activity.squad2.model"})
@EnableJpaRepositories(basePackages = {"com.activity.squad2.repository"})
@EnableConfigurationProperties(CustomHttpApiConfigProperties.class)
@ComponentScan(basePackages = {"com.activity.squad2"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}