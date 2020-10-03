package com.jin.rule;

import com.alibaba.nacos.api.exception.NacosException;
import com.netflix.loadbalancer.Server;

public interface IRpcRule {
    public Server choose(Object key);
}
