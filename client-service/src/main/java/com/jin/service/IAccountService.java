package com.jin.service;

import com.jin.dto.AccountDto;
import com.jin.common.CommonResponse;
import com.jin.entity.Product;
import com.jin.anno.RpcServer;


/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Jin
 * @since 2020-07-26
 */
@RpcServer(sever = "AccountService")
public interface IAccountService  {
    @RpcServer(sever = "AccountService")
    public CommonResponse changeaccount(AccountDto dto);

    @RpcServer(sever = "AccountService")
    public void annoOnInsterface();

    @RpcServer(sever = "AccountService")
    public Product trytest();

    public Product testReturn ();

}
