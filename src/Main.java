import servers.HttpTaskServer;
import servers.KVServer;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        KVServer kVServer = new KVServer();
        HttpTaskServer httpTaskServer = new HttpTaskServer();
        kVServer.start();
        httpTaskServer.start();
    }
}

