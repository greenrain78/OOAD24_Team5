package app.socket;

import app.domain.SocketMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

public class SocketClient {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public SocketClient(String id, Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.output = new PrintWriter(socket.getOutputStream(), true);
    }

    public SocketMessage requestStock(String src_id, String dst_id, int item_code, int quantity) {

        // item 조회
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(item_code));
        content.put("quantity", String.valueOf(quantity));
        SocketMessage msg = new SocketMessage("req_stock", src_id, dst_id, content);

        output.println(msg.toJson());

        // 재고 확인 응답
        try {
            SocketMessage resp = SocketMessage.fromJson(input.readLine());
            if (!resp.msg_type().equals("resp_stock")) {
                throw new RuntimeException("Invalid response: " + resp.toJson());
            }
            return resp;
        } catch (IOException e) {
            throw new RuntimeException("Error reading response", e);
        }
    }
}
