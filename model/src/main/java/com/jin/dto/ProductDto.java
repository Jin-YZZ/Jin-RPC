package com.jin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * creat by Jin 2020/9/13 21:49
 *
 * @Description:
 */
@Data
public class ProductDto  implements Serializable {
    private String productId;
    private Integer count;
}
