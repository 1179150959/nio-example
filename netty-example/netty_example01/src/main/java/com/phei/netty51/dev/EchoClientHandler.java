package com.phei.netty51.dev;

import com.phei.netty43.dev.TimeClientHandler;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.logging.Logger;

public class EchoClientHandler  extends ChannelHandlerAdapter {

    private static final Logger logger = Logger.getLogger( TimeClientHandler.class.getName());

    private int counter;

    static final String ECHO_REQ = "Hi, Lilinfeng. Welcome to Netty.$_";

    public EchoClientHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for( int i = 0; i < 100 ; i++ ) {
            ctx.writeAndFlush( Unpooled.copiedBuffer( ECHO_REQ.getBytes() ) );
        }

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        System.out.println( "this is " + ++counter + " times receive server : [" + msg + "]" );

    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {

        ctx.flush();

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        //释放资源l
        logger.warning("Unexpected exception from downstream : " + cause.getMessage() );
        ctx.close();

    }

}
