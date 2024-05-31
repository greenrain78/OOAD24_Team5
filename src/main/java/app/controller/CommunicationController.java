package app.controller;

import app.domain.Code;
import app.domain.Info;
import app.domain.OrderRequest;
import app.service.CommunicationService;
import app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/socket")
public class CommunicationController {

    @Autowired
    private CommunicationService socketClientService;
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/client/{id}/items")
    public ResponseEntity<Object> getItemsByDVM(@PathVariable String id) {
        log.info("getItemsByDVM: {}", id);
        try {
            Map<String, String> result = socketClientService.getItems(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/client/{id}/prepay")
    public ResponseEntity<Object> prepay(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
        log.info("prepay request: {}", orderRequest);
        try {
            log.info("prepay request: {}", orderRequest);
            Code result = paymentService.requestPrePayment(id, orderRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            log.error("error_message: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/client/{id}")
    public ResponseEntity<Object> getInfoByDVM(@PathVariable String id) {
        log.info("getInfoByDVM: {}", id);
        try {
            Info result = socketClientService.getInfoByID(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/clients")
    public List<Info> getAllDVM() {
        log.info("getAllDVM");
        return socketClientService.getAllInfo();
    }
    @GetMapping("/info")
    public String getMyInfo() {
        log.info("getMyInfo");
        return socketClientService.getMyInfo();
    }

    @PostMapping("/client")
    public ResponseEntity<Object> addDVM(@RequestBody Info info) {
        log.info("addDVM: {}", info);
        try {
            Info result = socketClientService.addInfo(info);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/client/{id}")
    public ResponseEntity<Object> deleteDVM(@PathVariable String id) {
        log.info("deleteDVM: {}", id);
        try {
            socketClientService.deleteInfo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
