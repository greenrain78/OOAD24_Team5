package app.controller;

import app.domain.Info;
import app.domain.OrderRequest;
import app.service.SocketClientService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/socket")
public class SocketClientController {

    @Autowired
    private SocketClientService socketClientService;

    @GetMapping("/client/{id}/items")
    public ResponseEntity<Object> getItemsByDVM(@PathVariable String id) {
        try {
            Map<String, String> result = socketClientService.getItems(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
//    @PostMapping("/client/{id}/prepay")
//    public ResponseEntity<Object> prepay(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
//        try {
//            Map<String, String> result = socketClientService.prepay(id, orderRequest);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("error", e);
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }
    @GetMapping("/client/{id}")
    public ResponseEntity<Object> getInfoByDVM(@PathVariable String id) {
        try {
            Info result = socketClientService.getInfoByID(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
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
    @DeleteMapping("/client/{id}")
    public ResponseEntity<Object> deleteDVM(@PathVariable String id) {
        try {
            socketClientService.deleteInfo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
