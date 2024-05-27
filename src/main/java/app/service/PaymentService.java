package app.service;

import app.adapter.CardCompany;
import app.domain.Item;
import app.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private ItemRepository itemRepository;
    private final CardCompany cardCompanyProxy = new CardCompany();
    @Transactional
    public boolean requestPayment(int itemCode, int cardNumber, int quantity) {
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            return false;   // 상품이 존재하지 않음
        }
        if (item.getQuantity() < quantity) {
            return false;   // 상품 수량 부족
        }
        // 결제 요청
        int totalPrice = item.getPrice() * quantity;
        if (!cardCompanyProxy.requestPayment(cardNumber, totalPrice)) {
            return false;   // 결제 실패
        }
        // 상품 수량 감소
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);
        return true;
    }
    @Transactional
    public boolean requestPrePayment(int itemCode, int quantity) {
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            return false;   // 상품이 존재하지 않음
        }
        if (item.getQuantity() < quantity) {
            return false;   // 상품 수량 부족
        }
        // 상품 수량 감소
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);
        return true;
    }
}
