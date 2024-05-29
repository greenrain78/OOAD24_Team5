package app.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Getter
@Component
public class MyInfo {
    @Value("${MY_IP}")
    private String ip;
    @Value("${MY_PORT}")
    private int port;
    @Value("${MY_ID}")
    private String id;
    @Value("${MY_X}")
    private int x;
    @Value("${MY_Y}")
    private int y;

    public Info getInfo() {
        return new Info(x, y, id, ip, port);
    }
}
