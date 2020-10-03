package com.jin.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jin.dto.ProductDto;
import com.jin.entity.Product;
import com.jin.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

/**
 * creat by Jin 2020/8/17 16:12
 *
 * @Description:
 */
@Service
@SuppressWarnings("all")
public class ProductServiceImpl implements IProductService {


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductMapper productMapper;


    public Product findbyId(Integer id) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        Long phone11 = valueOperations.increment("phone11");
        System.out.println(phone11+"*******************");
        return new Product();
    }

    @Override
    public Product decProduct(ProductDto dto) {
        Product product = productMapper.selectById(dto.getProductId());
        UpdateWrapper<Product> wrapper=new UpdateWrapper<>();
        wrapper.setSql("used=used+"+dto.getCount());
        wrapper.setSql("residue=residue-"+dto.getCount());
        wrapper.eq("id",dto.getProductId());
        productMapper.update(product,wrapper);
        return product;
    }
}
