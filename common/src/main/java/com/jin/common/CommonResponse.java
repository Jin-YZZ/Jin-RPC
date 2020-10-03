package com.jin.common;

import lombok.Data;

import java.io.Serializable;

/**
 * creat by Jin 2020/8/17 17:55
 *
 * @Description:
 */
@Data
public class CommonResponse implements Serializable {



    private String message;


    private Object data;
    public CommonResponse( Object data,String message) {
        this.message = message;
        this.data = data;
    }
    public static CommonResponse okResult(Object data) {
        CommonResponse result = new CommonResponse( data,"操作成功");
        return result;
    }
    public static CommonResponse okResult(Object data,String message) {
        CommonResponse result = new CommonResponse( data,message);
        result.message=message;
        return result;
    }
    public static CommonResponse errorResult(String message) {
        CommonResponse result = new CommonResponse(null,message);
        result.setMessage(message);
        return result;
    }

    public CommonResponse() {

    }
}
