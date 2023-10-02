package com.heydieproject.midtransintegration.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WebHook {

    public String retrieveResponse(String data) {
        log.info("DATA => {}", data);
        return data;
    }
}
