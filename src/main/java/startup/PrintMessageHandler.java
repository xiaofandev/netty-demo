package startup;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class PrintMessageHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		ByteBuf b = (ByteBuf) msg;
		System.out.println(msg.getClass().getName());
		System.out.println(b.toString(CharsetUtil.UTF_8));
		b.release();
	}
	
}
