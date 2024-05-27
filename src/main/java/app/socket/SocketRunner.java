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

@Component
public class SocketRunner implements ApplicationRunner {
    @Value("${my.info.socket}")
    private int PORT;
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SocketController controller;

    @Override
    public void run(ApplicationArguments args) {
        SocketServer socketServer = new SocketServer(PORT, controller);
        socketServer.run();
    }
}
