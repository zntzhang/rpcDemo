package netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import protocol.RpcRequest;
import protocol.RpcResponse;
import protocol.coder.RpcDecoder;
import protocol.coder.RpcEncoder;

public class Client {
    private ChannelFuture channelFuture;
    private ClientHandler clientHandler;

    public void connect() {
        //EventLoopGroup可以理解为是一个线程池，这个线程池用来处理连接、接受数据、发送数据
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();//客户端引导类
        clientHandler = new ClientHandler();
        bootstrap.group(group)//多线程处理
                .channel(NioSocketChannel.class)//制定通道类型为NioSocketChannel
                .handler(new ChannelInitializer<SocketChannel>() {//业务处理类
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        //注册解码的Handler
                        ch.pipeline().addLast(new RpcDecoder());
                        ch.pipeline().addLast(new RpcEncoder());
                        ch.pipeline().addLast(clientHandler);//注册handler
                    }
                });
        // 4.建立连接
        channelFuture = bootstrap.connect("127.0.0.1", 8888).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
            } else {
                System.err.println("连接失败!");
            }
        });

    }


    /**
     * 写出数据，开始等待唤醒
     */
    public RpcResponse send(RpcRequest rpcRequest) throws InterruptedException {
        channelFuture.channel().writeAndFlush(rpcRequest).await();
        return clientHandler.getRpcResponse(rpcRequest.getRequestId());
    }
}