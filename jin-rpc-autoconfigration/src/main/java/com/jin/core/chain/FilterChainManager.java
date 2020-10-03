package com.jin.core.chain;

import com.jin.core.chain.defaulterChains.ClientResuitFilter;
import com.jin.core.chain.defaulterChains.ExceptionFilter;
import com.jin.core.chain.defaulterChains.SeataXidFilter;
import com.jin.dto.RPCDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * creat by Jin 2020/9/5 21:14
 *
 * @Description:
 */
@Slf4j
public class FilterChainManager implements ApplicationListener<WebServerInitializedEvent> {

    public List<RpcFilter> filters = new ArrayList<>();

    public FilterChainManager() {
        init();
    }

    public void init() {
        filters.add(new SeataXidFilter());
        filters.add(new ClientResuitFilter());
        filters.add(new ExceptionFilter());
    }


    /**
     * 调用客户端  任务责任链
     *
     * @param dto
     */
    public void handleClientSend(RPCDto dto) throws Throwable {
        new MethodInvocationChain(filters, dto).processClientSend();
    }


    /**
     * 调用客户接收消息处理责任链
     *
     * @param dto
     * @return
     */
    public Object handleClientReceive(RPCDto dto) throws Throwable {
        return new MethodInvocationChain(filters, dto).processClientReceive();
    }

    /**
     * 调用服务端处理责任链
     *
     * @param dto
     * @return
     */
    public Object handleServerReceive(RPCDto dto) throws Throwable {
        return new MethodInvocationChain(filters, dto).processServerReceive();
    }


    @Override
    public void onApplicationEvent(WebServerInitializedEvent event) {
        sortfilters();
        log.info("RpcFilter 初始化排序完毕 size={}", filters.size());
    }

    public void addFilter(RpcFilter filter) {
        filters.add(filter);
    }

    public void sortfilters() {
        synchronized (filters) {
            List<RpcFilter> collect = filters.stream().sorted((el1, el2) -> {
                return el1.getOrder() - el2.getOrder();
            }).collect(Collectors.toList());
            ;
            filters = collect;
        }
    }

}
