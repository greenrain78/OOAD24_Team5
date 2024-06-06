package app.service;

import app.domain.Code;
import app.domain.Info;
import app.domain.MyInfo;
import app.domain.SocketMessage;
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

    public HashMap<String, Object> prepay(String authCode, int itemCode, int quantity) {
        // 모든 자판기에 재고 확인 요청
        List<SocketMessage> stockResponses = new ArrayList<>();
        for (Info info : socketClients.values()) {
            try (Socket socket = new Socket(info.getIp(), info.getPort());
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
            ) {
                SocketMessage res = socketRequester.getStock(itemCode, myInfo.getInfo().getId(), input, output);
                stockResponses.add(res);
            } catch (IOException e) {
                log.error("선결제 요청 중 재고 확인 요청 실패", e);
            }
        }
        // 재고가 부족한 경우 제외
        List<SocketMessage> availableClients = new ArrayList<>();
        for (SocketMessage stockResponse : stockResponses) {
            // 응답 메세지가 없는 경우
            if (stockResponse == null) {
                continue;
            }
            // 재고가 부족한 경우
            int itemNum = Integer.parseInt(stockResponse.getMsg_content().get("item_num"));
            if (itemNum < quantity) {
                continue;
            }
            // 재고가 충분한 경우
            availableClients.add(stockResponse);
        }
        // 거리순으로 자판기 정렬
        availableClients.sort((a, b) -> {
            double ax = Double.parseDouble(a.getMsg_content().get("coor_x"));
            double ay = Double.parseDouble(a.getMsg_content().get("coor_y"));
            double bx = Double.parseDouble(b.getMsg_content().get("coor_x"));
            double by = Double.parseDouble(b.getMsg_content().get("coor_y"));
            double distanceA = Math.sqrt(Math.pow(ax, 2) + Math.pow(ay, 2));
            double distanceB = Math.sqrt(Math.pow(bx, 2) + Math.pow(by, 2));
            return Double.compare(distanceA, distanceB);
        });
        // 선결제 요청
        for (SocketMessage availableClient : availableClients) {
            Info info = socketClients.get(availableClient.getSrc_id());
            try (Socket socket = new Socket(info.getIp(), info.getPort());
                 BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
            ) {
                Code code = new Code(authCode, LocalDateTime.now(), itemCode, quantity);
                socketRequester.prepay(myInfo.getInfo().getId(), info.getId(), code, input, output);
                HashMap<String, Object> result = new HashMap<>();
                result.put("code", code);
                result.put("info", info);
                result.put("x", availableClient.getMsg_content().get("coor_x"));
                result.put("y", availableClient.getMsg_content().get("coor_y"));
                return result;

            } catch (Exception e) {
                log.error("선결제 요청 중 선결제 실패", e);
            }
        }
        System.out.println("stockResponses:" + stockResponses);
        System.out.println("availableClients:" + availableClients);
        throw new IllegalArgumentException("모든 자판기에 선결제 요청을 실패했습니다.");
    }
//    아래는 모니터링 서비스를 위한 메소드들입니다.
    public Map<String, String> getItems(String id) {
        List<String> itemCodeList = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                "11", "12", "13", "14", "15", "16", "17", "18", "19", "20");
        Info info = socketClients.get(id);
        if (info == null) {
            throw new IllegalArgumentException("해당 ID가 존재하지 않음");
        }
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Map<String, String> items = new HashMap<>();
            for (String itemCode : itemCodeList) {
                SocketMessage res = socketRequester.getStock(Integer.parseInt(itemCode), myInfo.getInfo().getId(), input, output);
                if (res == null) {
                    items.put(itemCode, "-1");
                } else {
                    items.put(itemCode, res.getMsg_content().get("item_num"));
                }
            }
            return items;
        } catch (IOException e) {
            log.error("재고 확인 요청 실패", e);
            throw new IllegalArgumentException("재고 확인 요청 실패");
        }
    }

    public List<Info> getAllInfo() {
        return new ArrayList<>(socketClients.values());
    }
    public Info addInfo(Info info) {
        if (socketClients.containsKey(info.getId())) {
            throw new IllegalArgumentException("이미 존재하는 ID");
        }
        socketClients.put(info.getId(), info);
        return info;
    }
    public String getMyInfo() {
        return myInfo.getInfo().toString();
    }
    public void deleteInfo(String id) {
        if (socketClients.remove(id) == null) {
            throw new IllegalArgumentException("해당 ID가 존재하지 않음");
        }
    }
}
