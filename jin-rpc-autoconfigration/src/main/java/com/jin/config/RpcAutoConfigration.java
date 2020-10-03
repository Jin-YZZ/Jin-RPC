package com.jin.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.jin.bean.ServiceBeanDefinitionRegistry;
import com.jin.core.GlobalRpcClinentScanner;
import com.jin.core.chain.FilterChainManager;
import com.jin.properties.RpcServerConfigProperties;
import com.jin.rule.IRpcRule;
import com.jin.rule.WeightRule;
import com.jin.server.RpcNettyServer;
import com.jin.discovery.RpcServerNacosDiscovery;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * creat by Jin 2020/8/18 20:31
 *
 * @Description:
 */
@Configuration
@EnableConfigurationProperties({RpcServerConfigProperties.class})
@AutoConfigureAfter({NacosDiscoveryProperties.class,ServiceBeanDefinitionRegistry.class})

@SuppressWarnings("all")
public class RpcAutoConfigration {


    /**
     * RpcNettyServer  RPC 通信服务器
     * @param rpcServerConfigProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RpcNettyServer.class)
    public RpcNettyServer rpcServer(RpcServerConfigProperties rpcServerConfigProperties){
        return new RpcNettyServer(rpcServerConfigProperties);
    }

    /**
     * 客户端 增强扫描器
     * @param rpcServerConfigProperties
     * @return
     */


    @Bean("rpcrule")
    @ConditionalOnMissingBean(IRpcRule.class)
    public IRpcRule iRpcRule(NacosDiscoveryProperties discoveryProperties){
        return new WeightRule(discoveryProperties);
    }

    /**
     * 完成对RPC-Server的注册
     * @param nacosDiscoveryProperties
     * @param rpcServerConfigProperties
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(RpcServerNacosDiscovery.class)
    public RpcServerNacosDiscovery rpcNacosDiscovery(NacosDiscoveryProperties nacosDiscoveryProperties, RpcServerConfigProperties rpcServerConfigProperties){
        return new RpcServerNacosDiscovery(nacosDiscoveryProperties,rpcServerConfigProperties);
    }


    /**
     * 责任链 管理FilterChainManager
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(FilterChainManager.class)
    public FilterChainManager filterChainManager(){
        return new FilterChainManager();
    }

//    @Bean
//    public ServiceBeanDefinitionRegistry serviceBeanDefinitionRegistry (RpcServerConfigProperties rpcServerConfigProperties){
//            return  new ServiceBeanDefinitionRegistry(rpcServerConfigProperties);
//    }
}
