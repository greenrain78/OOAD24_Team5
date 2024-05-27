package app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info {
    private int x;
    private int y;
    private String id;
    private String ip;
    private int port;

    public Info(int x, int y, String id, String ip, int port) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Info{" +
                "x=" + x +
                ", y=" + y +
                ", id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
