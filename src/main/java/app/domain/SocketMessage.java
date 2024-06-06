package app.domain;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.HashMap;

@Getter
public class SocketMessage {
    private final String msg_type;
    private final String src_id;
    private final String dst_id;
    private final HashMap<String, String> msg_content;

    public SocketMessage(String msgType, String srcId, String dstId, HashMap<String, String> msgContent) {
        msg_type = msgType;
        src_id = srcId;
        dst_id = dstId;
        msg_content = msgContent;
    }

    public static SocketMessage fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, SocketMessage.class);
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
    @Override
    public String toString() {
        return "SocketMessage{" +
                "msg_type='" + msg_type + '\'' +
                ", src_id='" + src_id + '\'' +
                ", dst_id='" + dst_id + '\'' +
                ", msg_content=" + msg_content +
                '}';
    }
}
