package com.zbl.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

	private ServerSocketChannel serverSocketChannel;
	private Selector selector;
	private static final int PORT = 6666;

	public GroupChatServer() throws IOException {
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
		serverSocketChannel.configureBlocking(false);
		selector = Selector.open();
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void listener() {
		while (true) {
			try {
				int count = selector.select(2000);
				if (count > 0) {
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectionKeys.iterator();
					while (iterator.hasNext()) {
						SelectionKey selectionKey = iterator.next();
						if (selectionKey.isAcceptable()) {
							SocketChannel accept = serverSocketChannel.accept();
							accept.register(selector, SelectionKey.OP_READ);
							accept.configureBlocking(false);
							SocketAddress remoteAddress = accept.getRemoteAddress();
							System.out.println("客户端:" + remoteAddress.toString() + "上线了");
//							selectionKey
						}
						if (selectionKey.isReadable()) {
							readData(selectionKey);

						}
						iterator.remove();
					}
				} else {
					System.out.println("持续等待连接..");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void readData(SelectionKey selectionKey) {
		SocketChannel channel = null;
		try {
			channel = (SocketChannel) selectionKey.channel();
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			int count = channel.read(byteBuffer);
			if (count > 0) {
				String s = new String(byteBuffer.array());
				System.out.println(s);
				//向其他客户端发送消息,并排除自己
				sendMessageToOtherClient(s,channel);
			}

		} catch (IOException e) {
			try {
				System.out.println(channel.getRemoteAddress()+"离线...");
				selectionKey.cancel();
				channel.close();
			} catch (IOException ex) {

					ex.printStackTrace();
			}
			e.printStackTrace();
		}

	}
	public void sendMessageToOtherClient(String msg,SocketChannel self) throws IOException {
		System.out.println("转发消息...");
		Set<SelectionKey> selectionKeys = selector.selectedKeys();
		for (SelectionKey selectionKey : selectionKeys) {
			Channel channel = selectionKey.channel();
			if(channel instanceof SocketChannel&& channel!=self){
				SocketChannel dest = (SocketChannel) channel;
				ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
				dest.write(wrap);
			}
		}
	}

	public static void main(String[] args) {

	}
}
