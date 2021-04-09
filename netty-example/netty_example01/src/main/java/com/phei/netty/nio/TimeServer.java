package com.phei.netty.nio;

import com.phei.netty.bio.TimeServerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

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

        MultiPlexerTimeServer timeServer = new MultiPlexerTimeServer(port);

        new Thread( timeServer , "NIO-MultiPlexerTimeServer-001" ).start();

    }
}
