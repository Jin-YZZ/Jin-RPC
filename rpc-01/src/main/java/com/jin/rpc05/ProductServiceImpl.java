package com.jin.rpc05;

import com.jin.Product;
import com.jin.ProductService;

/**
 * creat by Jin 2020/8/17 14:21
 *
 * @Description:
 */
public class ProductServiceImpl implements ProductService {
    @Override
    public Product findbyId(Integer id) {
        return new Product(id,"商品一号");
    }

    @Override
    public String save(Product product) {
        return null;
    }
}
