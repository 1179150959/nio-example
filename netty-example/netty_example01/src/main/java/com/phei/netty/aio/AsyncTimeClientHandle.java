package com.phei.netty.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

public class AsyncTimeClientHandle implements CompletionHandler<Void, AsyncTimeClientHandler>, Runnable {


    private AsynchronousSocketChannel client;
    private String host;
    private int port;
    private CountDownLatch latch;

    public AsyncTimeClientHandle( String host, int port ) {

    }

    @Override
    public void run() {

        latch = new CountDownLatch( 1 );
        /*client.connect( new InetSocketAddress( host, port ), this, this );*/

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void completed(Void result, AsyncTimeClientHandler attachment) {

        byte[] req = "QUERY TIME ORDER".getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate( req.length );
        writeBuffer.put( req );
        writeBuffer.flip();

        client.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {

                if ( attachment.hasRemaining() ) {
                    client.write( attachment, attachment, this );

                } else {

                    ByteBuffer readBuffer = ByteBuffer.allocate( 1024 );
                    client.read(readBuffer, readBuffer, new CompletionHandler<Integer, ByteBuffer>() {
                        @Override
                        public void completed(Integer result, ByteBuffer attachment) {

                            attachment.flip();
                            byte[] bytes = new byte[ attachment.remaining() ];
                            attachment.get( bytes );

                            String body;

                            try {
                                body = new String( bytes, "UTF-8" );
                                System.out.println( "now is : " + body );
                                latch.countDown();
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void failed(Throwable exc, ByteBuffer attachment) {

                            try {
                                client.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    });

                }

            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {

                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    @Override
    public void failed(Throwable exc, AsyncTimeClientHandler attachment) {

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
