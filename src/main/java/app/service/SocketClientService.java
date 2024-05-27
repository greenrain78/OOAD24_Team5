package app.service;

import app.domain.Info;
import app.domain.MyInfo;
import app.socket.SocketClient;
import io.swagger.v3.core.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SocketClientService {

    private final HashMap<String, SocketClient> socketClients = new HashMap<>();

    @Autowired
    private MyInfo myInfo;


    public String getAllSocketClients() {
        List<String> clients = new ArrayList<>(socketClients.keySet());
        return Json.pretty(clients);
    }
    public void addSocketClient(String id, SocketClient socketClient) {
        socketClients.put(id, socketClient);
    }
    public String getMyInfo() {
        return myInfo.getInfo().toString();
    }
}
