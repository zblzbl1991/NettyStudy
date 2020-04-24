package com.zbl.nio.netty.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.java.Log;

@Log
public class ChatServer {
	public static void main(String[] args) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap().group(bossGroup, workGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 128).childOption(ChannelOption.SO_KEEPALIVE, true).childHandler(new ChannelInitializer<SocketChannel>() {
				/**
				 * This method will be called once the {@link Channel} was registered. After the method returns this instance
				 * will be removed from the {@link ChannelPipeline} of the {@link Channel}.
				 *
				 * @param ch the {@link Channel} which was registered.
				 * @throws Exception is thrown if an error occurs. In that case it will be handled by
				 *                   {@link #exceptionCaught(ChannelHandlerContext, Throwable)} which will by default close
				 *                   the {@link Channel}.
				 */
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					//加入解码器
					pipeline.addLast("decoder", new StringDecoder());
					//加入编码器
					pipeline.addLast("encoder", new StringEncoder());
					pipeline.addLast(new ChatServerHandler());
				}
			});
			log.info("服务器启动...");
			ChannelFuture channelFuture = serverBootstrap.bind(6668).sync();
			channelFuture.channel().closeFuture().sync();
		} finally {
				bossGroup.shutdownGracefully();
				workGroup.shutdownGracefully();
		}
	}

}
