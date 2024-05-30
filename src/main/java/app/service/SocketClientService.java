package app.service;

import app.actor.CardCompany;
import app.domain.*;
import app.repository.ItemRepository;
import app.socket.SocketRequester;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class SocketClientService {

    private final HashMap<String, Info> socketClients = new HashMap<>();

    private final SocketRequester socketRequester = new SocketRequester();
    private final CardCompany cardCompanyProxy = new CardCompany();
    @Autowired
    private MyInfo myInfo;
    @Autowired
    private ItemRepository itemRepository;

    @Transactional(rollbackFor = Exception.class)
    public Code prepay(String id, OrderRequest orderRequest) {
        // 요금 차감
        Item item = itemRepository.findByItemCode(orderRequest.getItemCode());
        cardCompanyProxy.requestPayment(orderRequest.getCardNumber(), item.getPrice() * orderRequest.getQuantity());
        // 인증코드 발급
        String authCode = "임시 인증코드";
        // 선결제 요청
        Info info = socketClients.get(id);
        try (Socket socket = new Socket(info.getIp(), info.getPort());
             BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter output = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Code code = new Code(authCode, LocalDateTime.now(), orderRequest.getItemCode(), orderRequest.getQuantity());
            socketRequester.prepay(myInfo.getInfo().getId(), info.getId(), code, input, output);
            return code;
        } catch (IOException e) {
            // 예외 발생 시 결제 취소
            cardCompanyProxy.cancelPayment(orderRequest.getCardNumber(), item.getPrice() * orderRequest.getQuantity());
            log.error("Connection failed", e);
            throw new IllegalArgumentException("Connection failed");
        } catch (Exception e) {
            // 예외 발생 시 결제 취소
            cardCompanyProxy.cancelPayment(orderRequest.getCardNumber(), item.getPrice() * orderRequest.getQuantity());
            log.error("Failed to prepay", e);
            throw new IllegalArgumentException("Failed to prepay");
        }
    }
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
