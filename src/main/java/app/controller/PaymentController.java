package app.controller;

import app.domain.FakeDrink;
import app.domain.OrderRequest;
import app.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Object> pay(@RequestBody OrderRequest orderRequest) {
        if (orderRequest.getQuantity() <= 0) {
            return ResponseEntity.badRequest().body("Invalid quantity");
        }
        try {
            FakeDrink drink = paymentService.requestPayment(orderRequest.getItemCode(), orderRequest.getCardNumber(), orderRequest.getQuantity());
            return ResponseEntity.ok(drink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
    @PostMapping("/pickup")
    public ResponseEntity<Object> pickup(@RequestBody String cert_code) {
        try {
            FakeDrink drink = paymentService.requestPickup(cert_code);
            return ResponseEntity.ok(drink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
