package com.zbl.nio;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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

		ByteBuffer allocate = ByteBuffer.allocate((int)file.length());
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
		ByteBuffer allocate = ByteBuffer.allocate((int)file.length());
		while(true){
			allocate.clear();
			int read = channel.read(allocate);
			if(read==-1){
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
}
