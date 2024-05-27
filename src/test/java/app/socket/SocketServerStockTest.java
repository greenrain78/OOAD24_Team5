package app.socket;

import app.domain.SocketMessage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Objects;
/*
    * 소켓 서버 조회 테스트
    * - 요청 메세지: req_stock
    * - 응답 메세지: resp_stock
    * 사용하는 item_code: 1
 */
@SpringBootTest
public class SocketServerStockTest {

    private static Socket socket;
    private static BufferedReader input;
    private static PrintWriter output;

    @BeforeAll
    public static void setUp() throws Exception {
        // 소켓 서버가 실행될 수 있도록 잠시 대기
        Thread.sleep(1000);
        System.out.println("SocketServerStockTest.setUp");
    }


    public void initClient() throws IOException {
        // 소켓 연결
        socket = new Socket("localhost", 11120);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(socket.getOutputStream(), true);
    }

    @DisplayName("소켓 통신 테스트 - Hello 메시지 전송")
    @Test
    public void testSocketCommunication() throws Exception {
        initClient();
        // 메세지 전송
        output.println("Hello");
        // 응답 확인
        String response = input.readLine();
        String expected = "{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"Invalid message format\"}}";
        assert Objects.equals(response, expected) : "실제 값: " + response;
    }
    @DisplayName("소켓 통신 테스트 - req_stock 메시지 전송")
    @Test
    public void requestStock() throws Exception {
        initClient();
        // 메세지 구성
        HashMap<String, String> stock = new HashMap<>();
        stock.put("item_code", "1");
        stock.put("item_num", "3");
        // 메세지 전송 - team1에서 team5로 요청
        SocketMessage message = new SocketMessage("req_stock", "team1", "team5", stock);
        output.println(message.toJson());
        // 응답 확인
        SocketMessage resp = SocketMessage.fromJson(input.readLine());
        assert Objects.equals(resp.msg_type(), "resp_stock") : "응답: " + resp.toJson();
        assert Objects.equals(resp.src_id(), "team5") : "응답: " + resp.toJson();
        assert Objects.equals(resp.dst_id(), "team1") : "응답: " + resp.toJson();
        assert Objects.equals(resp.msg_content().get("item_code"), "1") : "응답: " + resp.toJson();
        assert Objects.equals(resp.msg_content().get("item_num"), "10") : "응답: " + resp.toJson();
    }

    @DisplayName("소켓 통신 테스트 - dst_id가 다른 경우 req_stock 메시지 전송")
    @Test
    public void requestStockDifferentId() throws Exception {
        initClient();
        // 메세지 구성
        HashMap<String, String> stock = new HashMap<>();
        stock.put("item_code", "2");
        stock.put("item_num", "3");
        // 메세지 전송
        SocketMessage message = new SocketMessage("req_stock", "team1", "team6", stock);
        output.println(message.toJson());
        // 응답 확인
        SocketMessage resp = SocketMessage.fromJson(input.readLine());
        assert Objects.equals(resp.msg_type(), "error") : "응답: " + resp.toJson();
        assert Objects.equals(resp.src_id(), "team5") : "응답: " + resp.toJson();
        assert Objects.equals(resp.dst_id(), "team1") : "응답: " + resp.toJson();
        assert Objects.equals(resp.msg_content().get("error"), "Invalid ID") : "응답: " + resp.toJson();
    }

    @AfterAll
    public static void tearDown() throws Exception {
        // 소켓 종료
        input.close();
        output.close();
        socket.close();
    }
}