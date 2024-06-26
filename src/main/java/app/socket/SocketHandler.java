package app.socket;

import app.domain.Item;
import app.domain.MyInfo;
import app.domain.SocketMessage;
import app.service.ManagementService;
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
    private ManagementService managementService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MyInfo myInfo;

    public void responseStock(SocketMessage msg, PrintWriter output) {
        log.info("responseStock: {}", msg);
        // 소문자로
        String dstId = msg.getDst_id();
        // 내게 온 메세지인지 확인 or 0번으로 온 메세지인지 확인
        if (!myInfo.getId().equals(dstId) && !"0".equals(dstId)) {
            HashMap<String, String> content = new HashMap<>();
            content.put("error", "Invalid ID");
            output.println(new SocketMessage("error", myInfo.getId(), msg.getSrc_id(), content).toJson());
            return;
        }
        // item 조회
        int itemCode = Integer.parseInt(msg.getMsg_content().get("item_code"));
        Item item = managementService.getItemByItemCode(itemCode);
        // 응답 메세지 생성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.format("%02d", itemCode));
        content.put("item_num", String.valueOf(item.getQuantity()));
        content.put("coor_x", String.valueOf(myInfo.getX()));
        content.put("coor_y", String.valueOf(myInfo.getY()));
        SocketMessage response = new SocketMessage("resp_stock", myInfo.getId(), msg.getSrc_id(), content);
        log.info("responseStockMsg: {}", response);
        output.println(response.toJson());

    }
    public void responsePayment(SocketMessage msg, PrintWriter output) {
        log.info("responsePayment: {}", msg);
        // 내게 온 메세지인지 확인
        String dstId = msg.getDst_id();
        if (!myInfo.getId().equals(dstId)) {
            HashMap<String, String> content = new HashMap<>();
            content.put("error", "Invalid ID");
            output.println(new SocketMessage("error", myInfo.getId(), msg.getSrc_id(), content).toJson());
            return;
        }
        // 결제 요청
        int itemCode = Integer.parseInt(msg.getMsg_content().get("item_code"));
        int quantity = Integer.parseInt(msg.getMsg_content().get("item_num"));
        String code = msg.getMsg_content().get("cert_code");
        boolean result = paymentService.responsePrePayment(code, itemCode, quantity);
        // 응답 메세지 생성
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.format("%02d", itemCode));
        content.put("item_num", String.valueOf(quantity));
        content.put("availability", result ? "T" : "F");
        SocketMessage response = new SocketMessage("resp_prepay", myInfo.getId(), msg.getSrc_id(), content);
        log.info("responseStockMsg: {}", response);
        output.println(response.toJson());
    }

}
