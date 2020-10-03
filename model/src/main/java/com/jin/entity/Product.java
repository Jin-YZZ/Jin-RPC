package com.jin.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
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
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_product")
public class Product implements Serializable {

    @TableId
    private String id;
    @TableField("product_id")
    private String productId;
    private Long total;
    private Long used;
    private Long residue;

}
