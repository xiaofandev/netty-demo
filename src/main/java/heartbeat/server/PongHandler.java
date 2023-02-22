package heartbeat.server;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PongHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		CharSequence reqMsg = buf.readCharSequence(4, Charset.defaultCharset());
		
		if ("ping".equals(reqMsg)) {
			System.out.println("收到心跳请求消息");
			ByteBuf b = ctx.alloc().buffer(4);
			b.writeBytes("pong".getBytes());
			ctx.writeAndFlush(b);
		}
		buf.release();
	}
}
