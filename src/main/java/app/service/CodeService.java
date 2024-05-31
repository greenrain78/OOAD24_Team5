package app.service;

import app.domain.Code;
import app.domain.Item;
import app.repository.CodeRepository;
import app.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CodeService {
    @Autowired
    private CodeRepository codeRepository;
    @Autowired
    private ItemRepository itemRepository;

    public List<Code> getAllCodes() {
        return codeRepository.findAll();
    }

    // 기간이 지난 코드는 삭제
    @Transactional
    public void deleteExpiredCodes() {
        int ExpiredDays = 30;
        LocalDateTime time = LocalDateTime.now().minusDays(ExpiredDays);
        List<Code> codes = codeRepository.findAllByTimeBefore(time);
        for (Code code : codes) {
            Item item = itemRepository.findByItemCode(code.getItemCode());
            item.setQuantity(item.getQuantity() + code.getQuantity());
            codeRepository.delete(code);
        }
    }
}
