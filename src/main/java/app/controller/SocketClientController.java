package app.controller;

import app.domain.Info;
import app.service.SocketClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Object> addDVM(@RequestBody Info info) {
        try {
            Info result = socketClientService.addInfo(info);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
