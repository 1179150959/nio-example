package com.phei.netty101_1.dev;

import com.phei.netty101_1.dev.domain.Order;
import com.phei.netty101_1.dev.resp.HttpXmlResponseDecoder;
import com.phei.netty101_1.dev.resp.HttpXmlResponseEncoder;
import com.phei.netty51.dev.EchoClient;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpXmlClient {

    public void connect( int port ) throws IOException {

        //配置客户端NIO线程组
        EventLoopGroup group =  new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
            b.group( group ).channel( NioSocketChannel.class )
                    .option( ChannelOption.TCP_NODELAY, true )
                    .handler(new ChannelInitializer< SocketChannel >() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast( "http-decoder", new HttpResponseDecoder() );
                            socketChannel.pipeline().addLast(  "http-aggregator", new HttpObjectAggregator( 65536) );
                            socketChannel.pipeline().addLast( "xml-decoder",
                                    new HttpXmlResponseDecoder( Order.class, true ) );
                            socketChannel.pipeline().addLast( "http-encoder", new HttpRequestEncoder());
                            socketChannel.pipeline().addLast( "xml-encoder", new HttpXmlResponseEncoder());
                            socketChannel.pipeline().addLast( "xmlClinetHandler", new HttpXmlClinetHandler() );

                        }

                    });

            //发起异步连接操作
            ChannelFuture f = b.connect( new InetSocketAddress( port ) ).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {

            e.printStackTrace();

        } finally {

            //优雅退出，释放NIO线程组
            group.shutdownGracefully();

        }
    }


    public static void main(String[] args) throws IOException {

        int port = 8080;
        if ( args != null && args.length > 0 ) {
            try {
                port = Integer.valueOf( args[0] );
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        new HttpXmlClient().connect( port );
    }

}
