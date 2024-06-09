package app.socket;

import app.domain.SocketMessage;
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
    * 소켓 서버 테스트
    * 소켓 서버를 2개를 생성하여 테스트
    * DB와 기타 다른 부분은 공유함
    * 1번 서버는 spring의 SocketRunner를 사용하여 실행 --> item_code: 3
    * 2번 서버는 별도로 생성하여 실행                  --> item_code: 4
 */
@SpringBootTest
public class SocketServerTest {

    @Autowired
    private SocketHandler controller;
    private static SocketServer otherServer;

    public void setUp() throws InterruptedException {
        // 소켓 서버가 실행 중이 아니면 실행
        if (otherServer == null) {
            otherServer = new SocketServer(8080, controller);
            otherServer.start();
            Thread.sleep(1000);
        }
    }
    @DisplayName("별도 생성된 소켓 서버 테스트 - 조회")
    @Test
    public void testSocketServer() throws InterruptedException {
        setUp();
        try (Socket socket = new Socket("localhost", 8080);
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
            // 메세지 구성
            HashMap<String, String> content = new HashMap<>();
            content.put("item_code", "4");
            content.put("item_num", "3");
            SocketMessage message = new SocketMessage("req_stock", "team1", "team5", content);
            // 메세지 전송
            output.println(message.toJson());
            // 응답 확인
            SocketMessage resp = SocketMessage.fromJson(input.readLine());
            assert Objects.equals(resp.getMsg_type(), "resp_stock") : "응답: " + resp.toJson();
            assert Objects.equals(resp.getSrc_id(), "team5") : "응답: " + resp.toJson();
            assert Objects.equals(resp.getDst_id(), "team1") : "응답: " + resp.toJson();
            assert Objects.equals(resp.getMsg_content().get("item_code"), "04") : "응답: " + resp.toJson();
            assert Objects.equals(resp.getMsg_content().get("item_num"), "10") : "응답: " + resp.toJson();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}