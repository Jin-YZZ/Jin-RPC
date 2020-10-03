package com.jin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * creat by Jin 2020/8/17 16:19
 *
 * @Description:
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)//取消数据源的自带创建
public class ProductApplication {
    public static void main(String[] args) {
       SpringApplication.run(ProductApplication.class,args);
    }
}
