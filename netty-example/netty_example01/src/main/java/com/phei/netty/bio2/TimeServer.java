package com.phei.netty.bio2;

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

        ServerSocket server = null;
        try {
            server = new ServerSocket( port );
            System.out.println("this time server is start in port " + port );

            Socket socket = null;
            TimeServerHandlerExecutePool singleExecutor = new TimeServerHandlerExecutePool( 50, 10000 );
            while( true ) {
                socket = server.accept();
                singleExecutor.execute( new TimeServerHandler( socket ) );
            }
        } finally {

            if ( server != null ) {
                System.out.println(" the time server close ");
                server.close();
                server = null;
            }
        }
    }

}
