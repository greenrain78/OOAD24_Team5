package app.socket;

import app.domain.Code;
import app.domain.SocketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SocketRequester {
    public SocketMessage getStock(int itemCode, String myId, String dstId, BufferedReader input, PrintWriter output) {
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(itemCode));
        content.put("item_num", "-1");  // 몇개를 넣나 의미가 없으니깐 -1로 설정
        // 메세지 전송
        SocketMessage message = new SocketMessage("req_stock", myId, dstId, content);
        output.println(message.toJson());
        // 응답 확인
        try {
            return SocketMessage.fromJson(input.readLine());
        } catch (IOException e) {
            return null;
        }
    }

    public void prepay(String myId, String dstId, Code code, BufferedReader input, PrintWriter output) {
        // 메세지 구성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(code.getItemCode()));
        content.put("quantity", String.valueOf(code.getQuantity()));
        content.put("cert_code", code.getCode());
        // 메세지 전송
        SocketMessage message = new SocketMessage("req_prepay", myId, dstId, content);
        output.println(message.toJson());
        // 응답 확인
        try {
            SocketMessage resp = SocketMessage.fromJson(input.readLine());
            if (resp == null) {
                throw new RuntimeException("Failed to prepay - response is null");
            }
            if (!resp.msg_content().get("availability").equals("T")) {
                throw new RuntimeException("Failed to prepay - availability is false");
            }
        } catch (IOException e) {
            throw new RuntimeException  ("Failed to prepay - IOException");
        }
    }
}
