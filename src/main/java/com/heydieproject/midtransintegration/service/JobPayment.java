package com.heydieproject.midtransintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@EnableScheduling
@Slf4j
public class JobPayment {

//    @Scheduled(cron = "*/1 * * * * *")
    public void jobExpire() {
        log.info("NOW => {}", LocalDateTime.now());
    }

}
