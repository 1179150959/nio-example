package com.phei.netty.aio;

import com.phei.netty.nio.TimeClientHandle;

public class TimeClient {

    public static void main(String[] args) {

        int port = 8080;
        if ( args != null && args.length > 0 ) {

            try {
                port = Integer.valueOf( args[0] );
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        new Thread( new AsyncTimeClientHandle( "127.0.0.1", port ), "AIO-AsyncTimeClientHandle-001" ).start();

    }

}
