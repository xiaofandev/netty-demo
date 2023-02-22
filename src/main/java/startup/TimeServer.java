package startup;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {

	private final int port;
	private final ChannelInboundHandler inboundHandler;
	
	public TimeServer(int port, ChannelInboundHandler inboundHandler) {
		this.port = port;
		this.inboundHandler = inboundHandler;
	}
	
	public void run() {
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
			 .channel(NioServerSocketChannel.class)
			 .childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(inboundHandler);
				}
			})
			 .option(ChannelOption.SO_BACKLOG, 1024)
			 .childOption(ChannelOption.SO_KEEPALIVE, true);
			
			ChannelFuture f = b.bind(port).sync();
		    f.channel().closeFuture().sync();
		} catch (Exception e) {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) {
		int port = 8080;
		ChannelInboundHandler handler = new TimeServerHandler();
		TimeServer s = new TimeServer(port, handler);
		s.run();
	}
}
