package app.service;

import app.domain.Code;
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
        itemRepository.save(new Item("pick up test", 1000, itemCode));
        codeRepository.save(new Code(cert_code, LocalDateTime.now(), itemCode, quantity));
        // when
        boolean result = paymentService.requestPickup(cert_code);
        // then
        assert result;
        int quantityAfterPickup = itemRepository.findByItemCode(itemCode).getQuantity();
        assert quantityAfterPickup == 999: "quantityAfterPickup = " + quantityAfterPickup;
        Code code = codeRepository.findByCode(cert_code);
        assert code == null: "code = " + code;
    }
}
