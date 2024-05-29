package app.socket;

import app.controller.SocketServerController;
import app.domain.MyInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketRunner implements ApplicationRunner {
    @Autowired
    private SocketServerController controller;
    @Autowired
    private MyInfo myInfo;

    @Override
    public void run(ApplicationArguments args) {
        SocketServer socketServer = new SocketServer(myInfo.getPort(), controller);
        socketServer.run();
    }
}
