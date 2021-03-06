package com.phei.netty71.dev;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class SubReqClientHandler extends ChannelHandlerAdapter {


    public SubReqClientHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        for ( int i = 0 ; i < 10 ; i++ ) {
            ctx.writeAndFlush( SubReq( i ) );
        }
        ctx.flush();

    }

    private Object SubReq(int i) {

        SubscribeReq req = new SubscribeReq();
        req.setAddress( "南京市江宁区方山国家地质公园" );
        req.setPhoneNumber( "138XXXXXXXX" );
        req.setProductName( "Netty 权威指南" );
        req.setSubReqID( i );
        req.setUserName( "Lilinfeng" );

        return req;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println( "Receive server response : [" + msg + "]" );
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        ctx.close();

    }


}
