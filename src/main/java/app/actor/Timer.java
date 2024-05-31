package app.actor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class Timer {
    // 고정된 주기로 실행 (밀리초 단위)
    @Scheduled(fixedRate = 1000)
    public void fixedRateTask() {
        log.info("Fixed rate task - " + System.currentTimeMillis() / 1000);
    }
}
