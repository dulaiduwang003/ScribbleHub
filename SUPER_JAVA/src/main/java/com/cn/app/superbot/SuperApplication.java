package com.cn.app.superbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Super bot back application.
 *
 * @author bdth
 * @email 2074055628 @qq.om
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class SuperApplication {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SuperApplication.class, args);
    }

}
