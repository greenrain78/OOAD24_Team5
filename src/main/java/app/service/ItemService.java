package app.service;

import app.domain.Item;
import app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ItemService {
    @Autowired
    private ItemRepository itemRepository;
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemByItemCode(int itemCode) {
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }
        return item;
    }
    @Transactional
    public Item updateItem(Long id, Item itemDetails) {
        Item item = itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        item.setQuantity(itemDetails.getQuantity());
        item.setPrice(itemDetails.getPrice());
        return itemRepository.save(item);
    }
}
