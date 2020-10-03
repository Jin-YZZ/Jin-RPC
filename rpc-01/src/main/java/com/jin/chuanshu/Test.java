package com.jin.chuanshu;

import com.jin.Order;
import com.jin.OrderService;

/**
 * creat by Jin 2020/8/30 19:51
 *
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        OrderService service = Stub.getSub(OrderServiceImpl.class);
        Order order = service.findbyId(1);
    }
}
