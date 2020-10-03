package com.jin.rule;

        import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
        import com.alibaba.cloud.nacos.ribbon.NacosServer;
        import com.alibaba.nacos.api.exception.NacosException;
        import com.alibaba.nacos.api.naming.NamingService;
        import com.alibaba.nacos.api.naming.pojo.Instance;
        import com.netflix.loadbalancer.Server;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Component;

/**
 * creat by Jin 2020/8/18 20:21
 *
 * @Description: 这个是根基nacos 权重的负载均衡
 */

public class WeightRule implements IRpcRule {


    private NacosDiscoveryProperties discoveryProperties;

    public WeightRule(NacosDiscoveryProperties discoveryProperties) {
        this.discoveryProperties = discoveryProperties;
    }

    public Server choose(Object key) {
        try {
            String serviceName = (String) key;
            //获取Nocas服务发现的相关组件API
            NamingService namingService = discoveryProperties.namingServiceInstance();
            //获取 一个基于nacos client 实现权重的负载均衡算法
            Instance instance = namingService.selectOneHealthyInstance(serviceName);
            return new NacosServer(instance);
        } catch (NacosException e) {
            throw new RuntimeException(e.getErrMsg());
        }
    }

}
