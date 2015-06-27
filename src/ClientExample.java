import com.irineu.easysocket.EasySocketClient;
import com.irineu.easysocket.EasySocketConnection;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ClientExample {

    EasySocketClient easySocketClient = new EasySocketClient() {
        @Override
        public void onMessage(EasySocketConnection easySocketConnection, byte[] message) {
            String strMessage = new String(message, StandardCharsets.UTF_8);
            System.out.println(strMessage);
        }

        @Override
        public void onClose(EasySocketConnection easySocketConnection) {

        }

        @Override
        public void onConnection(EasySocketConnection easySocketConnection) {
        }
    };

    public ClientExample() throws IOException {
        easySocketClient.connect("127.0.0.1",8124);
    }
}
