package app.controller;

import app.domain.FakeDrink;
import app.domain.OrderRequest;
import app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public String pay(@RequestBody OrderRequest orderRequest) {
        if (orderRequest.getQuantity() <= 0) {
            return "선택한 상품의 수량이 0개 이하입니다.";
        }
        if (paymentService.requestPayment(orderRequest.getItemCode(), orderRequest.getCardNumber(), orderRequest.getQuantity())) {
            return "Payment successful";
        } else {
            return "Payment failed";
        }
    }

    @PostMapping("/prepay")
    public String prepay() {
        return "Prepayment successful";
    }
    @PostMapping("/pickup")
    public String pickup(@RequestBody String cert_code) {
        FakeDrink drink = paymentService.requestPickup(cert_code);
        return drink == null ? "Pickup failed" : "Pickup successful: " + drink;
    }
}
