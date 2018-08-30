package org.apache.rocketmq.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * guangyao.wu 2018/8/23 13:28
 */
@EnableAutoConfiguration
@SpringBootApplication
public class RocketApp {

    public static void main(String[] args) {
        SpringApplication.run(RocketApp.class, args);
    }

}
