package app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
/*
 * 소켓 메세지 테스트
 * - 단순 메세지 생성 및 파싱 테스트
 * 사용하는 item_code: 7
 */
public class SocketMessageTest {

    @DisplayName("단순 메세지 생성 테스트 - 성공 케이스")
    @Test
    public void testToJson() {
        SocketMessage message = new SocketMessage("type", "src", "dst", new HashMap<>() {{
            put("msg_content", "content");
        }});
        String json = message.toJson();
        assert json.equals("{\"msg_type\":\"type\",\"src_id\":\"src\",\"dst_id\":\"dst\",\"msg_content\":{\"msg_content\":\"content\"}}");
    }

    @DisplayName("단순 메세지 파싱 테스트 - 성공 케이스")
    @Test
    public void testFromJson() {
        String json = "{\"msg_type\":\"type\",\"src_id\":\"src\",\"dst_id\":\"dst\",\"msg_content\":{\"msg_content\":\"content\"}}";
        SocketMessage message = SocketMessage.fromJson(json);
        System.out.println(message);
        assert message.msg_type().equals("type");
        assert message.src_id().equals("src");
        assert message.dst_id().equals("dst");
        assert message.msg_content().get("msg_content").equals("content");
    }

    @DisplayName("재고확인 요청 메세지")
    @Test
    public void requestStockMessage() {
        HashMap<String, String> stock = new HashMap<>();
        stock.put("item_code", "7");
        stock.put("item_num", "3");
        SocketMessage message = new SocketMessage("req_stock", "Team5", "Team1", stock);
        String json = message.toJson();
        System.out.println(json);
    }
    @DisplayName("재고확인 응답 메세지")
    @Test
    public void responseStockMessage() {
        HashMap<String, String> stock = new HashMap<>();
        stock.put("item_code", "7");
        stock.put("item_num", "3");
        stock.put("coor_x", "14");
        stock.put("coor_y", "15");
        SocketMessage message = new SocketMessage("resp_stock", "Team1", "Team5", stock);
        String json = message.toJson();
        System.out.println(json);
    }
    @DisplayName("선결제 요청 메세지")
    @Test
    public void requestPaymentMessage() {
        HashMap<String, String> payment = new HashMap<>();
        payment.put("item_code", "7");
        payment.put("item_num", "3");
        payment.put("cert_code", "a12345");
        SocketMessage message = new SocketMessage("req_prepay", "Team5", "Team1", payment);
        String json = message.toJson();
        System.out.println(json);
    }
    @DisplayName("선결제 가능 여부 응답 메세지")
    @Test
    public void responsePaymentMessage() {
        HashMap<String, String> payment = new HashMap<>();
        payment.put("item_code", "7");
        payment.put("item_num", "3");
        payment.put("availability", "T");
        SocketMessage message = new SocketMessage("resp_prepay", "Team1", "Team5", payment);
        String json = message.toJson();
        System.out.println(json);
    }
}
