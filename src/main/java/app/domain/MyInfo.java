package app.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MyInfo {
    @Value("${MY_IP:127.0.0.1}")
    private String ip;

    @Getter
    @Value("${MY_PORT:11120}")
    private int port;

    @Getter
    @Value("${MY_ID:team5}")
    private String id;

    @Getter
    @Value("${MY_X:15}")
    private int x;

    @Getter
    @Value("${MY_Y:20}")
    private int y;

    public Info getInfo() {
        return new Info(id, ip, port);
    }
}
