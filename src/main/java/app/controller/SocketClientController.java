package app.controller;

import app.service.SocketClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/socket")
public class SocketClientController {

    @Autowired
    private SocketClientService socketClientService;

    @GetMapping("/clients")
    public String getAllSocketClients() {
        return socketClientService.getAllSocketClients();
    }

    @GetMapping("/info")
    public String getMyInfo() {
        return socketClientService.getMyInfo();
    }
}
