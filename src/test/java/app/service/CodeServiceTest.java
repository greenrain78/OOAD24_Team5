package app.service;

import app.domain.Code;
import app.domain.Item;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class CodeServiceTest {
    @Autowired
    private CodeService codeService;
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private ItemRepository itemRepository;

    @DisplayName("기간이 지난 코드 삭제 테스트")
    @Test
    public void deleteExpiredCodesTest() {
        // given
        LocalDateTime oldDate = LocalDateTime.now().minusDays(31);
        codeRepository.save(new Code("code1", oldDate, 1, 1));
        codeRepository.save(new Code("code2", LocalDateTime.now(), 2, 2));
        // when
        codeService.deleteExpiredCodes();
        // then
        Code code1 = codeRepository.findByCode("code1");
        Code code2 = codeRepository.findByCode("code2");
        assert code1 == null : "기간이 지난 코드가 삭제되지 않았습니다." + code1;
        assert code2 != null : "기간이 지나지 않은 코드가 삭제되었습니다." + null;
    }
    @DisplayName("기간이 지난 코드 삭제되고 item 복구 테스트")
    @Test
    public void deleteExpiredCodesAndRecoverItemTest() {
        // given
        LocalDateTime oldDate = LocalDateTime.now().minusDays(31);
        codeRepository.save(new Code("code1", oldDate, 999, 1));
        itemRepository.save(new Item("item1", 999, 10));
        // when
        codeService.deleteExpiredCodes();
        // then
        Code code1 = codeRepository.findByCode("code1");
        Item item1 = itemRepository.findByItemCode(999);
        assert code1 == null : "기간이 지난 코드가 삭제되지 않았습니다." + code1;
        assert item1.getQuantity() == 11 : "기간이 지난 코드의 수량이 item에 복구되지 않았습니다." + item1.getQuantity();
    }
}
