package com.jin;

import com.jin.common.CommonResponse;
import com.jin.dto.AccountDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("AccountService")
public interface AccountClient {
    @PostMapping("/account/change")
    public CommonResponse changeAccount(@RequestBody AccountDto dto);
}
