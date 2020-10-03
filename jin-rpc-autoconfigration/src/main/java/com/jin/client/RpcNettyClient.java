package com.jin.client;

import com.jin.cach.ClientsCashManager;
import com.jin.dto.RPCDto;
import com.jin.serializable.RPCDtoDecoder;
import com.jin.serializable.RPCDtoEncoder;
import com.netflix.loadbalancer.Server;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.TimeUnit;

/**
 * creat by Jin 2020/9/2 20:28
 *
 * @Description:
 */

public class RpcNettyClient {


    //属性
    private final String host;
    private final int port;
    private Channel channel;
    private EventLoopGroup group;
    private Log log = LogFactory.getLog(RpcNettyClient.class);

     private RpcNettyClient intance = this;

    public RpcNettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() {
        group = new NioEventLoopGroup();
        try {
            //创建客户端启动对象
            //注意客户端使用的不是 ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();

            //设置相关参数
            bootstrap.group(group) //设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端通道的实现类(反射)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //获取到pipeline
                            ChannelPipeline pipeline = ch.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder", new RPCDtoDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder", new RPCDtoEncoder());
                            pipeline.addLast(new NettyClientHandler()); //加入自己的处理器
                        }
                    });

            //启动客户端去连接服务器端
            //关于 ChannelFuture 要分析，涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            //给关闭通道进行监听
            channel = channelFuture.channel();
            log.info("-----------------" + channel.localAddress() + "---------------");
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("客户端 netty创建异常");
        } finally {
            group.shutdownGracefully();
        }
    }

    public static RpcNettyClient creatAndRun(String host, int port) {
        RpcNettyClient rpcNettyClient = new RpcNettyClient(host, port);
        //异步线程操作
        new Thread(() -> {
            rpcNettyClient.run();
        }).start();

        while (rpcNettyClient.intance.channel == null) {
            try {
                TimeUnit.MICROSECONDS.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return rpcNettyClient;
    }

    public void send(String serverName, Server server, RPCDto dto) {
        //通过channel 发送到服务器端
        ChannelFuture channelFuture = intance.channel.writeAndFlush(dto);
        if (channelFuture.isDone()) {
            log.info("hannelFuture.isDone()连接出现异常，将会重置nettyClientMap");
            ClientsCashManager.nettyClientMap.remove(serverName);
            log.info("由于系统断连连接未发消息"+serverName+server+dto);
            throw new RuntimeException("hannelFuture.isDone()连接出现异常，将会重置nettyClientMap");
        } else {
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        log.info("消息发送成功发送成功" + dto);
                    } else {
                        intance = creatAndRun(server.getHost(), server.getPort());
                        intance.channel.writeAndFlush(dto);
                        log.error(future.cause());
                        throw new RuntimeException(future.cause());
                    }
                }
            });
        }
    }

    public void shundown() {
        group.shutdownGracefully();
    }
}
