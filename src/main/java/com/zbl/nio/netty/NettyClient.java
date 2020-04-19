package com.zbl.nio.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
	public static void main(String[] args) throws InterruptedException {
		//创建一个事件循环组
		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		//客户端使用BootStrap
		try {
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(workGroup).channel(NioSocketChannel.class)//设置通道实现类
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast(new NettyClientHandler());
						}
					});
			System.out.println("客户端准备好了");
			//连接服务端
			ChannelFuture channelFuture = bootstrap.connect("127.0.0.1", 6666).sync();
			//监听关闭通道监听
			channelFuture.channel().closeFuture().sync();
		}finally {
			workGroup.shutdownGracefully();
		}
	}
}
