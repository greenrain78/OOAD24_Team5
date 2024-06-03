package app.repository;

import app.domain.Code;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
public class CodeRepositoryTest {

    @Autowired
    private CodeRepository codeRepository;

    @DisplayName("code 저장 테스트")
    @Test
    public void saveCode() {
        LocalDateTime time = LocalDateTime.now();
        // 초기 값이 있는지 확인
        assert codeRepository.findAll().isEmpty() : "실제 값 : " + codeRepository.findAll();
        // 저장
        Code testCode = new Code("testCode1", time, 1, 1);
        codeRepository.save(testCode);
        Code savedCode = codeRepository.findByCode("testCode1");

        // 저장된 데이터가 맞는지 확인
        assert savedCode.getCode().equals("testCode1") : "실제 값 : " + savedCode.getCode();
        assert savedCode.getTime().equals(time) : "실제 값 : " + savedCode.getTime();
        assert savedCode.getItemCode() == 1 : "실제 값 : " + savedCode.getItemCode();
        assert savedCode.getQuantity() == 1 : "실제 값 : " + savedCode.getQuantity();
    }
    @DisplayName("시간 이전 code 조회 테스트")
    @Test
    public void findAllByTimeBefore() {
        LocalDateTime time = LocalDateTime.now();
        LocalDateTime time2 = time.minusSeconds(1);
        // 초기 값이 있는지 확인
        assert codeRepository.findAllByTimeBefore(time).isEmpty() : "실제 값 : " + codeRepository.findAllByTimeBefore(time);
        // 저장
        Code testCode = new Code("testCode1", time2, 1, 1);
        codeRepository.save(testCode);
        // 저장된 데이터가 맞는지 확인
        List<Code> savedCodes = codeRepository.findAllByTimeBefore(time);
        assert savedCodes.size() == 1 : "실제 값 : " + savedCodes.size();
        assert savedCodes.get(0).getCode().equals("testCode1") : "실제 값 : " + savedCodes.get(0).getCode();
        assert savedCodes.get(0).getTime().equals(time2) : "실제 값 : " + savedCodes.get(0).getTime();
        assert savedCodes.get(0).getItemCode() == 1 : "실제 값 : " + savedCodes.get(0).getItemCode();
        assert savedCodes.get(0).getQuantity() == 1 : "실제 값 : " + savedCodes.get(0).getQuantity();
    }
}

