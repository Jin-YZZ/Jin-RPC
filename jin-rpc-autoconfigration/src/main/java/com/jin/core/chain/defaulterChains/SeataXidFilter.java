package com.jin.core.chain.defaulterChains;

import com.jin.core.chain.AbRpcFilter;
import com.jin.core.chain.FiterJoinpoin;
import com.jin.core.chain.RpcFilter;
import com.jin.dto.RPCDto;
import io.seata.core.context.RootContext;
import org.springframework.util.StringUtils;

/**
 * creat by Jin 2020/9/5 21:10
 *
 * @Description:
 */
public class SeataXidFilter extends AbRpcFilter {


    @Override
    public void invokeClinentSend(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable {
        String xid = RootContext.getXID();
        if (!StringUtils.isEmpty(xid)) {
            dto.setXid(xid);
        }
        super.invokeClinentSend(dto, fiterJoinpoin);
    }

    @Override
    public Object invokeServerReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin) throws Throwable {
        if (!StringUtils.isEmpty(dto.getXid())) {
            RootContext.bind(dto.getXid());
        }
        return super.invokeServerReceive(dto, fiterJoinpoin);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
