# java-easysocket
Maybe you alerady hear about some socket frameworks like Apache Mina, Netty or [???]... They are really easy and have a lot of features, BUT 1 or 2 years ago i've got problems with truncated messages, the reason is of my messages was not trasporting simple strings, but binary data, so a \r\n terminator IS THE WRONG and WORST solution for my problem. If i just set a read buffer size, my problem will not be solved, because my messages have variated sizes and the truncated problem will be back... The final solution: implement a codec for a basic need.

But i really love the "Event handlers" of Apache Mina and NodeJS, is a clean and simple way to solve a lot of repetitive problems of trigger Threads for handle connections and read incoming data. Plus i have implemented a simple protocol to transport each message with 4 bytes on header to specify the total size of the message for solve the initial problem on NodeJS and the result of this mix of simple and cool things is: **_java-easysocket_**

### Client Example
##### Example 1 : Object
```java
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
            /*
                Here you can handle any kind of binary data!
                Can be a String (JSON, CSV, XML..)
                Serialized Java Object
                Protobuf
                Etc...
            */
        }

        @Override
        public void onClose(EasySocketConnection easySocketConnection) {
            //onClose handler
        }

        @Override
        public void onConnection(EasySocketConnection easySocketConnection) {
            //onConnection handler
        }
    };

    public ClientExample() throws IOException {
        easySocketClient.connect("127.0.0.1",8124);
    }
}
```
##### Example 2 : Inheritance
```java
import com.irineu.easysocket.EasySocketClient;
import com.irineu.easysocket.EasySocketConnection;

import java.io.IOException;

public class ClientExample2 extends EasySocketClient{

    @Override
    public void onMessage(EasySocketConnection easySocketConnection, byte[] message) {
        /*
            Here you can handle any kind of binary data!
            Can be a String (JSON, CSV, XML..)
            Serialized Java Object
            Protobuf
            Etc...
        */
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
```
### Server Example

```java
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
```
Well.. i'm not will be the Mr. Obvious today, you can use the inheritance way for the server too (:

###NOTE

This is the first version, does not have filters or loggers like apache mina, i will improve that lib soon...
Ihave created this repo, the core, sleeped, awaked and finished in less than 12 hours LOL, please be patient (:
