package com.phei.netty101_1.dev;

import com.phei.netty101_1.dev.domain.OrderFactory;
import com.phei.netty101_1.dev.req.HttpXmlRequest;
import com.phei.netty101_1.dev.resp.HttpXmlResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import javax.swing.*;

public class HttpXmlClinetHandler extends SimpleChannelInboundHandler< HttpXmlResponse > {


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        HttpXmlRequest request = new HttpXmlRequest( null, OrderFactory.create(123) );
        ctx.writeAndFlush( request );

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg ) throws Exception {

        System.out.println( "The clinet receive response of http header is : "
            + msg.getHttpResponse().headers().names() );
        System.out.println( "The clinet receive response of http body is : "
            + msg.getResult() );

    }


}
