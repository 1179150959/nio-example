package com.phei.netty101.dev;

import com.phei.netty51.dev.EchoClient;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.io.IOException;

public class HttpFileServer {

    //D:\development\wk\svn-wk01\bigdata\nio-example\netty-example\netty_example01\src\main\java\com\phei\netty71\dev
    //  /nio-example/netty-example/netty_example01/src/main/java/com/phei/netty71/dev/
    private static final String DEFAULT_URL = "/nio-example/netty-example/netty_example01/src/main/java/com/phei/netty71/dev/";

    public void run ( final int port, final String url ) throws IOException {

        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group( boosGroup, workerGroup )
                    .channel( NioServerSocketChannel.class )
                    .childHandler(new ChannelInitializer< SocketChannel >() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast( "http-decoder", new HttpRequestDecoder());
                            socketChannel.pipeline().addLast( "http-aggregator", new HttpObjectAggregator(65536) );
                            socketChannel.pipeline().addLast( "http-encoder", new HttpResponseEncoder());
                            socketChannel.pipeline().addLast(  "http-chunked", new ChunkedWriteHandler());
                            socketChannel.pipeline().addLast( "fileServerHandler", new HttpFileServerHandler(url) );

                        }

                    });

            ChannelFuture f = b.bind( "127.0.0.1", 8080 ).sync();
            System.out.println( "HTTP 文件目录服务器启动，网址是：" + "http://127.0.0.1:" + port + url );
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {

            boosGroup.shutdownGracefully();
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

        String url = DEFAULT_URL;
        if ( args.length > 1 ) {
            url = args[1];
        }

        new HttpFileServer().run( port, url );
    }


}
