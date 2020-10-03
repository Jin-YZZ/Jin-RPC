package com.jin.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "spring.datasource.druid")
@Data
@NoArgsConstructor
public class DruidDataSourceProperties {


    private String url;
    private String username;
    private String password;
    private String driverClassName;

    private int initialSize;
    private int minIdle;
    private int maxActive = 100;
    private long maxWait;
    private boolean testOnBorrow;
    private boolean testOnReturn;
    private int minEvictableIdleTimeMillis;
    private int timeBetweenEvictionRunsMillis;
    private String filters;
}