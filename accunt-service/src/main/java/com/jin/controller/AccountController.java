package com.jin.controller;


import com.jin.dto.AccountDto;
import com.jin.common.CommonResponse;
import com.jin.service.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Jin
 * @since 2020-07-26
 */
@RestController
@RequestMapping("/account")
public class AccountController {

    @Autowired
    IAccountService accountService;
    @PostMapping("/change")
    public CommonResponse useAccount(@RequestBody AccountDto dto){
        System.out.println(666);
        CommonResponse changeaccount = accountService.changeaccount(dto);
        return changeaccount;
    }

}

