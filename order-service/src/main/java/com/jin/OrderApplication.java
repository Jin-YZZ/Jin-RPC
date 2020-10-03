package com.jin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * creat by Jin 2020/8/17 16:10
 *
 * @Description:
 */
@EnableFeignClients
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源的自带创建
public class OrderApplication {
    public static void main(String[] args) {
       SpringApplication.run(OrderApplication.class,args);
    }
}
