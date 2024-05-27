package app.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@Entity
@Table(name = "ITEMS")  // 테이블 지정
public class Item {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")  // 컬럼 지정
    private Long id;


    @NotNull
    @Column(name = "NAME")
    private String name;


    @NotNull
    @Column(name = "QUANTITY")
    private int quantity;

    @NotNull
    @Column(name = "PRICE")
    private int price;

    @NotNull
    @Column(name = "ITEM_CODE", unique = true) // 이거의 의미를 잘 모르겠음 id로 대체하던가 별도의 FK로 대체하던가 해야할듯
    private int itemCode;

    @Builder(toBuilder = true)
    public Item(String name, int quantity, int itemCode) {
        this.name = name;
        this.quantity = quantity;
        this.itemCode = itemCode;
    }

    public Item() {

    }

    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", itemCode=" + itemCode +
                '}';
    }
}
