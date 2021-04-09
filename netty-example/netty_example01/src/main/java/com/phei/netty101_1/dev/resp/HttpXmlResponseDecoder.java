package com.phei.netty101_1.dev.resp;

import com.phei.netty101_1.dev.AbstractHttpXmlDecoder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

import java.util.List;

public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {


    public HttpXmlResponseDecoder(Class<?> clazz) {
        this( clazz, true );
    }

    public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrint) {
        super(clazz, isPrint);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out ) throws Exception {

        HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse( msg, decode0( ctx, msg.content() ) );
        out.add( resHttpXmlResponse );

    }


}
