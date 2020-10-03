package com.jin.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * creat by Jin 2020/9/4 20:10
 *
 * @Description:
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_account")
public class Account implements Serializable {

    private static final long serialVersionUID=1L;

    private String id;

    private String userId;

    private Double total;

    private Double usesd;

    private Double residue;


}