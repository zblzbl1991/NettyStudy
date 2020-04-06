package com.zbl.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOSocketClient {
	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();

		socketChannel.configureBlocking(false);
		if(!socketChannel.connect(new InetSocketAddress("127.0.0.1",6666))){
//			boolean connected = socketChannel.isConnected();
//			System.out.println("connected:"+connected);
			while(!socketChannel.finishConnect()){
				System.out.println("建立连接中...");
			}
		}

		String str="test.........";
		ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
		socketChannel.write(wrap);
		System.in.read();
	}
}
