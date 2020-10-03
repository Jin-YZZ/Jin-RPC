package com.jin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * creat by Jin 2020/7/26 22:09
 *
 * @Description:
 */

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源的自带创建
public class ATAccountApplication {
    public static void main(String[] args) {
        SpringApplication.run(ATAccountApplication.class,args);
    }
}
