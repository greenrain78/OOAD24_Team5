package app.service;

import app.domain.Code;
import app.domain.Info;
import app.domain.MyInfo;
import app.socket.SocketRequester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CommunicationService {

    private final HashMap<String, Info> socketClients = new HashMap<>();
    private final SocketRequester socketRequester = new SocketRequester();
    @Autowired
    private MyInfo myInfo;
    private final List<String> itemCodeList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
            "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
    public Code prepay(String id, String authCode, int itemCode, int quantity) {
        // 선결제 요청
        Info info = socketClients.get(id);
        if (info == null) {
            throw new IllegalArgumentException("Invalid ID");
        }
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Code code = new Code(authCode, LocalDateTime.now(), itemCode, quantity);
            socketRequester.prepay(myInfo.getInfo().getId(), info.getId(), code, input, output);
            return code;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to prepay");
        }
    }
    public Map<String, String> getItems(String id) {
        Info info = getInfoByID(id);
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Map<String, String> items = new HashMap<>();
            for (String itemCode : itemCodeList) {
                int itemNum = socketRequester.getItemByItemCode(Integer.parseInt(itemCode), myInfo.getInfo().getId(), info.getId(), input, output);
                items.put(itemCode, String.valueOf(itemNum));
            }
            if (items.isEmpty() | items.containsKey(null)) {
                throw new IllegalArgumentException("Failed to get items");
            }
            return items;
        } catch (IOException e) {
            log.error("Connection failed", e);
            throw new IllegalArgumentException("Connection failed");
        }
    }
    public int getItemByItemCode(String id, int itemCode) {
        Info info = getInfoByID(id);
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            return socketRequester.getItemByItemCode(itemCode, myInfo.getInfo().getId(), info.getId(), input, output);
        } catch (IOException e) {
            log.error("Connection failed", e);
            throw new IllegalArgumentException("Connection failed");
        }
    }
    public Info getInfoByID(String id) {
        Info info = socketClients.get(id);
        if (info == null) {
            throw new IllegalArgumentException("Not found");
        }
        return info;
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
    public void deleteInfo(String id) {
        if (socketClients.remove(id) == null) {
            throw new IllegalArgumentException("Not found");
        }
    }
}
