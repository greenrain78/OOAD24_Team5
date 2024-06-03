package app.actor;

import app.service.ManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class Timer {
    @Autowired
    private ManagementService managementService;
    // 고정된 시간에 실행 (cron 표현식) - 1시간 마다 실행
    @Scheduled(cron = "0 0 0/1 * * *")
    public void fixedRateTask() {
        log.info("deleteExpiredCodes");
        managementService.deleteExpiredCodes();
    }
}
