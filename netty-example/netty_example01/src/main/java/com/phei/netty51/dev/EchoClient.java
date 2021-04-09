package com.phei.netty51.dev;

import com.phei.netty43.dev.TimeClient;
import com.phei.netty43.dev.TimeClientHandler;
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
import io.netty.handler.codec.string.StringDecoder;
import java.io.IOException;

//DelimiterBasedFrameDecoder 自定义分隔符 $_
public class EchoClient {

    public void connect( int port, String host ) throws IOException {

        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();

            b.group( group ).channel( NioSocketChannel.class )
                    .option( ChannelOption.TCP_NODELAY, true )
                    .handler( new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {

                            ByteBuf delimiter = Unpooled.copiedBuffer( "$_".getBytes() );
                            socketChannel.pipeline().addLast( new DelimiterBasedFrameDecoder( 1024, delimiter ) );
                            socketChannel.pipeline().addLast( new StringDecoder() );
                            socketChannel.pipeline().addLast( new EchoClientHandler() );


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

        new EchoClient().connect( port, "127.0.0.1" );
    }


}
