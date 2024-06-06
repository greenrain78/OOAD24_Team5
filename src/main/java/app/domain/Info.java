package app.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info {
    private String id;
    private String ip;
    private int port;

    public Info(String id, String ip, int port) {

        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String toString() {
        return "Info{" +
                "id='" + id + '\'' +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                '}';
    }
}
