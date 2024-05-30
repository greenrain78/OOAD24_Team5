package app.controller;

import app.domain.Item;
import app.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Slf4j
@RestController
@RequestMapping("/management")
public class ManagementController {

    @Autowired
    private ItemService itemService;
    @GetMapping
    public List<Item> getAllItems() {
        log.info("get all items");
        return itemService.getAllItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getItemById(@PathVariable Long id) {
        log.info("get item by id: {}", id);
        Optional<Item> item = itemService.getItemById(id);
        return item.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Item createItem(@RequestBody Item item) {
        log.info("create item: {}", item);
        return itemService.createItem(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @RequestBody Item itemDetails) {
        log.info("update item: {}", itemDetails);
        try {
            Item updatedItem = itemService.updateItem(id, itemDetails);
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("delete item by id: {}", id);
        itemService.deleteItem(id);
        return ResponseEntity.noContent().build();
    }

}