package com.irineu.easysocket;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by irineuantunes on 6/26/15.
 */
public abstract class EasySocket {

    private List<EasySocketConnection> activeConnections = new ArrayList<>();
    private AtomicInteger idGenerator = new AtomicInteger();

    public static void send(EasySocketConnection easySocketConnection, byte[] data) throws IOException {
        byte [] header = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data.length).array();
        byte [] buffer = new byte[header.length + data.length];
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(data, 0, buffer, header.length, data.length);
        easySocketConnection.getSocket().getOutputStream().write(buffer,0,buffer.length);
        easySocketConnection.getSocket().getOutputStream().flush();
    }

    public abstract void onMessage(EasySocketConnection easySocketConnection, byte [] message);
    public abstract void onClose(EasySocketConnection easySocketConnection);

    public final EasySocketConnection addConnection(final Socket socket) throws IOException {
        final EasySocketConnection easySocketConnection = new EasySocketConnection(socket,idGenerator.getAndIncrement());
        activeConnections.add(easySocketConnection);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        byte [] header = new byte[4];
                        easySocketConnection.getInputStream().read(header);
                        int size = ByteBuffer.allocate(4).wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt();
                        if(size==0)break;
                        easySocketConnection.setMessageSize(size);
                        byte[] message = new byte[easySocketConnection.getMessageSize()];
                        easySocketConnection.getInputStream().readFully(message);
                        onMessage(easySocketConnection, message);
                    }
                    activeConnections.remove(easySocketConnection);
                    onClose(easySocketConnection);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return easySocketConnection;
    }
}