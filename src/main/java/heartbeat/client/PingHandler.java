package heartbeat.client;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class PingHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		CharSequence respMsg = buf.readCharSequence(4, Charset.defaultCharset());
		if ("pong".equals(respMsg)) {
			System.out.println("收到服务端心跳响应");
		}
		buf.release();
	}
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
		if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.WRITER_IDLE){
				// 心跳时间内没有向服务器写消息
				// 发送ping消息
				System.out.println("长时间未收到服务器消息，发送心跳...");
				
				String ping = "ping";
				ByteBuf buf = ctx.alloc().buffer(4);
				buf.writeBytes(ping.getBytes());
				
				ChannelFuture f = ctx.writeAndFlush(buf);
				f.addListener(new ChannelFutureListener() {
					
					@Override
					public void operationComplete(ChannelFuture future) throws Exception {
						System.out.println("成功发送心跳");
					}
				});
			}
		}
	}
}
