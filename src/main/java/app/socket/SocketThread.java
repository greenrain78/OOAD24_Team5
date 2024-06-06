package app.socket;


import app.domain.SocketMessage;
import com.google.gson.JsonSyntaxException;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
@Slf4j
public class SocketThread extends Thread {
    private final Socket socket;
    private final SocketHandler handler;

    public SocketThread(Socket socket, SocketHandler handler) {
        this.socket = socket;
        this.handler = handler;
    }

    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String clientMessage;
            log.info("New client connected");
            while ((clientMessage = input.readLine()) != null) {
                handleRequest(clientMessage, output);
            }
        } catch (Exception e) {
            log.error("Error handling client", e);
        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                log.error("Error closing socket", e);
            }
        }

    }
    private void handleRequest(String clientMessage, PrintWriter output) {
        SocketMessage msg;
        // 메세지 파싱
        try {
            msg = SocketMessage.fromJson(clientMessage);
            log.info("Received: " + msg);
        } catch (JsonSyntaxException e) {
            log.error("Error parsing message: " + clientMessage);
            output.println("{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"Invalid message format\"}}");
            return;
        }
        try {
            // 요청 처리
            switch (msg.getMsg_type()) {
                case "req_stock":
                    handler.responseStock(msg, output);
                    break;
                case "req_prepay":
                    handler.responsePayment(msg, output);
                    break;
                default:
                    log.error("Unknown message type: " + clientMessage);
                    output.println("{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"Unknown message type\"}}");
            }
        } catch (Exception e) {
            log.error("Error processing message: " + clientMessage, e);
            output.println("{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"" + e.getMessage() + "\",\"error_class\":\"" + e.getClass().getName() + "\"}}");
        }
    }
}
