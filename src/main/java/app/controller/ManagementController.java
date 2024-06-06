package app.controller;

import app.domain.Item;
import app.service.ManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Slf4j
@RestController
public class ManagementController {

    @Autowired
    private ManagementService managementService;

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
}