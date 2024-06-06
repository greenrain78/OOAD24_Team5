package app.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Getter
@Component
public class MyInfo {
    @Value("${MY_IP:127.0.0.1}")
    private String ip;
    @Value("${MY_PORT:11120}")
    private int port;
    @Value("${MY_ID:team5}")
    private String id;
    @Value("${MY_X:15}")
    private int x;
    @Value("${MY_Y:20}")
    private int y;

    public Info getInfo() {
        return new Info(id, ip, port);
    }
}
