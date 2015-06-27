package com.irineu.easysocket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {

    static int port = 3000;

    static EasySocketHandler easySocketHandler = new EasySocketHandler(){

        @Override
        public void onMessage(EasySocket easySocket, byte[] message) {
            String strMessage = new String(message, StandardCharsets.UTF_8);
            System.out.println(strMessage);
            strMessage = "This is a server reply for: " + strMessage;
            try {
                EasySocketHandler.send(easySocket,strMessage.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(EasySocket easySocket) {
            System.out.println("Lost connection with clientId: "+easySocket.getId());
        }
    };

    public static void main(String[] args) {

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Server Started! waiting connections...");
            while(true){
                Socket socketClient = server.accept();
                EasySocket easySocket =  easySocketHandler.addConnection(socketClient);
                System.out.println("New client connected! id:"+easySocket.getId()+"@" + easySocket.getSocket().getRemoteSocketAddress().toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
