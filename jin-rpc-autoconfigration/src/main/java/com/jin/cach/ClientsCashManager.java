package com.jin.cach;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.jin.bance.ClientBalancer;
import com.jin.client.RpcNettyClient;
import com.jin.dto.ChannelAndDto;
import com.jin.dto.RPCDto;
import com.jin.dto.ReturnValue;
import com.jin.properties.RpcServerConfigProperties;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * creat by Jin 2020/9/2 20:54
 *
 * @Description:
 */
@Component
public class ClientsCashManager {

    @Autowired
    private NacosDiscoveryProperties discoveryProperties;

    @Autowired
    private RpcServerConfigProperties rpcServerConfigProperties;

    private final String suferix = "RPC-";

    // Map<serverName, Map<127.0.0.1:8888##1,RpcNettyClient>>
    public static Map<String, Map<String, RpcNettyClient>> nettyClientMap = new ConcurrentHashMap<>();

    public static Map<String, RPCDto> returntMap = new ConcurrentHashMap<>();

    public static volatile Map<String, CompletableFuture<RPCDto>> invokeMap = new ConcurrentHashMap<>();


    public static BlockingQueue<ChannelAndDto> queue = new ArrayBlockingQueue<>(10000 * 10);

    public static Lock lock = new ReentrantLock();

    public static RPCDto getRetrunValue(String key, AtomicInteger integer) {
        //没有先一种堵塞着
        while (returntMap.get(key) == null && integer.incrementAndGet() <= 100) {
            Thread.yield();
            try {
                TimeUnit.NANOSECONDS.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        RPCDto rpcDto = returntMap.get(key);
        returntMap.remove(key);
        return  rpcDto;
    }

    public static void clearnettyClientMap(String service, String ipAndPort) {
        nettyClientMap.get(service).get(ipAndPort).shundown();
        nettyClientMap.get(service).remove(ipAndPort);
    }

    public void sendMessage(String serverName, Server server, RPCDto dto) {
        RpcNettyClient client = findnettyClient(serverName, server);
        client.send(serverName, server, dto);
    }

    public RpcNettyClient findnettyClient(String serverName, Server server) {
        //从缓存中根据轮询拿取 客户端
        RpcNettyClient rpcNettyClient = null;
        Map<String, RpcNettyClient> clientMap = nettyClientMap.get(serverName);
        if (clientMap != null) {
            rpcNettyClient = clientMap.get(server.getHostPort() + ClientBalancer.MIDDLE + ClientBalancer.loadbance());
            if (rpcNettyClient != null) {
                return rpcNettyClient;
            }
        }

        //如果缓存中没有  呢么就创建个客户端
        //保存靳缓存 10个
        lock.lock();
        try {
            for (int i = 0; i < ClientBalancer.NUMS; i++) {
                RpcNettyClient client = RpcNettyClient.creatAndRun(server.getHost(), server.getPort());
                putPpcNettyClientToCash(serverName, server.getHostPort() + ClientBalancer.MIDDLE + i, client);
                if (i == 0) {
                    rpcNettyClient = client;
                }
            }
        } finally {
            lock.unlock();
        }

        return rpcNettyClient;
    }

    public void putPpcNettyClientToCash(String serverName, String key, RpcNettyClient rpcNettyClient) {
        if (nettyClientMap.get(serverName) == null) {
            nettyClientMap.put(serverName, new ConcurrentHashMap<>());
        }
        nettyClientMap.get(serverName).put(key, rpcNettyClient);
    }



    public static void putResultValue(String key, RPCDto dto) {
        returntMap.put(key, dto);
    }

    public static Boolean isreturnvoid(Class<?> clazz) {
        return clazz.getName().equals("void");
    }
}
