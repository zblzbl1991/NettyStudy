package com.zbl.nio.netty.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class ChatClient {
	public static void main(String[] args) throws InterruptedException {

		NioEventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			Bootstrap handler = new Bootstrap().group(workGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeline = ch.pipeline();
					//加入解码器
					pipeline.addLast("decoder", new StringDecoder());
					//加入编码器
					pipeline.addLast("encoder", new StringEncoder());
					pipeline.addLast(new ChatClientHandler());

				}
			});
			ChannelFuture sync = handler.connect("127.0.0.1", 6668).sync();
			Channel channel = sync.channel();
			Scanner scanner = new Scanner(System.in);
			while (scanner.hasNextLine()){
				String s = scanner.nextLine();
				channel.writeAndFlush(s+"\r\n");
			}
//			sync.channel().closeFuture().sync();
		} finally {
			workGroup.shutdownGracefully();
		}

	}
}
