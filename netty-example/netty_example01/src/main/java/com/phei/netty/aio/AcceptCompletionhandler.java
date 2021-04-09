package com.phei.netty.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class AcceptCompletionhandler implements CompletionHandler< AsynchronousSocketChannel, AsyncTimeServerHandle > {

    public void completed (AsynchronousSocketChannel result, AsyncTimeServerHandle attachment ) {

        attachment.asynchronousServerSocketChannel.accept( attachment, this );
        ByteBuffer buffer = ByteBuffer.allocate( 1024 );
        result.read( buffer, buffer,new ReadCompletionHandler(result) );

    }


    public void failed( Throwable exc, AsyncTimeServerHandle attachment ) {
        exc.printStackTrace();
        attachment.latch.countDown();

    }

}
