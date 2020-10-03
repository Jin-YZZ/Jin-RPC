package com.jin.discovery;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.registry.NacosServiceRegistry;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.jin.properties.RpcServerConfigProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.Properties;

/**
 * creat by Jin 2020/8/18 14:04
 *
 * @Description:
 */

@Slf4j
public class RpcServerNacosDiscovery implements InitializingBean {

    private final String reflectName="getNacosProperties";

    public final static String SUFERIX="RPC-";

    private NacosDiscoveryProperties nacosDiscoveryProperties;

    private RpcServerConfigProperties rpcServerConfigProperties;

    public RpcServerNacosDiscovery(NacosDiscoveryProperties nacosDiscoveryProperties, RpcServerConfigProperties rpcServerConfigProperties) {
        this.nacosDiscoveryProperties = nacosDiscoveryProperties;
        this.rpcServerConfigProperties = rpcServerConfigProperties;
    }


    /**
     * 在这里完成RpcServer的服务注册发现
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        Properties properties = getNacosProperties(nacosDiscoveryProperties);
        NamingService namingService = NacosFactory.createNamingService(properties);
        Instance instance = new Instance();
        instance.setIp(rpcServerConfigProperties.getHost());
        instance.setPort(rpcServerConfigProperties.getPort());
        instance.setWeight(rpcServerConfigProperties.getWeight());
        instance.setClusterName(rpcServerConfigProperties.getClusterName());
        namingService.registerInstance(SUFERIX+rpcServerConfigProperties.getRpcServername(), instance);
    }
    private Properties getNacosProperties(NacosDiscoveryProperties nacosDiscoveryProperties){
        Properties properties=null;
        try {
            NacosServiceRegistry nacosServiceRegistry = new NacosServiceRegistry(nacosDiscoveryProperties);
            Method method = NacosDiscoveryProperties.class.getDeclaredMethod(reflectName);
            method.setAccessible(true);

            properties = (Properties) method.invoke(nacosDiscoveryProperties);
        }catch (Exception e){
            throw new RuntimeException("反射调用： getNacosProperties出现异常");
        }
        return properties;

    }
}
