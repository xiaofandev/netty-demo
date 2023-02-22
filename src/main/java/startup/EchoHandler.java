package startup;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

@Sharable
public class EchoHandler extends ChannelInboundHandlerAdapter{

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String resp = "HTTP/1.1 200 Success\r\n";
		final ByteBuf b = ctx.alloc().buffer(resp.getBytes().length);
		b.writeBytes(resp.getBytes());
		ChannelFuture f = ctx.writeAndFlush(b);
		f.addListener(new ChannelFutureListener() {
			
			@Override
			public void operationComplete(ChannelFuture future) throws Exception {
				System.out.println("发送完成");
				ctx.close();
			}
		});
	}
	
}
