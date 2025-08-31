package com.savemystudies.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.savemystudies.backend.repository")
@EntityScan("com.savemystudies.backend.model")
public class SaveMyStudiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaveMyStudiesApplication.class, args);
    }
}