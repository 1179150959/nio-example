package com.phei.netty31.dev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class TimeClientHandler extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger( TimeClientHandler.class.getName());

    private final ByteBuf firstMesssage;


    public TimeClientHandler() {

        byte[] req = "QUERY TIME ORDER".getBytes();
        firstMesssage = Unpooled.buffer( req.length );
        firstMesssage.writeBytes( req );

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush( firstMesssage );
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ByteBuf buf = (ByteBuf) msg;
        byte[] req = new byte[ buf.readableBytes() ];
        buf.readBytes( req );
        String body = new String( req, "UTF-8" );
        System.out.println( "Now is : " + body );

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        //释放资源l
        logger.warning("Unexpected exception from downstream : " + cause.getMessage() );
        ctx.close();

    }

}
