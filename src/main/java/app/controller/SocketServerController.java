package app.controller;

import app.domain.MyInfo;
import app.domain.SocketMessage;
import app.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.HashMap;

@Slf4j
@Component
public class SocketServerController {

    @Autowired
    private SocketService socketService;

    @Autowired
    private MyInfo myInfo;

    public void requestStock(SocketMessage msg, PrintWriter output) {
        log.info("requestStock: {}", msg);
        try {
            // item 조회
            int itemCode = Integer.parseInt(msg.msg_content().get("item_code"));
            HashMap<String, String> content = socketService.requestStock(itemCode);
            SocketMessage response = new SocketMessage("resp_stock", myInfo.getId(), msg.src_id(), content);
            output.println(response.toJson());
        } catch (Exception e) {
            output.println(createErrorMessage(msg.src_id(), e).toJson());
        }
    }

    public void requestPayment(SocketMessage msg, PrintWriter output) {
        log.info("requestPayment: {}", msg);
        try {
            // 결제 요청
            int itemCode = Integer.parseInt(msg.msg_content().get("item_code"));
            int quantity = Integer.parseInt(msg.msg_content().get("quantity"));
            String code = msg.msg_content().get("cert_code");
            HashMap<String, String> content = socketService.requestPrePayment(itemCode, quantity, code);
            SocketMessage response = new SocketMessage("resp_prepay", myInfo.getId(), msg.src_id(), content);
            output.println(response.toJson());
        } catch (Exception e) {
            output.println(createErrorMessage(msg.src_id(), e).toJson());
        }
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
    private SocketMessage createErrorMessage(String srcID, Exception e){
        HashMap<String, String> content = new HashMap<>();
        content.put("error", e.getMessage());
        content.put("error_class", e.getClass().getName());
        return new SocketMessage("error", myInfo.getId(), srcID, content);
    }

}
