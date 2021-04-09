package com.phei.netty.aio;

import com.phei.netty.nio.MultiPlexerTimeServer;

import java.io.IOException;

public class TimeServer {

    public static void main(String[] args) throws IOException {

        int port = 8080;
        if ( args != null && args.length > 0 ) {
            try {
                port = Integer.valueOf( args[0] );
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        AsyncTimeServerHandle timeServer= new AsyncTimeServerHandle(port);

        new Thread( timeServer , "AIO-AsyncTimeServerHandle-001" ).start();

    }
}
