package app.socket;

import app.domain.Code;
import app.domain.SocketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class SocketRequester {
    public SocketMessage getStock(int itemCode, String myId, BufferedReader input, PrintWriter output) {
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(itemCode));
        content.put("item_num", "-1");  // 몇개를 넣나 의미가 없으니깐 -1로 설정
        // 메세지 전송 - 재고 확인 요청은 브로드캐스트 방식이므로 dst_id는 0
        SocketMessage message = new SocketMessage("req_stock", myId, "0", content);
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
        content.put("item_num", String.valueOf(code.getQuantity()));
        content.put("cert_code", code.getCode());
        // 메세지 전송
        SocketMessage message = new SocketMessage("req_prepay", myId, dstId, content);
        output.println(message.toJson());
        // 응답 확인
        try {
            SocketMessage resp = SocketMessage.fromJson(input.readLine());
            if (resp == null) {
                throw new RuntimeException("선결제 실패 - 응답이 없음");
            }
            if (!resp.getMsg_content().get("availability").equals("T")) {
                throw new RuntimeException("선결제 실패 - 구매 불가 응답");
            }
        } catch (IOException e) {
            throw new RuntimeException  ("선결제 실패 - 응답 확인 중 오류", e);
        }
    }
}
