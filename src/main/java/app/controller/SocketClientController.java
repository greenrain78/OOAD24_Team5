package app.controller;

import app.domain.Info;
import app.service.SocketClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/socket")
public class SocketClientController {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private SocketClientService socketClientService;

    @GetMapping("/clients")
    public List<Info> getAllDVM() {
        return socketClientService.getAllInfo();
    }
    @GetMapping("/info")
    public String getMyInfo() {
        return socketClientService.getMyInfo();
    }
    @PostMapping("/client")
    public String  addDVM(Info info) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        try {
            Info result =  socketClientService.addInfo(info);
            response.put("status", "success");
            response.put("data", result);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        }
        return objectMapper.writeValueAsString(response);
    }
}
