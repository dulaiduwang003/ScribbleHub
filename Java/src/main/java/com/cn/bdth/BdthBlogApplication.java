package com.cn.bdth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * bdth博客应用程序
 *
 * @author 时间海 @github dulaiduwang003
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class BdthBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdthBlogApplication.class, args);
    }


}
