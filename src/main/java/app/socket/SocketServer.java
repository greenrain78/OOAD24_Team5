package app.socket;

import app.controller.SocketController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
public class SocketServer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SocketController controller;
    private final int port;

    public SocketServer(int port, SocketController controller) {
        this.controller = controller;
        this.port = port;
    }
    public void run() {
        System.out.println("Server is starting" + port);
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Server is listening on port " + port);
                while (true) {
                    Socket socket = serverSocket.accept();
                    new SocketThread(socket, controller).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
