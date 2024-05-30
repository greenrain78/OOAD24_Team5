package app.service;

import app.domain.Code;
import app.repository.CodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CodeService {
    @Autowired
    private CodeRepository codeRepository;
    public List<Code> getAllCodes() {
        return codeRepository.findAll();
    }
    public Optional<Code> getCodeByCode(String code) {
        return Optional.ofNullable(codeRepository.findByCode(code));
    }
    public Code createCode(Code code) {
        return codeRepository.save(code);
    }
    @Transactional
    public Code deleteCode(String code) {
        Code codeToRemove = codeRepository.findByCode(code);
        codeRepository.delete(codeToRemove);
        return codeToRemove;
    }
    // 기간이 지난 코드는 삭제
    @Transactional
    public void deleteExpiredCodes() {
        int ExpiredDays = 30;
        List<Code> codes = codeRepository.findAll();
        for (Code code : codes) {
            // 오늘 날짜 기준으로 30일 이전 코드 삭제
            if (code.getTime().plusDays(ExpiredDays).isBefore(code.getTime())) {
                codeRepository.delete(code);
            }
        }
    }
}
