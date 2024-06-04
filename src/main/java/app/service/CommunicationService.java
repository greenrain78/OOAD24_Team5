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

    public Code prepay(String authCode, int itemCode, int quantity) {
        // 거리순으로 자판기 정렬
        List<Info> infos = new ArrayList<>(socketClients.values());
        infos.sort((a, b) -> {
            double aDist = Math.pow(a.getX() - myInfo.getInfo().getX(), 2) + Math.pow(a.getY() - myInfo.getInfo().getY(), 2);
            double bDist = Math.pow(b.getX() - myInfo.getInfo().getX(), 2) + Math.pow(b.getY() - myInfo.getInfo().getY(), 2);
            return Double.compare(aDist, bDist);
        });
        // 선결제 요청
        for (Info info : infos) {
            log.info("try to prepay to {}:{} distance: {}", info.getIp(), info.getPort(), Math.sqrt(Math.pow(info.getX() - myInfo.getInfo().getX(), 2) + Math.pow(info.getY() - myInfo.getInfo().getY(), 2)));
            try (Socket socket = new Socket(info.getIp(), info.getPort());
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
            ) {
                Code code = new Code(authCode, LocalDateTime.now(), itemCode, quantity);
                socketRequester.prepay(myInfo.getInfo().getId(), info.getId(), code, input, output);
                return code;
            } catch (Exception e) {
                log.error("Connection failed", e);
            }
        }
        throw new IllegalArgumentException("Failed to prepay");
    }
    public Map<String, String> getItems(String id) {
        Info info = socketClients.get(id);
        if (info == null) {
            throw new IllegalArgumentException("Not found");
        }
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
    public HashMap<String, Integer> getItemByItemCode(int itemCode) {
        // 모든 자판기에 대해서 itemCode에 해당하는 재고 조회
        HashMap<String, Integer> items = new HashMap<>();
        for (Info info : socketClients.values()) {
            try (Socket socket = new Socket(info.getIp(), info.getPort());
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
            ) {
                int itemNum = socketRequester.getItemByItemCode(itemCode, myInfo.getInfo().getId(), info.getId(), input, output);
                items.put(info.getId(), itemNum);
            } catch (IOException e) {
                log.error("Connection failed", e);
                items.put(info.getId(), -1);
            }
        }
        return items;
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
