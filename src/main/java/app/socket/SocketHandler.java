package app.socket;

import app.domain.Item;
import app.domain.MyInfo;
import app.domain.SocketMessage;
import app.service.ItemService;
import app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@Component
public class SocketHandler {
    @Autowired
    private ItemService itemService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MyInfo myInfo;

    public void responseStock(SocketMessage msg, PrintWriter output) {
        log.info("responseStock: {}", msg);
        // item 조회
        int itemCode = Integer.parseInt(msg.msg_content().get("item_code"));
        Item item = itemService.getItemByItemCode(itemCode);
        // 응답 메세지 생성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(item.getItemCode()));
        content.put("item_num", String.valueOf(item.getQuantity()));
        SocketMessage response = new SocketMessage("resp_stock", myInfo.getId(), msg.src_id(), content);
        output.println(response.toJson());

    }
    public void responsePayment(SocketMessage msg, PrintWriter output) {
        log.info("responsePayment: {}", msg);
        // 결제 요청
        int itemCode = Integer.parseInt(msg.msg_content().get("item_code"));
        int quantity = Integer.parseInt(msg.msg_content().get("quantity"));
        String code = msg.msg_content().get("cert_code");
        boolean result = paymentService.responsePrePayment(code, itemCode, quantity);
        // 응답 메세지 생성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(itemCode));
        content.put("item_num", String.valueOf(quantity));
        content.put("availability", result ? "T" : "F");
        SocketMessage response = new SocketMessage("resp_prepay", myInfo.getId(), msg.src_id(), content);
        output.println(response.toJson());
    }

    // 메세지가 유효한지 검사
    public boolean isValidMessage(SocketMessage msg, PrintWriter output) {
        if (myInfo.getId().equals(msg.dst_id())) {
            return true;
        } else {
            output.println(createErrorMessage(msg.src_id(), new IllegalArgumentException("Invalid ID")).toJson());
            return false;
        }
    }

    public SocketMessage createErrorMessage(String srcID, Exception e) {
        HashMap<String, String> content = new HashMap<>();
        content.put("error", e.getMessage());
        content.put("error_class", e.getClass().getName());
        return new SocketMessage("error", myInfo.getId(), srcID, content);
    }

}
