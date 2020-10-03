package com.jin;


import com.jin.common.CommonResponse;
import com.jin.entity.Order;

public interface OrderService {

    Order findbyId(Integer id);

    Order crete(Integer id);

    Order findTry(Integer id);

    CommonResponse createOrder(Order order);

    CommonResponse updateOrder(Order order);
}
