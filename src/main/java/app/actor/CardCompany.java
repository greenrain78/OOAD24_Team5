package app.actor;

import java.util.Objects;

public class CardCompany {
    public void requestPayment(String cardNumber, int total) {
        // 결제 기믹
        if (Objects.equals(cardNumber, "1234") && total == 10000) {
            throw new IllegalArgumentException("Payment failed");
        }
    }
    public void cancelPayment(String cardNumber, int total) {
        // 결제 취소 기믹
        if (Objects.equals(cardNumber, "1234") && total == 10000) {
            throw new IllegalArgumentException("Cancel failed");
        }
    }
}
