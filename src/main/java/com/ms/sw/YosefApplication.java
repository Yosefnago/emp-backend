package com.ms.sw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;


@SpringBootApplication
@EnableScheduling
public class YosefApplication {

    public static void main(String[] args) {


        SpringApplication.run(YosefApplication.class, args);
    }

}
