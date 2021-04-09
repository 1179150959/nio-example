package com.phei.netty101_1.dev;

import com.phei.netty101.dev.domain.Order;
import com.phei.netty101_1.dev.req.HttpXmlRequestDecoder;
import com.phei.netty101_1.dev.resp.HttpXmlResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpXmlServer {


    public void run ( final int port ) throws IOException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group( bossGroup, workerGroup )
                    .channel( NioServerSocketChannel.class )
                    .childHandler(new ChannelInitializer< SocketChannel >() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast( "http-decoder", new HttpRequestDecoder());
                            socketChannel.pipeline().addLast( "http-aggregator", new HttpObjectAggregator( 65536 ));
                            socketChannel.pipeline().addLast( "xml-decoder", new HttpXmlRequestDecoder(Order.class,true));

                            socketChannel.pipeline().addLast( "http-encoder", new HttpResponseEncoder());
                            socketChannel.pipeline().addLast( "xml-encoder", new HttpXmlResponseEncoder());
                            socketChannel.pipeline().addLast( "xmlServerHandler", new HttpXmlServerHandler() );

                        }

                    });
            //发起异步连接操作
            ChannelFuture f = b.bind( new InetSocketAddress( port ) ).sync();
            System.out.println( "HTTP 订购服务器启动，网址是： " + "http：//localhost：" + port );
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {

            e.printStackTrace();

        } finally {

            //优雅退出，释放NIO线程组
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();

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

        new HttpXmlServer().run( port );
    }

}
