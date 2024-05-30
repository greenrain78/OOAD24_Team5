package app.socket;

import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;
import java.net.Socket;
@Slf4j
public class SocketServer extends Thread {
    private final SocketHandler handler;
    private final int port;

    public SocketServer(int port, SocketHandler handler) {
        this.handler = handler;
        this.port = port;
    }
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            log.info("Socket Server is listening on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                new SocketThread(socket, handler).start();
            }
        } catch (Exception e) {
            log.error("Error starting server", e);
        }
    }
}
