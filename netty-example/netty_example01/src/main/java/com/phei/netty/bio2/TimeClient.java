package com.phei.netty.bio2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            socket = new Socket( "132.147.3.175" , port );
            in = new BufferedReader( new InputStreamReader( socket.getInputStream() ) );
            out = new PrintWriter( socket.getOutputStream(), true );

            out.print( "QUERY TIME ORDER" );
            System.out.println( "send order 2 server succeed" );
            String resp = in.readLine();
            System.out.println( "now is :" + resp );
        } catch ( Exception e) {
            e.printStackTrace();
        } finally {

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

            socket = null;
        }

    }
}
