package pro.eugw.lessoncountdownserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {

    static Logger log() {
        return LogManager.getLogger();
    }

    private Main() {
        log().info("Starting server");
        ServerSocket server = null;
        try {
            server = new ServerSocket(4444);
        } catch (IOException e) {
            log().error("Could not listen on port 4444");
            System.exit(-1);
        }
        while (true) {
            ClientWorker worker;
            try{
                worker = new ClientWorker(server.accept());
                Thread thread = new Thread(worker);
                thread.start();
            } catch (IOException e) {
                log().error("Accept failed: 4444");
                System.exit(-1);
            }
        }
    }

    public static void main(String[] args) {
        new Main();
    }

}
