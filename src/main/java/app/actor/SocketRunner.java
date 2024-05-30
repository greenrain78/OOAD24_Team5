package app.actor;

import app.socket.SocketHandler;
import app.domain.MyInfo;
import app.socket.SocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SocketRunner implements ApplicationRunner {
    @Autowired
    private SocketHandler handler;
    @Autowired
    private MyInfo myInfo;

    @Override
    public void run(ApplicationArguments args) {
        new SocketServer(myInfo.getPort(), handler).start();
    }
}
