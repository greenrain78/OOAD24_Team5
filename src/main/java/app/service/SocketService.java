package app.service;


import app.domain.Item;
import app.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class SocketService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private PaymentService paymentService;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public HashMap<String, String> requestStock(int itemCode) {
        // item 조회
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }
        // item 재고 확인
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(item.getItemCode()));
        content.put("item_num", String.valueOf(item.getQuantity()));
        return content;
    }

    public HashMap<String, String> requestPrePayment(int itemCode, int quantity) {
        // item 조회
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");
        }
        if (item.getQuantity() < quantity) {
            throw new IllegalStateException("Not enough stock");
        }
        // 결제 요청
        boolean result = paymentService.requestPrePayment(itemCode, quantity);
        HashMap<String, String> content = new HashMap<>();
        content.put("item_code", String.valueOf(itemCode));
        content.put("item_num", String.valueOf(quantity));    // 선결제 수량
        if (!result) {
            content.put("availability", "F");
        } else {
            content.put("availability", "T");
        }
        return content;

    }
}
