package com.zbl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIOSocketServer {
	public static void main(String[] args) throws IOException {
		//开启ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress("127.0.0.1", 6666));
		//设置非阻塞

		serverSocketChannel.configureBlocking(false);
		//开启selector
		Selector selector = Selector.open();
		//将selector注册到server上
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			if (selector.select(1000) == 0) {
				System.out.println("1秒内没有获取到客户端连接");
				continue;
			}
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = selectionKeys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				if (key.isAcceptable()) {
					//如果是accept,生成一个channel
					SocketChannel socketChannel = serverSocketChannel.accept();

					System.out.println("客户端连接成功,socketchannel" + socketChannel.hashCode());
					socketChannel.configureBlocking(false);
					//注册read事件到selector上
					socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				}
				if (key.isReadable()) {
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer attachment = (ByteBuffer) key.attachment();
					channel.read(attachment);
					System.out.println(new String(attachment.array()));
				}
				iterator.remove();

			}
		}
	}
}
