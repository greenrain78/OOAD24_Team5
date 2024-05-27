package app.domain;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderRequest {
    private int itemCode;
    private int cardNumber;
    private int quantity;

    public OrderRequest(int itemCode, int cardNumber, int quantity) {
        this.itemCode = itemCode;
        this.cardNumber = cardNumber;
        this.quantity = quantity;
    }

}
