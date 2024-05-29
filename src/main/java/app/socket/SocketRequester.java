package app.socket;

import app.domain.Code;
import app.domain.OrderRequest;
import app.domain.SocketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SocketRequester {
    private final List<String> itemCodeList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
    public Map<String, String> getItems(String myId, String dstId, BufferedReader input, PrintWriter output) {
        Map<String, String> items = new HashMap<>();
        itemCodeList.forEach(itemCode -> {
            HashMap<String, String> stock = new HashMap<>();
            stock.put("item_code", itemCode);
            stock.put("item_num", "3");
            // 메세지 전송 - team1에서 team5로 요청
            SocketMessage message = new SocketMessage("req_stock", myId, dstId, stock);
            output.println(message.toJson());
            // 응답 확인
            try {
                SocketMessage resp = SocketMessage.fromJson(input.readLine());
                items.put(resp.msg_content().get("item_code"), resp.msg_content().get("item_num"));
            } catch (IOException e) {
                items.put(itemCode, "-1");
            }
        });
        return items;
    }


    public void prepay(String myId, String dstId, Code code, BufferedReader input, PrintWriter output) {
        // 메세지 구성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(code.getItemCode()));
        content.put("quantity", String.valueOf(code.getQuantity()));
        content.put("cert_code", code.getCode());
        // 메세지 전송 - team1에서 team5로 요청
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
