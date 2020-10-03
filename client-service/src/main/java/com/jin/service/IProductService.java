package com.jin.service;

import com.jin.anno.RpcServer;
import com.jin.dto.ProductDto;
import com.jin.entity.Product;

/**
 * creat by Jin 2020/9/14 14:02
 *
 * @Description:
 */
@RpcServer(sever = "ProductService")
public interface IProductService {

    @RpcServer(sever = "ProductService")
    Product decProduct(ProductDto dto);


}
