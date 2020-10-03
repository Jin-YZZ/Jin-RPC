package com.jin.rpc03;

import com.jin.Order;
import com.jin.OrderService;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class OrderServiceImpl implements OrderService {
    @Override
    public Order findbyId(Integer id) {
        Order order = new Order();
        order.setOrder_id(id);
        order.setOrder_name("aoteman");
        return order;
    }
}
