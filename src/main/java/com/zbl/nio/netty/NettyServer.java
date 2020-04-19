package com.zbl.nio.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {
	public static void main(String[] args) throws InterruptedException {

		NioEventLoopGroup bossGroup = new NioEventLoopGroup();
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)//使用NioSerSocketChannel作为服务器通道实现
					.option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到的连接个数
					.childOption(ChannelOption.SO_KEEPALIVE, true)//设置连接保持活动状态
					.childHandler(new ChannelInitializer<SocketChannel>() {//给workGroup设置channel对象
						//给对应channel设置处理器
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new NettyServerHandler());
						}
					});
			System.out.println("服务器配置好了");
			//绑定一个端口并同步处理
			ChannelFuture channelFuture = serverBootstrap.bind(6666).sync();
			//对关闭通道进行监听
			channelFuture.channel().closeFuture().sync();
		}finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
}
