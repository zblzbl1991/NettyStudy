package com.zbl.nio.neyInAcion.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EchoServerMain {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup boosGroup = new NioEventLoopGroup();
        try {
            ChannelFuture channelFuture = new ServerBootstrap().group(boosGroup).channel(NioServerSocketChannel.class).localAddress("127.0.0.1",6668).childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast(new EchoServerHandler());
                }
            }).bind().sync();
            log.info("启动成功");
            channelFuture.channel().closeFuture().sync();
        } finally {
            boosGroup.shutdownGracefully();
        }
    }
}
