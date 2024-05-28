package app.socket;

import app.controller.SocketServerController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
public class SocketServer {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SocketServerController controller;
    private final int port;

    public SocketServer(int port, SocketServerController controller) {
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
