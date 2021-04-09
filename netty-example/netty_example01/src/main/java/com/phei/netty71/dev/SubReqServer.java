package com.phei.netty71.dev;

import com.phei.netty51_1.dev.EchoServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

public class SubReqServer {

    public void bind( int port ) throws IOException {

        //配置服务端的NIO线程组
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();

            b.group( bossGroup, workerGroup )
                    .channel(NioServerSocketChannel.class)
                    .option( ChannelOption.SO_BACKLOG, 100 )
                    .handler( new LoggingHandler( LogLevel.INFO ) )
                    .childHandler(new ChannelInitializer< SocketChannel >() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast( new ObjectDecoder(
                                    1024 * 1024,
                                    ClassResolvers.weakCachingConcurrentResolver(
                                            this.getClass().getClassLoader()
                                    )
                            ) );
                            socketChannel.pipeline().addLast( new ObjectEncoder() );
                            socketChannel.pipeline().addLast( new SubReqServerHandler() );

                        }
                    });

            //绑定端口，同步等待成功
            ChannelFuture f = b.bind( port ).sync();

            //等待服务端监听端口关闭
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            //优雅退出，释放线程池资源
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

        new SubReqServer().bind( port );
    }


}
