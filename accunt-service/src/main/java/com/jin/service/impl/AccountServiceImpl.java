package com.jin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jin.dto.AccountDto;
import com.jin.common.CommonResponse;
import com.jin.entity.Product;
import com.jin.entity.Account;
import com.jin.mapper.AccountMapper;

import com.jin.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Jin
 * @since 2020-07-26
 */
//@Service("accountservice")
@Service
@Slf4j
@SuppressWarnings("all")
public class AccountServiceImpl implements IAccountService {


    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public CommonResponse changeaccount(AccountDto dto) {
        log.info("开始更新数据库余额");
        Account account = accountMapper.selectOne(new LambdaQueryWrapper<Account>().eq(Account::getUserId, dto.getUserId()));
        UpdateWrapper<Account> updateWrapper=new UpdateWrapper<>();

        updateWrapper.setSql("residue=residue-"+dto.getComsumer());
        updateWrapper.setSql("usesd=usesd+"+dto.getComsumer());
        updateWrapper.eq("user_id", dto.getUserId());
        accountMapper.update(account,updateWrapper);

        log.info("数据库账号更新完毕{}" ,account);
         int i=5/0;
        return CommonResponse.okResult(account,"操作成功");
    }

    @Override
    public void annoOnInsterface() {
        System.out.println("999999999999999999999999999999999999999999999999999999");
    }

    public void func (){

    }
    public static void main(String[] args) throws NoSuchMethodException {
        Method func = AccountServiceImpl.class.getDeclaredMethod("func");
        Class<?> returnType = func.getReturnType();
        System.out.println( checkReturnTypeVoid(returnType));
        System.out.println(func.getReturnType());

    }


    @Override
    public Product trytest() {
        Long phone11 = redisTemplate.opsForValue().increment("phone11");
        System.out.println(phone11+"******************");
        return new Product();
    }

    public static Boolean checkReturnTypeVoid(Class<?> returnType){
        return returnType.getName().equals("void");
    }

    @Override
    public Product testReturn() {

        Product product = new Product();
        product.setId("1");
        product.setTotal(100L);
       // int i=5/0;
        return product;
    }
}
