package com.phei.netty101_1.dev.req;

import com.phei.netty101_1.dev.AbstractHttpXmlDecoder;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.util.List;

import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpXmlRequestDecoder extends AbstractHttpXmlDecoder<FullHttpRequest> {

    public HttpXmlRequestDecoder( Class< ? >  clazz ) {
        this( clazz, false );
    }

    public HttpXmlRequestDecoder( Class< ? >  clazz, boolean isPrint ) {
        super( clazz, isPrint );
    }


    @Override
    protected void decode(ChannelHandlerContext ctx, FullHttpRequest msg, List<Object> out ) throws Exception {

        if ( !msg.getDecoderResult().isSuccess() ) {
            sendError( ctx, BAD_REQUEST );
            return;
        }

        HttpXmlRequest request = new HttpXmlRequest( msg,decode0( ctx, msg.content() ) );
        out.add( request );

    }

    private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {

        FullHttpResponse response = new DefaultFullHttpResponse( HTTP_1_1, status,
                Unpooled.copiedBuffer( "Failure: " + status.toString() + "\r\n" , CharsetUtil.UTF_8 ) );

        response.headers().set( CONTENT_TYPE, "text/plain;charset=UTF-8" );
        ctx.writeAndFlush( response ).addListener( ChannelFutureListener.CLOSE );


    }

}
