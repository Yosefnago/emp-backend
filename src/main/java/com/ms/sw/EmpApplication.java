package com.ms.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class EmpApplication {

    static void main(String[] args) {

        SpringApplication.run(EmpApplication.class, args);

    }

}
