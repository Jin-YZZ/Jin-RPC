package com.jin.service;

import com.jin.*;
import com.jin.common.CommonResponse;
import com.jin.dto.AccountDto;
import com.jin.dto.ProductDto;
import com.jin.entity.Order;
import com.jin.entity.Product;
import com.jin.mapper.OrderMapper;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * creat by Jin 2020/8/17 16:15
 *
 * @Description:
 */
@Service
@SuppressWarnings("all")
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findbyId(Integer id) {
        return new Order();
    }

    @Autowired
    private IProductService productService;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private IAccountService accountService;

    @Autowired
    private AccountClient accountClient;

    @Override
    public Order crete(Integer id) {
        return null;
    }

    @Override
    public Order findTry(Integer id) {
        return null;
    }

    @Override
   // @GlobalTransactional
    public CommonResponse createOrder(Order order) {

       //减库存
        ProductDto dto = new ProductDto();
        dto.setCount(order.getCount());
        dto.setProductId(order.getProductId());
        //   productService.decProduct(dto);

        //减钱
//        AccountDto accountDto = new AccountDto();
//        accountDto.setUserId(order.getUserId());
//        accountDto.setComsumer(dto.getCount() * order.getMoney());
//        accountDto.setType(1);
//        CommonResponse changeaccount = accountService.changeaccount(accountDto);

        Product product = accountService.testReturn();

       //System.out.println(product);
        return CommonResponse.okResult(product);
    }

    @Override
    public CommonResponse updateOrder(Order order) {
        return null;
    }
}
