package com.jin.rpc03;

import com.jin.Order;
import com.jin.OrderService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * creat by Jin 2020/8/17 10:04
 *
 * @Description:
 */
public class Server {

    private static  boolean flag=true;
    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);

        while (flag){
            Socket accept = serverSocket.accept();
            process(accept);
            accept.close();
        }
        serverSocket.close();

    }

    private static void process(Socket socket)throws Exception{
        InputStream inputStream = socket.getInputStream();
        OutputStream outputStream = socket.getOutputStream();
        DataInputStream dis = new DataInputStream(inputStream);
        DataOutputStream dos = new DataOutputStream(outputStream);


        int id = dis.readInt();
        OrderService service = new OrderServiceImpl();
        Order order = service.findbyId(id);
        dos.writeInt(order.getOrder_id());
        dos.writeUTF(order.getOrder_name());
        dos.flush();

        inputStream.close();
        outputStream.close();
        dis.close();
        dos.close();

    }
}
