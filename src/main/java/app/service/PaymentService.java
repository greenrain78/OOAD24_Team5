package app.service;

import app.actor.CardCompany;
import app.domain.*;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class PaymentService {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private CommunicationService communicationService;
    private final CardCompany cardCompany = new CardCompany();
    @Transactional
    public FakeDrink requestPayment(int itemCode, String cardNumber, int quantity) {
        Item item = itemRepository.findByItemCode(itemCode);
        if (item == null) {
            throw new IllegalArgumentException("Item not found");   // 상품이 존재하지 않음
        }
        if (item.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough quantity");  // 상품 수량 부족
        }
        // 결제 요청
        int totalPrice = item.getPrice() * quantity;
        cardCompany.requestPayment(cardNumber, totalPrice);
        // 상품 수량 감소
        item.setQuantity(item.getQuantity() - quantity);
        itemRepository.save(item);
        return new FakeDrink(item.getName(), quantity);
    }

    @Transactional
    public HashMap<String, Object> requestPrePayment(OrderRequest orderRequest) {
        // 요금 차감
        Item item = itemRepository.findByItemCode(orderRequest.getItemCode());
        int totalPrice = item.getPrice() * orderRequest.getQuantity();
        cardCompany.requestPayment(orderRequest.getCardNumber(), totalPrice);
        // 랜덤한 uuid 인증코드 발급
        String authCode = java.util.UUID.randomUUID().toString().replaceAll("-", "").substring(0, 10);
        // 선결제 요청
        try {
            return communicationService.prepay(authCode, orderRequest.getItemCode(), orderRequest.getQuantity());
        } catch (Exception e) {
            // 예외 발생 시 결제 취소
            cardCompany.cancelPayment(orderRequest.getCardNumber(), totalPrice);
            throw new IllegalArgumentException("Failed to prepay", e);
        }
    }
    @Transactional
    public boolean responsePrePayment(String code, int itemCode, int quantity) {
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
        // 인증코드 저장
        codeRepository.save(new Code(code, LocalDateTime.now(), itemCode, quantity));
        return true;
    }

    @Transactional
    public FakeDrink requestPickup(String cert_code) {
        Code code = codeRepository.findByCode(cert_code);
        if (code == null) {
            throw new IllegalArgumentException("Invalid code");    // 코드가 존재하지 않음
        }
        Item item = itemRepository.findByItemCode(code.getItemCode());
        if (item == null) {
            throw new IllegalArgumentException("Item not found");   // 상품이 존재하지 않음
        }
        // 코드 삭제
        codeRepository.delete(code);
        return new FakeDrink(item.getName(), code.getQuantity());
    }
}
