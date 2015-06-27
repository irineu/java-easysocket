package com.irineu.easysocket;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by irineuantunes on 6/27/15.
 */
public abstract class EasySocketClient extends EasySocket{

    public EasySocketConnection connect(String host, int port) throws IOException {
        Socket socket = new Socket(host,port);
        EasySocketConnection easySocketConnection = addConnection(socket);
        onConnection(easySocketConnection);
        return easySocketConnection;
    }
}
