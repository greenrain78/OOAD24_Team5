package app.controller;

import app.domain.Code;
import app.domain.Item;
import app.service.CodeService;
import app.service.CommunicationService;
import app.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
@Slf4j
@RestController
public class ManagementController {

    @Autowired
    private ItemService itemService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private CommunicationService communicationService;
    @GetMapping("/items")
    public List<Item> getAllItems() {
        log.info("get all items");
        return itemService.getAllItems();
    }

    @GetMapping("/item/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.info("get item by id: {}", id);
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
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
    @GetMapping("/client/{id}/item/{itemCode}")
    public ResponseEntity<Object> getItemByDVM(@PathVariable String id, @PathVariable int itemCode) {
        log.info("getItemsByDVM: {}", id);
        try {
            int result = communicationService.getItemByItemCode(id, itemCode);
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
            Item updatedItem = itemService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/codes")
    public List<Code> getAllCodes() {
        log.info("get all codes");
        return codeService.getAllCodes();
    }
}