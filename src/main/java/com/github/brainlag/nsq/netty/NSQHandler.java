package com.github.brainlag.nsq.netty;

import com.github.brainlag.nsq.Connection;
import com.github.brainlag.nsq.frames.NSQFrame;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.logging.log4j.LogManager;


public class NSQHandler extends SimpleChannelInboundHandler<NSQFrame> {

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        Connection connection = ctx.channel().attr(Connection.STATE).get();
        if(connection != null) {
            LogManager.getLogger(this).info("Channel disconnected! " + connection);
        } else {
            LogManager.getLogger(this).error("No connection set for : " + ctx.channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        LogManager.getLogger(this).error("NSQHandler exception caught", cause);

		ctx.channel().close();
		Connection con = ctx.channel().attr(Connection.STATE).get();
		if (con != null) {
            con.close();
        } else {
			LogManager.getLogger(this).warn("No connection set for : " + ctx.channel());
		}
	}

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, final NSQFrame msg) throws Exception {
        final Connection con = ctx.channel().attr(Connection.STATE).get();
        if (con != null) {
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    con.incoming(msg);
                }
            });
        } else {
            LogManager.getLogger(this).warn("No connection set for : " + ctx.channel());
        }
    }
}
