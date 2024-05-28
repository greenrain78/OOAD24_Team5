package app.service;

import app.domain.Info;
import app.domain.MyInfo;
import app.domain.SocketMessage;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

@Service
public class SocketClientService {

    private final HashMap<String, Info> socketClients = new HashMap<>();

    // item code == 1~20
    private final List<String> itemCodeList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
    @Autowired
    private MyInfo myInfo;

    public Info getInfoByID(String id) {
        Info info = socketClients.get(id);
        if (info == null) {
            throw new IllegalArgumentException("Not found");
        }
        return info;
    }
    public Map<String, String> getItems(String id) {
        Info info = getInfoByID(id);
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Map<String, String> items = new HashMap<>();
            itemCodeList.forEach(itemCode -> {
                HashMap<String, String> stock = new HashMap<>();
                stock.put("item_code", itemCode);
                stock.put("item_num", "3");
                // 메세지 전송 - team1에서 team5로 요청
                SocketMessage message = new SocketMessage("req_stock", "team1", "team5", stock);
                output.println(message.toJson());
                // 응답 확인
                try {
                    SocketMessage resp = SocketMessage.fromJson(input.readLine());
                    items.put(resp.msg_content().get("item_code"), resp.msg_content().get("item_num"));
                } catch (IOException e) {
                    items.put(itemCode, "-1");
                }

            });
            return items;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Connection failed");
        }
    }

    public List<Info> getAllInfo() {
        return new ArrayList<>(socketClients.values());
    }
    public Info addInfo(Info info) {
        if (socketClients.containsKey(info.getId())) {
            throw new IllegalArgumentException("Already exists");
        }
        socketClients.put(info.getId(), info);
        return info;
    }
    public String getMyInfo() {
        return myInfo.getInfo().toString();
    }
}
