package pro.eugw.lessoncountdownserver;

import java.io.*;
import java.net.Socket;

import static pro.eugw.lessoncountdownserver.Main.log;

public class ClientWorker implements Runnable {

    private Socket client;

    ClientWorker(Socket c) {
        client = c;
    }

    @Override
    public void run() {
        try {
            new RequestHandler(client);
        } catch (IOException e) {
            log().error("Accept failed: 4444");
            e.printStackTrace();
        }
    }

}
