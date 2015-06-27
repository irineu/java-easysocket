import com.irineu.easysocket.EasyServerSocket;
import com.irineu.easysocket.EasySocket;
import com.irineu.easysocket.EasySocketConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerExample {

    static int port = 3000;

    EasyServerSocket easyServerSocket= new EasyServerSocket(){

        @Override
        public void onListening() {
            System.out.println("Server Started! waiting connections...");
        }

        @Override
        public void onConnection(EasySocketConnection easySocketConnection) {
            System.out.println("New client connected! id:"+ easySocketConnection.getId()+"@" + easySocketConnection.getSocket().getRemoteSocketAddress().toString());
        }

        @Override
        public void onMessage(EasySocketConnection easySocketConnection, byte[] message) {
            String strMessage = new String(message, StandardCharsets.UTF_8);
            System.out.println(strMessage);
            strMessage = "This is a server reply for: " + strMessage;
            try {
                EasySocket.send(easySocketConnection, strMessage.getBytes(StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClose(EasySocketConnection easySocketConnection) {
            System.out.println("Lost connection with clientId: "+ easySocketConnection.getId());
        }
    };

    public ServerExample() throws IOException {
        easyServerSocket.listen(port);
    }
}
