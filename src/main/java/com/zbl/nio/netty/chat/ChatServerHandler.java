package com.zbl.nio.netty.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.text.SimpleDateFormat;

public class ChatServerHandler extends SimpleChannelInboundHandler {
	//定义一个channel组
	//GlobalEventExecutor 全局事件执行器
	ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
}
