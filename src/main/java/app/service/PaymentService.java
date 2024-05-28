package app.service;

import app.adapter.CardCompany;
import app.domain.Code;
import app.domain.Item;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CodeRepository codeRepository;
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

    public boolean requestPickup(String cert_code) {
        Code code = codeRepository.findByCode(cert_code);
        if (code == null) {
            return false;   // 코드가 존재하지 않음
        }
        Item item = itemRepository.findByItemCode(code.getItemCode());
        if (item == null) {
            return false;   // 상품이 존재하지 않음
        }
        // 상품 수량 감소
        item.setQuantity(item.getQuantity() - code.getQuantity());
        itemRepository.save(item);
        // 코드 삭제
        codeRepository.delete(code);
        return true;
    }
}
