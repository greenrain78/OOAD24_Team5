package app.service;

import app.adapter.CardCompany;
import app.domain.*;
import app.repository.ItemRepository;
import app.socket.SocketRequester;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final SocketRequester socketRequester = new SocketRequester();
    private final CardCompany cardCompanyProxy = new CardCompany();
    @Autowired
    private MyInfo myInfo;
    @Autowired
    private ItemRepository itemRepository;

//    @Transactional
//    public Map<String, String> prepay(String id, OrderRequest orderRequest) {
//        // 요금 차감
//        Item item = itemRepository.findByItemCode(orderRequest.getItemCode());
//        cardCompanyProxy.requestPayment(orderRequest.getCardNumber(), item.getPrice() * orderRequest.getQuantity());
//        // 인증코드 발급
//        String authCode = "임시 인증코드";
//        // 선결제 요청
//        Info info = socketClients.get(id);
//        try (Socket socket = new Socket(info.getIp(), info.getPort());
//             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
//        ) {
////            Map<String, String> result = socketRequester.prepay(myInfo.getInfo().getId(), info.getId(), orderRequest, authCode, input, output);
//            if (result.isEmpty()) {
//                throw new IllegalArgumentException("Failed to prepay");
//            }
//            if (result.containsKey(null)) {
//                throw new IllegalArgumentException("Failed to prepay");
//            }
//            return result;
//        } catch (IOException e) {
//            e.printStackTrace();
//            throw new IllegalArgumentException("Connection failed");
//        }
//    }
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
            Map<String, String> items = socketRequester.getItems(myInfo.getInfo().getId(), info.getId(), input, output);
            if (items.isEmpty()) {
                throw new IllegalArgumentException("Failed to get items");
            }
            if (items.containsKey(null)) {
                throw new IllegalArgumentException("Failed to get items");
            }
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

    public void deleteInfo(String id) {
        if (socketClients.remove(id) == null) {
            throw new IllegalArgumentException("Not found");
        }
    }


}
