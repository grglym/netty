package example3.server.handle;

import example3.server.SimpleChatServer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SimpleChatServerHandler extends SimpleChannelInboundHandler<String> {
    public static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //super.handlerAdded(ctx);
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] - " + incoming.remoteAddress() + " 加入\n");
        channels.add(incoming);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //super.handlerRemoved(ctx);
        Channel incoming = ctx.channel();
        channels.writeAndFlush("[SERVER] = " + incoming.remoteAddress() + " 离开\n");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String s) throws Exception {
        Channel incoming = channelHandlerContext.channel();
        for(Channel channel : channels) {
            if(channel != incoming) {
                channel.writeAndFlush("[" + incoming.remoteAddress() +"] "+ s + "\n");
            } else {
                channel.writeAndFlush("[you] " + s + "\n");
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //super.channelInactive(ctx);
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 掉线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //super.exceptionCaught(ctx, cause);
        Channel incoming = ctx.channel();
        System.out.println("SimpleChatClient: " + incoming.remoteAddress() + " 异常");
    }
}
