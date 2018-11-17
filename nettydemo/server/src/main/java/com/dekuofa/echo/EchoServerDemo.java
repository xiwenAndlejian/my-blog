package com.dekuofa.echo;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * @author dekuofa <br>
 * @date 2018-11-16 <br>
 */
public class EchoServerDemo {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<Channel>() {
                        @Override
                        protected void initChannel(Channel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                    // 将从客户端接受到的消息回写给客户端
                                    ctx.writeAndFlush(msg);
                                }
                            });
                        }
                    });
            // 绑定（侦听）8000端口
            ChannelFuture future = bootstrap.bind(8000).sync();
            future.channel().closeFuture().sync();
        } finally {
            // 释放资源
            group.shutdownGracefully();
        }

    }
}

