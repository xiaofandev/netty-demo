package startup;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Date;

public class EchoClient {

	public static Socket connectWithRetry() throws InterruptedException, UnknownHostException, IOException {
		Socket socket = new Socket();
		try {
			socket.connect(new InetSocketAddress("localhost", 8080));
			return socket;
		} catch (Exception e1) {
			socket.close();
			
			System.out.println("连接失败,10秒后尝试第二次连接...");
			Thread.sleep(10000);
			
			Socket sockeet2 = new Socket();
			try {
				sockeet2.connect(new InetSocketAddress("localhost", 8080));
				return sockeet2;
			} catch (Exception e) {
				sockeet2.close();
				
				System.out.println("连接失败，停止尝试");
				return null;
			}
		}
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException, InterruptedException {
		Socket socket = connectWithRetry();
		System.out.println("连接成功");
//		PrintWriter writer = new PrintWriter(socket.getOutputStream());
//		writer.write("xxx");
//		writer.flush();
//		System.out.println("已发送");
		InputStream input = socket.getInputStream();
		byte[] array = new byte[8];
		input.read(array);
		// 字节数组转long
		ByteBuffer buffer = ByteBuffer.allocate(8);
		buffer.put(array, 0, array.length);
		buffer.flip();
		System.out.println(new Date(buffer.getLong()));
		
		input.close();
		socket.close();
		System.out.println("连接已关闭");
	}
}
