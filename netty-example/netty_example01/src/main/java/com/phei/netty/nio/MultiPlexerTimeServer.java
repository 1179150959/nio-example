package com.phei.netty.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

public class MultiPlexerTimeServer implements Runnable {

    private Selector selector;

    private ServerSocketChannel servChannel;

    private volatile boolean stop;

    public MultiPlexerTimeServer(int port) {

        try {
            selector = Selector.open();
            servChannel = ServerSocketChannel.open();
            servChannel.configureBlocking( false );
            servChannel.socket().bind( new InetSocketAddress( port ), 1024 );
            servChannel.register(selector, SelectionKey.OP_ACCEPT );
            System.out.println( "the time server is start in port :" + port );
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1 );
        }

    }

    public void stop(){
        this.stop = true;
    }

    @Override
    public void run() {

        while( !stop ) {

            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.keys();
                Iterator<SelectionKey> it =  selectionKeys.iterator();
                SelectionKey key = null;
                while ( it.hasNext() ) {

                    key = it.next();
                    it.remove();
                    try {
                        handleInput( key );
                    } catch (Exception e) {
                        e.printStackTrace();
                        if ( key != null ) {
                            key.cancel();
                            if ( key.channel() != null )
                                key.channel().close();
                        }
                    }


                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if ( selector != null ) {
                try {
                    selector.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void handleInput(SelectionKey key) throws IOException {

        //??????????????????????????????
        if ( key.isValid() ) {

            if ( key.isAcceptable() ) {
                ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
                SocketChannel sc = ssc.accept();
                sc.configureBlocking( false );
                sc.register( selector, SelectionKey.OP_READ );
            }

            if ( key.isReadable() ) {

                SocketChannel sc = (SocketChannel)key.channel();
                ByteBuffer readBuffer = ByteBuffer.allocate( 1024 );
                int readBytes = sc.read( readBuffer );

                if ( readBytes > 0 ) {

                    readBuffer.flip();
                    byte[] bytes = new byte[ readBuffer.remaining() ];
                    readBuffer.get( bytes );
                    String body = new String( bytes, "UTF-8" );
                    System.out.println( "the time server receive order : " + body );
                    String currentTime = "QUERY TIME ORDER".equalsIgnoreCase( body ) ? new java.util.Date( System.currentTimeMillis()).toString() : "BAD ORDER";
                    doWrite( sc, currentTime);

                } else if ( readBytes < 0 ) {
                    //??????????????????
                    key.cancel();
                    sc.close();
                } else {
                    //?????? 0 ????????????
                }

            }

        }

    }

    private void doWrite(SocketChannel channel, String response ) throws IOException {

        if ( response != null && response.trim().length() > 0 ) {

            byte[] bytes = response.getBytes();
            ByteBuffer writeBuffer = ByteBuffer.allocate( bytes.length );
            writeBuffer.put( bytes );
            writeBuffer.flip();
            channel.write( writeBuffer );
        }

    }
}
