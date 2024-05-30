package app.service;

import app.domain.Code;
import app.domain.FakeDrink;
import app.domain.Item;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class PaymentServiceTest {
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private CodeRepository codeRepository;

    @DisplayName("pick up test")
    @Test
    public void testPickup() {
        // given
        String cert_code = "1234";
        int itemCode = 901;
        int quantity = 1;
        itemRepository.save(new Item("pick up test", itemCode, 1000));
        codeRepository.save(new Code(cert_code, LocalDateTime.now(), itemCode, quantity));
        // when
        FakeDrink result = paymentService.requestPickup(cert_code);
        // then
        assert result != null;
        assert result.name().equals("pick up test"): "result.name() = " + result.name();
        assert result.quantity() == 1: "result.quantity() = " + result.quantity();
        int quantityAfterPickup = itemRepository.findByItemCode(itemCode).getQuantity();
        assert quantityAfterPickup == 1000: "quantityAfterPickup = " + quantityAfterPickup; // 선결제시 수량이 감소했음
        Code code = codeRepository.findByCode(cert_code);
        assert code == null: "code = " + code;
    }
}
