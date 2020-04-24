package com.zbl.nio.netty.chat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.java.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

@Log
public class ChatServerHandler extends SimpleChannelInboundHandler<String> {
	//定义一个channel组
	//GlobalEventExecutor 全局事件执行器
	static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

		Channel channel = ctx.channel();
		channels.forEach(ch->{
			if(channel!=ch){
				ch.writeAndFlush(sdf.format(new Date())+"[客户]"+channel.remoteAddress()+"说:"+msg+"\n");
			}else{
				ch.writeAndFlush(sdf.format(new Date())+"[你]说:"+msg+"\n");
			}
		});

	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channels.writeAndFlush("[客户端]"+ channel.remoteAddress()+"加入了聊天\n");
		channels.add(channel);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端{}进行了连接"+ctx.channel().id().asLongText());

	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.info("客户端{}断开了连接"+ctx.channel().id().asLongText());
	}
	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		channels.writeAndFlush("[客户端]"+channel.remoteAddress()+"离开了聊天\n");
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//		cause.printStackTrace();
		ctx.close();
	}
}
