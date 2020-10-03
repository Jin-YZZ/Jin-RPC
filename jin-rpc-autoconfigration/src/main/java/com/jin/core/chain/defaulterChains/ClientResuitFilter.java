package com.jin.core.chain.defaulterChains;

import com.jin.core.chain.AbRpcFilter;
import com.jin.core.chain.FiterJoinpoin;
import com.jin.dto.RPCDto;

/**
 * creat by Jin 2020/9/12 15:05
 *
 * @Description:
 */
public class ClientResuitFilter extends AbRpcFilter {


    @Override
    public Object invokeClinentReceive(RPCDto dto, FiterJoinpoin fiterJoinpoin)throws Throwable {
        if (checkReturnTypeVoid(dto.getReturnType())){
            return  null;
        }
        return super.invokeClinentReceive(dto, fiterJoinpoin);
    }

    @Override
    public int getOrder() {
        return 1000;
    }
    public static Boolean checkReturnTypeVoid(Class<?> returnType){
        return returnType.getName().equals("void");
    }
}
