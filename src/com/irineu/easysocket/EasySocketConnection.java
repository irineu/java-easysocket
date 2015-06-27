package com.irineu.easysocket;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by irineuantunes on 6/27/15.
 */
public class EasySocketConnection {
    private Socket socket;
    private int messageSize = -1;
    private int id;
    private DataInputStream inputStream;

    public EasySocketConnection(Socket socket, int id) throws IOException {
        this.socket = socket;
        this.inputStream = new DataInputStream(socket.getInputStream());
        this.id = id;
    }

    public int getMessageSize() {
        return messageSize;
    }

    public void setMessageSize(int messageSize) {
        this.messageSize = messageSize;
    }

    public Socket getSocket() {
        return socket;
    }

    public DataInputStream getInputStream() {
        return inputStream;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasySocketConnection)) return false;

        EasySocketConnection that = (EasySocketConnection) o;

        return getId() == that.getId();

    }

    @Override
    public int hashCode() {
        return getId();
    }
}
