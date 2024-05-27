package app.controller;

import app.domain.Info;
import app.domain.SocketMessage;
import app.service.SocketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.util.HashMap;

@Component
public class SocketController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SocketService socketService;

    private final Info myInfo = new Info(15, 14, "team5", "127.0.0.1", 12345);

    public void requestStock(SocketMessage msg, PrintWriter output) {
        try {
            // ID 확인
            checkID(msg.dst_id());
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
        try {
            // ID 확인
            checkID(msg.dst_id());
            // 결제 요청
            int itemCode = Integer.parseInt(msg.msg_content().get("item_code"));
            int quantity = Integer.parseInt(msg.msg_content().get("quantity"));
            HashMap<String, String> content = socketService.requestPrePayment(itemCode, quantity);
            SocketMessage response = new SocketMessage("resp_prepay", myInfo.getId(), msg.src_id(), content);
            output.println(response.toJson());
        } catch (Exception e) {
            output.println(createErrorMessage(msg.src_id(), e).toJson());
        }
    }
    private void checkID(String dstId) {
        if (!dstId.equals(myInfo.getId())) {
            throw new IllegalArgumentException("Invalid ID");
        }
    }
    private SocketMessage createErrorMessage(String srcID, Exception e){
        // 모든 오류를 처리하도록 하면 종종 JDBC 관련 오류가 발생하여 테스트가 실패할 수 있습니다.
        // 이런 경우 예외 자체를 반환하면 알아서 처리하여 잘 되는 것 같습니다.
//        if (e instanceof IllegalArgumentException || e instanceof IllegalStateException) {
//            throw e;
//        }
        HashMap<String, String> content = new HashMap<>();
        content.put("error", e.getMessage());
        content.put("error_class", e.getClass().getName());
        return new SocketMessage("error", myInfo.getId(), srcID, content);
    }

}
