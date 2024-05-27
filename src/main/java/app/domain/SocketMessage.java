package app.domain;

import com.google.gson.Gson;
import lombok.Getter;

import java.util.HashMap;

public class SocketMessage {
    private final String msg_type;
    private final String src_id;
    private final String dst_id;
    private final HashMap<String, String> msg_content;
    private static final Gson gson = new Gson();

    public SocketMessage(String msgType, String srcId, String dstId, HashMap<String, String> msgContent) {
        msg_type = msgType;
        src_id = srcId;
        dst_id = dstId;
        msg_content = msgContent;
    }

    public static SocketMessage fromJson(String json) {
        return gson.fromJson(json, SocketMessage.class);
    }

    public String toJson() {
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
    public String msg_type() {
        return msg_type;
    }
    public String src_id() {
        return src_id;
    }
    public String dst_id() {
        return dst_id;
    }
    public HashMap<String, String> msg_content() {
        return msg_content;
    }
}
