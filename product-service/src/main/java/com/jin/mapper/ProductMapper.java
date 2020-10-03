package com.jin.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jin.entity.Product;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Jin
 * @since 2020-07-26
 */
@Mapper
public interface ProductMapper extends BaseMapper<Product> {

}
