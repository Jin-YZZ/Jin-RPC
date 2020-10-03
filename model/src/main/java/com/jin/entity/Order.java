package com.jin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * creat by Jin 2020/8/17 10:01
 *
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_order")
public class Order implements Serializable {
 private static final long serialVersionUID=1L;

 private String id;

 private String userId;

 private String productId;

 private Integer count;

 private Double money;

 private Integer status;

}
