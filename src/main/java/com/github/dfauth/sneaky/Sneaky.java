package com.github.dfauth.sneaky;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;


public class Sneaky {

    private static final Logger logger = LoggerFactory.getLogger(Sneaky.class);

    public Sneaky(int serverPort, String hostname, int port) throws IOException {
        String dest = "localhost";
        int destPort = 80;

        ServerSocket sock = new ServerSocket(8080);
        Socket t = sock.accept();
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                InputStream istream = t.getInputStream();
                OutputStream ostream = new Socket(dest, destPort).getOutputStream();
                int i=0;
                while((i = istream.read(buffer, 0, buffer.length)) > 0) {
                    ostream.write(buffer, 0, i);
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                throw new RuntimeException(e);
            }
        }).start();
    }
}
