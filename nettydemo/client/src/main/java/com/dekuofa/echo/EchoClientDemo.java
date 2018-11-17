package com.dekuofa.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author dekuofa <br>
 * @date 2018-11-17 <br>
 */
public class EchoClientDemo {

    private static final String EXIT_COMMAND = "exit()";

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)// 注意此处使用的类与 EchoServer 中的不同
                    .handler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                @Override
                                protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
                                    System.out.println("接收到来自服务端的消息:" + msg.toString(CharsetUtil.UTF_8));
                                }
                            });
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 8000);
            channelFuture.addListener(future -> {
                if (future.isSuccess()) {
                    System.out.println("连接服务端成功！");

                    startConsoleCommand(channelFuture.channel());
                } else {
                    System.err.println("连接服务端失败！");
                    future.cause().printStackTrace();
                }
            });
            channelFuture.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }

    }

    private static void startConsoleCommand(Channel channel) {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                String msg = scanner.nextLine();
                // 输入退出命令，关闭 channel
                if (EXIT_COMMAND.equals(msg)) {
                    System.out.println("接受退出指令，断开与服务端的连接。");
                    channel.close();
                    break;
                }
                ByteBuf byteBuf = channel.alloc().buffer();
                byteBuf.writeBytes(msg.getBytes(CharsetUtil.UTF_8));
                channel.writeAndFlush(byteBuf).addListener(future -> {
                    if (future.isSuccess()) {
                        System.out.println("发送成功");
                    } else {
                        future.cause().printStackTrace();
                    }
                });
            }
        }).start();
    }

}
