package com.phei.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;

public class TimeServerHandler implements Runnable {

    private Socket socket;

    public TimeServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;

        try {
            in = new BufferedReader( new InputStreamReader( this.socket.getInputStream()));
            out = new PrintWriter( this.socket.getOutputStream() , true );

            String currentTime = null;
            String body = null;

            while( true ) {
                body = in.readLine();
                if ( body == null )
                    break;

                System.out.println( "the time server receive order : " + body  );

                currentTime = " QUERY TIME ORDER".equalsIgnoreCase( body ) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
                out.println( currentTime );
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                if ( in != null ) {
                    in.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            try {
                if ( out != null ) {
                    out.close();
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            try {
                if ( socket != null ) {
                    socket.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }

            this.socket = null;

        }

    }

}
