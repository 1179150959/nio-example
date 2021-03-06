package com.phei.netty71.dev;

import com.phei.netty51.dev.EchoClient;
import com.phei.netty51.dev.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;

import java.io.IOException;

public class SubReqClient {


    public void connnect( int port, String host ) throws IOException {

        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();

            b.group( group ).channel( NioSocketChannel.class )
                    .option( ChannelOption.TCP_NODELAY, true )
                    .handler( new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            socketChannel.pipeline().addLast( new ObjectDecoder(
                                    1024 * 1024 ,
                                    ClassResolvers.weakCachingConcurrentResolver( this.getClass().getClassLoader() )
                            ) );
                            socketChannel.pipeline().addLast( new ObjectEncoder());
                            socketChannel.pipeline().addLast( new SubReqClientHandler() );


                        }
                    } );
            //发起异步连接操作
            ChannelFuture f = b.connect( host, port ).sync();

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

        new SubReqClient().connnect( port, "127.0.0.1" );
    }

}
