package app.socket;

import app.domain.Code;
import app.domain.SocketMessage;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;

/*
    * 소켓 서버 선결제 테스트
    * - 요청 메세지: req_prepay
    * - 응답 메세지: resp_prepay
    * 사용하는 item_code: 2
 */
@SpringBootTest
public class SocketServerPrepayTest {

    private static Socket socket;
    private static BufferedReader input;
    private static PrintWriter output;

    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private ItemRepository itemRepository;

    @BeforeAll
    public static void setUp() throws Exception {
        // 소켓 서버가 실행될 수 있도록 잠시 대기
        Thread.sleep(1000);
    }
    private static void initClient() throws IOException {
        // 소켓 연결
        socket = new Socket("localhost", 11120);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    @DisplayName("소켓 통신 테스트 - req_prepay 메시지 전송")
    @Test
    public void requestPrepay() throws Exception {
        initClient();
        int ITEM_CODE = 2;
        int TOTAL_QUANTITY = 10;
        int PREPAY_QUANTITY = 3;
        String CERT_CODE = "aaaaa";
        // 선결제 전 제고
        itemRepository.findByItemCode(ITEM_CODE).setQuantity(TOTAL_QUANTITY);

        // 선결제 요청
        HashMap<String, String> prepay_content = new HashMap<>();
        prepay_content.put("item_code", String.valueOf(ITEM_CODE));
        prepay_content.put("quantity", String.valueOf(PREPAY_QUANTITY));
        prepay_content.put("cert_code", CERT_CODE);
        // 메세지 전송 - team1에서 team5로 요청
        SocketMessage message = new SocketMessage("req_prepay", "team1", "team5", prepay_content);
        output.println(message.toJson());
        // 선결제 응답
        SocketMessage resp = SocketMessage.fromJson(input.readLine());
        assert Objects.equals(resp.msg_type(), "resp_prepay") : "응답: " + resp.toJson();
        assert Objects.equals(resp.src_id(), "team5") : "응답: " + resp.toJson();
        assert Objects.equals(resp.dst_id(), "team1") : "응답: " + resp.toJson();
        assert Objects.equals(resp.msg_content().get("item_code"), String.valueOf(ITEM_CODE)) : "응답: " + resp.toJson();
        assert Objects.equals(resp.msg_content().get("item_num"), String.valueOf(PREPAY_QUANTITY)) : "응답: " + resp.toJson();

        // 선결제 후 재고 확인
        int quantity = itemRepository.findByItemCode(ITEM_CODE).getQuantity();
        assert quantity == TOTAL_QUANTITY - PREPAY_QUANTITY : "재고: " + quantity;
        // 인증코드 확인
        Code code = codeRepository.findById(1L).orElse(null);
        assert code != null : "인증코드 없음";
        assert Objects.equals(code.getCode(), CERT_CODE) : "인증코드: " + code;
        assert code.getItemCode() == ITEM_CODE : "인증코드: " + code;
        assert code.getQuantity() == PREPAY_QUANTITY : "인증코드: " + code;
        assert code.getTime() != null : "인증코드: " + code;
    }


    @AfterAll
    public static void tearDown() throws Exception {
        input.close();
        output.close();
        socket.close();
    }
}