package app.controller;

import app.domain.Code;
import app.domain.Item;
import app.service.CommunicationService;
import app.service.ManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class ManagementController {

    @Autowired
    private ManagementService managementService;
    @Autowired
    private CommunicationService communicationService;
    @GetMapping("/items")
    public List<Item> getAllItems() {
        log.info("get all items");
        return managementService.getAllItems();
    }

    @GetMapping("/item/{itemCode}")
    public ResponseEntity<Item> getItemByItemCode(@PathVariable int itemCode) {
        log.info("get item by id: {}", itemCode);
        try {
            Item item = managementService.getItemByItemCode(itemCode);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }
    @GetMapping("/client/{id}/items")
    public ResponseEntity<Object> getItemsByDVM(@PathVariable String id) {
        log.info("getItemsByDVM: {}", id);
        try {
            Map<String, String> result = communicationService.getItems(id);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/client/item/{itemCode}")
    public ResponseEntity<Object> getItemByDVM(@PathVariable int itemCode) {
        log.info("getItemByDVM: {}", itemCode);
        try {
            HashMap<String, Integer> result = communicationService.getItemByItemCode(itemCode);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/item/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        log.info("update item: {}", itemDetails);
        try {
            Item updatedItem = managementService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codes")
    public List<Code> getAllCodes() {
        log.info("get all codes");
        return managementService.getAllCodes();
    }
}