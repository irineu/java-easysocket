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
public abstract class EasySocketHandler {

    private List<EasySocket> activeConnections = new ArrayList<>();
    private AtomicInteger idGenerator = new AtomicInteger();

    public static void send(EasySocket easySocket, byte[] data) throws IOException {
        byte [] header = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(data.length).array();
        byte [] buffer = new byte[header.length + data.length];
        System.arraycopy(header, 0, buffer, 0, header.length);
        System.arraycopy(data, 0, buffer, header.length, data.length);
        easySocket.getSocket().getOutputStream().write(buffer,0,buffer.length);
        easySocket.getSocket().getOutputStream().flush();
    }

    public abstract void onMessage(EasySocket easySocket, byte [] message);
    public abstract void onClose(EasySocket easySocket);

    public final EasySocket addConnection(final Socket socket) throws IOException {
        final EasySocket easySocket = new EasySocket(socket,idGenerator.getAndIncrement());
        activeConnections.add(easySocket);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        byte [] header = new byte[4];
                        easySocket.getInputStream().read(header);
                        int size = ByteBuffer.allocate(4).wrap(header).order(ByteOrder.LITTLE_ENDIAN).getInt();
                        if(size==0)break;
                        easySocket.setMessageSize(size);
                        byte[] message = new byte[easySocket.getMessageSize()];
                        easySocket.getInputStream().readFully(message);
                        onMessage(easySocket, message);
                    }
                    activeConnections.remove(easySocket);
                    onClose(easySocket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        return easySocket;
    }
}