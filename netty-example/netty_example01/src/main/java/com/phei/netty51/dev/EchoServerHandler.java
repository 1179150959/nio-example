package com.phei.netty51.dev;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.IOException;
import java.util.Date;

public class EchoServerHandler extends ChannelHandlerAdapter {

    private int counter = 0;

    public void channelRead(ChannelHandlerContext ctx, Object msg ) throws IOException {

        String body = (String) msg;
        System.out.println( "this is " + ++counter + " times receive client : [" + body + "]" );
        body += "$_";
        ByteBuf echo = Unpooled.copiedBuffer( body.getBytes() );
        ctx.writeAndFlush( echo );

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        //发生异常关闭链路
        ctx.close();
    }
}
