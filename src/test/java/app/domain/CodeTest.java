package app.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class CodeTest {
    @DisplayName("Code 생성 테스트 - 성공 케이스")
    @Test
    public void testCreateCode() {
        LocalDateTime time = LocalDateTime.now();
        Code code = new Code("code1", time, 1, 1);
        assert code.getCode().equals("code1");
        assert code.getTime().equals(time);
        assert code.getItemCode() == 1;
        assert code.getQuantity() == 1;
    }
}
