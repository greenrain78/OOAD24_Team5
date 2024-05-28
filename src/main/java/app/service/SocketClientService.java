package app.service;

import app.domain.Info;
import app.domain.MyInfo;
import app.domain.SocketMessage;
import app.socket.SocketRequester;
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

    private final SocketRequester socketRequester = new SocketRequester();

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
