package app.domain;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Getter
@Component
public class MyInfo {
    @Value("${my.info.ip}")
    private String ip;
    @Value("${server.port}")
    private int port;
    @Value("${my.info.id}")
    private String id;
    @Value("${my.info.x}")
    private int x;
    @Value("${my.info.y}")
    private int y;

    public Info getInfo() {
        return new Info(x, y, id, ip, port);
    }
}
