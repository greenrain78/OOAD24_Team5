package app.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "CODES")  // 테이블 지정
public class Code {
    @Id  // Primary Key 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // AUTO_INCREMENT 설정 (id값이 null일 경우 자동 생성)
    @Column(name = "ID")  // 컬럼 지정
    private Long id;


    @NotNull
    @Column(name = "CODE")
    private String code;


    @NotNull
    @Column(name = "TIME")
    private LocalDateTime time;

    @NotNull
    @Column(name = "ITEM_CODE", unique = true) // 이거의 의미를 잘 모르겠음 id로 대체하던가 별도의 FK로 대체하던가 해야할듯
    private int itemCode;

    @NotNull
    @Column(name = "ITEM_QUANTITY")
    private int quantity;

    @Builder(toBuilder = true)
    public Code(String code, LocalDateTime time, int itemCode, int quantity) {
        this.code = code;
        this.time = time;
        this.itemCode = itemCode;
        this.quantity = quantity;
    }

    public Code() {

    }

    public String toString() {
        return "Code{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", time=" + time +
                ", itemCode=" + itemCode +
                ", quantity=" + quantity +
                '}';
    }
}
