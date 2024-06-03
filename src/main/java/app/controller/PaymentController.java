package app.controller;

import app.domain.Code;
import app.domain.FakeDrink;
import app.domain.OrderRequest;
import app.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/pay")
    public ResponseEntity<Object> pay(@RequestBody OrderRequest orderRequest) {
        log.info("pay request: {}", orderRequest);
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
    @PostMapping("/prepay/{id}")
    public ResponseEntity<Object> prepay(@PathVariable String id, @RequestBody OrderRequest orderRequest) {
        log.info("prepay request: {}", orderRequest);
        try {
            log.info("prepay request: {}", orderRequest);
            Code result = paymentService.requestPrePayment(id, orderRequest);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("error", e);
            log.error("error_message: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/pickup")
    public ResponseEntity<Object> pickup(@RequestBody String cert_code) {
        log.info("pickup request: {}", cert_code);
        try {
            FakeDrink drink = paymentService.requestPickup(cert_code);
            return ResponseEntity.ok(drink);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }
}
