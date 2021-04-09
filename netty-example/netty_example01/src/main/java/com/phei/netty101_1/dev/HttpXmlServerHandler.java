package com.phei.netty101_1.dev;

import com.phei.netty101_1.dev.req.HttpXmlRequest;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpXmlServerHandler extends SimpleChannelInboundHandler< HttpXmlRequest > {


    @Override
    protected void messageReceived(ChannelHandlerContext ctx, HttpXmlRequest httpXmlRequest ) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        cause.printStackTrace();
        if ( ctx.channel().isActive() ) {
            sendError( ctx, INTERNAL_SERVER_ERROR );
        }

    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {

        FullHttpResponse response = new DefaultFullHttpResponse( HTTP_1_1, status ,
                Unpooled.copiedBuffer( "失败：" + status.toString() + "\r\n", CharsetUtil.UTF_8));
        response.headers().set( CONTENT_TYPE, "text/plain;charset=UTF-8" );
        ctx.writeAndFlush( response ).addListener(ChannelFutureListener.CLOSE );

    }
}
