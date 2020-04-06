package com.zbl.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Set;

public class NIOStudy01 {
	public static void main(String[] args) {

	}

	@Test
	public void study01() throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream("e:\\hello.txt");
		FileChannel channel = fileOutputStream.getChannel();
		ByteBuffer allocate = ByteBuffer.allocate(1024);
		allocate.put("helloworld".getBytes());
		allocate.flip();
		channel.write(allocate);
		fileOutputStream.close();

	}

	@Test
	public void study02() throws Exception {
		File file = new File("e:\\hello.txt");
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel channel = fileInputStream.getChannel();

		ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
		channel.read(allocate);
		System.out.println(new String(allocate.array()));

	}

	@Test
	public void study03() throws Exception {
		File file = new File("e:\\hello.txt");
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel channel = fileInputStream.getChannel();
		FileOutputStream fileOutputStream = new FileOutputStream("hello2.txt");
		FileChannel channel1 = fileOutputStream.getChannel();
		ByteBuffer allocate = ByteBuffer.allocate((int) file.length());
		while (true) {
			allocate.clear();
			int read = channel.read(allocate);
			if (read == -1) {
				break;
			}
			allocate.flip();
			channel1.write(allocate);
		}
		fileInputStream.close();
		fileOutputStream.close();
	}

	@Test
	public void study04() throws Exception {
		File file = new File("e:\\hello.txt");
		FileInputStream fileInputStream = new FileInputStream(file);
		FileChannel channel = fileInputStream.getChannel();
		FileOutputStream fileOutputStream = new FileOutputStream("hello3.txt");
		FileChannel channel1 = fileOutputStream.getChannel();
		long l = channel.transferTo(0, file.length(), channel1);
		fileInputStream.close();
		fileOutputStream.close();
	}

	@Test
	public void study05() throws Exception {
		//开启ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(6666));
		//设置非阻塞

		serverSocketChannel.configureBlocking(false);
		//开启selector
		Selector selector = Selector.open();
		//将selector注册到server上
		SelectionKey selectionKey = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		while (true) {
			if (selector.select(1000) == 0) {
				continue;
			}
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			for (SelectionKey key : selectionKeys) {
				if(key.isAcceptable()){
					//如果是accept,生成一个channel
					SocketChannel socketChannel = serverSocketChannel.accept();
					socketChannel.configureBlocking(false);
					//注册read事件到selector上
					 socketChannel.register(selector, SelectionKey.OP_READ,ByteBuffer.allocate(1024));
				}
				if(key.isReadable()){
					SocketChannel channel = (SocketChannel) key.channel();
					ByteBuffer attachment = (ByteBuffer) key.attachment();
					channel.read(attachment);
					System.out.println(new String(attachment.array()));
				}
			}
		}
	}
	@Test
	public   void socketClient() throws IOException {
		SocketChannel socketChannel = SocketChannel.open();

		socketChannel.configureBlocking(false);
		if(!socketChannel.connect(new InetSocketAddress(6666))){
			boolean connected = socketChannel.isConnected();
			System.out.println("connected:"+connected);
			while(!socketChannel.finishConnect()){
				System.out.println("建立连接中...");
			}
		}

		String str="test.........";
		ByteBuffer wrap = ByteBuffer.wrap(str.getBytes());
		socketChannel.write(wrap);
	}


}
