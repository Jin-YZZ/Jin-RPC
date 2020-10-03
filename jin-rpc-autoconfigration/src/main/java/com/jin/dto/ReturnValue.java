package com.jin.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * creat by Jin 2020/9/14 18:47
 *
 * @Description:
 */
@Data
public class ReturnValue  implements Serializable {

    private String Id;
    private  Object returnValue;

    private boolean OK;

    private boolean VOID;


    private  String errorMessage;

}
