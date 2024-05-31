package app.service;

import app.domain.Code;
import app.domain.Item;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManagementService {
    @Autowired
    private CodeRepository codeRepository;
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
    public List<Code> getAllCodes() {
        return codeRepository.findAll();
    }

    // 기간이 지난 코드는 삭제
    @Transactional
    public void deleteExpiredCodes() {
        int ExpiredDays = 30;
        LocalDateTime time = LocalDateTime.now().minusDays(ExpiredDays);
        List<Code> codes = codeRepository.findAllByTimeBefore(time);
        for (Code code : codes) {
            Item item = itemRepository.findByItemCode(code.getItemCode());
            item.setQuantity(item.getQuantity() + code.getQuantity());
            codeRepository.delete(code);
        }
    }
}
