package app.socket;


import app.controller.SocketController;
import app.domain.SocketMessage;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketThread extends Thread {
    private final Socket socket;
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SocketController controller;

    public SocketThread(Socket socket, SocketController controller) {
        this.socket = socket;
        this.controller = controller;
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
        try {
            SocketMessage msg = SocketMessage.fromJson(clientMessage);
            log.info("Received: " + msg);
            // 해당 요청이 유효한지 확인
            if (!controller.isValidMessage(msg, output)) {
                return;
            }
            // 요청 처리
            switch (msg.msg_type()) {
                case "req_stock":
                    controller.requestStock(msg, output);
                    break;
                case "req_prepay":
                    controller.requestPayment(msg, output);
                    break;
                default:
                    log.error("Unknown message type: " + clientMessage);
                    output.println("{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"Unknown message type\"}}");
            }
        } catch (JsonSyntaxException e) {
            log.error("Error parsing message: " + clientMessage);
            output.println("{\"msg_type\":\"error\",\"msg_content\":{\"error\":\"Invalid message format\"}}");
        }



    }
}
