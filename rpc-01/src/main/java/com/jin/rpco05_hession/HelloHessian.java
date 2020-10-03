package com.jin.rpco05_hession;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.jin.Order;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * creat by Jin 2020/8/17 14:52
 *
 * @Description:
 */
public class HelloHessian {
    public static void main(String[] args)throws Exception {
        Order order = new Order(10086,"亚洲舞王赵四罗JJ");
        byte[] serialize = serialize(order);
        System.out.println(serialize);
        Object deserialize = deserialize(serialize);
        System.out.println(deserialize);
    }


    public static  byte[] serialize(Object o) throws Exception{
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Hessian2Output output = new Hessian2Output(baos);
        output.writeObject(o);
        output.flush();
        byte[] bytes = baos.toByteArray();
        baos.close();
        output.close();
        return bytes;
    }

    public static Object deserialize(byte[] bytes) throws Exception{
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        Hessian2Input input = new Hessian2Input(bais);
        Object o = input.readObject();
        bais.close();
        input.close();
        return o;
    }

}
