package app.socket;

import app.controller.SocketServerController;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
@Slf4j
public class SocketServer extends Thread {
    private final SocketServerController controller;
    private final int port;

    public SocketServer(int port, SocketServerController controller) {
        this.controller = controller;
        this.port = port;
    }
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Socket Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new SocketThread(socket, controller).start();
            }
        } catch (Exception e) {
            log.error("Error starting server", e);
        }
    }
}
