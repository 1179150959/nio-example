package com.phei.netty101_1.dev;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.http.FullHttpRequest;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IUnmarshallingContext;

import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

public abstract class AbstractHttpXmlDecoder<T> extends MessageToMessageDecoder<T> {

    private IBindingFactory factory ;
    private StringReader reader ;
    private Class< ? > clazz;
    private boolean isPrint;
    private final static String CHARSET_NAME = "UTF-8";
    private final static Charset UTF_8 = Charset.forName( CHARSET_NAME );


    public AbstractHttpXmlDecoder( Class< ? >  clazz ) {
        this( clazz, false );
    }

    public AbstractHttpXmlDecoder( Class< ? >  clazz, boolean isPrint ) {
        this.clazz = clazz;
        this.isPrint = isPrint;
    }

    protected Object decode0(ChannelHandlerContext ctx, ByteBuf body ) throws Exception {

        factory = BindingDirectory.getFactory( clazz );
        String content = body.toString( UTF_8 );
        if ( isPrint ) {
            System.out.println( "The body is : " + content );

        }
        reader = new StringReader( content );
        IUnmarshallingContext uctx = factory.createUnmarshallingContext();
        Object result = uctx.unmarshalDocument( reader );
        reader.close();
        reader = null;
        return result;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {

        if ( reader != null ) {
            reader.close();
            reader = null;
        }

    }


}
