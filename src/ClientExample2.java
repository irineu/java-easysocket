import com.irineu.easysocket.EasySocketClient;
import com.irineu.easysocket.EasySocketConnection;

import java.io.IOException;

public class ClientExample2 extends EasySocketClient{

    @Override
    public void onMessage(EasySocketConnection easySocketConnection, byte[] message) {

    }

    @Override
    public void onClose(EasySocketConnection easySocketConnection) {

    }

    @Override
    public void onConnection(EasySocketConnection easySocketConnection) {

    }

    public ClientExample2() throws IOException {
        connect("127.0.0.1",8124);
    }
}
