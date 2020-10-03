package com.jin.server;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.jin.dto.RPCDto;
import com.jin.properties.RpcServerConfigProperties;
import com.jin.serializable.RPCDtoDecoder;
import com.jin.serializable.RPCDtoEncoder;
import com.jin.utils.ApplicationContextUtils;
import com.jin.utils.HessianUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * creat by Jin 2020/9/2 21:12
 *
 * @Description:
 */
@Slf4j
public class RpcNettyServer implements InitializingBean {
    private static boolean flag = true;


    private RpcServerConfigProperties properties;

    public RpcNettyServer(RpcServerConfigProperties properties) {
        this.properties = properties;
    }

    public void run() {
        //创建BossGroup 和 WorkerGroup
        //说明
        //1. 创建两个线程组 bossGroup 和 workerGroup
        //2. bossGroup 只是处理连接请求 , 真正的和客户端业务处理，会交给 workerGroup完成
        //3. 两个都是无限循环
        //4. bossGroup 和 workerGroup 含有的子线程(NioEventLoop)的个数
        //   默认实际 cpu核数 * 2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(properties.getBossgroupnums());
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象，配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //使用链式编程来进行设置
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)//使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, properties.getThreadLong()) // 设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, properties.getKeepalive())
                    //                    .handler(null) // 该 handler对应 bossGroup , childHandler 对应 workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            log.info("客户socketchannel hashcode=" + socketChannel.hashCode()); //可以使用一个集合管理 SocketChannel， 再推送消息时，可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
                            //获取到pipeline
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //向pipeline加入解码器
                            pipeline.addLast("decoder", new RPCDtoDecoder());
                            //向pipeline加入编码器
                            pipeline.addLast("encoder", new RPCDtoEncoder());
                            //加入自己的业务处理handler
                            pipeline.addLast(new NettyServerHandler());
                        }
                    });

            //绑定一个端口并且同步, 生成了一个 ChannelFuture 对象
            //启动服务器(并绑定端口)
            ChannelFuture channelFuture = bootstrap.bind(properties.getHost(), properties.getPort()).sync();
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        log.info("监听端口 " + properties.getHost() + ":" + properties.getPort() + " 成功");
                    } else {
                        log.info("监听端口 " + properties.getHost() + ":" + properties.getPort() + " 失败：" + properties.getPort() + "已经被占用");
                    }
                }
            });


            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            throw new RuntimeException("RPC 服务服务端创建失败" + properties.getHost() + ":" + properties.getPort());
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            this.run();
        }, "netty-server").start();

    }

    public void sendMessage(ChannelHandlerContext ctx, RPCDto msg) {
        try {
            ctx.channel().writeAndFlush(Unpooled.copiedBuffer(HessianUtils.serialize(msg)));
        } catch (Exception ex) {
            log.info("消息发送失败消息ID：" + msg.getId());
        }
    }
}
