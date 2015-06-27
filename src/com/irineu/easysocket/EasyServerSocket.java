package com.irineu.easysocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by irineuantunes on 6/27/15.
 */
public abstract class EasyServerSocket extends EasySocket {

    private int port;
    private ServerSocket serverSocket;

    public abstract void onListening();
    public abstract void onConnection(EasySocketConnection easySocketConnection);

    public EasyServerSocket listen(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        onListening();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    Socket socketClient = null;
                    try {
                        socketClient = serverSocket.accept();
                        EasySocketConnection easySocketConnection =  addConnection(socketClient);
                        onConnection(easySocketConnection);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        return this;
    }
}
