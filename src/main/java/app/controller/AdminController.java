package app.controller;

import app.domain.Info;
import app.service.CommunicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CommunicationService communicationService;

    @GetMapping("/clients")
    public List<Info> getAllDVM() {
        log.info("getAllDVM");
        return communicationService.getAllInfo();
    }
    @GetMapping("/info")
    public String getMyInfo() {
        log.info("getMyInfo");
        return communicationService.getMyInfo();
    }

    @PostMapping("/client")
    public ResponseEntity<Object> addDVM(@RequestBody Info info) {
        log.info("addDVM: {}", info);
        try {
            Info result = communicationService.addInfo(info);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @DeleteMapping("/client/{id}")
    public ResponseEntity<Object> deleteDVM(@PathVariable String id) {
        log.info("deleteDVM: {}", id);
        try {
            communicationService.deleteInfo(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}