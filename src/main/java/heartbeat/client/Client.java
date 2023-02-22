package heartbeat.client;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

public class Client {

	public static void main(String[] args) {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
			 .channel(NioSocketChannel.class)
			 .option(ChannelOption.SO_KEEPALIVE, true)
			 .handler(new ChannelInitializer<SocketChannel>() {
	
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("idle_event", new IdleStateHandler(5, 10, 60, TimeUnit.SECONDS));
					ch.pipeline().addLast("ping", new PingHandler());
					ch.pipeline().addLast("retry", new RetryHandler());
				}
			 });
		
			ChannelFuture f = b.connect("localhost", 8080).sync();
			f.addListener(new ChannelFutureListener() {
				
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (!future.isSuccess()) {
						// 重新连接
						System.out.println("连接失败，重新连接...");
					} else {
						System.out.println("连接成功");
					}
				}
			});
			f.channel().closeFuture().sync();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
			
		}
	}
}
